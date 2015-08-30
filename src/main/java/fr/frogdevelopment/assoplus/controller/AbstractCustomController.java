/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.controller;

import fr.frogdevelopment.assoplus.dto.OptionDto;
import fr.frogdevelopment.assoplus.dto.ReferenceDto;
import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TreeItem;
import javafx.stage.Window;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

abstract class AbstractCustomController implements Initializable {

    private ResourceBundle resources;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        this.initialize();
    }

    protected String getMessage(String key) {
        return resources.getString(key);
    }

    protected Window getParent(Event event) {
        Node source = (Node) (event.getSource());
        return source.getScene().getWindow();
    }

    protected abstract void initialize();


    protected void showYesNoDialog(String message, Consumer onYes) {

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setHeaderText(getMessage("global.warning.title"));
        dialog.setContentText(message);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.YES);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.NO);

        dialog.showAndWait()
                .filter(response -> response == ButtonType.YES)
                .ifPresent(onYes);
    }
}
