package edu.wpi.cs3733d18.teamQ.manageDB;

import edu.wpi.cs3733d18.teamQ.pathfinding.Edge;

import java.sql.ResultSet;
import java.sql.SQLException;

import static edu.wpi.cs3733d18.teamQ.manageDB.NodeDB.getNode;

 class EdgeStringConverter {

    /***
     * Converts an Edge to a String that can be input to the db
     * @param input The edge to convert
     * @return The given edge in String form
     */
     static String edgeToString(Edge input){
        String edge = "(\'" +input.getEdgeID() +"\',"
                +"\'" + input.getStartNode().getNodeID() +"\',"
                +"\'" + input.getEndNode().getNodeID() +"\',"
                +input.getDistance() + ","
                +input.isEnabledInt() + ')';
        return edge;
    }

    /***
     * Converts an Edge to a String that can be input into the db as an UPDATE
     * @param input The edge to convert
     * @return The given edge in String form ready to UPDATE
     */
     static String edgeToStringUpdate(Edge input){
        String edge = "EDGEID=\'" +input.getEdgeID() +"\',"
                +"STARTNODE=\'" + input.getStartNode() +"\',"
                +"ENDNODE=\'" + input.getEndNode() +"',"
                +"DISTANCE="+input.getDistance();
        return edge;
    }

    /**
     * Converts a ResultSet for an edge into a String
     * @param rs1 The ResultSet to convert
     * @return The ReultSet in String form
     */
     static String edgeToString(ResultSet rs1){
        String edge = "";
        try {
            edge = rs1.getString("EDGEID") + ','
                    +rs1.getString("STARTNODE") + ','
                    +rs1.getString("ENDNODE") + ','
                    +rs1.getString("DISTANCE") + ','
                    +rs1.getString("ISENABLED") + '\n';
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return edge;
    }

    /**
     * Converts a ResultSet into an Edge
     * @param rs The ResultSet to convert
     * @return An Edge with the data of the ResultSet
     */
     static Edge convertToTypeEdge(ResultSet rs){
        Edge edge = null;
        try {
            edge = new Edge(rs.getString("EDGEID"),
                    getNode(rs.getString("STARTNODE")),
                    getNode(rs.getString("ENDNODE")),
                    rs.getDouble("DISTANCE"),
                    rs.getInt("ISENABLED")==1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return edge;
    }




}
