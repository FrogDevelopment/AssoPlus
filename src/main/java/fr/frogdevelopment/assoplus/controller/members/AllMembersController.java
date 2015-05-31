package fr.frogdevelopment.assoplus.controller.members;

import fr.frogdevelopment.assoplus.dto.MemberDTO;
import fr.frogdevelopment.assoplus.service.MembersService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller("allMembersController")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AllMembersController implements Initializable {

	private static final Logger LOGGER = LoggerFactory.getLogger(AllMembersController.class);

	@Autowired
	private MembersService membersService;

	@FXML
	private TableView<MemberDTO> table;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			table.setItems(membersService.getAllData());
		} catch (Exception e) {
			LOGGER.error("FIXME", e);
		}
	}

}
