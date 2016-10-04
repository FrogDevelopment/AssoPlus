/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.member.controller;

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

import fr.frogdevelopment.assoplus.core.controller.AbstractCustomDialogController;
import fr.frogdevelopment.assoplus.core.dto.Reference;
import fr.frogdevelopment.assoplus.member.dto.Degree;
import fr.frogdevelopment.assoplus.member.dto.Option;
import fr.frogdevelopment.assoplus.member.service.DegreeService;

import java.util.Comparator;
import java.util.function.Consumer;

@Controller
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DegreeController extends AbstractCustomDialogController {

    @Autowired
    private DegreeService degreeService;

    @FXML
    private TreeTableView<Reference> treeTableView;
    @FXML
    private TextField tfLabel;
    @FXML
    private TextField tfCode;
    @FXML
    private Button btnRemove;

    private ObservableList<Degree> degrees;
    private TreeItem<Reference> rootItem;

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
        degrees = FXCollections.observableArrayList(degreeService.getAll());

        rootItem = new TreeItem<>(new Degree());

        degrees.forEach(licenceDto -> {
            TreeItem<Reference> degreeItem = new TreeItem<>(licenceDto);
            licenceDto.getOptions().forEach(o -> degreeItem.getChildren().add(new TreeItem<>(o)));

            rootItem.getChildren().add(degreeItem);
        });

        rootItem.getChildren().sort(Comparator.comparing(o1 -> o1.getValue().getCode()));
        treeTableView.setRoot(rootItem);
        treeTableView.setShowRoot(false);
        rootItem.setExpanded(true);
    }

    public void onSave() {
        degreeService.save(degrees);
        initData();
    }

    public void onAddLicence() {
        Degree degree = new Degree();
        degrees.add(degree);

        final TreeItem<Reference> newItem = new TreeItem<>(degree);
        rootItem.getChildren().add(newItem);

        rootItem.setExpanded(true);
        final int rowIndex = treeTableView.getRow(newItem);

        treeTableView.getSelectionModel().select(rowIndex);
        tfCode.requestFocus();
    }

    public void onAddOption() {
        TreeItem<Reference> selectedItem = treeTableView.getSelectionModel().getSelectedItem();

        if (selectedItem.getValue() instanceof Option) {
            selectedItem = selectedItem.getParent();
        }

        Degree degree = (Degree) selectedItem.getValue();
        Option option = new Option();

        degree.addOption(option);

        final TreeItem<Reference> newItem = new TreeItem<>(option);
        selectedItem.getChildren().add(newItem);

        selectedItem.expandedProperty().set(true);
        final int rowIndex = treeTableView.getRow(newItem);

        treeTableView.getSelectionModel().select(rowIndex);
        tfCode.requestFocus();
    }

    public void onRemove() {
        final TreeItem<Reference> selectedItem = treeTableView.getSelectionModel().getSelectedItem();

        String message = getMessage("global.confirm.delete");
        Consumer onYes;
        Reference value = selectedItem.getValue();
        if ((value instanceof Degree)) {
            message = String.format(message, "le Diplôme '" + value.getCode() + "'"); // fixme
            onYes = o -> removeLicence(selectedItem);
        } else if ((value instanceof Option)) {
            message = String.format(message, "l'Option '" + value.getCode() + "'"); // fixme
            onYes = o -> removeOption(selectedItem);
        } else {
            return;
        }

        showYesNoDialog(message, onYes);
    }

    private void removeLicence(final TreeItem<Reference> selectedItem) {
        rootItem.getChildren().remove(selectedItem);

        Degree degree = (Degree) selectedItem.getValue();
        degree.setToDelete(true);
    }

    private void removeOption(final TreeItem<Reference> selectedItem) {
        TreeItem<Reference> parent = selectedItem.getParent();
        parent.getChildren().remove(selectedItem);

        Option optionDto = (Option) selectedItem.getValue();
        optionDto.setToDelete(true);
    }
}
