package edu.wpi.cs3733d18.teamQ.ui.ArrowShapes;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class StartArrow extends Polygon{
    private int floor;
    private double height;
    private double width;
    private double overHang = .33;

    private double x;
    private double y;

    public StartArrow(double height, double width, int floor, double x, double y) {
        this.height = height;
        this.width = width;
        this.floor = floor;
        this.x = x;
        this.y = y;

        this.getPoints().addAll(new Double []{
                0.0,0.0,
                width, 0.0,
                (1 + overHang) * width, height / 2.0,
                width, height,
                0.0, height, });

        this.setLayoutX(x);
        this.setLayoutY(y);
        this.setStroke(Color.BLACK);
    }

    public int getFloor() {
        return floor;
    }
}
