/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.nio.file.FileSystems;
import java.util.ResourceBundle;

public class Main extends Application {

	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

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
		RandomAccessFile accessFile = new RandomAccessFile(lockFilePath, "rw");
		FileLock fileLock = accessFile.getChannel().tryLock();

		if (fileLock == null) { // we couldnt acquire lock as it is already locked by another program instance
			return true;
		}

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					fileLock.release();
				} catch (IOException e) {
					LOGGER.error("Erreur dans la release du lock");
				}
				LOGGER.info("================== Sortie de l'application ================== ");
			}
		});

		return false;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		if (isInstanceAlreadyLocked()) {
			System.err.println("********************************** Already running ********************************** ");
			LOGGER.error("Exited cause already running.");
			System.exit(-1);
		}


		LOGGER.info("Chargement de la configuration");

		Parent root = load("/fxml/main.fxml");

		primaryStage.setTitle("AssoManager");
		primaryStage.setScene(new Scene(root, 600, 600));

		LOGGER.info("Ouverture de l'application");
		primaryStage.show();
	}

	private static final ApplicationContext CONTEXT = new ClassPathXmlApplicationContext("/spring.xml");

	public static Parent load(String url) {
		try (InputStream fxmlStream = Class.class.getResourceAsStream(url)) {
			FXMLLoader loader = new FXMLLoader();
			loader.setControllerFactory(CONTEXT::getBean);
			loader.setLocation(Class.class.getResource("/fxml/"));
			loader.setResources(ResourceBundle.getBundle("bundles.label"));
			return loader.load(fxmlStream);
		} catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
