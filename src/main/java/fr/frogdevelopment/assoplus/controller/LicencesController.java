/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.controller;

import fr.frogdevelopment.assoplus.dto.LicenceDto;
import fr.frogdevelopment.assoplus.dto.OptionDto;
import fr.frogdevelopment.assoplus.dto.ReferenceDto;
import fr.frogdevelopment.assoplus.service.LicencesService;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.Comparator;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import static javafx.scene.control.ButtonBar.ButtonData.CANCEL_CLOSE;
import static javafx.scene.control.ButtonBar.ButtonData.OK_DONE;

@Controller("licencesController")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LicencesController implements Initializable {

	@Autowired
	private LicencesService licencesService;

	@FXML
	private TreeTableView<ReferenceDto> treeTableView;
	@FXML
	private TreeTableColumn<ReferenceDto, String> columnCode;
	@FXML
	private TreeTableColumn<ReferenceDto, String> columnLabel;

	private Set<LicenceDto> licencesDto;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		init();
	}

	@SuppressWarnings("unchecked")
	private void init() {
		licencesDto = licencesService.getAllOrderedByCode();

		TreeItem<ReferenceDto> rootItem = new TreeItem<>(new LicenceDto());

		licencesDto.forEach(licenceDto -> {
			TreeItem<ReferenceDto> treeItem = new TreeItem<>(licenceDto);
			licenceDto.getOptions().stream().forEach(optionDto -> treeItem.getChildren().add(new TreeItem<>(optionDto)));
			rootItem.getChildren().add(treeItem);
			rootItem.getChildren().sort(Comparator.comparing(o1 -> o1.getValue().getCode()));
		});

		columnCode.setCellValueFactory(new TreeItemPropertyValueFactory<>("code"));
		columnCode.setCellFactory(p -> new TextFieldTreeTableCell(new StringConverter<String>() {
			@Override
			public String toString(String object) {
				return object;
			}

			@Override
			public String fromString(String string) {
				return string;
			}
		}));

		columnLabel.setCellValueFactory(new TreeItemPropertyValueFactory<>("label"));
		columnLabel.setCellFactory(p -> new TextFieldTreeTableCell(new StringConverter<String>() {
			@Override
			public String toString(String object) {
				return object;
			}

			@Override
			public String fromString(String string) {
				return string;
			}
		}));

		rootItem.setExpanded(true);
		treeTableView.setRoot(rootItem);
		treeTableView.setShowRoot(false);

		treeTableView.setEditable(true);
	}

	public void onSave(Event event) {
		licencesService.saveOrUpdateAll(licencesDto);
//		init();
	}

	public void onClose(Event event) {
		close(event);
	}

	private void close(Event event) {
		Stage window = (Stage) ((Button) event.getSource()).getScene().getWindow();
		window.close();
	}

	public void onAddLicence() {
		final TreeItem<ReferenceDto> root = treeTableView.getRoot();

		LicenceDto licenceDto = new LicenceDto();
		licencesDto.add(licenceDto);

		final TreeItem<ReferenceDto> newItem = new TreeItem<>(licenceDto);
		root.getChildren().add(newItem);

		root.expandedProperty().set(true);
		final int rowIndex = treeTableView.getRow(newItem);

		treeTableView.edit(rowIndex, columnCode);
	}

	public void onAddOption() {
		final TreeItem<ReferenceDto> selectedItem = treeTableView.getSelectionModel().getSelectedItem();

		if (!(selectedItem.getValue() instanceof LicenceDto)) {
			return;
		}

		LicenceDto licenceDto = (LicenceDto) selectedItem.getValue();
		OptionDto optionDto = new OptionDto();
		licenceDto.addOption(optionDto);
		optionDto.setLicenceDto(licenceDto);

		final TreeItem<ReferenceDto> newItem = new TreeItem<>(optionDto);
		selectedItem.getChildren().add(newItem);

		selectedItem.expandedProperty().set(true);
		final int rowIndex = treeTableView.getRow(newItem);

		treeTableView.edit(rowIndex, columnCode);
	}

	public void onRemoveOption() {
		final TreeItem<ReferenceDto> selectedItem = treeTableView.getSelectionModel().getSelectedItem();

		if (!(selectedItem.getValue() instanceof OptionDto)) {
			return;
		}

		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setHeaderText("ATTENTION");
		dialog.setContentText("Vous allez supprimer une Option, voulez-vous continuer ?");
		dialog.getDialogPane().getButtonTypes().add(ButtonType.YES);
		dialog.getDialogPane().getButtonTypes().add(ButtonType.NO);

		dialog.showAndWait()
				.filter(response -> response == ButtonType.YES)
				.ifPresent(response -> removeOption(selectedItem));
	}

	private void removeOption(final TreeItem<ReferenceDto> selectedItem) {
		TreeItem<ReferenceDto> parent = selectedItem.getParent();
		parent.getChildren().remove(selectedItem);

		OptionDto optionDto = (OptionDto) selectedItem.getValue();
		LicenceDto licenceDto = (LicenceDto) parent.getValue();
		licenceDto.getOptions().remove(optionDto);

		if (optionDto.getId() != null) {
			licencesService.deleteOption(licenceDto, optionDto);
		}
	}


}
