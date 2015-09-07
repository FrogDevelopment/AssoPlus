/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.controller;

import javafx.application.Platform;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MainController extends AbstractCustomController {

    @Autowired
    private MembersController membersController;
    
    @Override
    protected void initialize() {
    }

    public void importMembers() {
        Stage dialog = openDialog("/fxml/import_members.fxml");
        dialog.setTitle(getMessage("import.title"));
        dialog.setWidth(800);
        dialog.setHeight(200);

        dialog.setOnCloseRequest(event -> {
            membersController.initialize();
        });

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
}
