/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus;

import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Preloader.StateChangeNotification;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.frogdevelopment.assoplus.core.utils.SpringFXMLLoader;

import java.io.IOException;

import static fr.frogdevelopment.assoplus.core.utils.ApplicationUtils.ICON_16;
import static fr.frogdevelopment.assoplus.core.utils.ApplicationUtils.ICON_32;
import static fr.frogdevelopment.assoplus.core.utils.ApplicationUtils.ICON_48;
import static fr.frogdevelopment.assoplus.core.utils.ApplicationUtils.isInstanceAlreadyLocked;

public class Main extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static HostServices hostServices;

    public static void main(String[] args) throws IOException {
        if (isInstanceAlreadyLocked()) {
            System.err.println("********************************** Already running ********************************** ");
            LOGGER.error("Exit cause already running.");
            System.exit(-1);
        }

        LauncherImpl.launchApplication(Main.class, FrogPreloader.class, args);
    }

    private Parent root;

    @Override
    public void init() throws Exception {
        // chargé dans un thread différent => n'empêche pas l'animation de la ProgressBar
        LOGGER.info("Chargement de la configuration");

        root = SpringFXMLLoader.load("/main.fxml");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        LOGGER.info("Configuration charg\u00e9e");

        // Context spring chargé => on prévient le préloader
        notifyPreloader(new StateChangeNotification(StateChangeNotification.Type.BEFORE_START));

        primaryStage.setTitle("AssoManager");
        primaryStage.setScene(new Scene(root));
        primaryStage.setHeight(600);
        primaryStage.setWidth(1000);
        primaryStage.getScene().getStylesheets().add("frog.css");
        primaryStage.getIcons().addAll(ICON_16, ICON_32, ICON_48);

        hostServices = super.getHostServices();

        LOGGER.info("Ouverture de l'application");
        primaryStage.show();
    }

    // FIXME
    public static void showDocument(String url) {
        hostServices.showDocument(url);
    }

}
