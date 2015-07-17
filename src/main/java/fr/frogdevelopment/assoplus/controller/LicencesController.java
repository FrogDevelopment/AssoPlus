/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import fr.frogdevelopment.assoplus.dto.LicenceDto;
import fr.frogdevelopment.assoplus.dto.OptionDto;
import fr.frogdevelopment.assoplus.dto.ReferenceDto;
import fr.frogdevelopment.assoplus.service.LicencesService;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

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
	@SuppressWarnings("unchecked")
	public void initialize(URL location, ResourceBundle resources) {
		licencesDto = licencesService.getAllOrderedByCode();

		TreeItem<ReferenceDto> rootItem = new TreeItem<>(new LicenceDto());

		licencesDto.forEach(licence -> {
			TreeItem<ReferenceDto> treeItem = new TreeItem<>(licence);
			licence.getOptions().forEach(option -> treeItem.getChildren().add(new TreeItem<>(option)));
			rootItem.getChildren().add(treeItem);
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
	}

	public void onClose(Event event) {
		close(event);
	}

	private void close(Event event) {
		Stage window = (Stage) ((Button) event.getSource()).getScene().getWindow();
		window.close();
	}

	public void onAddLicence() {
	}

	public void onAddOption() {
		final TreeItem<ReferenceDto> selectedItem = treeTableView.getSelectionModel().getSelectedItem();

		LicenceDto licenceDto = (LicenceDto) selectedItem.getValue();
		OptionDto optionDto = new OptionDto();
		licenceDto.addOption(optionDto);
        optionDto.setLicenceDto(licenceDto);

		final TreeItem<ReferenceDto> newItem = new TreeItem<>(optionDto);
		selectedItem.getChildren().add(newItem);

		selectedItem.expandedProperty().set(true);
		final int rowIndex = treeTableView.getRow(newItem);

		treeTableView.edit(rowIndex, columnCode);//Does 99% not start or get canceled, why?

	}
}
