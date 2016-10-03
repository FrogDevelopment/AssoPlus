package fr.frogdevelopment.assoplus.core.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.InputStream;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class SpringFXMLLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringFXMLLoader.class);

    private static final ApplicationContext CONTEXT = new ClassPathXmlApplicationContext("/spring-context.xml");

    private SpringFXMLLoader() {
        throw new IllegalAccessError("Utility class");
    }

    public static Parent load(String url) {
        return load(url, null);
    }

    public static <T> Parent load(String url, Consumer<T> controllerConsumer) {
        url = "/fxml/" + url;
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

        } catch (Exception e) {
            LOGGER.error("Error while loading " + url);
            throw new IllegalStateException("Error while loading " + url, e);
        }
    }

    static <B> B getBean(Class<B> clazz) {
        return CONTEXT.getBean(clazz);
    }
}
