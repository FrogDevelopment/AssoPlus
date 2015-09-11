/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import fr.frogdevelopment.assoplus.components.controls.MaskHelper;
import fr.frogdevelopment.assoplus.components.controls.Validator;
import fr.frogdevelopment.assoplus.dto.EventDto;
import fr.frogdevelopment.assoplus.service.EventsService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static fr.frogdevelopment.assoplus.components.controls.Validator.validateNotNull;

@Controller("eventController")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EventController extends AbstractCustomController {

    private static final int MAX_CHAR = 500;

    private DateTimeFormatter dateTimeFormatter;

    @Autowired
    private EventsService eventsService;

    @FXML
    private Label lblError;
    @FXML
    private TextField txtTitle;
    @FXML
    private DatePicker dpDate;
    @FXML
    private Label countChar;
    @FXML
    private TextArea taText;

    @FXML
    private Button btnPrevious;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnNew;
    @FXML
    private Button btnNext;

    private ObservableList<EventDto> data;
    private EventDto eventDto;
    private int currentIndex;

    @Override
    public void initialize() {
        dateTimeFormatter = DateTimeFormatter.ofPattern(getMessage("global.date.format"));

        MaskHelper.addMaskDate(dpDate);
        dpDate.setPromptText(getMessage("global.date.format.prompt"));
        dpDate.setConverter(new StringConverter<LocalDate>() {
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
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateTimeFormatter);
                } else {
                    return null;
                }
            }
        });

        taText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                countChar.setText(newValue.length() + "/" + MAX_CHAR);
            } else {
                countChar.setText("0/" + MAX_CHAR);
            }
        });

        taText.setOnKeyTyped(event -> {
            String text = taText.getText();
            if (text == null) {
                return;
            }

            int length = text.length();
            if (length >= MAX_CHAR - 1) {
                taText.setText(text.substring(0, MAX_CHAR - 1));
                taText.positionCaret(length);
            }
        });
    }

    void newData(ObservableList<EventDto> data) {
        this.data = data;
        eventDto = new EventDto();

        currentIndex = data.size() - 1;

        setData();
    }

    void updateData(ObservableList<EventDto> data, int index) {
        this.data = data;
        eventDto = data.get(index);
        currentIndex = index;

        setData();
    }

    private void setData() {
        txtTitle.setText(eventDto.getTitle());
        dpDate.setValue(LocalDate.parse(eventDto.getDate(), dateTimeFormatter));
        taText.setText(eventDto.getText());
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

    public void save() {
        boolean isOk = Validator.validateNoneBlank(txtTitle, taText);
        isOk &= validateNotNull(dpDate);

        if (isOk) {
            eventDto.setTitle(txtTitle.getText());
            eventDto.setDate(dpDate.getValue().format(dateTimeFormatter));
            eventDto.setText(taText.getText());

            if (eventDto.getId() == 0) {
                eventsService.saveData(eventDto);
                data.add(eventDto);
            } else {
                eventsService.updateData(eventDto);
            }

        } else {
            lblError.setText(getMessage("global.warning.msg.check"));
        }
    }

}
