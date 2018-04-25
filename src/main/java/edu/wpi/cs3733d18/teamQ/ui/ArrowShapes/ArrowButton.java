package edu.wpi.cs3733d18.teamQ.ui.ArrowShapes;

import com.jfoenix.controls.JFXButton;
import javafx.scene.Node;

public class ArrowButton extends JFXButton {
    private ArrowShape shape;
    private int floor;

    public ArrowButton(ArrowShape shape, int floor, double width, double height) {
        this.shape = shape;
        this.floor = floor;

        this.setShape(shape);

        this.setMinSize(width, height);
        this.setMaxSize(width, height);
        this.setPrefSize(width, height);
    }

    public ArrowButton(String text, ArrowShape shape, int floor) {
        super(text);
        this.shape = shape;
        this.floor = floor;
    }

    public ArrowButton(String text, Node graphic, ArrowShape shape, int floor) {
        super(text, graphic);
        this.shape = shape;
        this.floor = floor;
    }

    public int getFloor() {
        return floor;
    }
}
