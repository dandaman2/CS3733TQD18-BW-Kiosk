package edu.wpi.cs3733d18.teamQ.ui.Controller;

import edu.wpi.cs3733d18.teamQ.ui.LineEdge;
import edu.wpi.cs3733d18.teamQ.ui.CircleNode;
import edu.wpi.cs3733d18.teamQ.ui.User;
import javafx.scene.Cursor;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import edu.wpi.cs3733d18.teamQ.pathfinding.Edge;
import edu.wpi.cs3733d18.teamQ.pathfinding.Node;

import java.util.ArrayList;


public class MapEditUtil {
    private Rectangle cr = new Rectangle();
    EditMapController controller;
    private LineEdge curSel = new LineEdge();
    private LineEdge oppSel = new LineEdge();
    private Color edgeColor = Color.WHITE;

    public void initClick(ImageView curfloor, TextField xdField, TextField ydField){
        curfloor.setOnMouseClicked(e -> {
            System.out.println("[" + e.getX() + ", " + e.getY() + "]");
            if(controller.mode.equals("en")){
                    String x3cor = ""+e.getX()+"";
                    xdField.setText(x3cor);

                    String y3cor = ""+e.getY()+"";
                    ydField.setText(y3cor);
            }
        });

    }

    public ArrayList<LineEdge> SetEdges(int floor, int dim, boolean heat){
        Color edgeColor;
        if(heat)
            edgeColor = Color.BLACK;
        else
            edgeColor = Color.BLUE;

        User user = User.getUser();
        user.saveToDB();

        ArrayList<Edge> edgesOnFloor = user.getFloorEdges(floor);//getEdgesByFloor(floor);

        ArrayList<LineEdge> de = new ArrayList<>();

        for(Edge e: edgesOnFloor){
            de.add(new LineEdge(e, dim));
        }

        for (LineEdge le : de) {
            try {

                le.setOnMousePressed((event) -> {
//                    if(controller.mode == "ee") {

                    curSel.setStroke(edgeColor);    // resets originally selected line color
//                        oppSel.setStroke(edgeColor);

                    curSel = le;      // switches which line is selected
                    curSel.setStroke(Color.GREEN);    // recolors newly seleced line

                    for (LineEdge le2 : de){
                        if(curSel.getEdge().getEdgeID().equals(le2.getEdge().getEdgeID())){
                            le2.setStroke(Color.GREEN);
                            System.out.println("other: " + le2.getEdge().getEdgeID() + " line " + le2);
                        }
                    }
                    System.out.println("curSel; " + curSel + " id " + curSel.getEdge().getEdgeID());
                    // sets text fields to edge properties
                    controller.y2dNode_fromEdgeField.setText(le.getEdge().getStartNode().getNameLong());
                    controller.x2dNode_toEdgeField.setText(le.getEdge().getEndNode().getNameLong());
//                    }
                });

                le.startXProperty().bind(getCircleFor(le.getEdge().getStartNode(),dim).centerXProperty());
                le.startYProperty().bind(getCircleFor(le.getEdge().getStartNode(),dim).centerYProperty());

                le.endXProperty().bind(getCircleFor(le.getEdge().getEndNode(),dim).centerXProperty());
                le.endYProperty().bind(getCircleFor(le.getEdge().getEndNode(),dim).centerYProperty());

                le.setStrokeWidth(3);
                le.setStroke(edgeColor);
                le.setCursor(Cursor.HAND);


            } catch (NullPointerException err) {
                System.out.println("floor edge prob");
            }
        }
        return de;
    }

    //highlights the selected edge in the opposite dimensional region
    public void hlOther(LineEdge lineEdge, ArrayList<LineEdge> edgeList){
        int dim = lineEdge.getDimension();

        int oppDim;
        if(dim == 3)
            oppDim = 2;
        else
            oppDim = 3;

//            for(LineEdge le : twoDPoints){
//                le.setFill(edgeColor);
//            }


        Node node1 = getCircleFor(lineEdge.getEdge().getStartNode(), oppDim).getNode();
        Node node2 = getCircleFor(lineEdge.getEdge().getEndNode(), oppDim).getNode();
        Edge lookUpEdge = controller.getEdgeFromNodes(node1, node2);

//        for(LineEdge le: edgeList){
//            if()
//            if(le.getNode().getNodeID().equals(lineEdge.getNode().getNodeID())){
//                le.setFill(select2Color);
//            }
//            else {
//                le.setFill(edgeColor);
//            }

//        }

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
