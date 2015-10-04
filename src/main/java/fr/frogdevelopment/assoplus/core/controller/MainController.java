/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.core.controller;

import fr.frogdevelopment.assoplus.member.controller.MembersController;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MainController extends AbstractCustomController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private MembersController membersController;

    @Override
    protected void initialize() {

        Thread.setDefaultUncaughtExceptionHandler(this::showError);
    }

    private void showError(Thread t, Throwable e) {
        LOGGER.error("***Default exception handler***");
        if (Platform.isFxApplicationThread()) {
            showError(e);
        } else {
            LOGGER.error("An unexpected error occurred in " + t);

        }
    }

    public void importMembers() {
        Stage dialog = openDialog("/fxml/import_members.fxml");
        dialog.setTitle(getMessage("import.title"));
        dialog.setWidth(800);
        dialog.setHeight(200);

        dialog.setOnCloseRequest(event -> membersController.reinit());

        dialog.show();
    }

    public void manageDegrees() {
        Stage dialog = openDialog("/fxml/degrees.fxml");
        dialog.setTitle(getMessage("member.degrees"));
        dialog.setWidth(550);
        dialog.setHeight(400);

        dialog.show();
    }

    public void onExit() {
        Platform.exit();
    }

    public void onAbout() {
        Stage dialog = openDialog("/fxml/about.fxml");
        dialog.setTitle(getMessage("about.title"));
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setWidth(400);
        dialog.setHeight(350);
        dialog.setResizable(false);

        dialog.show();
    }
}
