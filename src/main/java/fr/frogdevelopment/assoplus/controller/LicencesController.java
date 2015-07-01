/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.controller;

import fr.frogdevelopment.assoplus.bean.Licence;
import fr.frogdevelopment.assoplus.bean.Reference;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TextFieldTreeTableCell;
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
	private TreeTableView<Reference> treeTableView;
	@FXML
	private TreeTableColumn<Reference, String> colLicence;
	@FXML
	private TreeTableColumn<Reference, String> colOption;


	private TextFieldTreeTableCell textFieldTreeTableCell = new TextFieldTreeTableCell(new StringConverter<String>() {
		@Override
		public String toString(String object) {
			return object;
		}

		@Override
		public String fromString(String string) {
			return string;
		}
	});

	@Override
	@SuppressWarnings("unchecked")
	public void initialize(URL location, ResourceBundle resources) {

		List<Licence> licences = Arrays.<Licence>asList(
				new Licence("L1", "Licence 1"),
				new Licence("L2", "Licence 2"),
				new Licence("L3", "Licence 3"),
				new Licence("M1", "Master 1"),
				new Licence("M2", "Master 2"),
				new Licence("D", "Doctorat")
		);

		TreeItem<Reference> rootItem = new TreeItem<>(new Licence());

		licences.forEach(licence -> {
			TreeItem<Reference> treeItem = new TreeItem<>(licence);
			licence.getOptions().forEach(option -> treeItem.getChildren().add(new TreeItem<>(option)));
			rootItem.getChildren().add(treeItem);
		});

		colLicence.setCellValueFactory((TreeTableColumn.CellDataFeatures<Reference, String> p) -> new ReadOnlyStringWrapper(p.getValue().getValue().getCode()));
		colLicence.setCellFactory(p -> textFieldTreeTableCell);

		colOption.setCellValueFactory((TreeTableColumn.CellDataFeatures<Reference, String> p) -> new ReadOnlyStringWrapper(p.getValue().getValue().getLabel()));
		colOption.setCellFactory(p -> textFieldTreeTableCell);

		rootItem.setExpanded(true);
		treeTableView.setRoot(rootItem);
		treeTableView.setShowRoot(false);

		treeTableView.setEditable(true);
	}

	public void onCancel(Event event) {
		Stage window = (Stage) ((Button) event.getSource()).getScene().getWindow();
		window.close();
	}

}
