package fr.frogdevelopment.assoplus.controller.members;

import fr.frogdevelopment.assoplus.Main;
import fr.frogdevelopment.assoplus.service.MembersService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller("membersController")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MembersController implements Initializable {

	private static final Logger LOGGER = LoggerFactory.getLogger(MembersController.class);

	private static final String ADD = "/fxml/members/addMember.fxml";
	private static final String ALL = "/fxml/members/table.fxml";
	private static String CURRENT = "";

	@FXML
	private Pane content;

	@Autowired
	private MembersService membersService;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		displayView(ADD);
	}

	private void displayView(String view) {
		if (CURRENT.equals(view)) {
			return;
		}
		content.getChildren().clear();
		content.getChildren().add(Main.load(view));
		CURRENT = view;
	}

	@FXML
	private void onAdd(ActionEvent event) {
		displayView(ADD);
	}

	@FXML
	private void onAll(ActionEvent event) {
		displayView(ALL);
	}

}
