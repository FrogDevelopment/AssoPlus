/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.core.controller;

import fr.frogdevelopment.assoplus.core.controller.AbstractCustomDialogController;
import javafx.event.ActionEvent;
import javafx.scene.control.Hyperlink;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import fr.frogdevelopment.assoplus.Main;

@Controller
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AboutController extends AbstractCustomDialogController {

    @Override
    protected void initialize() {
    }

    public void onAction(ActionEvent actionEvent) {
        Hyperlink source = (Hyperlink) actionEvent.getSource();
        Main.showDocument(source.getText());
    }
}
