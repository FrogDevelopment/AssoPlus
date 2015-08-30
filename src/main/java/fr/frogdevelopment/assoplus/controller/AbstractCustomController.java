/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.controller;

import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Window;

import java.net.URL;
import java.util.ResourceBundle;

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
}
