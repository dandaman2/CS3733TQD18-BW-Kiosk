package edu.wpi.cs3733d18.teamQ.manageDB;

import edu.wpi.cs3733d18.teamQ.pathfinding.Node;

import java.sql.ResultSet;
import java.sql.SQLException;

 class NodeStringConverter {

    /**
     * Converts a ResultSet into a Node
     * @param results The ResultSet to convert
     * @return A Node with the data of the ResultSet
     */
     static Node convertToTypeNode(ResultSet results){
        Node toReturn = null;
        try {
            toReturn = new Node(
                    results.getString("NODEID"),
                    results.getDouble("XCOORD"),
                    results.getDouble("YCOORD"),
                    results.getInt("FLOOR"),
                    results.getString("BUILDING"),
                    results.getString("NODETYPE"),
                    results.getString("LONGNAME"),
                    results.getString("SHORTNAME"),
                    results.getString("TEAMNAME"),
                    results.getDouble("XCOORD3D"),
                    results.getDouble("YCOORD3D"),
                    results.getDouble("HITS"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    /**
     * Converts a ResultSet for a node into a String
     * @param rs2 The ResultSet to convert
     * @return The ReultSet in String form
     */
     static String nodeRsToString(ResultSet rs2){
        String node ="";
        try {
            node = rs2.getString("NODEID") +','
                    +rs2.getString("XCOORD") +','
                    +rs2.getString("YCOORD") +','
                    +rs2.getString("FLOOR") +','
                    +rs2.getString("BUILDING") +','
                    +rs2.getString("NODETYPE") +','
                    +rs2.getString("LONGNAME") +','
                    +rs2.getString("SHORTNAME") +','
                    +rs2.getString("TEAMNAME") + ','
                    +rs2.getString("XCOORD3D") +','
                    +rs2.getString("YCOORD3D") +','
                    +rs2.getString("HITS") +'\n';
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return node;
    }

    /***
     * Converts a Node to a String that can be input to the db
     * @param input The Node to convert
     * @return The given node in String form
     */
     static String nodeToString(Node input){
        String node = "(\'" +input.getNodeID() +"\',"
                +input.getxPos() +','
                +input.getyPos() +','
                +input.getFloor() +','
                +"\'" + input.getBuilding() +"\',"
                +"\'" + input.getType() +"\',"
                +"\'" + input.getNameLong() +"\',"
                +"\'" + input.getNameShort() +"\',"
                +"\'" + input.getAssignedTeam() +"\',"
                +input.getxPos3D() +','
                +input.getyPos3D() +','
                +input.getHits()+ ')';
        return node;
    }

    /***
     * Converts a Node to a String that can be input into the db as an UPDATE
     * @param input The Node to convert
     * @return The given node in String form ready to UPDATE
     */
     static String nodeToStringUpdate(Node input){
        String node = "NODEID=\'" +input.getNodeID() +"\',"
                +"XCOORD="+input.getxPos() +','
                +"YCOORD="+input.getyPos() +','
                +"FLOOR="+input.getFloor() +','
                +"BUILDING=\'" + input.getBuilding() +"\',"
                +"NODETYPE=\'" + input.getType() +"\',"
                +"LONGNAME=\'" + input.getNameLong() +"\',"
                +"SHORTNAME=\'" + input.getNameShort() +"\',"
                +"TEAMNAME=\'" + input.getAssignedTeam() +"\',"
                +"XCOORD3D="+input.getxPos3D() +','
                +"YCOORD3D="+input.getyPos3D() +','
                +"HITS="+input.getHits();
        return node;
    }


}
