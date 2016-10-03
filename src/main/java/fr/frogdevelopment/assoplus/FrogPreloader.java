package fr.frogdevelopment.assoplus;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FrogPreloader extends javafx.application.Preloader {

    private Stage preloaderStage;

    @Override
    public void start(Stage stage) throws Exception {
        preloaderStage = stage;

        Label label = new Label("Loading, please wait ...");
        label.setFont(new Font(30.0));

        ProgressBar progress = new ProgressBar();
        progress.setPrefWidth(150);

        BorderPane borderPane = new BorderPane(progress, label, null, null, null);

        preloaderStage.setScene(new Scene(borderPane, 250, 75));
        preloaderStage.initStyle(StageStyle.UNDECORATED);

        stage.getScene().getStylesheets().add("frog.css");

        preloaderStage.show();
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification stateChangeNotification) {
        if (stateChangeNotification.getType() == StateChangeNotification.Type.BEFORE_START) {
            preloaderStage.hide();
        }
    }
}
