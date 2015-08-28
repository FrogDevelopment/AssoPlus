/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.controller;

import javafx.application.Platform;
import javafx.fxml.Initializable;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MainController implements Initializable {

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	public void onExit() {
		Platform.exit();
	}
}
