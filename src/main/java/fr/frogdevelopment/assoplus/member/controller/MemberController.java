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
import fr.frogdevelopment.assoplus.member.dto.DegreeDto;
import fr.frogdevelopment.assoplus.member.dto.MemberDto;
import fr.frogdevelopment.assoplus.member.dto.OptionDto;
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
public class MemberController extends AbstractCreateUpdateDialogController<MemberDto> {

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
    public ComboBox<DegreeDto> cbDegree;
    @FXML
    public ComboBox<OptionDto> cbOption;
    @FXML
    private TextField txtPhone;
    @FXML
    private CheckBox cbSubscription;
    @FXML
    private CheckBox cbAnnals;

    private ObservableList<DegreeDto> degreeDtos;
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
        dpBirthday.focusedProperty().addListener((observable, oldValue, newValue) -> {
                    if (oldValue) {
                        String value = dpBirthday.getEditor().getText();
                        dpBirthday.setValue(getLocalDate(value));
                    }
                }
        );

        degreeDtos = FXCollections.observableArrayList(degreeService.getAll());
        cbDegree.setItems(degreeDtos);
        optionDtos = FXCollections.observableArrayList(optionsService.getAll());
        cbDegree.setConverter(new StringConverter<DegreeDto>() {

            private final Map<String, DegreeDto> _cache = new HashMap<>();

            @Override
            public String toString(DegreeDto item) {
                _cache.put(item.getLabel(), item);
                return item.getLabel();
            }

            @Override
            public DegreeDto fromString(String label) {
                return _cache.get(label);
            }
        });

        cbDegree.setOnAction(event -> {
            final DegreeDto selectedItem = cbDegree.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                final String codeLicence = selectedItem.getCode();
                final List<OptionDto> dtos = optionDtos
                        .stream()
                        .filter(o -> codeLicence.equals(o.getDegreeCode()))
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

    private LocalDate getLocalDate(String string) {
        if (string != null && !string.isEmpty()) {
            return LocalDate.parse(string, dateTimeFormatter);
        } else {
            return null;
        }
    }

    @Override
    protected MemberDto newEntity() {
        return new MemberDto();
    }

    protected void setData() {
        txtStudentNumber.setText(entityDto.getStudentNumber());
        txtStudentNumber.setDisable((entityDto.getId() != 0));
        txtLastname.setText(entityDto.getLastname());
        txtFirstname.setText(entityDto.getFirstname());
        dpBirthday.setValue(entityDto.getBirthday());
        txtEmail.setText(entityDto.getEmail());

        degreeDtos.forEach(degreeDto -> {
            if (degreeDto.getCode().equals(entityDto.getDegreeCode())) {
                cbDegree.getSelectionModel().select(degreeDto);
            }
        });

        cbOption.setItems(null);
        final String codeDegree = entityDto.getDegreeCode();
        if (StringUtils.isNoneBlank(codeDegree)) {
            final List<OptionDto> dtos = optionDtos
                    .stream()
                    .filter(o -> codeDegree.equals(o.getDegreeCode()))
                    .collect(Collectors.toList());
            cbOption.setItems(FXCollections.observableArrayList(dtos));

            optionDtos.forEach(optionDto -> {
                if (optionDto.getCode().equals(entityDto.getOptionCode())) {
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
            entityDto.setDegreeCode(cbDegree.getValue().getCode());
        } else {
            entityDto.setDegreeCode(null);
        }
        if (cbOption.getValue() != null) {
            entityDto.setOptionCode(cbOption.getValue().getCode());
        } else {
            entityDto.setOptionCode(null);
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