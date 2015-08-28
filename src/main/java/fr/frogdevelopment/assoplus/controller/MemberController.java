/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import fr.frogdevelopment.assoplus.components.controls.MaskHelper;
import fr.frogdevelopment.assoplus.components.controls.NumberTextField;
import fr.frogdevelopment.assoplus.dto.LicenceDto;
import fr.frogdevelopment.assoplus.dto.MemberDto;
import fr.frogdevelopment.assoplus.dto.OptionDto;
import fr.frogdevelopment.assoplus.service.LicencesService;
import fr.frogdevelopment.assoplus.service.MembersService;
import fr.frogdevelopment.assoplus.service.OptionsService;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static fr.frogdevelopment.assoplus.components.controls.Validator.validateNotBlank;
import static fr.frogdevelopment.assoplus.components.controls.Validator.validateNotNull;

@Controller
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MemberController implements Initializable {

    private DateTimeFormatter dateTimeFormatter;

    private ResourceBundle resources;

    @Autowired
    private MembersService membersService;

    @Autowired
    private LicencesService licencesService;


    @Autowired
    private OptionsService optionsService;

    @FXML
    private VBox vbTop;
    @FXML
    private Button btnShowHide;
    @FXML
    private NumberTextField txtStudentNumber;
    @FXML
    private TextField txtLastname;
    @FXML
    private TextField txtFirstname;
    @FXML
    private DatePicker dpBirthday;
    @FXML
    private TextField txtEmail;
    @FXML
    public ComboBox<LicenceDto> cbLicence;
    @FXML
    public ComboBox<OptionDto> cbOption;
    @FXML
    private TextField txtPhone;
    @FXML
    private TextField txtAddress;
    @FXML
    private TextField txtPostalCode;
    @FXML
    private TextField txtCity;

    private MemberDto memberDto;
    private ObservableList<MemberDto> data;
    private ObservableList<OptionDto> optionDtos;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        dateTimeFormatter = DateTimeFormatter.ofPattern(resources.getString("global.date.format"));

        MaskHelper.addMaskPhone(txtPhone);

        MaskHelper.addMaskDate(dpBirthday);
        dpBirthday.setPromptText(resources.getString("global.date.format.prompt"));
        dpBirthday.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateTimeFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                return getLocalDate(string);
            }
        });

        cbLicence.setItems(FXCollections.observableArrayList(licencesService.getAll()));
        optionDtos = FXCollections.observableArrayList(optionsService.getAll());
        cbLicence.setConverter(new StringConverter<LicenceDto>() {

            private final Map<String, LicenceDto> _cache = new HashMap<>();

            @Override
            public String toString(LicenceDto item) {
                _cache.put(item.getLabel(), item);
                return item.getLabel();
            }

            @Override
            public LicenceDto fromString(String label) {
                return _cache.get(label);
            }
        });

        cbLicence.setOnAction(event -> {
            final LicenceDto selectedItem = cbLicence.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                final String codeLicence = selectedItem.getCode();
                final List<OptionDto> dtos = optionDtos
                        .stream()
                        .filter(o -> codeLicence.equals(o.getLicenceCode()))
                        .collect(Collectors.toList());
                cbOption.setItems(FXCollections.observableArrayList(dtos));
            } else {
                cbOption.setItems(null);
            }
        });

        cbOption.setConverter(new StringConverter<OptionDto>() {

            private final Map<String, OptionDto> _cache = new HashMap<>();

            @Override
            public String toString(OptionDto item) {
                if (item == null) return null;

                _cache.put(item.getLabel(), item);
                return item.getLabel();
            }

            @Override
            public OptionDto fromString(String label) {
                return _cache.get(label);
            }
        });
    }

    protected LocalDate getLocalDate(String string) {
        if (string != null && !string.isEmpty()) {
            return LocalDate.parse(string, dateTimeFormatter);
        } else {
            return null;
        }
    }

    public void setData(ObservableList<MemberDto> data, MemberDto dto) {
        memberDto = dto;

        if (memberDto == null) {
            memberDto = new MemberDto();
        }

        txtFirstname.textProperty().bindBidirectional(dto.firstnameProperty());
        txtLastname.textProperty().bindBidirectional(dto.lastnameProperty());
        dpBirthday.valueProperty().bindBidirectional(dto.birthdayProperty());
        txtStudentNumber.textProperty().bindBidirectional(dto.studentNumberProperty(), new NumberStringConverter());
        txtEmail.textProperty().bindBidirectional(dto.emailProperty());
//        cbLicence.setValue();
//                cbOption
        txtPhone.textProperty().bindBidirectional(dto.phoneProperty());
        txtAddress.textProperty().bindBidirectional(dto.addressProperty());
        txtPostalCode.textProperty().bindBidirectional(dto.postalCodeProperty());
        txtCity.textProperty().bindBidirectional(dto.cityProperty());

//        txtFirstname.setText(dto.getFirstname());
//        txtLastname.setText(dto.getLastname());
//        dpBirthday.setValue(getLocalDate(dto.getBirthday()));
//        txtStudentNumber.setValue(dto.getStudentNumber());
//        txtEmail.setText(dto.getEmail());
////        cbLicence.setValue();
////                cbOption
//        txtPhone.setText(dto.getPhone());
//        txtAddress.setText(dto.getAddress());
//        txtPostalCode.setText(dto.getPostalCode());
//        txtCity.setText(dto.getCity());
    }

    public void saveData() {
        boolean isOk = validateNotBlank(txtStudentNumber, txtLastname, txtFirstname, txtEmail)
                && validateNotNull(dpBirthday, cbLicence, cbOption);

        if (isOk) {
            memberDto.setStudentNumber(Integer.parseInt(txtStudentNumber.getText()));
            memberDto.setLastname(txtLastname.getText());
            memberDto.setFirstname(txtFirstname.getText());
            memberDto.setBirthday(dpBirthday.getValue());
            memberDto.setEmail(txtEmail.getText());
            memberDto.setLicenceCode(cbLicence.getValue().getCode());
            memberDto.setOptionCode(cbOption.getValue().getCode());
            memberDto.setPhone(txtPhone.getText());
            memberDto.setAddress(txtAddress.getText());
            memberDto.setPostalCode(txtPostalCode.getText());
            memberDto.setCity(txtCity.getText());

            if (memberDto.getId() == null) {
                membersService.saveData(memberDto);
                data.add(memberDto);
            } else {
                membersService.updateData(memberDto);
            }
        }
    }

}
