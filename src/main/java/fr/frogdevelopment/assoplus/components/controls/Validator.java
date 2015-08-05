package fr.frogdevelopment.assoplus.components.controls;

import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.TextInputControl;

import org.apache.commons.lang3.StringUtils;

public class Validator {


    // TODO via un listener

    public static boolean validate(TextInputControl textInputControl) {
        boolean isOk = true;
        if (StringUtils.isBlank(textInputControl.getText())) {
            textInputControl.setStyle("-fx-border-color: red");
            isOk = false;
        } else {
            textInputControl.setStyle("-fx-border-color: null");
        }

        return isOk;
    }

    public static boolean validate(ComboBoxBase comboBoxBase) {
        boolean isOk = true;
        if (comboBoxBase.getValue() == null) {
            comboBoxBase.setStyle("-fx-border-color: red");
            isOk = false;
        } else {
            comboBoxBase.setStyle("-fx-border-color: null");
        }

        return isOk;
    }

}
