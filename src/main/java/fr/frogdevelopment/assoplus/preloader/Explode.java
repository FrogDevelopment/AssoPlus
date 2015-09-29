/*
 * Copyright (c) Frog Development 2015.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.frogdevelopment.assoplus.preloader;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * @author msi
 */
public class Explode extends Group {

    Circle c1, c2, c3, c4, c5, c6;
    SimpleDoubleProperty opac = new SimpleDoubleProperty(1);

    public Explode() {
        init();
    }

    public void init() {
        Color c = Color.AQUA;
        c1 = new Circle(0, 20, 1, c);
        c2 = new Circle(0, 20, 1, c);
        c3 = new Circle(0, 20, 1, c);
        c4 = new Circle(0, 20, 1, c);
        c5 = new Circle(0, 20, 1, c);
        c6 = new Circle(0, 20, 1, c);

        c1.opacityProperty().bind(opac);
        c2.opacityProperty().bind(opac);
        c3.opacityProperty().bind(opac);
        c4.opacityProperty().bind(opac);
        c5.opacityProperty().bind(opac);
        c6.opacityProperty().bind(opac);

        this.getChildren().addAll(c1, c2, c3, c4, c5, c6);

    }

    public void explode() {
        Timeline t = new Timeline();
        t.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(0), new KeyValue(opac, 1d)),
                new KeyFrame(Duration.millis(0), event -> {
                    getChildren().forEach(n -> {
                        n.setLayoutX(0);
                        n.setLayoutY(0);
                    });

//                    Iterator it = getChildren().iterator();
//                    while(it.hasNext()){
//                        Node n = (Node)it.next();
//                        n.setLayoutX(0);
//                        n.setLayoutY(0);
//                    }
                }),

                new KeyFrame(Duration.millis(100), new KeyValue(c1.layoutXProperty(), 30d)),
                new KeyFrame(Duration.millis(100), new KeyValue(c2.layoutXProperty(), 30d)),
                new KeyFrame(Duration.millis(100), new KeyValue(c3.layoutXProperty(), 30d)),
                new KeyFrame(Duration.millis(100), new KeyValue(c4.layoutXProperty(), 30d)),
                new KeyFrame(Duration.millis(100), new KeyValue(c5.layoutXProperty(), 30d)),
                new KeyFrame(Duration.millis(100), new KeyValue(c6.layoutXProperty(), 30d)),


                new KeyFrame(Duration.millis(600), new KeyValue(c1.layoutXProperty(), 3d)),
                new KeyFrame(Duration.millis(600), new KeyValue(c2.layoutXProperty(), 15d)),
                new KeyFrame(Duration.millis(600), new KeyValue(c3.layoutXProperty(), 9d)),
                new KeyFrame(Duration.millis(600), new KeyValue(c4.layoutXProperty(), 19d)),
                new KeyFrame(Duration.millis(600), new KeyValue(c5.layoutXProperty(), 6d)),
                new KeyFrame(Duration.millis(600), new KeyValue(c6.layoutXProperty(), 11d)),

                new KeyFrame(Duration.millis(600), new KeyValue(c1.layoutYProperty(), 0d)),
                new KeyFrame(Duration.millis(600), new KeyValue(c2.layoutYProperty(), 5d)),
                new KeyFrame(Duration.millis(600), new KeyValue(c3.layoutYProperty(), 13d)),
                new KeyFrame(Duration.millis(600), new KeyValue(c4.layoutYProperty(), 9d)),
                new KeyFrame(Duration.millis(600), new KeyValue(c5.layoutYProperty(), 26d)),
                new KeyFrame(Duration.millis(600), new KeyValue(c6.layoutYProperty(), 30d)),

                new KeyFrame(Duration.millis(600), new KeyValue(opac, 0d)),
                new KeyFrame(Duration.millis(600), event -> {
                    getChildren().forEach(n -> {
                        n.setLayoutX(0);
                        n.setLayoutY(20);
                    });
//                    Iterator it = getChildren().iterator();
//                    while (it.hasNext()) {
//                        Node n = (Node) it.next();
//                        n.setLayoutX(0);
//                        n.setLayoutY(20);
//                    }
                })
        );

        t.play();
    }

}
