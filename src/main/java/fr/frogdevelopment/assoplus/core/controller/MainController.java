/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.core.controller;

import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MainController extends AbstractCustomController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    @Override
    protected void initialize() {
        Thread.setDefaultUncaughtExceptionHandler(this::showError);
    }

    private void showError(Thread t, Throwable e) {
        LOGGER.error("***Default exception handler***");
        if (Platform.isFxApplicationThread()) {
            showError(e);
        }
        LOGGER.error("An unexpected error occurred", e);
    }

    public void onExit() {
        Platform.exit();
    }

    public void onAbout() {
        Stage dialog = openDialog("about.fxml");
        dialog.setTitle(getMessage("about.title"));
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setWidth(400);
        dialog.setHeight(350);
        dialog.setResizable(false);

        dialog.show();
    }
}
