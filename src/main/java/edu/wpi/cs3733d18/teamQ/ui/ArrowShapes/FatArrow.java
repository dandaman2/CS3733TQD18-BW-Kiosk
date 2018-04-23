package edu.wpi.cs3733d18.teamQ.ui.ArrowShapes;

public class FatArrow extends ArrowShape {
    private double overHang = .33;

    public FatArrow(double height, double width, int floor, double x, double y) {
        super(height, width, floor, x, y);

        this.getPoints().addAll(new Double[]{
                0.0, 0.0,
                width, 0.0,
                (1 + overHang) * width, height / 2.0,
                width, height,
                0.0, height,
                overHang * width, height / 2.0});
    }
}
