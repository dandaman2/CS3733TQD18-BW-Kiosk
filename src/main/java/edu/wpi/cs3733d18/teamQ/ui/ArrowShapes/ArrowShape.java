package edu.wpi.cs3733d18.teamQ.ui.ArrowShapes;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;


public class ArrowShape extends Polygon{
    private int floor;
    private double height;
    private double width;
    private double overHang = .33;

    private double x;
    private double y;

    public ArrowShape(double height, double width, int floor, double x, double y) {
        this.height = height;
        this.width = width;
        this.floor = floor;
        this.x = x;
        this.y = y;

        this.setLayoutX(x);
        this.setLayoutY(y);
        this.setStroke(Color.BLACK);
    }

    public int getFloor() {
        return floor;
    }
}
