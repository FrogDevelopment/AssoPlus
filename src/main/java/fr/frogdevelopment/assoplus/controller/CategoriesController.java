/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.controller;

import fr.frogdevelopment.assoplus.service.CategoriesService;
import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller("categoriesController")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CategoriesController implements Initializable {

	@Autowired
	private CategoriesService categoriesService;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		init();
	}

	@SuppressWarnings("unchecked")
	private void init() {
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

}
