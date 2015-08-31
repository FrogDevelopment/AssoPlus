/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static fr.frogdevelopment.assoplus.utils.ApplicationUtils.*;

public class Main extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        if (isInstanceAlreadyLocked()) {
            System.err.println("********************************** Already running ********************************** ");
            LOGGER.error("Exit cause already running.");
            System.exit(-1);
        }

        LOGGER.info("Chargement de la configuration");

        Parent root = load("/fxml/main.fxml");

        primaryStage.setTitle("AssoManager");
        primaryStage.setScene(new Scene(root));
        primaryStage.setHeight(600);
        primaryStage.setWidth(1000);
        primaryStage.getIcons().addAll(ICON_16, ICON_32, ICON_48);

        LOGGER.info("Ouverture de l'application");
        primaryStage.show();
    }

}
