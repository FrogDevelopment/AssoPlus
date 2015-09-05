/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import fr.frogdevelopment.assoplus.components.controls.MaskHelper;
import fr.frogdevelopment.assoplus.components.controls.Validator;
import fr.frogdevelopment.assoplus.dto.DegreeDto;
import fr.frogdevelopment.assoplus.dto.MemberDto;
import fr.frogdevelopment.assoplus.dto.OptionDto;
import fr.frogdevelopment.assoplus.service.LicencesService;
import fr.frogdevelopment.assoplus.service.MembersService;
import fr.frogdevelopment.assoplus.service.OptionsService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static fr.frogdevelopment.assoplus.components.controls.Validator.validateNotBlank;

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
    public ComboBox<DegreeDto> cbDegree;
    @FXML
    public ComboBox<OptionDto> cbOption;
    @FXML
    private TextField txtPhone;
    @FXML
    private CheckBox cbSubscription;
    @FXML
    private CheckBox cbAnnals;

    @FXML
    private Button btnPrevious;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnNew;
    @FXML
    private Button btnNext;

    private MemberDto memberDto;
    private ObservableList<MemberDto> data;
    private ObservableList<DegreeDto> degreeDtos;
    private ObservableList<OptionDto> optionDtos;
    private int currentIndex;

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

        degreeDtos = FXCollections.observableArrayList(licencesService.getAll());
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

    protected LocalDate getLocalDate(String string) {
        if (string != null && !string.isEmpty()) {
            return LocalDate.parse(string, dateTimeFormatter);
        } else {
            return null;
        }
    }

    void newData(ObservableList<MemberDto> data) {
        this.data = data;
        memberDto = new MemberDto();

        currentIndex = data.size() - 1;

        todo();
    }

    void updateData(ObservableList<MemberDto> data, int index) {
        this.data = data;
        memberDto = data.get(index);
        currentIndex = index;

        todo();
    }

    private void todo() {
        txtStudentNumber.setText(memberDto.getStudentNumber());
        txtStudentNumber.setDisable((memberDto.getId() != 0));
        txtLastname.setText(memberDto.getLastname());
        txtFirstname.setText(memberDto.getFirstname());
        dpBirthday.setValue(memberDto.getBirthday());
        txtEmail.setText(memberDto.getEmail());

        degreeDtos.stream().forEach(degreeDto -> {
            if (degreeDto.getCode().equals(memberDto.getDegreeCode())) {
                cbDegree.getSelectionModel().select(degreeDto);
            }
        });

        cbOption.setItems(null);
        final String codeDegree = memberDto.getDegreeCode();
        if (StringUtils.isNoneBlank(codeDegree)) {
            final List<OptionDto> dtos = optionDtos
                    .stream()
                    .filter(o -> codeDegree.equals(o.getDegreeCode()))
                    .collect(Collectors.toList());
            cbOption.setItems(FXCollections.observableArrayList(dtos));

            optionDtos.stream().forEach(optionDto -> {
                if (optionDto.getCode().equals(memberDto.getOptionCode())) {
                    cbOption.getSelectionModel().select(optionDto);
                }
            });
        }

        txtPhone.setText(memberDto.getPhone());
        cbSubscription.setSelected(memberDto.getSubscription());
        cbAnnals.setSelected(memberDto.getAnnals());

        txtStudentNumber.requestFocus();

        btnPrevious.setDisable(currentIndex == 0);
        btnNext.setDisable(currentIndex == data.size() - 1);
    }

    public void previousData() {
        updateData(data, --currentIndex);
    }

    public void saveData() {
        save();
    }

    public void newData() {
        newData(data);
    }

    public void nextData() {
        updateData(data, ++currentIndex);
    }

    // see http://howtodoinjava.com/2014/11/11/java-regex-validate-email-address/
    private static final String REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern pattern = Pattern.compile(REGEX);

    private boolean save() {
        boolean isOk = Validator.validateNoneBlank(txtStudentNumber, txtLastname, txtFirstname);

        String studentNumber = txtStudentNumber.getText();
        if (memberDto.getId() == 0) {
            isOk &= Validator.validate(() -> data
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

        if (isOk) {
            lblError.setText(null);

            memberDto.setStudentNumber(studentNumber);
            memberDto.setLastname(txtLastname.getText());
            memberDto.setFirstname(txtFirstname.getText());

            memberDto.setBirthday(dpBirthday.getValue());
            memberDto.setEmail(txtEmail.getText());
            if (cbDegree.getValue() != null) {
                memberDto.setDegreeCode(cbDegree.getValue().getCode());
            } else {
                memberDto.setDegreeCode(null);
            }
            if (cbOption.getValue() != null) {
                memberDto.setOptionCode(cbOption.getValue().getCode());
            } else {
                memberDto.setOptionCode(null);
            }
            memberDto.setPhone(txtPhone.getText());

            memberDto.setSubscription(cbSubscription.isSelected());
            memberDto.setAnnals(cbAnnals.isSelected());

            if (memberDto.getId() == 0) {
                membersService.saveData(memberDto);
                data.add(memberDto);
            } else {
                membersService.updateData(memberDto);
            }
        } else {
            lblError.setText(getMessage("global.error.msg.check"));
        }

        return isOk;
    }

}
