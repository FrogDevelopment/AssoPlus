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
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import static fr.frogdevelopment.assoplus.utils.ApplicationUtils.ICON_16;
import static fr.frogdevelopment.assoplus.utils.ApplicationUtils.ICON_32;
import static fr.frogdevelopment.assoplus.utils.ApplicationUtils.ICON_48;
import static fr.frogdevelopment.assoplus.utils.ApplicationUtils.load;

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

    protected void showYesNoDialog(String messageKey, Consumer<? super ButtonType> onYes) {

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(getMessage("global.warning.title"));
        dialog.setContentText(messageKey);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.YES);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.NO);

        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/img/shield-warning_16.png"));


        dialog.showAndWait()
                .filter(response -> response == ButtonType.YES)
                .ifPresent(onYes);
    }

    protected void showWarning(String messageKey, String message) {

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(getMessage("global.warning.title"));
        alert.setHeaderText(getMessage(messageKey));
        alert.setContentText(message);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/img/shield-warning_16.png"));

        alert.show();
    }

    protected void showError(String messageKey, String message) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(getMessage("global.error.title"));
        alert.setHeaderText(getMessage(messageKey));
        alert.setContentText(message);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/img/shield-error_16.png"));

        alert.show();
    }
}
