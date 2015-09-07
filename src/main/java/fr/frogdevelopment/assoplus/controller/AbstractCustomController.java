/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import static fr.frogdevelopment.assoplus.utils.ApplicationUtils.ICON_16;
import static fr.frogdevelopment.assoplus.utils.ApplicationUtils.ICON_32;
import static fr.frogdevelopment.assoplus.utils.ApplicationUtils.ICON_48;
import static fr.frogdevelopment.assoplus.utils.ApplicationUtils.load;
import static javafx.scene.control.Alert.AlertType.*;

abstract class AbstractCustomController implements Initializable {

    @FXML
    protected Pane child;

    private ResourceBundle resources;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        this.initialize();
    }

    protected String getMessage(String key) {
        return resources.getString(key);
    }

    protected Window getParent() {
        return child.getScene().getWindow();
    }

    protected abstract void initialize();

    protected Stage openDialog(String url) {
        return openDialog(url, null);
    }

    protected <T> Stage openDialog(String url, Consumer<T> controllerConsumer) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.WINDOW_MODAL);
//        dialog.initStyle(StageStyle.UTILITY);
        dialog.initOwner(getParent());
        dialog.getIcons().addAll(ICON_16, ICON_32, ICON_48);

        Parent root = load(url, controllerConsumer);
        dialog.setScene(new Scene(root));

        return dialog;
    }


    // TODO http://ux.stackexchange.com/questions/9946/should-i-use-yes-no-or-ok-cancel-on-my-message-box

    protected void showInformation(String headerKey) {
        showInformation(headerKey, null);
    }

    protected void showInformation(String headerKey, String message) {

        Alert alert = new Alert(INFORMATION);
        alert.setHeaderText(getMessage(headerKey));
        if (StringUtils.isNotBlank(message)) {
            alert.setContentText(message);
        }

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/img/dialog-information_16.png"));

        alert.show();
    }

    protected void showYesNoDialog(String message, Consumer<? super ButtonType> onYes) {
        showYesNoDialog(null, message, onYes);
    }

    protected void showYesNoDialog(String headerKey, String message, Consumer<? super ButtonType> onYes) {
        Alert alert = new Alert(CONFIRMATION);
        if (StringUtils.isNotBlank(headerKey)) {
            alert.setHeaderText(getMessage(headerKey));
        }
        alert.setContentText(message);

        alert.getButtonTypes().clear();
        ButtonType[] buttons = {ButtonType.YES, ButtonType.NO};
        alert.getButtonTypes().addAll(buttons);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/img/dialog-confirm_16.png"));

        alert.showAndWait()
                .filter(response -> response == ButtonType.YES)
                .ifPresent(onYes);
    }

    protected void showConfirmation(String message, Consumer<? super ButtonType> onOK) {
        showConfirmation(null, message, onOK);
    }

    protected void showConfirmation(String headerKey, String message, Consumer<? super ButtonType> onOK) {
        Alert alert = new Alert(CONFIRMATION);
        if (StringUtils.isNotBlank(headerKey)) {
            alert.setHeaderText(getMessage(headerKey));
        }
        alert.setContentText(message);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/img/dialog-confirm_16.png"));

        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(onOK);
    }

    protected void showWarning(String headerKey) {
        showWarning(headerKey, null);
    }

    protected void showWarning(String headerKey, String message) {

        Alert alert = new Alert(WARNING);
        alert.setHeaderText(getMessage(headerKey));
        if (StringUtils.isNotBlank(message)) {
            alert.setContentText(message);
        }

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/img/dialog-warning_16.png"));

        alert.show();
    }

    protected void showError(Exception e) {

        Alert alert = new Alert(ERROR);
        alert.setHeaderText(getMessage("global.error.header"));
        alert.setContentText(ExceptionUtils.getMessage(e));

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/img/dialog-error_16.png"));

        alert.show();
    }

    protected void showError(String headerKey, Exception e) {

        Alert alert = new Alert(ERROR);
        alert.setHeaderText(getMessage(headerKey));
        alert.setContentText(ExceptionUtils.getMessage(e));

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/img/dialog-error_16.png"));

        alert.show();
    }

    protected void showError(String headerKey, String messageKey) {

        Alert alert = new Alert(ERROR);
        alert.setHeaderText(getMessage(headerKey));
        alert.setContentText(getMessage(messageKey));

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/img/dialog-error_16.png"));

        alert.show();
    }


    protected Optional<ButtonType> showCustomConfirmation(String headerKey, String contentKey, ButtonType overrideBtn, ButtonType ignoreBtn) {
        Alert alert = new Alert(CONFIRMATION);
        alert.setHeaderText(getMessage(headerKey));
        alert.setContentText(getMessage(contentKey));

        alert.getButtonTypes().clear();
        alert.getButtonTypes().add(overrideBtn);
        alert.getButtonTypes().add(ignoreBtn);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/img/dialog-confirm_16.png"));

        return alert.showAndWait();
    }
}
