package edu.wpi.cs3733d18.teamQ.ui;

import javafx.scene.shape.Circle;
import edu.wpi.cs3733d18.teamQ.pathfinding.Node;

public class CircleNode extends Circle{
    Node node;
    int dimension =0;


    public CircleNode(Node node, int dimension) {
        this.node = node;
        this.dimension = dimension;
    }

    public CircleNode() {
    }

    public Node getNode() {
        return this.node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }
}
