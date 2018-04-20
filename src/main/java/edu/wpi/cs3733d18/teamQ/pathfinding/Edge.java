package edu.wpi.cs3733d18.teamQ.pathfinding;

public class Edge {

    private String edgeID;
    private Node startNode;
    private Node endNode;
    private double distance;
    private boolean state;

    public Edge(String edgeID, Node sNode, Node eNode, double dis){
        this.edgeID = edgeID;
        this.startNode = sNode;
        this.endNode = eNode;
        this.distance = dis;
        this.state = true;
    }

    public Edge(Double distance, Node startNode, Node endNode) {
        this.distance = distance;
        this.startNode = startNode;
        this.endNode = endNode;
    }


    public String getEdgeID() {
        return edgeID;
    }

    public Node getStartNode() {
        return startNode;
    }

    public Node getEndNode() {
        return endNode;
    }

    public double getDistance() {
        return distance;
    }


    public void setEdgeID(String edgeID) {
        this.edgeID = edgeID;
    }

    public void setStartNode(Node startNode) {
        this.startNode = startNode;
    }

    public void setEndNode(Node endNode) {
        this.endNode = endNode;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }



}