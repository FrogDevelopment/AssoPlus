/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.controller;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import fr.frogdevelopment.assoplus.components.controls.Validator;
import fr.frogdevelopment.assoplus.dto.CategoryDto;
import fr.frogdevelopment.assoplus.service.CategoriesService;

import java.net.URL;
import java.util.ResourceBundle;

@Controller("categoriesController")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CategoriesController implements Initializable {

	@FXML
	private TextField txtLabel;
	@FXML
	private TextField txtCode;
	@FXML
	private ListView categories;

	@Autowired
	private CategoriesService categoriesService;

	private ObservableList<CategoryDto> dtos;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		init();
	}

	@SuppressWarnings("unchecked")
	private void init() {
		dtos = categoriesService.getAllData();

		categories.setItems(dtos);

//		categories.setEditable(true);
	}

	public void onSave() {
		init();
	}

	public void onClose(Event event) {
		close(event);
	}

	private void close(Event event) {
		Stage window = (Stage) ((Button) event.getSource()).getScene().getWindow();
		window.close();
	}

	public void onAddCategory() {

		boolean isOk = Validator.validate(txtCode);
		isOk &= Validator.validate(txtLabel);

		if (isOk) {
			CategoryDto categoryDto = new CategoryDto();
			categoryDto.setCode(txtCode.getText());
			categoryDto.setLabel(txtLabel.getText());
			dtos.add(categoryDto);
		}
	}

	public void onRemoveCategory() {

		CategoryDto selectedItem = (CategoryDto) categories.getSelectionModel().getSelectedItem();

		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setHeaderText("ATTENTION");
		dialog.setContentText("Vous allez supprimer une Catégorie, voulez-vous continuer ?");
		dialog.getDialogPane().getButtonTypes().add(ButtonType.YES);
		dialog.getDialogPane().getButtonTypes().add(ButtonType.NO);

		dialog.showAndWait()
				.filter(response -> response == ButtonType.YES)
				.ifPresent(response -> removeCategory(selectedItem));
	}

	private void removeCategory(final CategoryDto selectedItem) {
		dtos.remove(selectedItem);
		if (selectedItem.getId() != 0) {
			categoriesService.deleteData(selectedItem);
		}
	}

}
