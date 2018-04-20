package edu.wpi.cs3733d18.teamQ.ui;

import edu.wpi.cs3733d18.teamQ.pathfinding.Node;
import edu.wpi.cs3733d18.teamQ.ui.Controller.PathfindingCont;
import edu.wpi.cs3733d18.teamQ.ui.TransPoint;
import edu.wpi.cs3733d18.teamQ.ui.User;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class MapNoder {
    private AnchorPane pane;
    private ImageView backImage;
    private TextField searchField;
    private User user = User.getUser();
    private PathfindingCont controller = new PathfindingCont();
    private boolean is2D;
    private ArrayList<Circle> pointList;
    private Circle inner;
    private Label nodeLabel;


    public MapNoder(AnchorPane pane, ImageView backImage, boolean is2D, TextField searchField) {
        this.pane = pane;
        this.backImage = backImage;
        this.searchField = searchField;
        this.is2D = is2D;
        pointList = new ArrayList<Circle>();
    }

    public void displayNodes(int floor, ArrayList<String> exculdedType, boolean is2D){
        this.is2D = is2D;

        // List of nodes on the current floor
        ArrayList<Node> nodeList = user.getFloorNodes(floor);
        ArrayList<Node> nodeListByType = new ArrayList<Node>();
        TransPoint tp;

        // builds node list without specified types
        for (Node n : nodeList) {
            if(!exculdedType.contains(n.getType())) {
                nodeListByType.add(n);
            }
        }

        for (Node n: nodeListByType) {
            tp = translateCoord(n);
            double tx = tp.getTx();
            double ty = tp.getTy();
            Circle circle = new Circle(7);
            circle.setCenterY(ty);
            circle.setCenterX(tx);
            circle.setFill(Color.DARKRED);

            circle.setOnMouseClicked((e) -> {
                searchField.setText(n.getNameLong() + ","+ n.getNodeID());
            });

            Tooltip tip = new Tooltip(n.getNameLong());
            Tooltip.install(circle, tip);

//           circle.setOnMouseEntered((e) -> {
//                System.out.println("hover label");
//                nodeLabel = new Label( "  " + n.getNameLong() + " ");
//                nodeLabel.setPrefHeight(14);
//                nodeLabel.setLayoutX(tx);
//                nodeLabel.setLayoutY(ty - 7);
//               System.out.println("x: " + tx + " y: " + ty);
//                nodeLabel.setStyle("-fx-background-color: #3e3c3a;");
//                nodeLabel.setTextFill(Color.WHITE);
//                nodeLabel.toFront();
//            });
//
//           circle.setOnMouseExited((e) -> {
//               nodeLabel.setVisible(false);
//           });

            pointList.add(circle);
            pane.getChildren().add(circle);
        }

    }

    public void removeAllNodes(){
        pane.getChildren().removeAll(pointList);
        pane.getChildren().remove(inner);
    }


    /**
     * Takes in a node and returns a transpoint object
     * @param n
     * @return
     */
    public TransPoint translateCoord(Node n){
        Parent currentParent = backImage.getParent();
        double imageWidth = backImage.getBoundsInLocal().getWidth()*backImage.getScaleX();
        double imageHeight = backImage.getBoundsInLocal().getHeight()*backImage.getScaleY();
        double offsetX = backImage.getLayoutX() - (backImage.getScaleX() - 1) * backImage.getBoundsInLocal().getWidth()/2;
        double offsetY = backImage.getLayoutY() - (backImage.getScaleY() - 1) * backImage.getBoundsInLocal().getHeight()/2;

        while((currentParent != null) && !(currentParent instanceof AnchorPane)){
            offsetX += currentParent.getLayoutX();
            offsetY += currentParent.getLayoutY();
            currentParent = currentParent.getParent();
        }

        double newX, newY;

        if(is2D){
            newX = n.getxPos() * imageWidth / 5000.0 + offsetX ;
            newY = n.getyPos() * imageHeight / 3400.0 + offsetY ;
        } else {
            newX = n.getxPos3D() * imageWidth / 5000.0 + offsetX;
            newY = n.getyPos3D() * imageHeight / 2774.0 + offsetY;
        }

        return new TransPoint(newX,newY,n.getFloor());
    }

}
