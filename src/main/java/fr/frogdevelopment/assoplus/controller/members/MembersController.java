/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.controller.members;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import fr.frogdevelopment.assoplus.dto.MemberDto;
import fr.frogdevelopment.assoplus.service.MembersService;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

@Controller("membersController")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MembersController implements Initializable {

	private static final Logger LOGGER = LoggerFactory.getLogger(MembersController.class);

    @Autowired
	private MembersService membersService;

    @FXML
    private VBox vbTop;

	@FXML
	private VBox vbLeft;

	@FXML
	private TextField txtStudentNumber;
	@FXML
	private TextField txtLastname;
	@FXML
	private TextField txtFirstname;
	@FXML
	private TextField txtBirthday;
	@FXML
	private TextField txtEmail;
	@FXML
	private TextField txtLicence;
	@FXML
	private TextField txtOption;
	@FXML
	private TextField txtPhone;
	@FXML
	private TextField txtAddress;
	@FXML
	private TextField txtPostalCode;
	@FXML
	private TextField txtCity;
	@FXML
	private CheckBox cbFeePaid;

    @FXML
    private TableView<MemberDto> table;

    private ObservableList<MemberDto> data;


    @Override
	public void initialize(URL location, ResourceBundle resources) {
        try {
            data = membersService.getAllData();
            table.setItems(data);
        } catch (Exception e) {
            LOGGER.error("FIXME", e);
        }
	}

    public void saveData() {
        MemberDto member = new MemberDto();
        member.setStudentNumber(Integer.parseInt(txtStudentNumber.getText()));
        member.setLastname(txtLastname.getText());
        member.setFirstname(txtFirstname.getText());
        member.setBirthday(txtBirthday.getText());
        member.setEmail(txtEmail.getText());
        member.setLicence(txtLicence.getText());
        member.setOption(txtOption.getText());
        member.setPhone(txtPhone.getText());
        member.setAddress(txtAddress.getText());
        member.setPostalCode(txtPostalCode.getText());
        member.setCity(txtCity.getText());
        member.setFeePaid(cbFeePaid.isSelected());

        membersService.saveData(member);

        // fixme voir si id maj dans DTO
        data.add(member);
    }

    public void importMembers(Event event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importer des adhérents");
	    File file = fileChooser.showOpenDialog(vbLeft.getScene().getWindow());

	    membersService.importMembers(file);
    }
}
