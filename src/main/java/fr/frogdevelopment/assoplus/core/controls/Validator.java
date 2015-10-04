package fr.frogdevelopment.assoplus.core.controls;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.Control;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Tooltip;

import javafx.scene.image.ImageView;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.decoration.Decorator;
import org.controlsfx.control.decoration.GraphicDecoration;

import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class Validator {

    private static ResourceBundle resourceBundle;

    public static String getMessage(String key) {
        if (resourceBundle == null) {
            resourceBundle = ResourceBundle.getBundle("label");
        }

        return resourceBundle.getString(key);
    }

    public static void clear(Control control) {
        control.setStyle("-fx-border-color: null"); // todo use CSS
        Decorator.removeAllDecorations(control);
    }

    public static void clear(Control... controls) {
        for (Control control : controls) {
            clear(control);
        }
    }

    // Validate generic

    public static boolean validate(Callable<Boolean> check, Consumer<Control> onOK, Consumer<Control> onKO, Control control) {
        try {
            // clean all  to avoid adding a second decoration
            clear(control);
            boolean isOk = check.call();
            if (isOk) {
                if (onOK != null) {
                    onOK.accept(control);
                }
            } else {
                control.setStyle("-fx-border-color: red"); // todo use CSS
                Node decoration = new ImageView("/img/dialog-error_16.png");
                Decorator.addDecoration(control, new GraphicDecoration(decoration, Pos.CENTER_RIGHT));
                if (onKO != null) {
                    onKO.accept(control);
                }
            }

            return isOk;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static boolean validate(Callable<Boolean> check, Control control) {
        return validate(check, null, null, control);
    }

    public static boolean validate(Callable<Boolean> check, Consumer<Control> onOK, Consumer<Control> onKO, Control... controls) {
        boolean isOk = true;
        for (Control control : controls) {
            isOk &= validate(check, onOK, onKO, control);
        }

        return isOk;
    }

    public static boolean validate(Callable<Boolean> check, String keyErrorMsg, Control control) {
        return validate(check, Validator::clearTooltip, control1 -> setTooltip(control, keyErrorMsg), control);
    }

    public static boolean validate(Callable<Boolean> check, String keyErrorMsg, Control... controls) {
        boolean isOk = true;

        for (Control control : controls) {
            isOk &= validate(check, keyErrorMsg, control);
        }

        return isOk;
    }

    // TextInputControl
    public static boolean validateNotBlank(String keyErrorMsg, TextInputControl textInputControl) {
        return validate(() -> StringUtils.isNotBlank(textInputControl.getText()), Validator::clearTooltip, control -> setTooltip(control, keyErrorMsg), textInputControl);
    }

    public static boolean validateNotBlank(TextInputControl textInputControl) {
        return validateNotBlank("global.error.msg.required", textInputControl);
    }

    public static boolean validateNoneBlank(String keyErrorMsg, TextInputControl... textInputControles) {
        boolean isOk = true;
        for (TextInputControl textInputControl : textInputControles) {
            isOk &= validateNotBlank(keyErrorMsg, textInputControl);
        }
        return isOk;
    }

    public static boolean validateNoneBlank(TextInputControl... textInputControles) {
        return validateNoneBlank("global.error.msg.required", textInputControles);
    }

    // ComboBoxBase
    public static boolean validateNotNull(String keyErrorMsg, ComboBoxBase comboBoxBase) {
        return validate(() -> comboBoxBase.getValue() != null, Validator::clearTooltip, control -> setTooltip(control, keyErrorMsg), comboBoxBase);
    }

    public static boolean validateNoneNull(String keyErrorMsg, ComboBoxBase... comboBoxBases) {
        boolean isOk = true;
        for (ComboBoxBase comboBoxBase : comboBoxBases) {
            isOk &= validateNotNull(keyErrorMsg, comboBoxBase);
        }
        return isOk;
    }

    public static boolean validateNotNull(ComboBoxBase comboBoxBase) {
        return validateNotNull("global.error.msg.required", comboBoxBase);
    }

    public static boolean validateNoneNull(ComboBoxBase... comboBoxBases) {
        return validateNoneNull("global.error.msg.required", comboBoxBases);
    }


    // Tooltip

    public static void setTooltip(Control control, String keyMsg) {
        Tooltip tooltip = new Tooltip(getMessage(keyMsg));
        tooltip.setAutoHide(true);
        control.setTooltip(tooltip);
    }

    public static void clearTooltip(Control control) {
        control.setTooltip(null);
    }

}
