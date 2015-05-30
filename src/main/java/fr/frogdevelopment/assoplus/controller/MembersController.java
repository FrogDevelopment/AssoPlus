package fr.frogdevelopment.assoplus.controller;

import fr.frogdevelopment.assoplus.dto.MemberDTO;
import fr.frogdevelopment.assoplus.service.MembersService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MembersController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MembersController.class);

    @Autowired
    private MembersService membersService;

    private ObservableList<MemberDTO> data;

    @FXML
    private TextField txtNumber;
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
    private TableView table;

    @Override
    @SuppressWarnings("unchecked")
    public void initialize(URL location, ResourceBundle resources) {
        try {
            data = membersService.getAllData();

            table.setItems(null);
            table.setItems(data);

        } catch (Exception e) {
            LOGGER.error("FIXME",e);
        }
    }

    public void saveData() {
        MemberDTO member = new MemberDTO();
        member.setNumber(Integer.parseInt(txtNumber.getText()));
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

        membersService.saveData(member);

        data.add(member);
    }

}
