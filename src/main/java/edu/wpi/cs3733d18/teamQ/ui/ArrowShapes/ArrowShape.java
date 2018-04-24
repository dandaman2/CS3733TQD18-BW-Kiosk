package edu.wpi.cs3733d18.teamQ.ui.ArrowShapes;

import com.jfoenix.controls.JFXButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;


public class ArrowShape extends Polygon{
    private double height;
    private double width;
    private double overHang = .33;

    private double x;
    private double y;

    public ArrowShape(double height, double width, double x, double y) {
        this.height = height;
        this.width = width;
        this.x = x;
        this.y = y;

//        this.setLayoutX(x);
//        this.setLayoutY(y);
    }
}
