/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.controller;

import fr.frogdevelopment.assoplus.Main;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller("mainController")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MainController implements Initializable {

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	public void onLicence(ActionEvent event) {
		MenuItem source = (MenuItem) (event.getSource());
		Window parent = source.getParentPopup().getOwnerWindow();
//		parent.hide();

		Parent root = Main.load("/fxml/licence.fxml");
		Stage dialog = new Stage(/*StageStyle.TRANSPARENT*/);
		dialog.initModality(Modality.WINDOW_MODAL);
		dialog.initOwner(parent);
		dialog.setTitle("test");
		dialog.setScene(new Scene(root, 450, 450));
		dialog.show();

	}
}
