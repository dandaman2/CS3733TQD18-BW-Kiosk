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
    EditMapController controller;
    private LineEdge curSel = new LineEdge();
    private LineEdge oppSel = new LineEdge();
    private Color edgeColor;
    private Color selectedColor = Color.GREEN;

    public MapEditUtil(EditMapController controller) {
        this.controller = controller;
    }

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
        Color offColor = Color.GRAY;
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


                // binds line to corresponding circleNodes
                le.startXProperty().bind(getCircleFor(le.getEdge().getStartNode(),dim).centerXProperty());
                le.startYProperty().bind(getCircleFor(le.getEdge().getStartNode(),dim).centerYProperty());

                le.endXProperty().bind(getCircleFor(le.getEdge().getEndNode(),dim).centerXProperty());
                le.endYProperty().bind(getCircleFor(le.getEdge().getEndNode(),dim).centerYProperty());

                le.setStrokeWidth(3);
                if(le.getEdge().isEnabled()) {
                    le.setStroke(edgeColor);
                } else {
                    le.setStroke(offColor);
                }
                le.setCursor(Cursor.HAND);

                le.setOnMousePressed((event) -> {
                    controller.selector.getSelectionModel().select(2);

                    // resets originally selected line color
                    curSel.setStroke(edgeColor);
                    oppSel.setStroke(edgeColor);

                    curSel = le;      // switches which line is selected
                    curSel.setStroke(selectedColor);    // recolors newly selected line

                    hlOther(curSel, dim);   // hightlights edge on opposite map

                    // sets text fields to edge properties
                    if(controller.mode == "ee") {
                        controller.y2dNode_fromEdgeField.setText(le.getEdge().getStartNode().getNameLong());
                        controller.x2dNode_toEdgeField.setText(le.getEdge().getEndNode().getNameLong());
                    }
                });

            } catch (NullPointerException err) {
                System.out.println("floor edge prob");
            }
        }
        return de;
    }

    //highlights the selected edge in the opposite dimensional region
    public void hlOther(LineEdge lineEdge, int dim){
        System.out.println("Highlighting Other...");
        ArrayList<LineEdge> oppEdgeList;
        if(dim == 2)
            oppEdgeList = controller.drawn3DEdges;
        else
            oppEdgeList = controller.drawn2DEdges;

        for (LineEdge le2 : oppEdgeList){
            if(lineEdge.getEdge().getEdgeID().equals(le2.getEdge().getEdgeID())){
                oppSel = le2;
                oppSel.setStroke(selectedColor);
            }
        }

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

    public LineEdge getCurSel() {
        return curSel;
    }
}
