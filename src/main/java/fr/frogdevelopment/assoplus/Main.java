/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus;

import fr.frogdevelopment.assoplus.utils.ApplicationUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.nio.file.FileSystems;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class Main extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        if (ApplicationUtils.isInstanceAlreadyLocked()) {
            System.err.println("********************************** Already running ********************************** ");
            LOGGER.error("Exit cause already running.");
            System.exit(-1);
        }

        LOGGER.info("Chargement de la configuration");

        Parent root = ApplicationUtils.load("/fxml/main.fxml");

        primaryStage.setTitle("AssoManager");
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.getIcons().addAll(ApplicationUtils.ICONS);

        LOGGER.info("Ouverture de l'application");
        primaryStage.show();
    }

}
