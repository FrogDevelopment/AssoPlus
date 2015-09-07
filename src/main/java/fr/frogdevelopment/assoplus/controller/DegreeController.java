/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import fr.frogdevelopment.assoplus.dto.DegreeDto;
import fr.frogdevelopment.assoplus.dto.OptionDto;
import fr.frogdevelopment.assoplus.dto.ReferenceDto;
import fr.frogdevelopment.assoplus.service.DegreeService;
import fr.frogdevelopment.assoplus.service.OptionsService;

import java.util.Comparator;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Controller
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DegreeController extends AbstractCustomDialogController {

    @Autowired
    private DegreeService degreeService;

    @Autowired
    private OptionsService optionsService;

    @FXML
    private TreeTableView<ReferenceDto> treeTableView;
    @FXML
    private TextField tfLabel;
    @FXML
    private TextField tfCode;
    @FXML
    private Button btnRemove;

    private ObservableList<DegreeDto> degreeDtos;
    private ObservableList<OptionDto> optionDtos;
    private TreeItem<ReferenceDto> rootItem;

    @Override
    protected void initialize() {
        initData();

        treeTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                tfCode.textProperty().unbindBidirectional(oldValue.getValue().codeProperty());
                tfLabel.textProperty().unbindBidirectional(oldValue.getValue().labelProperty());
            }

            if (newValue != null) {
                tfCode.textProperty().bindBidirectional(newValue.getValue().codeProperty());
                tfLabel.textProperty().bindBidirectional(newValue.getValue().labelProperty());

                tfCode.setDisable(false);
                tfLabel.setDisable(false);
                btnRemove.setDisable(false);
            } else {
                tfCode.setText(null);
                tfLabel.setText(null);

                tfCode.setDisable(true);
                tfLabel.setDisable(true);
                btnRemove.setDisable(true);
            }
        });
    }

    private void initData() {
        degreeDtos = FXCollections.observableArrayList(degreeService.getAll());
        optionDtos = FXCollections.observableArrayList(optionsService.getAll());

        rootItem = new TreeItem<>(new DegreeDto());

        degreeDtos.forEach(licenceDto -> {
            TreeItem<ReferenceDto> treeItem = new TreeItem<>(licenceDto);
            optionDtos.stream()
                    .filter(optionDto -> optionDto.getDegreeCode().equals(licenceDto.getCode()))
                    .forEach(optionDto -> treeItem.getChildren().add(new TreeItem<>(optionDto)));
            rootItem.getChildren().add(treeItem);
        });

        rootItem.getChildren().sort(Comparator.comparing(o1 -> o1.getValue().getCode()));
        treeTableView.setRoot(rootItem);
        treeTableView.setShowRoot(false);
        rootItem.setExpanded(true);
    }

    public void onSave() {
        degreeService.saveOrUpdateAll(degreeDtos);
        optionsService.saveOrUpdateAll(optionDtos);
        initData();
    }

    public void onAddLicence() {
        DegreeDto degreeDto = new DegreeDto();
        degreeDtos.add(degreeDto);

        final TreeItem<ReferenceDto> newItem = new TreeItem<>(degreeDto);
        rootItem.getChildren().add(newItem);

        rootItem.setExpanded(true);
        final int rowIndex = treeTableView.getRow(newItem);

        treeTableView.getSelectionModel().select(rowIndex);
        tfCode.requestFocus();
    }

    public void onAddOption() {
        TreeItem<ReferenceDto> selectedItem = treeTableView.getSelectionModel().getSelectedItem();

        if (selectedItem.getValue() instanceof OptionDto) {
            selectedItem = selectedItem.getParent();
        }

        DegreeDto degreeDto = (DegreeDto) selectedItem.getValue();
        OptionDto optionDto = new OptionDto();
        optionDto.setDegreeCode(degreeDto.getCode());

        optionDtos.add(optionDto);

        final TreeItem<ReferenceDto> newItem = new TreeItem<>(optionDto);
        selectedItem.getChildren().add(newItem);

        selectedItem.expandedProperty().set(true);
        final int rowIndex = treeTableView.getRow(newItem);

        treeTableView.getSelectionModel().select(rowIndex);
        tfCode.requestFocus();
    }

    public void onRemove() {
        final TreeItem<ReferenceDto> selectedItem = treeTableView.getSelectionModel().getSelectedItem();

        String message = getMessage("global.confirm.delete");
        Consumer onYes;
        ReferenceDto value = selectedItem.getValue();
        if ((value instanceof DegreeDto)) {
            message = String.format(message, "le Diplôme '" + value.getCode() + "'"); // fixme
            onYes = o -> removeLicence(selectedItem);
        } else if ((value instanceof OptionDto)) {
            message = String.format(message, "l'Option '" + value.getCode() + "'"); // fixme
            onYes = o -> removeOption(selectedItem);
        } else {
            return;
        }

        showYesNoDialog(message, onYes);
    }

    private void removeLicence(final TreeItem<ReferenceDto> selectedItem) {
        rootItem.getChildren().remove(selectedItem);

        DegreeDto degreeDto = (DegreeDto) selectedItem.getValue();
        degreeDtos.remove(degreeDto);


        if (degreeDto.getId() != 0) {
            degreeService.deleteLicence(degreeDto);

            // suppression des options et maj liste
            optionDtos = FXCollections.observableArrayList(optionDtos.stream()
                    .filter(optionDto -> optionDto.getDegreeCode().equals(degreeDto.getCode()))
                    .peek(optionsService::deleteData)
                    .collect(Collectors.toList()));
        }
    }

    private void removeOption(final TreeItem<ReferenceDto> selectedItem) {
        TreeItem<ReferenceDto> parent = selectedItem.getParent();
        parent.getChildren().remove(selectedItem);

        OptionDto optionDto = (OptionDto) selectedItem.getValue();
        optionDtos.remove(optionDto);

        if (optionDto.getId() != 0) {
            optionsService.deleteOption(optionDto);
        }
    }
}
