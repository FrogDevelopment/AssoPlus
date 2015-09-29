/*
 * Copyright (c) Frog Development 2015.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.frogdevelopment.assoplus.preloader;

import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GUI extends Application implements ChangeListener<Number> {

    private Group paths;
    private Text message;
    private Scene scene;
    private Rectangle progress;
    private DropShadow glow;
    private DropShadow shadow;
    private BoxBlur boxBlur;
    private Explode explode;
    private String loadingMessage = "Loading... ";

    private SVGPath path, path2, path3, path4, path5;
    private Circle particle1, particle2, particle3, particle4, particle5, neonLoad;

    private final List<Animation> animations = new ArrayList<>();

    public Double getRandom() {
        double rand = Math.random() * 15;
        return rand > 5 ? rand : getRandom();
    }

    /**
     * Animates the particles which moves on the paths
     *
     * @param particle
     * @param path
     */
    public void animateParticle(Node particle, Shape path) {
        PathTransition transition = new PathTransition();
        transition.setPath(path);
        transition.setNode(particle);

        transition.setDuration(Duration.seconds(getRandom()));
        transition.setCycleCount(-1);
        animations.add(transition);
        transition.play();
    }

    /**
     * All Animation is started by this function
     */
    public void animate() {
        Timeline mainAnimation = new Timeline();
        mainAnimation.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(0), new KeyValue(glow.radiusProperty(), 1d)),
                new KeyFrame(Duration.millis(5000), new KeyValue(glow.radiusProperty(), 13d)),
                new KeyFrame(Duration.millis(10000), new KeyValue(glow.radiusProperty(), 0d)),

                new KeyFrame(Duration.millis(1000), new KeyValue(shadow.radiusProperty(), 5d)),
                new KeyFrame(Duration.millis(1000), new KeyValue(boxBlur.iterationsProperty(), 10d)),

                new KeyFrame(Duration.millis(0), new KeyValue(paths.opacityProperty(), 0.6d)),
                new KeyFrame(Duration.millis(5000), new KeyValue(paths.opacityProperty(), 1d)),
                new KeyFrame(Duration.millis(10000), new KeyValue(paths.opacityProperty(), 0.6d))
        );

        mainAnimation.setCycleCount(-1);
        animations.add(mainAnimation);

        mainAnimation.play();

        animateParticle(particle1, path);
        animateParticle(particle2, path2);
        animateParticle(particle3, path3);
        animateParticle(particle4, path4);
        animateParticle(particle5, path5);
    }

    /**
     * This function helps to make the path for animating particles
     *
     * @throws IOException
     */
    public void makePaths() throws IOException {
        path = new SVGPath();
        path2 = new SVGPath();
        path3 = new SVGPath();
        path4 = new SVGPath();
        path5 = new SVGPath();

        path.setContent(PathLoader.getPath(1));
        path2.setContent(PathLoader.getPath(2));
        path3.setContent(PathLoader.getPath(3));
        path4.setContent(PathLoader.getPath(4));
        path5.setContent(PathLoader.getPath(5));

        path.setStroke(Color.YELLOW);
        path2.setStroke(Color.RED);
        path3.setStroke(Color.ORANGE);
        path4.setStroke(Color.WHEAT);
        path5.setStroke(Color.AQUA);

        path.setFill(Color.TRANSPARENT);
        path2.setFill(Color.TRANSPARENT);
        path3.setFill(Color.TRANSPARENT);
        path4.setFill(Color.TRANSPARENT);
        path5.setFill(Color.TRANSPARENT);

        path.setEffect(boxBlur);
        path2.setEffect(boxBlur);
        path3.setEffect(boxBlur);
        path4.setEffect(boxBlur);
        path5.setEffect(boxBlur);
    }

    /**
     * Make the particles which moves with the path defined
     */
    public void makeParticle() {
        particle1 = new Circle(0, 0, 2, Color.YELLOW);
        particle2 = new Circle(0, 0, 2, Color.RED);
        particle3 = new Circle(0, 0, 2, Color.ORANGE);
        particle4 = new Circle(0, 0, 2, Color.WHEAT);
        particle5 = new Circle(0, 0, 2, Color.AQUA);
        neonLoad = new Circle(0, 0, 2, Color.AQUA);

        particle1.setEffect(glow);
        particle2.setEffect(glow);
        particle3.setEffect(glow);
        particle4.setEffect(glow);
        particle5.setEffect(glow);

        neonLoad.setEffect(glow);
    }

    /**
     * Initialize the Effects which is to be used in the controls
     * and graphics
     */
    public void initEffects() {
        boxBlur = new BoxBlur();
        boxBlur.setWidth(10);
        boxBlur.setHeight(3);
        boxBlur.setIterations(1);

        glow = new DropShadow();
        glow.setColor(Color.YELLOW);
        glow.setOffsetX(0);
        glow.setOffsetY(0);
        glow.setRadius(1);

        shadow = new DropShadow();
        shadow.setOffsetX(4);
        shadow.setOffsetY(4);
        shadow.setRadius(1);
    }

    /**
     * Build all the main component of this animation
     *
     * @throws IOException
     */
    public void buildComponents() throws IOException {
        initEffects();
        makePaths();
        makeParticle();

        paths = new Group(path, path2, path3, path4, path5);
        paths.setOpacity(0.6);

        progress = new Rectangle();
        progress.setHeight(2);
        progress.setLayoutY(360);
        //progress.setFill(Color.WHITE); 

        message = new Text(loadingMessage);
        message.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        message.setFill(Color.WHEAT);
        message.setEffect(glow);
        message.setLayoutX(250);
        message.setLayoutY(300);

        Group particles = new Group(particle1, particle2, particle3, particle4, particle5);
        explode = new Explode();
        Group explodes = new Group(neonLoad);
        Group g = new Group(paths, particles, message, explodes, explode);
        explodes.setClip(progress);
        scene = new Scene(g, 685, 500, Color.BLACK);
    }

    /**
     * When the loading finishes then all animation are being stopped .
     */
    public void dispose() {
        neonLoad.layoutXProperty().removeListener(this);
        animations.forEach(javafx.animation.Animation::stop);
    }

    /**
     * When neon loading get's loaded then the explode of neon is done from this function
     */
    public void explodeAnimate() {
        neonLoad.setLayoutY(360);
        neonLoad.layoutXProperty().addListener(this);
        neonLoad.setRadius(30);
        Timeline anim = new Timeline();
        anim.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(0), new KeyValue(neonLoad.layoutXProperty(), 0)),
                new KeyFrame(Duration.millis(800), new KeyValue(neonLoad.layoutXProperty(), scene.getWidth()))
        );

        anim.setCycleCount(-1);
        anim.play();

        animations.add(anim);
    }

    /**
     * ====================
     * TESTING PURPOSE ONLY
     * ====================
     *
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {

        buildComponents();
        stage.setScene(scene);
        stage.show();
        animate();
        explodeAnimate();
        Task t = new Task() {

            @Override
            protected Object call() throws Exception {
                double i = 0.0;
                while (i < 1) {
                    try {
                        Thread.sleep((long) (Math.random() * 5000));
                        i += 0.1;
                        update(i);
                        System.out.println(i);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
                return null;
            }

        };
        Thread th = new Thread(t);
        th.start();
    }

    /**
     * Returns the instance of the scene
     *
     * @return
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * This is the main update function which is triggered from preloader class
     * and this makes the gui to show some info the loading progress
     *
     * @param percent
     */
    public void update(double percent) {
        if (percent > 1) {
            dispose();
            return;


        }
        message.setText(loadingMessage + Math.round(percent * 100) + " %");
        Timeline anim = new Timeline();
        anim.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(600), new KeyValue(progress.widthProperty(), scene.getWidth() * percent))
        );

        Stage st = (Stage) scene.getWindow();
        st.setTitle("Loading..." + Math.floor(percent * 100));

        anim.play();


    }

    /**
     * This function is the listener which helps to listen the position of the
     * neon light and explodes when the neon reaches in the percentaged X position
     *
     * @param observable
     * @param oldValue
     * @param newValue
     */
    @Override
    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        int x = (int) (Math.floor(progress.getWidth()));
        int curr = (int) (Math.floor(newValue.doubleValue()));
        if (curr >= x - 5 && curr <= x + 5) {
            explode.setLayoutY(progress.getLayoutY() - 40);
            explode.setLayoutX(newValue.doubleValue());
            explode.explode();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
