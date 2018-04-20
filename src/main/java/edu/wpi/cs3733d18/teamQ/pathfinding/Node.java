package edu.wpi.cs3733d18.teamQ.pathfinding;

import static java.lang.Math.abs;

public class Node {
    private String nodeID;  //based off of other attributes
    private int index;
    private Double xPos;
    private Double yPos;
    private int floor;
    private String building;
    private String type;
    private String nameLong;
    private String nameShort;
    private String assignedTeam;
    private Double xPos3D;
    private Double yPos3D;
    private Double hits;

    public Node(Double xPos, Double yPos, int floor, int index) {
        this.index = index;
        this.xPos = xPos;
        this.yPos = yPos;
        this.floor = floor;
        this.hits = 0.0;
    }

    public Node(String nodeID, Double xCoord, Double yCoord, int floorNum, String building,
                String nodeType, String longName, String shortName, String team, Double xCoord3D, Double yCoord3D, Double hits){
        this.nodeID = nodeID;
        this.xPos = xCoord;
        this.yPos = yCoord;
        this.floor = floorNum;
        this.building = building;
        this.type = nodeType;
        this.nameLong = longName;
        this.nameShort = shortName;
        this.assignedTeam = team;
        this.xPos3D = xCoord3D;
        this.yPos3D = yCoord3D;
        this.index = 0;
        this.hits = hits;
    }

    public int getIndex() { return this.index; }

    public void setIndex(int index) { this.index = index;    }

    public void changeTypeNode (String type){
        this.type = type;
    }

    public String getNodeID() {
        return nodeID;
    }

    public void setNodeID(String nodeID) {
        this.nodeID = nodeID;
    }

    public Double getxPos() {
        return this.xPos;
    }

    public void setxPos(Double xPos) {
        this.xPos = xPos;
    }

    public Double getyPos() {
        return this.yPos;
    }

    public void setyPos(Double yPos) {
        this.yPos = yPos;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }


    public Double calcDistance(Node endNode){
        Double x1 = this.getxPos();
        Double y1 = this.getyPos();
        Double x2 = endNode.getxPos();
        Double y2 = endNode.getyPos();
        Double distance = Math.sqrt(Math.pow((x1-x2),2)+Math.pow((y1-y2),2));
        return distance;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNameLong() {
        return nameLong;
    }

    public void setNameLong(String nameLong) {
        this.nameLong = nameLong;
    }

    public String getNameShort() {
        return nameShort;
    }

    public void setNameShort(String nameShort) {
        this.nameShort = nameShort;
    }

    public String getAssignedTeam() {
        return assignedTeam;
    }

    public void setAssignedTeam(String assignedTeam) {
        this.assignedTeam = assignedTeam;
    }

    public Double getxPos3D() {
        return xPos3D;
    }

    public void setxPos3D(Double xPos3D) {
        this.xPos3D = xPos3D;
    }

    public Double getyPos3D() {
        return yPos3D;
    }

    public void setyPos3D(Double yPos3D) {
        this.yPos3D = yPos3D;
    }

    public Double getHits() {
        return hits;
    }

    public void incrementHit() {
        this.hits++;
    }
}