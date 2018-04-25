package edu.wpi.cs3733d18.teamQ.ui;

import edu.wpi.cs3733d18.teamQ.pathfinding.Edge;
import edu.wpi.cs3733d18.teamQ.pathfinding.Node;
import edu.wpi.cs3733d18.teamQ.ui.Controller.ScreenUtil;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;

import static edu.wpi.cs3733d18.teamQ.manageDB.DatabaseSystem.*;

public class User {
    private int levelAccess;
    private Stage primaryStage;
    private String nodeLocation;
    private Scene welcomeScene;

    private int time = 300;
    private int currentTimeIndex = 2;

    private int gifIndex = 0;


    // Arrays to hold pre-loaded images
    private ArrayList<Image> maps3D = new ArrayList<Image>();
    private ArrayList<Image> maps2D = new ArrayList<Image>();

    private ArrayList<Node> nodes = new ArrayList<Node>();
    private ArrayList<Edge> edges = new ArrayList<Edge>();
    private ArrayList<Employee> employees = new ArrayList<Employee>();

    private ArrayList<Node> nodesToEdit = new ArrayList<Node>();
    private ArrayList<Node> nodesToRemove = new ArrayList<Node>();
    private ArrayList<Node> nodesToAdd = new ArrayList<Node>();

    private ArrayList<Edge> edgesToEdit = new ArrayList<Edge>();
    private ArrayList<Edge> edgesToRemove = new ArrayList<Edge>();
    private ArrayList<Edge> edgesToAdd = new ArrayList<Edge>();

    private ArrayList<Employee> employeesToEdit = new ArrayList<Employee>();
    private ArrayList<Employee> employeesToRemove = new ArrayList<Employee>();
    private ArrayList<Employee> employeesToAdd = new ArrayList<Employee>();

    public Employee getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Employee currentUser) {
        this.currentUser = currentUser;
    }

    private Employee currentUser = null;

    private String pathType;

    //For utility functions
    ScreenUtil uUtil = new ScreenUtil();

    // Algorithm choice- initialize to 0 for A*
    private int selectedAlg = 0;

    //The idle time duration
    Timeline timeline;
    final double IDLEDURATION = 5000; //60 seconds

    //Sets the sleeper thread for timeout
//    private Runnable r = new Runnable() {
//        public void run() {
//            long mTime = System.currentTimeMillis();
//            long end = mTime + 20000; // 20 seconds
//
//            while (mTime < end)
//            {
//                mTime = System.currentTimeMillis();
//            }
//            System.out.println("======TIME EXPIRED======");
//            //goToWelcome();
//
//        }
//    };

    public void goToWelcome(){
        try{
            FXMLLoader welcomeLoader;
            if(runningFromIntelliJ()) {
                welcomeLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/WelcomeScreen.fxml"));
            } else{
                welcomeLoader = new FXMLLoader(getClass().getResource("fxmlFiles/WelcomeScreen.fxml"));
            }
            Parent welcomePane = welcomeLoader.load();
            Scene welcomeScene = uUtil.prodAndBindScene(welcomePane, primaryStage);
            primaryStage.setScene(welcomeScene);

            user.setLevelAccess(0);
        }
        catch(IOException io){
            System.out.println("errWelS");
            io.printStackTrace();
        }
    }



    //sets the idle timer for returning to the welcome screen
//    public void initTimer(AnchorPane pane){
//        try {
//            timeline.stop();
//        }
//        catch (NullPointerException ex){
//            System.out.println("timeline not stoppin");
//        }
//        timeline = new Timeline(new KeyFrame(
//                Duration.millis(IDLEDURATION),
//                ae -> goToWelcome()));
//
//        pane.addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                timeline.playFromStart();
//            }
//        });
//        timeline.playFromStart();
//
//
//
////        timeline = new Timeline(new KeyFrame(
////                Duration.millis(IDLEDURATION),
////                ae -> goToWelcome()));
////        pane.setOnMouseMoved(e->{
////            System.out.println("playing from start");
////            timeline.playFromStart();
////        });
////        timeline.play();
//    }




    public void saveToDB() {
        System.out.println("Saving to DB");
        if(nodesToEdit.size()>0) {
            System.out.println("Editing nodes");
        }
        for(Node n:nodesToEdit){
            editNode(n);
            System.out.println(".");
        }
        nodesToEdit = new ArrayList<Node>();
        if(nodesToRemove.size()>0) {
            System.out.println("Removing nodes");
        }
        for(Node n:nodesToRemove){
            removeNode(n);
            System.out.println(".");
        }
        nodesToRemove = new ArrayList<Node>();
        if(nodesToAdd.size()>0) {
            System.out.println("Adding nodes");
        }
        for(Node n:nodesToAdd){
            addNode(n);
            System.out.println(".");
        }
        nodesToAdd = new ArrayList<Node>();
        if(edgesToEdit.size()>0){
            System.out.println("Editing edges");
        }
        for (Edge e:edgesToEdit) {
            editEdge(e);
            System.out.println(".");
        }
        edgesToEdit = new ArrayList<Edge>();
        if(edgesToRemove.size()>0) {
            System.out.println("Removing edges");
        }
        for (Edge e:edgesToRemove) {
            removeEdge(e);
            System.out.println(".");
        }
        edgesToRemove = new ArrayList<Edge>();
        if(edgesToAdd.size()>0) {
            System.out.println("Adding edges");
        }
        for (Edge e:edgesToAdd) {
            addEdge(e);
            System.out.println(".");
        }
        edgesToAdd = new ArrayList<Edge>();
        if(employeesToEdit.size()>0) {
            System.out.println("Editing employees");
        }
        for(Employee em:employeesToEdit){
            editEmployee(em);
            System.out.println(".");
        }
        employeesToEdit = new ArrayList<Employee>();
        if(employeesToRemove.size()>0) {
            System.out.println("Removing employees");
        }
        for(Employee em:employeesToRemove){
            removeEmployee(em);
            System.out.println(".");
        }
        employeesToRemove = new ArrayList<Employee>();
        if(employeesToAdd.size()>0) {
            System.out.println("Adding employees");
        }
        for(Employee em:employeesToAdd){
            addEmployee(em);
            System.out.println(".");
        }
        employeesToAdd = new ArrayList<Employee>();
        System.out.println("Saved");
    }

    private static User user = new User();

    //constructor
    private User(){
        this.setLevelAccess(0);
        this.setNodeLocation("GELEV00N02");
    };

    public ArrayList<Edge> getNodeEdges(Node node1) {
        ArrayList<Edge> nodeEdges = new ArrayList<Edge>();
        for (Edge e: edges) {
            if((!nodeEdges.contains(e))&&
                    e.getStartNode().getNodeID().equals(node1.getNodeID())||
                    e.getEndNode().getNodeID().equals(node1.getNodeID())){
                nodeEdges.add(e);
            }
        }
        return nodeEdges;
    }

    public Node getNode(String nodeID1) {
        for (Node n:nodes) {
            if(n.getNodeID().equals(nodeID1)){
                return n;
            }
        }
        return null;
    }

    public void addEdgeSingleton(Edge edge) {
        edges.add(edge);
        edgesToAdd.add(edge);
    }

    public void addNodeSingleton(Node holder) {
        nodes.add(holder);
        nodesToAdd.add(holder);
        System.out.println("Node added to singleton");
    }

    public void addEmployeeSingleton(Employee employee){
        employees.add(employee);
        employeesToAdd.add(employee);
    }

    public double getMaxHits() {
        double maxHits = 0;
        for (Node n: nodes){
            if(n.getHits()>maxHits){
                maxHits = n.getHits();
            }
        }
        return maxHits;
    }

    public ArrayList<Edge> getFloorEdges(int floor) {
        ArrayList<Edge> floorEdges = new ArrayList<Edge>();
        for (Edge e:edges) {
            if(!floorEdges.contains(e)&&e.getStartNode().getFloor()==floor&&e.getEndNode().getFloor()==floor){
                floorEdges.add(e);
            }
        }
        return floorEdges;
    }

    public String getPathType() {
        return pathType;
    }

    public void setPathType(String pathType) {
        this.pathType = pathType;
    }

    private static class UserHolder{
        private static final User INSTANCE = new User();
    }

    public static User getUser(){
        return UserHolder.INSTANCE;
    }


    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public String getNodeLocation() {
        return nodeLocation;
    }

    public void setNodeLocation(String nodeLocation) {
        this.nodeLocation = nodeLocation;
    }

    /**
     * returns the level access of the user
     * @return level
     */
    public int getLevelAccess() {
        return levelAccess;
    }

    /**
     * sets the level access of the user
     * @param levelAccess
     */
    public void setLevelAccess(int levelAccess) {
        this.levelAccess = levelAccess;
    }


    //helper methods-----------------------------------------------

    /**
     * sets the users level access to 0
     */
    public void resetUserLevel(){
        this.setLevelAccess(0);
    }


    // loads all of the images into their respective arrayLists
    public void initLoadMaps(){

        System.out.println("Loading Map Images into Array");
        if(runningFromIntelliJ()) {
            // adds subbasement 2 maps
            maps3D.add(new Image("/Maps/L2-ICONS.png"));
            maps2D.add(new Image("/Maps/00_thelowerlevel2.png"));

            // adds subbasement 1 maps
            maps3D.add(new Image("/Maps/L1-ICONS.png"));
            maps2D.add(new Image("/Maps/00_thelowerlevel1.png"));

            // adds level 1 maps
            maps3D.add(new Image("/Maps/1-ICONS.png"));
            maps2D.add(new Image("/Maps/01_thefirstfloor.png"));

            // adds level 2 maps
            maps3D.add(new Image("/Maps/2-ICONS.png"));
            maps2D.add(new Image("/Maps/02_thesecondfloor.png"));

            // adds level 3 maps
            maps3D.add(new Image("/Maps/3-ICONS.png"));
            maps2D.add(new Image("/Maps/03_thethirdfloor.png"));
        }
        else{
            // adds subbasement 2 maps
            maps3D.add(new Image("Maps/L2-ICONS.png"));
            maps2D.add(new Image("Maps/00_thelowerlevel2.png"));

            // adds subbasement 1 maps
            maps3D.add(new Image("Maps/L1-ICONS.png"));
            maps2D.add(new Image("Maps/00_thelowerlevel1.png"));

            // adds level 1 maps
            maps3D.add(new Image("Maps/1-ICONS.png"));
            maps2D.add(new Image("Maps/01_thefirstfloor.png"));

            // adds level 2 maps
            maps3D.add(new Image("Maps/2-ICONS.png"));
            maps2D.add(new Image("Maps/02_thesecondfloor.png"));

            // adds level 3 maps
            maps3D.add(new Image("Maps/3-ICONS.png"));
            maps2D.add(new Image("Maps/03_thethirdfloor.png"));
        }
        System.out.println("Maps loaded");
    }

    public ArrayList<Image> getMaps3D() {
        return maps3D;
    }

    public ArrayList<Image> getMaps2D() {
        return maps2D;
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public void setNodes(ArrayList<Node> nodes) {
        this.nodes = nodes;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public void setEdges(ArrayList<Edge> edges) {
        this.edges = edges;
    }

    public ArrayList<Employee> getEmployees(){
        return employees;
    }

    public void setEmployees(ArrayList<Employee> employees){
        this.employees = employees;
    }

    public ArrayList<Node> getNodesToEdit() {
        return nodesToEdit;
    }

    public void editNodeSingleton(Node nodeToEdit) {
        for (int i = 0; i < nodes.size(); i++) {
            Node n = nodes.get(i);
            if (n.getNodeID().equals(nodeToEdit.getNodeID())) {
                nodes.set(i, nodeToEdit);
                //n = nodeToEdit;
                //System.out.println("editing singleton node");
            }
        }
        for (int i = 0; i < nodesToEdit.size(); i++) {
            Node n = nodesToEdit.get(i);
            if (n.getNodeID().equals(nodeToEdit.getNodeID())) {
                nodesToEdit.set(i,nodeToEdit);
                //n = nodeToEdit;
                //System.out.println("modify");
                return;
            }
        }
        //System.out.println("add");
        nodesToEdit.add(nodeToEdit);
    }

    public void editEdgeSingleton(Edge toBeToggled) {
        for (int i = 0; i < edges.size(); i++) {
            Edge e = edges.get(i);
            if (e.getEdgeID().equals(toBeToggled.getEdgeID())) {
                edges.set(i, toBeToggled);
                //n = nodeToEdit;
                //System.out.println("editing singleton node");
            }
        }
        for (int i = 0; i < edgesToEdit.size(); i++) {
            Edge e = edgesToEdit.get(i);
            if (e.getEdgeID().equals(toBeToggled.getEdgeID())) {
                edgesToEdit.set(i,toBeToggled);
                //n = nodeToEdit;
                //System.out.println("modify");
                return;
            }
        }
        //System.out.println("add");
        edgesToEdit.add(toBeToggled);
    }

    public void editEmployeeSingleton(Employee employeeToEdit){
        for (int i = 0; i < employees.size(); i++) {
            Employee em = employees.get(i);
            if (em.getUsername().equals(employeeToEdit.getUsername())) {
                employees.set(i, employeeToEdit);
                System.out.println("editing singleton employee");
            }
        }
        for (int i = 0; i < employeesToEdit.size(); i++) {
            Employee em = employeesToEdit.get(i);
            if (em.getUsername().equals(employeeToEdit.getUsername())) {
                employeesToEdit.set(i,employeeToEdit);
                System.out.println("modify employee");
                return;
            }
        }
        System.out.println("add employee to edit list");
        employeesToEdit.add(employeeToEdit);
    }

    public ArrayList<Node> getFloorNodes(int floor){
        ArrayList<Node> floorNodes = new ArrayList<Node>();
        for (Node n:nodes) {
            if(n.getFloor()==floor){
                floorNodes.add(n);
            }
        }
        return floorNodes;
    }

    public void removeEdgeSingleton(Edge e) {
        edges.remove(e);
        edgesToRemove.add(e);
    }

    public void removeNodeSingleton(Node tbr) {
        nodes.remove(tbr);
        nodesToRemove.add(tbr);
    }

    public void removeEmployeeSingleton(Employee etr){
        employees.remove(etr);
        employeesToRemove.add(etr);
    }

    public int getSelectedAlg() {
        return selectedAlg;
    }

    public void setSelectedAlg(int selectedAlg) {
        this.selectedAlg = selectedAlg;
    }


    //for memento usages
    public void setWelcomeScene(Scene welcomeScene) {
        this.welcomeScene = welcomeScene;
    }
    public Scene getWelcomeScene(){
        return welcomeScene;
    }

    public void setTime(String timeText){
        if(timeText.equals("0")){
            currentTimeIndex = 0;
            this.time = 30;
        }
        else if(timeText.equals("1")){
            currentTimeIndex = 1;
            this.time = 60;
        }
        else if(timeText.equals("2")){
            currentTimeIndex = 2;
            this.time = 300;
        }
        else if(timeText.equals("3")){
            currentTimeIndex = 3;
            this.time = 3000;
        }
        else{
            currentTimeIndex = 4;
            this.time = 18000;
        }
    }

    public int getTime(){
        return time;
    }

    public int getCurrentTimeIndex() {
        return currentTimeIndex;
    }

    public void setCurrentTimeIndex(int currentTimeIndex) {
        this.currentTimeIndex = currentTimeIndex;
    }


    //determines which gifts are going to be played


    public int getGifIndex() {
        return gifIndex;
    }

    public void setGifIndex(int gifIndex) {
        this.gifIndex = gifIndex;
    }
}
