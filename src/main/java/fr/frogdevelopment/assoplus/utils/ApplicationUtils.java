/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.utils;

import fr.frogdevelopment.assoplus.controller.MemberController;
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
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Consumer;

public class ApplicationUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationUtils.class);

    private ApplicationUtils() {}

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

    public static Parent load(String url, Consumer<FXMLLoader> consumer) {
        try (InputStream fxmlStream = Class.class.getResourceAsStream(url)) {
            FXMLLoader loader = new FXMLLoader();
            loader.setControllerFactory(CONTEXT::getBean);
            loader.setLocation(Class.class.getResource("/fxml/"));
            loader.setResources(ResourceBundle.getBundle("label"));
            final Parent parent = loader.load(fxmlStream);

            if (consumer != null) {
                consumer.accept(loader);
            }

            return parent;
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    public static final Set<Image> ICONS = new HashSet<>();
    {
        ICONS.add(new Image("/img/frog_16.png"));
        ICONS.add(new Image("/img/frog_32.png"));
        ICONS.add(new Image("/img/frog_48.png"));
    }

    public static Stage openDialog(Window parent, String url) {
        return openDialog(parent, url, null);
    }

    public static Stage openDialog(Window parent, String url, Consumer<FXMLLoader> consumer) {
        Parent root = load(url, consumer);
        Stage dialog = new Stage();
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(parent);
        dialog.getIcons().addAll(ICONS);
        dialog.setScene(new Scene(root));
//        dialog.setOnCloseRequest(event1 -> setLicences());

        return dialog;
    }
}
