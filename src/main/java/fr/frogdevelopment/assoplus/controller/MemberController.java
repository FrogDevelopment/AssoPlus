/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.controller;

import fr.frogdevelopment.assoplus.components.controls.MaskHelper;
import fr.frogdevelopment.assoplus.components.controls.Validator;
import fr.frogdevelopment.assoplus.dto.LicenceDto;
import fr.frogdevelopment.assoplus.dto.MemberDto;
import fr.frogdevelopment.assoplus.dto.OptionDto;
import fr.frogdevelopment.assoplus.service.LicencesService;
import fr.frogdevelopment.assoplus.service.MembersService;
import fr.frogdevelopment.assoplus.service.OptionsService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static fr.frogdevelopment.assoplus.components.controls.Validator.validateNotBlank;
import static fr.frogdevelopment.assoplus.components.controls.Validator.validateNotNull;

@Controller
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MemberController extends AbstractCustomDialogController {

    private DateTimeFormatter dateTimeFormatter;

    @Autowired
    private MembersService membersService;

    @Autowired
    private LicencesService licencesService;

    @Autowired
    private OptionsService optionsService;

    @FXML
    private Label lblError;
    @FXML
    private TextField txtStudentNumber;
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

    private MemberDto memberDto;
    private ObservableList<MemberDto> data;
    private ObservableList<OptionDto> optionDtos;

    @Override
    protected void initialize() {
        dateTimeFormatter = DateTimeFormatter.ofPattern(getMessage("global.date.format"));

        MaskHelper.addMaskPhone(txtPhone);

        MaskHelper.addMaskDate(dpBirthday);
        dpBirthday.setPromptText(getMessage("global.date.format.prompt"));
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
        this.data = data;
        memberDto = dto;

        txtFirstname.textProperty().bindBidirectional(dto.firstnameProperty());
        txtLastname.textProperty().bindBidirectional(dto.lastnameProperty());
        dpBirthday.valueProperty().bindBidirectional(dto.birthdayProperty());
        txtStudentNumber.textProperty().bindBidirectional(dto.studentNumberProperty());
        txtEmail.textProperty().bindBidirectional(dto.emailProperty());
//        cbLicence.setValue();
//                cbOption
        txtPhone.textProperty().bindBidirectional(dto.phoneProperty());

        txtFirstname.setText(dto.getFirstname());
        txtLastname.setText(dto.getLastname());
        dpBirthday.setValue(dto.getBirthday());
        txtStudentNumber.setText(dto.getStudentNumber());
        txtEmail.setText(dto.getEmail());
//        cbLicence.setValue();
//                cbOption
        txtPhone.setText(dto.getPhone());
    }

    public void saveData() {
        boolean isOk = validateNotBlank(txtStudentNumber, txtLastname, txtFirstname, txtEmail)
                && validateNotNull(dpBirthday, cbLicence, cbOption);

        String studentNumber = txtStudentNumber.getText();
        isOk &= Validator.validate(() -> data
                        .stream()
                        .noneMatch(dto -> dto.getStudentNumber().equals(studentNumber)),
                "member.error.msg.already.present",
                txtStudentNumber);

        if (isOk) {
            lblError.setText(null);

            memberDto.setStudentNumber(studentNumber);
            memberDto.setLastname(txtLastname.getText());
            memberDto.setFirstname(txtFirstname.getText());
            memberDto.setBirthday(dpBirthday.getValue());
            memberDto.setEmail(txtEmail.getText());
            memberDto.setDegreeCode(cbLicence.getValue().getCode());
            memberDto.setOptionCode(cbOption.getValue().getCode());
            memberDto.setPhone(txtPhone.getText());

            if (memberDto.getId() == 0) {
                membersService.saveData(memberDto);
                data.add(memberDto);
            } else {
                membersService.updateData(memberDto);
            }
        }else {
            lblError.setText(getMessage("global.error.msg.check"));
        }

    }

}
