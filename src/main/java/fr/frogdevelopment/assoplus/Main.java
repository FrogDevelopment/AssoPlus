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

public class Main extends Application {

	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	@Override
	public void start(Stage primaryStage) throws Exception {
		LOGGER.info("Chargement de la configuration");

		Parent root = load("/fxml/members.fxml");

		primaryStage.setTitle("AssoManager");
		primaryStage.setScene(new Scene(root, 600, 600));

		LOGGER.info("Ouverture de l'application");
		primaryStage.show();
	}

	public static Parent load(String url) {
		try (InputStream fxmlStream = Main.class.getResourceAsStream(url)) {
			ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/spring.xml");
			FXMLLoader loader = new FXMLLoader();
			loader.setControllerFactory(applicationContext::getBean);
			return loader.load(fxmlStream);
		} catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}


	public static void main(String[] args) {
		launch(args);
	}
}
