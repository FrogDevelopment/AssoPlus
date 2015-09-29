/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.preloader;

import javafx.application.Preloader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.LoggerFactory;

import java.io.IOException;

// http://blog.ngopal.com.np/2012/07/26/animated-preloader-in-javafx-2/
// http://blog.ngopal.com.np/2012/08/13/fancy-preloader-part-2/
// fixme voir http://docs.oracle.com/javafx/2/deployment/preloaders.htm#BABDDGCD pour interaction(n° licence par ex)
public class FrogPreloader extends Preloader {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(FrogPreloader.class);

    private Stage stage;
    private GUI gui;

    private Scene createPreloaderScene() {
        gui = new GUI();
        try {
            gui.buildComponents();
        } catch (IOException ex) {
            LOGGER.error(ExceptionUtils.getMessage(ex));
        }
        return gui.getScene();
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setScene(createPreloaderScene());
        stage.show();
        gui.animate();
        gui.explodeAnimate();
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification scn) {
        //ignore, hide after application signals it is ready
    }

    @Override
    public void handleProgressNotification(ProgressNotification pn) {
        if (pn.getProgress() != 1.0) {
            gui.update(pn.getProgress());
        }
    }

    @Override
    public void handleApplicationNotification(PreloaderNotification info) {
        if (info instanceof ProgressNotification) {
            gui.update(((ProgressNotification) info).getProgress());
        } else if (info instanceof StateChangeNotification) {
            //hide after get any state update from application
            stage.hide();
        }
    }
}
