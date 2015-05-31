package fr.frogdevelopment.assoplus.controller;

import javafx.fxml.Initializable;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller("mainController")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MainController implements Initializable {

//	@FXML
//	private SplitPane membersTab;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
}
