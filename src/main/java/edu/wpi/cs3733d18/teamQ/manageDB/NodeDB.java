package edu.wpi.cs3733d18.teamQ.manageDB;


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

import static edu.wpi.cs3733d18.teamQ.manageDB.database.executeStatement;
import static edu.wpi.cs3733d18.teamQ.manageDB.database.getTable;

 class NodeDB {

    private static String execution;

    /**
     * Reads Node data into the db from a CSV
     * @param csvFile Path to the CSV to read in
     */
    static void readNodeCSV(InputStream csvFile) {
        System.out.println("Starting CSV read");
        Scanner inputStream = new Scanner(csvFile);
        String data = inputStream.nextLine();
        String sqlCommand;
        while (inputStream.hasNextLine()){
            data = inputStream.nextLine();
            String[] values = data.split(",");
            sqlCommand = "(" + "'" +values[0] +"'"
                    + "," + Double.parseDouble(values[1])
                    + "," + Double.parseDouble(values[2])
                    + "," + Integer.parseInt(values[3])
                    + ",'" + values[4] + "'"
                    + ",'" + values[5] + "'"
                    + ",'" + values[6] + "'"
                    + ",'" + values[7] + "'"
                    + ",'" + values[8] + "'"
                    + "," + Double.parseDouble(values[9])
                    + "," + Double.parseDouble(values[10])
                    + "," + 0.0 + ")";
            addNodesToDb(sqlCommand);
        }
        inputStream.close();
        System.out.println("Closing Scanner");
    }

    /**
     * Takes in a Node's data in the form of a string, and enters it into the db
     * @param data A node in String form, see NodeStringConverter
     */
     static void addNodesToDb(String data){
        try {
            Statement stmnt = database.connection.createStatement();
            execution = "INSERT INTO APP.NODES VALUES "+ data;
            stmnt.executeUpdate(execution);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a node to the db
     * @param node The node to add
     */
     static void addNode(Node node) {
        try {
            String query = "INSERT INTO APP.NODES VALUES " + NodeStringConverter.nodeToString(node);
            Statement queryStmnt = database.connection.createStatement();
            queryStmnt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns an ArrayList of all nodes currently stored in the db
     * @return All nodes in the db
     */
     static ArrayList<Node> getNodes(){
        ResultSet rs = getTable("APP.NODES");
        ArrayList<Node> nodes = new ArrayList<Node>();
        try {
            while (rs.next()){
                nodes.add(NodeStringConverter.convertToTypeNode(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nodes;
    }

    /**
     * Returns an ArrayList of all of the nodes on a given floor
     * @param floor The floor to get
     * @return All nodes on the floor
     */


     static ArrayList<Node> getNodesOnFloor(int floor){
         String query = "SELECT * FROM APP.NODES WHERE FLOOR=" + floor ;
        ResultSet results = null;
        ArrayList<Node> nodes = new ArrayList<Node>();
        try {
            Statement stmnt = database.connection.createStatement();
            results = stmnt.executeQuery(query);
            System.out.println("Floor Nodes");
            while (results.next()){
                nodes.add(NodeStringConverter.convertToTypeNode(results));
            }
        } catch (SQLException e) {
            System.out.println("Failed");
            e.printStackTrace();
        }
        return nodes;
    }

    /**
     * Returns the node with the given nodeID
     * @param nodeID The nodeID to search for
     * @return The node with the matching nodeID
     */
     static Node getNode(String nodeID) {
        ResultSet results = null;
        Node foundNode =null;
        try {
            String query = "SELECT * FROM APP.NODES WHERE NODEID=" + "'" +nodeID+"'";
            Statement queryStmnt = database.connection.createStatement();
            results = queryStmnt.executeQuery(query);
            while (results.next()){
                foundNode = NodeStringConverter.convertToTypeNode(results);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return foundNode;
    }

    /**
     * Removes the given node from the db
     * @param node The node to remove
     */
     static void removeNode(Node node){
        String databaseExecution = "DELETE FROM APP.NODES WHERE NODEID =\'"+node.getNodeID() +"\'";
        executeStatement(databaseExecution);
    }

    /**
     * Updates the node type of the given node
     * @param node The node to update
     */
     static void editNode(Node node){
        String databaseExecution = "UPDATE APP.NODES SET " + NodeStringConverter.nodeToStringUpdate(node) +" WHERE NODEID =\'"+node.getNodeID() +"\'";
        executeStatement(databaseExecution);
    }

    /**
     * Exports all nodes in the db to a CSV
     */
     static void exportNodeToCSV() {
        String query1 = "SELECT * FROM APP.NODES";
        FileWriter Nodes = null;
        String NodeHeader = "nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName,teamAssigned,xcoord3d,ycoord3d\n";
        try {
            File nodecsv = new File("NewMapQNodes.csv");
            Nodes = new FileWriter(nodecsv);
            System.out.println(nodecsv.getAbsolutePath());
            try {
                Statement stmnt1 = database.connection.createStatement();
                ResultSet rs2 = stmnt1.executeQuery(query1);
                Nodes.append(NodeHeader);
                while (rs2.next()) {
                    Nodes.append(NodeStringConverter.nodeRsToString(rs2));
                }
                System.out.println("CSV files successfully created!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                Nodes.flush();
                Nodes.close();
                System.out.println("Works");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

     static double getMaxHits() {
         ResultSet results = null;
         int maxHits = 0;
         try {
             String query = "SELECT MAX(HITS) AS MAXHITS FROM APP.NODES";
             Statement queryStmnt = database.connection.createStatement();
             results = queryStmnt.executeQuery(query);
             while (results.next()){
                 maxHits = results.getInt("MAXHITS");
                 System.out.println("Max hits: "+maxHits);
                 return maxHits;
             }
         } catch (SQLException e) {
             e.printStackTrace();
             return maxHits;
         }
         return maxHits;
     }

}
