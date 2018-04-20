package edu.wpi.cs3733d18.teamQ.ui.Controller;

import edu.wpi.cs3733d18.teamQ.ui.CircleNode;
import edu.wpi.cs3733d18.teamQ.ui.Controller.EditMapController;
import edu.wpi.cs3733d18.teamQ.ui.User;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import edu.wpi.cs3733d18.teamQ.pathfinding.Edge;
import edu.wpi.cs3733d18.teamQ.pathfinding.Node;

import java.util.ArrayList;

import static edu.wpi.cs3733d18.teamQ.manageDB.DatabaseSystem.*;


public class MapEditUtil {
    private Rectangle cr = new Rectangle();
    EditMapController controller;

    public void initClick(ImageView curfloor, TextField xdField, TextField ydField){
        curfloor.setOnMouseClicked(e -> {
            //controller.resetTextFields();
            System.out.println("[" + e.getX() + ", " + e.getY() + "]");
            if(controller.mode.equals("en")){
                    String x3cor = ""+e.getX()+"";
                    xdField.setText(x3cor);

                    String y3cor = ""+e.getY()+"";
                    ydField.setText(y3cor);
            }
        });

    }

    public ArrayList<Line> SetEdges(ArrayList<Line> de, int floor, int dim, boolean heat){

        Color edgeColor = Color.BLUE;
        if(heat) {
            edgeColor = Color.BLACK;
        }

        User user = User.getUser();
        user.saveToDB();

        ArrayList<Node> nodesOnFloor = user.getFloorNodes(floor);//getNodesByFloor(floor);
        ArrayList<Edge> edgesOnFloor = user.getFloorEdges(floor);//getEdgesByFloor(floor);

        de = new ArrayList<>();

        for (Edge e : edgesOnFloor) {

            Line line = new Line();
            try {
                line.startXProperty().bind(getCircleFor(e.getStartNode(),dim).centerXProperty());
                line.startYProperty().bind(getCircleFor(e.getStartNode(),dim).centerYProperty());

                line.endXProperty().bind(getCircleFor(e.getEndNode(),dim).centerXProperty());
                line.endYProperty().bind(getCircleFor(e.getEndNode(),dim).centerYProperty());


                line.setStrokeWidth(3);
                line.setStroke(edgeColor);
                de.add(line);
            } catch (NullPointerException err) {
                System.out.println("floor edge prob");
            }
        }
        return de;

    }

    public CircleNode getCircleFor(Node node, int dim) {
        ArrayList<CircleNode> listOfCircleNodes;

        if(dim == 2){
            listOfCircleNodes = controller.twoDPoints;
        }
        else{
            listOfCircleNodes = controller.threeDPoints;
        }

        for (CircleNode cn : listOfCircleNodes) {
            if (cn.getNode().getNodeID().equals(node.getNodeID())) {
                return cn;
            }
        }
        return new CircleNode();
    }


    public MapEditUtil(EditMapController controller) {
        this.controller = controller;
    }
}
