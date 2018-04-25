package edu.wpi.cs3733d18.teamQ.manageDB;

import edu.wpi.cs3733d18.teamQ.ui.Requests.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Scanner;

import static edu.wpi.cs3733d18.teamQ.manageDB.database.executeStatement;
import static edu.wpi.cs3733d18.teamQ.manageDB.database.getTable;

 class RequestDB {

    private static String executionRequest;

    /**
     * Reads Request data into the db from a CSV
     * @param csvFile2 Path to the CSV to read in
     */
    static void readRequestCSV(InputStream csvFile2) {
        System.out.println("Starting CSV read");
        Scanner inputStream2 = new Scanner(csvFile2);
        String data = inputStream2.nextLine();
        String sqlCommand2;
        while (inputStream2.hasNextLine()){
            data = inputStream2.nextLine();
            String[] values = data.split(",");
            sqlCommand2 = "(" + Integer.parseInt(values[0])
                    + "," + Integer.parseInt(values[1])
                    + "," + Integer.parseInt(values[2])
                    + ",'" + values[3] + "'"
                    + ",'" + values[4] + "'"
                    + "," + Boolean.parseBoolean(values[5])
                    + ",'" + values[6] + "'"
                    + ",'" + values[7] + "'"
                    + ",'" + values[8] + "'"
                    + ",'" + java.sql.Date.valueOf(values[9]) + "'"
                    + ",'" + values[10] + "'"
                    + ",'" + values[11] + "'"
                    + ",'" + values[12] + "'"
                    + ",'" + values[13] + "')";
            addRequestsToDb(sqlCommand2);
        }
        inputStream2.close();
        System.out.println("Closing Scanner");
    }

    /**
     * Takes in a Request's data in the form of a string, and enters it into the db
     * @param data A Request in String form, see requestToString
     */
     static void addRequestsToDb(String data){
        try {
            Statement stmnt2 = database.connection.createStatement();
            executionRequest = "INSERT INTO APP.REQUEST VALUES "+ data;
            stmnt2.executeUpdate(executionRequest);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a Request to the db
     * @param request The Request to add
     */
     static void addRequest(Request request) {
        try {
            String query = "INSERT INTO APP.REQUEST VALUES " + requestToString(request);
            Statement queryStmnt = database.connection.createStatement();
            queryStmnt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns an ArrayList of all requests currently stored in the db
     * @return All requests in the db
     */
     static ArrayList<Request> getRequests(){
        ResultSet rs = getTable("APP.REQUEST");
        ArrayList<Request> requests = new ArrayList<Request>();
        try {
            //System.out.println("attempting to search requests");
            while (rs.next()){
                GregorianCalendar cal = new GregorianCalendar();//convert sql date to gregorian
                if(rs.getDate("DATEFULFILLED")!=null) {
                    cal.setTime(rs.getDate("DATEFULFILLED"));
                }
                int requestType = rs.getInt("TYPE");
                if(requestType==1) {
                    requests.add(new InterpreterRequest(rs.getString("FIRSTNAME"),
                            rs.getString("LASTNAME"),
                            rs.getString("ROOMNAME"),
                            rs.getInt("FULFILLED"),
                            rs.getString("NAMEWHOFULFILLED"),
                            rs.getString("EMAIL"),
                            rs.getString("PHONENUMBER"),
                            rs.getString("REQUESTID"),
                            rs.getString("SPECIFICS"),
                            cal,
                            rs.getString("PRIORITY")));
                } else if(requestType==2){
                    requests.add(new SanitationRequest(rs.getString("FIRSTNAME"),
                            rs.getString("LASTNAME"),
                            rs.getString("ROOMNAME"),
                            rs.getInt("FULFILLED"),
                            rs.getString("NAMEWHOFULFILLED"),
                            rs.getString("EMAIL"),
                            rs.getString("PHONENUMBER"),
                            rs.getString("REQUESTID"),
                            cal,
                            rs.getString("SPECIFICS"),
                            rs.getString("PRIORITY")));
                } else if(requestType==3){
                    requests.add(new EmergencyRequest(rs.getString("FIRSTNAME"),
                            rs.getString("LASTNAME"),
                            rs.getString("ROOMNAME"),
                            rs.getInt("FULFILLED"),
                            rs.getString("NAMEWHOFULFILLED"),
                            rs.getString("EMAIL"),
                            rs.getString("PHONENUMBER"),
                            rs.getString("REQUESTID"),
                            cal,
                            rs.getString("PRIORITY")));
                } else if(requestType==4){
                    requests.add(new GiftRequest(rs.getString("FIRSTNAME"),
                            rs.getString("LASTNAME"),
                            rs.getString("ROOMNAME"),
                            rs.getInt("FULFILLED"),
                            rs.getString("NAMEWHOFULFILLED"),
                            rs.getString("EMAIL"),
                            rs.getString("PHONENUMBER"),
                            rs.getString("REQUESTID"),
                            cal,
                            rs.getString("PRIORITY")));
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to find request table");
            e.printStackTrace();
        }
        return requests;
    }

    /**
     * Returns all unfulfilled currently store requests of the given type
     * @param serviceType The type to retrieve
     * @return An ArrayList of Requests of the given type
     */
    static ArrayList<Request> getRequestsOfType(int serviceType){
        ResultSet results = null;
        ArrayList<Request> requests = new ArrayList<Request>();
        try {
            String query = "SELECT * FROM APP.REQUEST WHERE TYPE=" + serviceType + " AND FULFILLED=" + 0;
            System.out.println(query);
            Statement queryStmnt = database.connection.createStatement();
            results = queryStmnt.executeQuery(query);
            while (results.next()) {
                GregorianCalendar cal = new GregorianCalendar();//convert sql date to gregorian
                try {
                    cal.setTime(results.getDate("DATEFULFILLED"));
                }catch (NullPointerException e){
                    cal = null;
                }
                int requestType = results.getInt("TYPE");
                if(requestType==1) {
                    requests.add(new InterpreterRequest(results.getString("FIRSTNAME"),
                            results.getString("LASTNAME"),
                            results.getString("ROOMNAME"),
                            results.getInt("FULFILLED"),
                            results.getString("NAMEWHOFULFILLED"),
                            results.getString("EMAIL"),
                            results.getString("PHONENUMBER"),
                            results.getString("REQUESTID"),
                            results.getString("SPECIFICS"),
                            cal,
                            results.getString("PRIORITY")));
                }else if(requestType==2){
                    requests.add(new SanitationRequest(results.getString("FIRSTNAME"),
                            results.getString("LASTNAME"),
                            results.getString("ROOMNAME"),
                            results.getInt("FULFILLED"),
                            results.getString("NAMEWHOFULFILLED"),
                            results.getString("EMAIL"),
                            results.getString("PHONENUMBER"),
                            results.getString("REQUESTID"),
                            cal,
                            results.getString("SPECIFICS"),
                            results.getString("PRIORITY")));
                }else if(requestType==3){
                    requests.add(new EmergencyRequest(results.getString("FIRSTNAME"),
                            results.getString("LASTNAME"),
                            results.getString("ROOMNAME"),
                            results.getInt("FULFILLED"),
                            results.getString("NAMEWHOFULFILLED"),
                            results.getString("EMAIL"),
                            results.getString("PHONENUMBER"),
                            results.getString("REQUESTID"),
                            cal,
                            results.getString("PRIORITY")));
                }else if(requestType==4){
                    requests.add(new GiftRequest(results.getString("FIRSTNAME"),
                            results.getString("LASTNAME"),
                            results.getString("ROOMNAME"),
                            results.getInt("FULFILLED"),
                            results.getString("NAMEWHOFULFILLED"),
                            results.getString("EMAIL"),
                            results.getString("PHONENUMBER"),
                            results.getString("REQUESTID"),
                            cal,
                            results.getString("PRIORITY")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return requests;
    }

    /**
     * Returns the Request with the given requestUD
     * @param requestID The requestID to search for
     * @return The Request with the matching requestID
     */
     static Request getRequest(String requestID) {
        ResultSet results = null;
        Request foundReqeust =null;
        try {
            String query = "SELECT * FROM APP.REQUEST WHERE REQUESTID='" + requestID + "'";
            Statement queryStmnt = database.connection.createStatement();
            results = queryStmnt.executeQuery(query);
            while (results.next()){
                GregorianCalendar cal = new GregorianCalendar();//convert sql date to gregorian
                try {
                    cal.setTime(results.getDate("DATEFULFILLED"));
                }catch (NullPointerException e){
                    cal = null;
                }
                int requestType = results.getInt("TYPE");
                if(requestType==1) {
                    foundReqeust = new InterpreterRequest(results.getString("FIRSTNAME"),
                            results.getString("LASTNAME"),
                            results.getString("ROOMNAME"),
                            results.getInt("FULFILLED"),
                            results.getString("NAMEWHOFULFILLED"),
                            results.getString("EMAIL"),
                            results.getString("PHONENUMBER"),
                            results.getString("REQUESTID"),
                            results.getString("SPECIFICS"),
                            cal,
                            results.getString("PRIORITY"));
                }else if(requestType==2){
                    foundReqeust = new SanitationRequest(results.getString("FIRSTNAME"),
                            results.getString("LASTNAME"),
                            results.getString("ROOMNAME"),
                            results.getInt("FULFILLED"),
                            results.getString("NAMEWHOFULFILLED"),
                            results.getString("EMAIL"),
                            results.getString("PHONENUMBER"),
                            results.getString("REQUESTID"),
                            cal,
                            results.getString("SPECIFICS"),
                            results.getString("PRIORITY"));
                } else if(requestType==3){
                    foundReqeust = new EmergencyRequest(results.getString("FIRSTNAME"),
                            results.getString("LASTNAME"),
                            results.getString("ROOMNAME"),
                            results.getInt("FULFILLED"),
                            results.getString("NAMEWHOFULFILLED"),
                            results.getString("EMAIL"),
                            results.getString("PHONENUMBER"),
                            results.getString("REQUESTID"),
                            cal,
                            results.getString("PRIORITY"));
                } else if(requestType==4){
                    foundReqeust = new GiftRequest(results.getString("FIRSTNAME"),
                            results.getString("LASTNAME"),
                            results.getString("ROOMNAME"),
                            results.getInt("FULFILLED"),
                            results.getString("NAMEWHOFULFILLED"),
                            results.getString("EMAIL"),
                            results.getString("PHONENUMBER"),
                            results.getString("REQUESTID"),
                            cal,
                            results.getString("PRIORITY"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return foundReqeust;
    }

    /**
     * Removes the given Request from the db
     * @param request The Request to remove
     */
     static void removeRequest(Request request) {
        String databaseExecution = "DELETE FROM APP.REQUEST WHERE REQUESTID ='" + request.getRequestID() + "'";
        executeStatement(databaseExecution);
    }

    /**
     * Updates the fulfilled status, nameWhoFulfilled, and dateFulfilled of the given Request
     * @param request The Request to update
     */
     static void editRequest(Request request) {
        java.util.Date utilDate = request.getDate().getTime();
        System.out.println(utilDate);
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        System.out.println(sqlDate);
        String databaseExecution = "UPDATE APP.REQUEST SET FULFILLED=" + 1 + ", NAMEWHOFULFILLED='"
                + request.getNameWhoFulfilled() + "', DATEFULFILLED='" + sqlDate + "' WHERE REQUESTID = '" + request.getRequestID()+ "'";
        executeStatement(databaseExecution);
    }

    /**
     * Exports all requests in the db to a CSV
     */
     static void exportRequestToCSV() {
        String query1 = "SELECT * FROM APP.REQUEST";
        FileWriter Requests = null;
        String RequestHeader = "REQUESTID, ROOM, FIRSTNAME, LASTNAME, FULFILLED, NAMEWHOFULFILLED, EMAIL, PHONENUMBER, " +
                "DATEFULFILLED, ROOMNAME, DESCRIPTION, PRIORITY, LANGUAGEREQUESTED\n";
        try {
            File requestcsv = new File("NewRequest.csv");
            Requests = new FileWriter(requestcsv);
            System.out.println(requestcsv.getAbsolutePath());
            try {
                Statement stmnt1 = database.connection.createStatement();
                ResultSet rs1 = stmnt1.executeQuery(query1);
                Requests.append(RequestHeader);
                while (rs1.next()) {
                    Requests.append(rs1.getString("REQUESTID") + ',');
                    Requests.append(rs1.getString("ROOM") + ',');
                    Requests.append(rs1.getString("FIRSTNAME") + ',');
                    Requests.append(rs1.getString("LASTNAME") + ',');
                    Requests.append(rs1.getString("FULFILLED") + ',');
                    Requests.append(rs1.getString("NAMEWHOFULFILLED") + ',');
                    Requests.append(rs1.getString("EMAIL") + ',');
                    Requests.append(rs1.getString("PHONENUMBER") + ',');
                    Requests.append(rs1.getString("DATEFULFILLED") + ',');
                    Requests.append(rs1.getString("ROOMNAME") + ',');
                    Requests.append(rs1.getString("DESCRIPTION") + ',');
                    Requests.append(rs1.getString("PRIORITY") + ',');
                    Requests.append(rs1.getString("LANGUAGEREQUESTED") + '\n');
                }
                System.out.println("CSV files successfully created!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                Requests.flush();
                Requests.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns an int equal to the greatest current requestID + 1, to assign to a newly created request
     * @return The requestID to assign to a new Request
     */
     static int getNewRequestID() {
        ResultSet results = null;
        int newID = 1;
        try {
            String query = "SELECT MAX(CAST(REQUESTID AS INT)) AS MAXID FROM APP.REQUEST";
            Statement queryStmnt = database.connection.createStatement();
            results = queryStmnt.executeQuery(query);
            while (results.next()){
                newID = results.getInt("MAXID")+1;
                System.out.println("New ID: "+newID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return newID;
        }
        return newID;
    }

    /**
     * Converts a Request to a String that can be input to the db
     * @param input The Request to convert
     * @return The given Request in String form
     */
     static String requestToString(Request input) {
        String requestType = input.getClass().getSimpleName();
        int type = 1;
        if(requestType.equals("InterpreterRequest")){
            type = 1;
        } else if(requestType.equals("SanitationRequest")){
            type = 2;
        } else if(requestType.equals("EmergencyRequest")){
            type = 3;
        } else if(requestType.equals("GiftRequest")){
            type = 4;
        }
        String request = "('" + input.getRequestID() + "',"
                + type + ","
                + input.getRoom() + ','
                + "'" + input.getFirstName() + "',"
                + "'" + input.getLastName() + "',"
                + input.isFulfilled() + ","
                + "'" + input.getNameWhoFulfilled() + "',"
                + "'" + input.getEmail() + "',"
                + "'" + input.getPhoneNumber() + "',"
                + "" + " null" + ","
                + "'" + input.getRoomName() + "',"
                + "'" + input.getDescription() + "',"
                + "'" + input.getPriority() + "',"
                + "'" + input.getSpecifics() + "')";
        return request;
    }
}
