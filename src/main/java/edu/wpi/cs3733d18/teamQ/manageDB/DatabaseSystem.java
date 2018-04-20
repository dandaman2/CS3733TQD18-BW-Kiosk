package edu.wpi.cs3733d18.teamQ.manageDB;

import edu.wpi.cs3733d18.teamQ.pathfinding.Edge;
import edu.wpi.cs3733d18.teamQ.pathfinding.Node;
import edu.wpi.cs3733d18.teamQ.ui.Employee;
import edu.wpi.cs3733d18.teamQ.ui.Requests.Request;

import java.util.ArrayList;

public class DatabaseSystem {
    private static database systemDB;
    private static EdgeDB systemEdgeDB;
    private static EmployeeDB systemEmployeeDB;
    private static NodeDB systemNodeDB;
    private static RequestDB systemRequestDB;
    private static AbilityDB systemAbilityDB;

    public DatabaseSystem() {
        systemDB = new database();
        systemEdgeDB = new EdgeDB();
        systemEmployeeDB = new EmployeeDB();
        systemNodeDB = new NodeDB();
        systemRequestDB = new RequestDB();
        systemAbilityDB = new AbilityDB();
    }

    /**
     * Initializes the database
     * @return database.initializeDB
     */
    public static boolean initializeDb(){
        return database.initializeDb();
    }

    public static boolean runningFromIntelliJ(){
        return database.runningFromIntelliJ();
    }

    /**
     * Closes the database
     * @return True if succesful, else false
     */
    public boolean closeDB(){
        return systemDB.closeDB();
    }

    /**
     * Adds an edge to the database
     * @param edge the Edge object to add
     */
    public static void addEdge(Edge edge){
        EdgeDB.addEdge(edge);
    }

    /**
     * Adds an edge to the database from a string
     * @param data the edge in string form
     */
    public void addEdgesToDb(String data){
        systemEdgeDB.addEdgesToDb(data);
    }

    public static ArrayList<Edge> getEdgesByFloor(int f){
        return EdgeDB.getEdgesByFloor(f);
    }
    /**
     * Gets all edges from the database
     * @return ArrayList of all Edges
     */
    public static ArrayList<Edge> getEdges(){
        return EdgeDB.getEdges();
    }

    public static ArrayList<Edge> getEdgesByNode(Node n){
        return EdgeDB.getEdgesByNode(n);
    }
    /**
     * Removes a given edge from the database
     * @param edge the Edge to remove
     */
    public static void removeEdge(Edge edge){
        EdgeDB.removeEdge(edge);
    }

    /**
     * Exports the Edge table to a .csv file
     */
    public static void exportEdgeToCSV(){
        EdgeDB.exportEdgeToCSV();
    }


    /**
     * Adds an employee to the database
     * @param employee the Employee to add
     */
    public static void addEmployee(Employee employee){
        systemEmployeeDB.addEmployee(employee);
    }

    /**
     * Gets all employees from the databse
     * @return ArrayList of all Employees
     */
    public static ArrayList<Employee> getEmployees(){
        return systemEmployeeDB.getEmployees();
    }

    /**
     * Gets an employee from the database
     * @param username the username of the employee to get
     * @return the Employee with the given username
     */
    public static Employee getEmployee(String username){
        return systemEmployeeDB.getEmployee(username);
    }

    /**
     * Removes a given employee from the database
     * @param employee the Employee to remove
     */
    public static void removeEmployee(Employee employee){
        systemEmployeeDB.removeEmployee(employee);
    }

    /**
     * Edits a given employee in the database
     * @param employee the Employee to remove
     */
    public static void editEmployee(Employee employee){
        systemEmployeeDB.editEmployee(employee);
    }

    public static ArrayList<Integer> getEmployeeAbilities(Employee employee){
        return EmployeeDB.getEmployeeAbilities(employee);
    }

    /**
     * Adds a node to the database from the string format
     * @param data the node as a string
     */
    public void addNodesToDb(String data){
        systemNodeDB.addNodesToDb(data);
    }

    /**
     * Adds a node to the database
     * @param node the Node to add
     */
    public static void addNode(Node node){
        NodeDB.addNode(node);
    }

    /**
     * Gets all of the nodes from the databse
     * @return the ArrayList of all the nodes
     */
    public static ArrayList<Node> getNodes(){
        return NodeDB.getNodes();
    }


    public static ArrayList<Node> getNodesByFloor(int floor){
        return NodeDB.getNodesOnFloor(floor);
    }
    /**
     * Gets all of the nodes on a floor
     * @param floor The floor to get the nodes on
     * @return ArrayList of all nodes on the floor
     */
    public ArrayList<Node> getNodesOnFloor(int floor){
        return systemNodeDB.getNodesOnFloor(floor);
    }

    /**
     * Gets a node from the database
     * @param nodeID the id of the node to get
     * @return the node with the given nodeID
     */
    public static Node getNode(String nodeID){
        return NodeDB.getNode(nodeID);
    }

    /**
     * Removes a given node from the database
     * @param node the node to remove
     */
    public static void removeNode(Node node){
        NodeDB.removeNode(node);
    }

    /**
     * Edits a node in the databse
     * @param node the Node to edit
     */
    public static void editNode(Node node){
        systemNodeDB.editNode(node);
    }

    /**
     * Exports the Node table to a .csv file
     */
    public static void exportNodeToCSV(){
        NodeDB.exportNodeToCSV();
    }

    public static double getMaxHits(){
        return NodeDB.getMaxHits();
    }

    /**
     * Adds a request to the database from a string
     * @param data the request as a string
     */
    public void addRequestsToDB(String data){
        systemRequestDB.addRequestsToDb(data);
    }

    /**
     * Adds a request to the databse
     * @param request the InterpreterRequest to add
     */
    public static void addRequest(Request request){
        RequestDB.addRequest(request);
    }

    /**
     * Gets all requests from the database
     * @return ArrayList of all requests
     */
    public static ArrayList<Request> getRequests(){
        return RequestDB.getRequests();
    }

    /**
     * Gets a given request from the database
     * @param requestID the requestID of the request
     * @return the request with the given requestID
     */
    public static Request getRequest(String requestID){
        return RequestDB.getRequest(requestID);
    }

    /**
     * Removes a request from the database
     * @param request the request to remove
     */
    public static void removeRequest(Request request){
        RequestDB.removeRequest(request);
    }

    /**
     * Edits a request in the databse
     * @param request the request to edit
     */
    public static void editRequest(Request request){
        RequestDB.editRequest(request);
    }

    /**
     * Gets all requests of a given type
     * @param serviceType the type of request
     * @return an ArrayList of Requests with type serviceType
     */
    public static ArrayList<Request> getRequestsOfType(int serviceType){
        return RequestDB.getRequestsOfType(serviceType);
    }

    /**
     * Gets the next request id
     * @return an integer for the request id
     */
    public static int getNewRequestID(){
        return RequestDB.getNewRequestID();
    }

    /**
     * Adds an ability to the database
     * @param employee the employee to add the ability for
     * @param serviceType the type of service
     */
    public static void addAbility(Employee employee, int serviceType){
        AbilityDB.addAbility(employee, serviceType);
    }

    /**
     * Removes an ability from the database
     * @param employee the employee to remove it from
     * @param serviceType the type of service to remove
     */
    public static void removeAbility(Employee employee, int serviceType){
        AbilityDB.removeAbility(employee, serviceType);
    }

}
