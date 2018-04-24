package edu.wpi.cs3733d18.teamQ.ui;

import javafx.scene.paint.Color;

import javax.xml.bind.Element;

public class PathData {
    private static final Color pathColor = Color.PURPLE;
    private static final int pathStrokeWidth = 8;

    private static final Color antColor = Color.GOLD;
    private static final int antStrokeWidth = 2;
    private static final double antSpacing = 5d;

    public static Color getPathColor() {
        return pathColor;
    }

    public static int getPathStrokeWidth() {
        return pathStrokeWidth;
    }

    public static Color getAntColor() {
        return antColor;
    }

    public static int getAntStrokeWidth() {
        return antStrokeWidth;
    }

    public static double getAntSpacing() {
        return antSpacing;
    }
}
