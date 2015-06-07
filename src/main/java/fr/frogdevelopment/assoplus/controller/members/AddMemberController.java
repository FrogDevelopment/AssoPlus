/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.controller.members;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import fr.frogdevelopment.assoplus.dto.MemberDtok;
import fr.frogdevelopment.assoplus.service.MembersService;

import java.net.URL;
import java.util.ResourceBundle;

//@Controller("addMemberController")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AddMemberController implements Initializable {

	private static final Logger LOGGER = LoggerFactory.getLogger(AddMemberController.class);

	@Autowired
	private MembersService membersService;

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
	private Label lbNumber;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	public void saveData() {
		MemberDtok member = new MemberDtok();
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
	}

}
