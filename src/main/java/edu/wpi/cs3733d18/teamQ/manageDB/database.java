package edu.wpi.cs3733d18.teamQ.manageDB;

import java.sql.*;

import static edu.wpi.cs3733d18.teamQ.manageDB.EmployeeDB.populateEmployeeDB;


class database {

    static Connection connection;

    /**
     * Initializes the database through connectDB
     * @return connectDB
     */
     static boolean initializeDb(){
        if(!databaseDriverCheck()){
            return false;
        }
        return connectDb();
    }

    /**
     * Checks for the derby db driver
     * @return true if driver is good, otherwise false
     */
    private static boolean databaseDriverCheck(){
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Driver issue");
            return false;
        }
    }

    /**
     * Closes the db connection
     * @return true if connection closed successfully, otherwise false
     */
     boolean closeDB(){
        try {
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Connects to the db, if it doesn't exist, it creates the db and populates the nodes and edges from CSVs
     * @return true if connected successfully, otherwise false
     */
    private static boolean connectDb(){
        try {
                System.out.println("Connecting to database");
                connection = DriverManager.getConnection("jdbc:derby:CS3733TeamQ;");
        } catch (SQLException e){
            System.out.println("Database connection failed");
            e.printStackTrace();
            System.out.println("Database doesn't exist");
            System.out.println("Creating DB");
            try {
                connection = DriverManager.getConnection("jdbc:derby:CS3733TeamQ;create= true");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        if(!checkTables()){
            createTables();
            System.out.println("Populating database");
            if(runningFromIntelliJ()) {
                NodeDB.readNodeCSV(database.class.getResourceAsStream("/csvFiles/nodes.csv"));
                EdgeDB.readEdgeCSV(database.class.getResourceAsStream("/csvFiles/edges.csv"));
            } else {
                NodeDB.readNodeCSV(database.class.getResourceAsStream("/csvFiles/nodes.csv"));
                EdgeDB.readEdgeCSV(database.class.getResourceAsStream("/csvFiles/edges.csv"));
            }
            populateEmployeeDB();
        }
        return true;
    }

    /**
     * Checks if all tables are present in the db
     * @return false if any tables are missing, otherwise true
     */
    private static boolean checkTables(){
        if(getTable("APP.NODES")==null || getTable("APP.EDGE")==null ||
                getTable("APP.REQUEST")==null || getTable("APP.EMPLOYEE")==null){
            return false;
        }
        return true;
    }

    /**
     * Creates all tables in the db
     */
    private static void createTables(){
        try {
            Statement stmnt = connection.createStatement();
            String execution = "CREATE TABLE NODES\n" +
                    "(NODEID VARCHAR(11) PRIMARY KEY,\n" +
                    " XCOORD DOUBLE,\n" +
                    " YCOORD DOUBLE,\n" +
                    " FLOOR INT,\n" +
                    " BUILDING VARCHAR(100),\n" +
                    " NODETYPE VARCHAR(100),\n" +
                    " LONGNAME VARCHAR(100),\n" +
                    " SHORTNAME VARCHAR(100),\n" +
                    " TEAMNAME VARCHAR(100),\n" +
                    " XCOORD3D DOUBLE, \n" +
                    " YCOORD3D DOUBLE,\n" +
                    " HITS DOUBLE)";
            String execution2 = "CREATE TABLE EDGE\n" +
                    "(EDGEID VARCHAR(100) PRIMARY KEY,\n" +
                    " STARTNODE VARCHAR(100),\n" +
                    " ENDNODE VARCHAR(100),\n" +
                    " DISTANCE DOUBLE)";
            String execution3 = "CREATE TABLE REQUEST\n" +
                    "(REQUESTID VARCHAR(100) PRIMARY KEY,\n" +
                    " TYPE INT,\n" +
                    " ROOM INT,\n" +
                    " FIRSTNAME VARCHAR(100),\n" +
                    " LASTNAME VARCHAR(100),\n" +
                    " FULFILLED INT,\n" +
                    " NAMEWHOFULFILLED VARCHAR(100),\n" +
                    " EMAIL VARCHAR(100),\n" +
                    " PHONENUMBER VARCHAR(100),\n" +
                    " DATEFULFILLED DATE,\n" +
                    " ROOMNAME VARCHAR(100),\n" +
                    " DESCRIPTION VARCHAR(100),\n" +
                    " PRIORITY VARCHAR(20),\n" +
                    " SPECIFICS VARCHAR(600))";
            String execution4 = "CREATE TABLE EMPLOYEE\n" +
                    "(USERNAME VARCHAR(100) PRIMARY KEY,\n" +
                    "PASSWORD VARCHAR(100),\n" +
                    "ISADMIN INT,\n" +
                    "FACEID VARCHAR(500))";
            String execution5 = "CREATE TABLE ABILITY " +
                    "(USERNAME VARCHAR(100), " +
                    "TYPE INT, " +
                    "FOREIGN KEY (USERNAME) REFERENCES EMPLOYEE (USERNAME))";
            stmnt.executeUpdate(execution);
            stmnt.executeUpdate(execution2);
            stmnt.executeUpdate(execution3);
            stmnt.executeUpdate(execution4);
            stmnt.executeUpdate(execution5);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Queries the database for all the entries in the given table
     * @param table The name of the table to retrieve
     * @return ResultSet of all entries in the table
     */
    static ResultSet getTable(String table){
        String checkExecution = "SELECT * FROM "+table;
        ResultSet answer = null;
        try {
            Statement checkStmn = connection.createStatement();
            answer = checkStmn.executeQuery(checkExecution);
        } catch (SQLException e) {
            System.out.println("Table does not exist");
            return null;
        }
        return answer;
    }

    /**
     * Executes a query
     * @param databaseExecution The statement to run
     */
    static void executeStatement(String databaseExecution) {
        try {
            Statement stmnt = database.connection.createStatement();
            stmnt.executeUpdate(databaseExecution);
//            System.out.println("Update Successful");

        } catch (SQLException e) {
            System.out.println("Update Failed");
            e.printStackTrace();
        }
    }

     static boolean runningFromIntelliJ()
    {
        String classPath = System.getProperty("java.class.path");
        return classPath.contains("idea_rt.jar");
    }
}