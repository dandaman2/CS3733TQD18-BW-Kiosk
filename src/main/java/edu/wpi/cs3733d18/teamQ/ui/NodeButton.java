package edu.wpi.cs3733d18.teamQ.ui;

import edu.wpi.cs3733d18.teamQ.pathfinding.Node;
import javafx.scene.control.Button;

public class NodeButton extends Button {
    Node node;

    public NodeButton(Node node) {
        this.node = node;
        setStyle("-fx-base: darkred;"+"-fx-background-radius: 50%;" + "-fx-min-height: 10px;"+ "-fx-max-height: 10px;" + "-fx-min-width: 10px;" + "-fx-max-width: 10px");
    }

    public NodeButton(String text, Node node) {
        super(text);
        this.node = node;
    }

    public NodeButton(String text, javafx.scene.Node graphic, Node node) {
        super(text, graphic);
        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }
}
