/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus;

import com.sun.javafx.application.LauncherImpl;
import fr.frogdevelopment.assoplus.core.preloader.LongAppInitPreloader;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static fr.frogdevelopment.assoplus.core.utils.ApplicationUtils.*;
import static javafx.application.Preloader.ProgressNotification;
import static javafx.application.Preloader.StateChangeNotification;

public class Main extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static HostServices hostServices;

    public static void main(String[] args) {
        LauncherImpl.launchApplication(Main.class, LongAppInitPreloader.class, args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        longStart();

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

        hostServices = super.getHostServices();

        LOGGER.info("Configuration charg\u00e9e");
        // After the app is ready, show the stage
        ready.addListener((ov, t, t1) -> {
            if (Boolean.TRUE.equals(t1)) {
                Platform.runLater(() -> {
                    LOGGER.info("Ouverture de l'application");
                    primaryStage.show();
                });
            }
        });
    }

    // FIXME
    public static void showDocument(String url) {
        hostServices.showDocument(url);
    }

    @Override
    public void init() throws Exception {
        super.init();
    }

    private BooleanProperty ready = new SimpleBooleanProperty(false);

    private void longStart() {
        //simulate long init in background
        Task task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                int max = 10;
                for (int i = 1; i <= max + 1; i++) {
                    Thread.sleep(100);
                    // Send progress to preloader
                    notifyPreloader(new ProgressNotification(((double) i) / max));
                }
                // After init is ready, the app is ready to be shown
                // Do this before hiding the preloader stage to prevent the
                // app from exiting prematurely
                ready.setValue(Boolean.TRUE);

                notifyPreloader(new StateChangeNotification(StateChangeNotification.Type.BEFORE_START));

                return null;
            }
        };
        new Thread(task).start();
    }
}
