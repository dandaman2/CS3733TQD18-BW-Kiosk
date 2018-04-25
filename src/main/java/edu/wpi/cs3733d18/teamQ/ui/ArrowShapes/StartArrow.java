package edu.wpi.cs3733d18.teamQ.ui.ArrowShapes;

public class StartArrow extends ArrowShape{
    private double overHang = .33;

    public StartArrow(double height, double width,  double x, double y) {
        super(height, width, x, y);

        this.getPoints().addAll(new Double []{
                0.0,0.0,
                width, 0.0,
                (1 + overHang) * width, height / 2.0,
                width, height,
                0.0, height, });
    }
}
