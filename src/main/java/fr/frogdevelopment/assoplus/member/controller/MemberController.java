/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.member.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import fr.frogdevelopment.assoplus.core.controller.AbstractCreateUpdateDialogController;
import fr.frogdevelopment.assoplus.core.controls.MaskHelper;
import fr.frogdevelopment.assoplus.core.controls.Validator;
import fr.frogdevelopment.assoplus.member.dto.Degree;
import fr.frogdevelopment.assoplus.member.dto.Member;
import fr.frogdevelopment.assoplus.member.dto.Option;
import fr.frogdevelopment.assoplus.member.service.DegreeService;
import fr.frogdevelopment.assoplus.member.service.MembersService;
import fr.frogdevelopment.assoplus.member.service.OptionsService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Controller
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MemberController extends AbstractCreateUpdateDialogController<Member> {

    private DateTimeFormatter dateTimeFormatter;

    @Autowired
    private MembersService membersService;

    @Autowired
    private DegreeService degreeService;

    @Autowired
    private OptionsService optionsService;

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
    public ComboBox<Degree> cbDegree;
    @FXML
    public ComboBox<Option> cbOption;
    @FXML
    private TextField txtPhone;
    @FXML
    private CheckBox cbSubscription;
    @FXML
    private CheckBox cbAnnals;

    private ObservableList<Degree> degrees;
    private ObservableList<Option> optionDtos;

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
        dpBirthday.focusedProperty().addListener((observable, oldValue, newValue) -> {
                    if (oldValue) {
                        String value = dpBirthday.getEditor().getText();
                        dpBirthday.setValue(getLocalDate(value));
                    }
                }
        );

        degrees = FXCollections.observableArrayList(degreeService.getAll());
        cbDegree.setItems(degrees);
        optionDtos = FXCollections.observableArrayList(optionsService.getAll());
        cbDegree.setConverter(new StringConverter<Degree>() {

            private final Map<String, Degree> _cache = new HashMap<>();

            @Override
            public String toString(Degree item) {
                _cache.put(item.getLabel(), item);
                return item.getLabel();
            }

            @Override
            public Degree fromString(String label) {
                return _cache.get(label);
            }
        });

        cbDegree.setOnAction(event -> {
            final Degree selectedItem = cbDegree.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                final String codeLicence = selectedItem.getCode();
                final List<Option> dtos = optionDtos
                        .stream()
                        .filter(o -> codeLicence.equals(o.getDegreeCode()))
                        .collect(Collectors.toList());
                cbOption.setItems(FXCollections.observableArrayList(dtos));
            } else {
                cbOption.setItems(null);
            }
        });

        cbOption.setConverter(new StringConverter<Option>() {

            private final Map<String, Option> _cache = new HashMap<>();

            @Override
            public String toString(Option item) {
                if (item == null) return null;

                _cache.put(item.getLabel(), item);
                return item.getLabel();
            }

            @Override
            public Option fromString(String label) {
                return _cache.get(label);
            }
        });
    }

    private LocalDate getLocalDate(String string) {
        if (string != null && !string.isEmpty()) {
            return LocalDate.parse(string, dateTimeFormatter);
        } else {
            return null;
        }
    }

    @Override
    protected Member newEntity() {
        return new Member();
    }

    protected void setData() {
        txtStudentNumber.setText(entityDto.getStudentNumber());
        txtStudentNumber.setDisable((entityDto.getId() != 0));
        txtLastname.setText(entityDto.getLastname());
        txtFirstname.setText(entityDto.getFirstname());
        dpBirthday.setValue(entityDto.getBirthday());
        txtEmail.setText(entityDto.getEmail());

        degrees.forEach(degreeDto -> {
            if (degreeDto.getCode().equals(entityDto.getDegree().getCode())) {
                cbDegree.getSelectionModel().select(degreeDto);
            }
        });

        cbOption.setItems(null);
        final String codeDegree = entityDto.getDegree().getCode();
        if (StringUtils.isNoneBlank(codeDegree)) {
            final List<Option> dtos = optionDtos
                    .stream()
                    .filter(o -> codeDegree.equals(o.getDegreeCode()))
                    .collect(Collectors.toList());
            cbOption.setItems(FXCollections.observableArrayList(dtos));

            optionDtos.forEach(optionDto -> {
                if (optionDto.getCode().equals(entityDto.getOption().getCode())) {
                    cbOption.getSelectionModel().select(optionDto);
                }
            });
        }

        txtPhone.setText(entityDto.getPhone());
        cbSubscription.setSelected(entityDto.getSubscription());
        cbAnnals.setSelected(entityDto.getAnnals());

        txtStudentNumber.requestFocus();
    }

    // see http://howtodoinjava.com/2014/11/11/java-regex-validate-email-address/
    private static final String REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern pattern = Pattern.compile(REGEX);

    @Override
    protected boolean check() {
        boolean isOk = Validator.validateNoneBlank(txtStudentNumber, txtLastname, txtFirstname);

        String studentNumber = txtStudentNumber.getText();
        if (entityDto.getId() == 0) {
            isOk &= Validator.validate(() -> entities
                            .stream()
                            .noneMatch(dto -> dto.getStudentNumber().equals(studentNumber)),
                    "member.error.msg.already.present",
                    txtStudentNumber);
        }

        if (StringUtils.isNotBlank(txtEmail.getText())) {
            isOk &= Validator.validate(() -> pattern.matcher(txtEmail.getText()).matches(),
                    "member.error.msg.email.incorrect",
                    txtEmail);
        }

        return isOk;
    }

    @Override
    protected void clear() {
        Validator.clear(txtStudentNumber, txtLastname, txtFirstname);
    }

    protected void save() {
        entityDto.setStudentNumber(txtStudentNumber.getText());
        entityDto.setLastname(txtLastname.getText());
        entityDto.setFirstname(txtFirstname.getText());

        entityDto.setBirthday(dpBirthday.getValue());
        entityDto.setEmail(txtEmail.getText());
        if (cbDegree.getValue() != null) {
            entityDto.setDegree(cbDegree.getValue());
        } else {
            entityDto.setDegree(null);
        }
        if (cbOption.getValue() != null) {
            entityDto.setOption(cbOption.getValue());
        } else {
            entityDto.setOption(null);
        }
        entityDto.setPhone(txtPhone.getText());

        entityDto.setSubscription(cbSubscription.isSelected());
        entityDto.setAnnals(cbAnnals.isSelected());

        if (entityDto.getId() == 0) {
            membersService.saveData(entityDto);
            entities.add(entityDto);
        } else {
            membersService.updateData(entityDto);
        }
    }

}
