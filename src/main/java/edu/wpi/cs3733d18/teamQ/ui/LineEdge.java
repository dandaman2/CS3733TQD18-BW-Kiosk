package edu.wpi.cs3733d18.teamQ.ui;

import edu.wpi.cs3733d18.teamQ.pathfinding.Edge;
import javafx.scene.shape.Line;

public class LineEdge extends Line{
    Edge edge;
    int dimension = 0;

    public LineEdge(Edge edge, int dimension) {
        this.edge = edge;
        this.dimension = dimension;
    }

    public LineEdge() {
    }

    public Edge getEdge() {
        return edge;
    }

    public void setEdge(Edge edge) {
        this.edge = edge;
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }
}
