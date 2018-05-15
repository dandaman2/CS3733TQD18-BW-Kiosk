package edu.wpi.cs3733d18.teamQ.ui.Controller;
import edu.wpi.cs3733d18.teamQ.pathfinding.Node;
import javafx.scene.shape.Polyline;


import java.util.ArrayList;

public class TransitionData {

    int floor;
    Polyline pathData;
    final double PIXELSPERSECOND = 100;
    ArrayList<Node> nodes = new ArrayList<>();


    public TransitionData(){}

    public TransitionData(int floor, Polyline pathData, ArrayList<Node> nodes) {
        this.floor = floor;
        this.pathData = pathData;
        this.nodes = nodes;

    }


    /**
     * Returns the distance of the path
     *
     * @param path
     * @return
     */
    public double pathLength(ArrayList<Node> path) {
        double length = 0.0;
        for (int i = 0; i < path.size() - 1; i++) {
            length += Math.sqrt((Math.pow((path.get(i).getxPos() - path.get(i + 1).getxPos()), 2)) +
                    (Math.pow((path.get(i).getyPos() - path.get(i + 1).getyPos()), 2)));
        }
        return length;
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public void setNodes(ArrayList<Node> nodes) {
        this.nodes = nodes;
    }

    public double getTransitionLength() {
        ArrayList<Double> path = new ArrayList<>();
        for(int i=0; i <pathData.getPoints().size(); i++){
            path.add(pathData.getPoints().get(i));
        }

        double length = 0.0;
        for (int i = 0; i < path.size() - 3; i+=2) {
            length += Math.sqrt((Math.pow((path.get(i) - path.get(i + 2)), 2)) +
                    (Math.pow((path.get(i+1) - path.get(i + 3)), 2)));
        }
        return length;
    }

    //returns the transition duration based on the path length in pixels
    public double getCalcDuration(){
        double length = getTransitionLength();
        return length/PIXELSPERSECOND;
    }

    //returns the polyline as a list of nodes
    public ArrayList<Node> getNodeShells(){
        ArrayList<Node> nodes = new ArrayList<>();
        for(int i=0; i<pathData.getPoints().size()-1; i+=2){
            double x = pathData.getPoints().get(i);
            double y = pathData.getPoints().get(i+1);
            Node temp = new Node(x,y,floor,x,y);
            nodes.add(temp);
        }
        return nodes;

    }



    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public Polyline getPathData() {
        return pathData;
    }

    public void setPathData(Polyline pathData) {
        this.pathData = pathData;
    }
}