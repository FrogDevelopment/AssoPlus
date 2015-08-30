/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
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

public class ApplicationUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationUtils.class);

    public static final Image ICON_16 = new Image("/img/frog_16.png");
    public static final Image ICON_32 = new Image("/img/frog_32.png");
    public static final Image ICON_48 = new Image("/img/frog_48.png");

    private ApplicationUtils() {
    }

    /**
     * Acquire a lock on the lock file and when the main starts it will try to acquire lock on this file.
     * If it succeeds this mean that it is the first instance of the program and if it fails (due to already exist lock) this mean that it is not the first instance of the application.
     * Note that we will release the lock once we end the application and there wont be any harm if the application crashed or so as we can re-start it and it will work just fine.
     *
     * @return <code>true</code> si une instance est déjà ouverte pour la session utilisateur
     */
    public static boolean isInstanceAlreadyLocked() throws IOException {
        LOGGER.info("================== Vérification du lock de l'application ================== ");

        String userHome = System.getProperty("user.dir");
        String separator = FileSystems.getDefault().getSeparator();
        String lockFilePath = userHome + separator + "application.lock";
        File file = new File(lockFilePath);
        RandomAccessFile accessFile = new RandomAccessFile(file, "rw");
        FileLock fileLock = accessFile.getChannel().tryLock();

        if (fileLock == null) { // we couldnt acquire lock as it is already locked by another program instance
            return true;
        }

        file.deleteOnExit();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    fileLock.release();
                    accessFile.close();
                } catch (IOException e) {
                    LOGGER.error("Erreur dans la release du lock");
                }
                LOGGER.info("================== Sortie de l'application ================== ");
            }
        });

        return false;
    }

    private static final ApplicationContext CONTEXT = new ClassPathXmlApplicationContext("/spring.xml");

    public static Parent load(String url) {
        return load(url, null);
    }

    public static <T> Parent load(String url, Consumer<T> controllerConsumer) {
        try (InputStream fxmlStream = Class.class.getResourceAsStream(url)) {
            FXMLLoader loader = new FXMLLoader();
            loader.setControllerFactory(CONTEXT::getBean);
            loader.setLocation(Class.class.getResource("/fxml/"));
            loader.setResources(ResourceBundle.getBundle("label"));
            final Parent parent = loader.load(fxmlStream);

            if (controllerConsumer != null) {
                controllerConsumer.accept(loader.getController());
            }

            return parent;
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    public static Stage openDialog(Window parent, String url) {
        return openDialog(parent, url, null);
    }

    public static <T> Stage openDialog(Window parent, String url, Consumer<T> controllerConsumer) {
        Parent root = load(url, controllerConsumer);
        Stage dialog = new Stage();
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(parent);
        dialog.getIcons().addAll(ICON_16, ICON_32, ICON_48);
        dialog.setScene(new Scene(root));

        return dialog;
    }
}
