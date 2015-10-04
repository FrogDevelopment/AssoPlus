/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.event.controller;

import fr.frogdevelopment.assoplus.core.controller.AbstractCreateUpdateDialogController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import fr.frogdevelopment.assoplus.core.controls.MaskHelper;
import fr.frogdevelopment.assoplus.core.controls.Validator;
import fr.frogdevelopment.assoplus.event.dto.EventDto;
import fr.frogdevelopment.assoplus.event.service.EventsService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static fr.frogdevelopment.assoplus.core.controls.Validator.validateNotNull;

@Controller("eventController")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EventController extends AbstractCreateUpdateDialogController<EventDto> {

    private static final int MAX_CHAR = 500;

    private DateTimeFormatter dateTimeFormatter;

    @Autowired
    private EventsService eventsService;

    @FXML
    private TextField txtTitle;
    @FXML
    private DatePicker dpDate;
    @FXML
    private Label countChar;
    @FXML
    private TextArea taText;
    @FXML
    private Button btnPublish;

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

    @Override
    protected EventDto newEntity() {
        return new EventDto();
    }

    protected void setData() {
        txtTitle.setText(entityDto.getTitle());
        if (StringUtils.isNotBlank(entityDto.getDate())) {
            dpDate.setValue(LocalDate.parse(entityDto.getDate(), dateTimeFormatter));
        }
        taText.setText(entityDto.getText());

        if (entityDto.getPublished()) {
            txtTitle.setDisable(true);
            dpDate.setDisable(true);
            taText.setDisable(true);

            btnSave.setDisable(true);
            btnPublish.setDisable(true);
        } else {
            txtTitle.setDisable(false);
            dpDate.setDisable(false);
            taText.setDisable(false);

            btnSave.setDisable(false);
            btnPublish.setDisable(entityDto.getId() == 0);
        }
    }

    @Override
    protected boolean check() {
        boolean isOk = Validator.validateNoneBlank(txtTitle, taText);
        isOk &= validateNotNull(dpDate);

        return isOk;
    }

    @Override
    protected void clear() {
        Validator.clear(txtTitle, taText, dpDate);
    }

    protected void save() {
        entityDto.setTitle(txtTitle.getText());
        entityDto.setDate(dpDate.getValue().format(dateTimeFormatter));
        entityDto.setText(taText.getText());

        if (entityDto.getId() == 0) {
            eventsService.saveData(entityDto);
            entities.add(entityDto);
            btnPublish.setDisable(false);
        } else {
            eventsService.updateData(entityDto);
        }
    }

    public void publishEvent() {
        if (eventsService.publishEvent(entityDto)) {
            entityDto.setPublished(true);
            eventsService.updateData(entityDto);
            btnPublish.setDisable(true);
        }
    }
}
