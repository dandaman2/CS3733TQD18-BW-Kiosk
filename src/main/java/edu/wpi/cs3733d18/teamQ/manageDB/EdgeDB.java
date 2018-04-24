package edu.wpi.cs3733d18.teamQ.manageDB;

import edu.wpi.cs3733d18.teamQ.pathfinding.Edge;
import edu.wpi.cs3733d18.teamQ.pathfinding.Node;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import static edu.wpi.cs3733d18.teamQ.manageDB.NodeDB.getNode;
import static edu.wpi.cs3733d18.teamQ.manageDB.database.executeStatement;
import static edu.wpi.cs3733d18.teamQ.manageDB.database.getTable;

 class EdgeDB {

    private static String executionEdge;

    /**
     * Reads Edge data into the db from a CSV
     * @param csvFile1 Path to the CSV to read in
     */
    static void readEdgeCSV(InputStream csvFile1) {
        System.out.println("Starting CSV read");
        Scanner inputStream1 = new Scanner(csvFile1);
        String data = inputStream1.nextLine();
        String sqlCommand1;
        double distance;
        while (inputStream1.hasNextLine()){
            data = inputStream1.nextLine();
            String[] values = data.split(",");
            distance = calcEdgeDistance(getNode(values[1]), getNode(values[2]));
            sqlCommand1 = "(" + "'" +values[0] +"'"
                    + ",'" + values[1] + "'"
                    + ",'" + values[2] + "'"
                    + "," + distance
                    + "," + 1 + ")";
            addEdgesToDb(sqlCommand1);
        }
        inputStream1.close();
        System.out.println("Closing Scanner");
    }

    /**
     * Calculates the distance between two nodes (edge length), order does not matter
     * @param startNode One of the edge's nodes
     * @param endNode One of the edge's nodes
     * @return The distance between the two nodes
     */
    private static double calcEdgeDistance(Node startNode, Node endNode){
        if(!(startNode==null || endNode==null)){
            Double x1 = startNode.getxPos();
            Double y1 = startNode.getyPos();
            Double x2 = endNode.getxPos();
            Double y2 = endNode.getyPos();
            Double distance = Math.sqrt(Math.pow((x1-x2),2)+Math.pow((y1-y2),2));
            return distance;
        }else {
            return 0.0;
        }
    }

    /**
     * Takes in an Edge's data in the form of a string, and enters it into the db
     * @param data An edge in String form, see EdgeStringConverter
     */
     static void addEdgesToDb(String data){
        try {
            Statement stmnt1 = database.connection.createStatement();
            executionEdge = "INSERT INTO APP.EDGE VALUES "+ data;
            stmnt1.executeUpdate(executionEdge);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds an edge to the db
     * @param edge The edge to add
     */
     static void addEdge(Edge edge) {
        try {
            String query = "INSERT INTO APP.EDGE VALUES " + EdgeStringConverter.edgeToString(edge);
            Statement queryStmnt = database.connection.createStatement();
            queryStmnt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns an ArrayList of all edges currently stored in the db
     * @return All edges in the db
     */
     static ArrayList<Edge> getEdges(){
        ResultSet rs = getTable("APP.EDGE");
        ArrayList<Edge> edges = new ArrayList<Edge>();
        try {
            while (rs.next()){
                edges.add(EdgeStringConverter.convertToTypeEdge(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return edges;
    }

    /**
     * Removes the given edge from the db
     * @param edge The edge to remove
     */
     static void removeEdge(Edge edge){
        String databaseExecution = "DELETE FROM APP.EDGE WHERE EDGEID =\'"+edge.getEdgeID() +"\'";
        executeStatement(databaseExecution);
    }

    /**
     * Exports all edges in the db to a CSV
     */
     static void exportEdgeToCSV() {
        String query1 = "SELECT * FROM APP.EDGE";
        FileWriter Edges = null;
        String EdgeHeader = "EDGEID, STARTNODE, ENDNODE, DISTANCE\n";
        try {
            File edgecsv = new File("NewMapQEdges.csv");
            Edges = new FileWriter(edgecsv);
            System.out.println(edgecsv.getAbsolutePath());
            try {
                Statement stmnt1 = database.connection.createStatement();
                ResultSet rs1 = stmnt1.executeQuery(query1);
                Edges.append(EdgeHeader);
                while (rs1.next()) {
                    Edges.append(EdgeStringConverter.edgeToString(rs1));
                }
                System.out.println("CSV files successfully created!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                Edges.flush();
                Edges.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void editEdge(Edge edge) {
        String databaseExecution = "UPDATE APP.EDGE SET NODETYPE=" + EdgeStringConverter.edgeToStringUpdate(edge) +", ISENABLED="+edge.isEnabledInt() + " WHERE NODEID =\'" + edge.getEdgeID() + "\'";
        try {
            Statement stmnt = database.connection.createStatement();
            stmnt.executeUpdate(databaseExecution);
//            System.out.println("Update Successful");

        } catch (SQLException e) {
            System.out.println("Update Failed");
            e.printStackTrace();
        }
    }

    public static ArrayList<Edge> getEdgesByNode(Node node){
        ResultSet results = null;
        ArrayList<Edge> edges = new ArrayList<Edge>();
        String query = "SELECT * FROM APP.EDGE WHERE STARTNODE=\'" + node.getNodeID() + "\' OR ENDNODE=\'" + node.getNodeID() + "\'" ;
        try {
            Statement stmnt = database.connection.createStatement();
            results = stmnt.executeQuery(query);
            System.out.println("Edges by node");
            while (results.next()){
                edges.add(EdgeStringConverter.convertToTypeEdge(results));
            }
        } catch (SQLException e) {
            System.out.println("Failed");
            e.printStackTrace();
        }
        return edges;
    }

    public static ArrayList<Edge> getEdgesByFloor(int floor){
        ResultSet results = null;
        ArrayList<Edge> edges = new ArrayList<Edge>();
        String query = "SELECT * FROM APP.EDGE WHERE STARTNODE IN (SELECT NODEID FROM APP.NODES WHERE FLOOR=" + floor + ") AND ENDNODE IN (SELECT NODEID FROM APP.NODES WHERE FLOOR=" + floor + ")" ;
        try {
            Statement stmnt = database.connection.createStatement();
            results = stmnt.executeQuery(query);
            System.out.println("Floor Edges");
            while (results.next()){
                edges.add(EdgeStringConverter.convertToTypeEdge(results));
            }
        } catch (SQLException e) {
            System.out.println("Failed");
            e.printStackTrace();
        }
        return edges;
    }

}
