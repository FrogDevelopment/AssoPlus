/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.controller;

import fr.frogdevelopment.assoplus.dto.LicenceDto;
import fr.frogdevelopment.assoplus.dto.ReferenceDto;
import javafx.beans.property.ReadOnlyStringWrapper;
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
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

@Controller("licencesController")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LicencesController implements Initializable {

	@FXML
	private TreeTableView<ReferenceDto> treeTableView;
	@FXML
	private TreeTableColumn<ReferenceDto, String> colCode;
	@FXML
	private TreeTableColumn<ReferenceDto, String> colLabel;

	private List<LicenceDto> licences;

	@Override
	@SuppressWarnings("unchecked")
	public void initialize(URL location, ResourceBundle resources) {

		licences = Arrays.<LicenceDto>asList(
				new LicenceDto(0, "L1", "Licence 1"),
				new LicenceDto(0, "L2", "Licence 2"),
				new LicenceDto(0, "L3", "Licence 3"),
				new LicenceDto(0, "M1", "Master 1"),
				new LicenceDto(0, "M2", "Master 2"),
				new LicenceDto(0, "D", "Doctorat")
		);

		TreeItem<ReferenceDto> rootItem = new TreeItem<>(new LicenceDto());

		licences.forEach(licence -> {
			TreeItem<ReferenceDto> treeItem = new TreeItem<>(licence);
			licence.getOptions().forEach(option -> treeItem.getChildren().add(new TreeItem<>(option)));
			rootItem.getChildren().add(treeItem);
		});

		colCode.setCellValueFactory(new TreeItemPropertyValueFactory<>("code"));
		colCode.setCellFactory(p -> new TextFieldTreeTableCell(new StringConverter<String>() {
			@Override
			public String toString(String object) {
				return object;
			}

			@Override
			public String fromString(String string) {
				return string;
			}
		}));

		colLabel.setCellValueFactory(new TreeItemPropertyValueFactory<>("label"));
		colLabel.setCellFactory(p -> new TextFieldTreeTableCell(new StringConverter<String>() {
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

	public void onCancel(Event event) {
		Stage window = (Stage) ((Button) event.getSource()).getScene().getWindow();
//		window.close();
	}

}
