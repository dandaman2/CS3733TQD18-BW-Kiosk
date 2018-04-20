package edu.wpi.cs3733d18.teamQ.ui.Controller;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import edu.wpi.cs3733d18.teamQ.pathfinding.*;
import edu.wpi.cs3733d18.teamQ.ui.ArrowShapes.ProgressArrows;
import edu.wpi.cs3733d18.teamQ.ui.*;
import edu.wpi.cs3733d18.teamQ.ui.ProxyMaps.FloorMaps;
import edu.wpi.cs3733d18.teamQ.ui.ProxyMaps.IMap;
import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.textfield.TextFields;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static edu.wpi.cs3733d18.teamQ.manageDB.DatabaseSystem.getNodes;
import static edu.wpi.cs3733d18.teamQ.manageDB.DatabaseSystem.runningFromIntelliJ;
import static edu.wpi.cs3733d18.teamQ.ui.PathInstructions.captureAndSaveDisplay;
import static java.lang.Math.acos;
import static java.lang.Math.sqrt;
import com.sun.javafx.css.Stylesheet;

public class PathfindingCont extends JPanel implements Initializable, IZoomableCont, DocumentListener {

    //Path Inputs
    @FXML
    private TextField startingNodeField;



    @FXML
    private TextField endingNodeField;

    @FXML
    private Button exchange;

    //Shortcuts for quickpaths
    @FXML
    private Button quickBath;

    @FXML
    private Button quickExit;

    @FXML
    private Button quickEat;

    @FXML
    private Button quickInfo;

    @FXML
    private Button quickElev;

    @FXML
    private Button quickStair;

    //Map options
    @FXML
    private ToggleButton dToggle;

    //Floors
    @FXML
    private JFXButton floorL21;

    @FXML
    private JFXButton floorL11;

    @FXML
    private JFXButton floor11;

    @FXML
    private JFXButton floor21;

    @FXML
    private JFXButton floor31;

    //AnchorPane of whole scene
    @FXML
    private AnchorPane screenBinding;

    @FXML
    JFXButton homeButton;

    //Map Image
    @FXML
    private VBox vbox;

    @FXML
    AnchorPane backImagePane;

    @FXML
    ImageView backImage;

    //Email
    @FXML
    private JFXDrawer emailDrawer;

    @FXML
    private Button emailVar;

    //Animation
    @FXML
    private HBox hboxProgress;
    @FXML
    HBox topBar;

    @FXML
    private Button playButton;

//    private NavigationBreadCrumb breadCrumb;


    //animation variables
    public ArrayList<TransitionData> transitions = new ArrayList<>();
    private ScreenUtil sdUtil = new ScreenUtil();
    ArrayList<Line> drawnPath = new ArrayList<>();
    ImageView selectedLocation;
    Node curSelected;
    Node youHere;
    Node startNode;
    Boolean isSelected = false;
    ArrayList<Button> transList = new ArrayList<Button>();
    ArrayList<Label> labelList = new ArrayList<Label>();
    Label starLabel = new Label();
    ScreenUtil pUtil = new ScreenUtil();
    User user = User.getUser();

    private Boolean isPathDisplayed = false;
    private ImageView manImage;



    //global array of nodes
    public ArrayList<Node> nodes;
    public ArrayList<Node> queuedPath = new ArrayList<Node>();
    public ArrayList<ArrayList<Node>> pathChunks = new ArrayList<ArrayList<Node>>(5);


    //create instance of FloorMaps
    private IMap floorMaps;
    private int startingFloor = 2;

    //Constant for pixel to distance
    private final int ROUGH_PIXELS_PER_FOOT = 4;

    // Drawing nodes on map
    private MapNoder noder;
    private ArrayList<String> excludedTypesFromNodes = new ArrayList<String>();


    //Strings used to see if change was made
    String sTempStart = "";
    String sTempEnd = "";


    //the zoompane for the pathfinding
    ZoomPane zoom;

    //timeline for idle
    Timeline timeline = null;

    //arraylist of file path names
    ArrayList<SnapData> allFiles = new ArrayList<>();
    private int imageNumber=1;

    /**
     * Initializes the things necessary to create moveable map with pathfinding features
     * @param url
     * @param rb
     */
    public void initialize(URL url, ResourceBundle rb) {
        nodes = getNodes();

        //list of nodes from running algorithm
        queuedPath = new ArrayList<>();

        //get array of all nodes
        //make an array of string that are gotten from the nodes
        //once user selects string, go through array of nodes to find the appropriate node
        initAutoComp(nodes);
        youHere = user.getNode("GELEV00N02");
        startNode = youHere;

        initializeButtons();
        initZoom();
        initEmailDrawer();
        initStar();
        initializeTopBar();


        floorMaps = new FloorMaps(user.getMaps3D(), user.getMaps2D(), backImage, startingFloor, false);
        noder = new MapNoder(backImagePane, backImage, floorMaps.getIs2D(), endingNodeField);
        excludedTypesFromNodes.add("HALL");
        excludedTypesFromNodes.add("ELEV");
        excludedTypesFromNodes.add("STAI");

        initMap();
        backImagePane.getChildren().addAll(drawnPath);
        noder.displayNodes(floorMaps.getCurrFloor(), excludedTypesFromNodes, floorMaps.getIs2D());
        initYouAreHere();
        //new TimeoutData().initTimer(screenBinding);
        //initTimer();
    }


    /******************************************************
     *
     * Initializers
     *
     ******************************************************/


    private void initializeTopBar(){
        topBar.setBackground(new Background(new BackgroundFill(Paint.valueOf("#012D5A"), new CornerRadii(0), null)));
    }

    private void initializeButtons(){
        //homeButton.setBackground(new Background(new BackgroundFill(Paint.valueOf("#ECECEC"), new CornerRadii(0), null)));
        //homeButton.setStyle("-fx-text-fill: #FFFFFF;");
        //homeButton.setRipplerFill(Paint.valueOf("#FFFFFF"));

        Image info;
        if(runningFromIntelliJ()) {
            info = new Image("../resources/ButtonImages/whiteHut.png");
        } else{
            info = new Image("ButtonImages/whiteHut.png");
        }
        ImageView infoView = new ImageView(info);
        infoView.setFitWidth(42);
        infoView.setFitHeight(40);
        homeButton.setGraphic(infoView);


        floorL21.setBackground(new Background(new BackgroundFill(Paint.valueOf("#012D5A"), new CornerRadii(0), null)));
        floorL21.setStyle("-fx-text-fill: #FFFFFF;");
        floorL21.setRipplerFill(Paint.valueOf("#FFFFFF"));

        floorL11.setBackground(new Background(new BackgroundFill(Paint.valueOf("#012D5A"), new CornerRadii(0), null)));
        floorL11.setStyle("-fx-text-fill: #FFFFFF;");
        floorL11.setRipplerFill(Paint.valueOf("#FFFFFF"));

        floor11.setBackground(new Background(new BackgroundFill(Paint.valueOf("#012D5A"), new CornerRadii(0), null)));
        floor11.setStyle("-fx-text-fill: #FFFFFF;");
        floor11.setRipplerFill(Paint.valueOf("#FFFFFF"));

        floor21.setBackground(new Background(new BackgroundFill(Paint.valueOf("#012D5A"), new CornerRadii(0), null)));
        floor21.setStyle("-fx-text-fill: #FFFFFF;");
        floor21.setRipplerFill(Paint.valueOf("#FFFFFF"));

        floor31.setBackground(new Background(new BackgroundFill(Paint.valueOf("#012D5A"), new CornerRadii(0), null)));
        floor31.setStyle("-fx-text-fill: #FFFFFF;");
        floor31.setRipplerFill(Paint.valueOf("#FFFFFF"));
    }

    /**
     * Initializes the autocomplete functionality of the search bar
     * @param nodeList
     */
    private void initAutoComp(ArrayList<Node> nodeList) {
        ArrayList<String> nodeIdentification = pUtil.getNameIdNode(nodeList);
        TextFields.bindAutoCompletion(startingNodeField, nodeIdentification);
        TextFields.bindAutoCompletion(endingNodeField, nodeIdentification);
        endingNodeField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                updatePath();
            }
        });
        startingNodeField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals("")){
                    startingNodeField.setText(youHere.getNameLong()+ ","+youHere.getNodeID());
                    updatePath();
                }else {
                    updatePath();
                }
            }
        });

    }

    /**
     * Initializes the zoom and scroll features on the map
     */
    public void initZoom() {
        zoom = new ZoomPane(this);
        Parent zoomPane = zoom.createZoomPane(backImage);
        vbox.getChildren().setAll(zoomPane);
        VBox.setVgrow(zoomPane, Priority.ALWAYS);

        backImage.fitWidthProperty().bind(backImagePane.widthProperty());
        backImage.fitHeightProperty().bind(backImagePane.heightProperty());
    }


    /**
     * initializes the email drawer
     */
    public void initEmailDrawer() {
        try {

            FXMLLoader emailLoader;
            if(runningFromIntelliJ()) {
                emailLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/EmailDrawer.fxml"));
            } else{
                emailLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/EmailDrawer.fxml"));
            }
            HBox toolbar = emailLoader.load();
            EmailDrawerController emCont = emailLoader.getController();
            emCont.setCont(this);
            emailDrawer.setSidePane(toolbar);
        }
        catch (IOException ex) {
            System.out.println("errEmailDrawer");
            ex.printStackTrace();
        }
        emailDrawer.setVisible(false);
        emailDrawer.setDisable(true);

    }

    /**
     * Initializes the map
     */
    public void initMap(){
        floorMaps.updateFloorMap(startingFloor);
        updateDrawings();
    }


    /**
     * Initializes the You Are Here function and prints the man on the map
     */
    public void initYouAreHere(){
        Image man;
        if (runningFromIntelliJ()) {
            man = new Image("/ButtonImages/YouRHereMan.png");
        } else {
            man = new Image("/ButtonImages/YouRHereMan.png");
        }
        manImage = new ImageView(man);
        //System.out.println("Man image");
        drawMan(youHere);
    }


    /**
     * Initializes star icon at finishing node(Destination)
     */
    public void initStar(){
        Image star;
        if (runningFromIntelliJ()) {
            star = new Image("/ButtonImages/star.png");
        }
        else{
            star = new Image("ButtonImages/star.png");
        }
        selectedLocation = new ImageView(star);
        selectedLocation.setFitHeight(30);
        selectedLocation.setFitWidth(30);
    }


    /******************************************************
     *
     * Pathfinding
     *
     ******************************************************/


    //TODO: Call the function when change in either of the Text Fields


// Sends the updated path info to the Pathfinding Controller
    public void updatePath() {
        System.out.println("Running");

        if (startingNodeField.getText().isEmpty()){
            startingNodeField.setText(youHere.getNameLong()+ ","+youHere.getNodeID());
        }

        if ((!sTempStart.equals(startingNodeField.getText())) || (!sTempEnd.equals(endingNodeField.getText()))){
            sTempStart = startingNodeField.getText();
            sTempEnd = endingNodeField.getText();
            if (!(startingNodeField.getText().isEmpty()) && !(endingNodeField.getText().isEmpty())) {
                getNodeFromTextFields(startingNodeField, endingNodeField);
                generatePath();
            }else {
                isPathDisplayed = false;
                drawPath(null);
            }
        }else {
            System.out.println("Content has not changed");
        }
        updateDrawings();


    }


    /**
     * Creates and displays a path from the current position to the chosen end place
     * Makes the lower menu visible/invisible by toggling optionsHome
     */
    public void generatePath() {
        backImagePane.getChildren().removeAll(drawnPath);
        ArrayList<String> RestrictedTYPES = new ArrayList<String>();
//        if(checkElevator.isSelected() == true)
//            RestrictedTYPES.add("ELEV");
//        if(checkStairs.isSelected() == true)
//            RestrictedTYPES.add("STAI");

        ArrayList<Node> floor2Nodes = new ArrayList<Node>();
        floor2Nodes = user.getFloorNodes(2);
        // adds nodes to graph


        Graph g = null; // to be chosen Algorithm in Switch statement

        switch(user.getSelectedAlg()) {
            case 0 :
                g = new Astar();break;
            case 1 :
                g = new BFS();break;
            case 2 :
                g = new DFS();break;
            case 3 :
                g = new Dijkstra();break;
            case 4 :
                g = new Best_First_Search();break;
        }
        g.init2(floor2Nodes, user.getEdges());

        queuedPath = g.findPath(startNode, curSelected,RestrictedTYPES);

        for (Node n:queuedPath) {
            n.incrementHit();
            user.editNodeSingleton(n);
        }


        drawPath(queuedPath);
        isPathDisplayed = true;
    }


    /**
     * Function to find the nearest location of the parsed type
     * @param Type Type of location to find
     */
    public void getNearType(String Type) {
        ArrayList<String> RestrictedTYPES = new ArrayList<String>(); // empty

        Graph g = new ByTypeSearch();
        ArrayList<Node> floor2Nodes = new ArrayList<Node>();
        Node n;
        // isolates the nodes on the second floor and adds them to floor2Nodes array
        for(int i = 0; i < user.getNodes().size(); i++){
            n = user.getNodes().get(i);

            if(n.getFloor() == 2){
                floor2Nodes.add(n);
            }
        }
        g.init2(floor2Nodes,user.getEdges());

        youHere = user.getNode("GELEV00N02");
        queuedPath = g.findShortestPathByType(youHere,Type,RestrictedTYPES);
        drawPath(queuedPath);
        isPathDisplayed = true;
    }





/**
 *  Failure checks before proceding
 *  -----------------------------------------------------------------
  */

    /**
     * Returns node if node with inputted ID exists
     * @param nodeID
     * @return
     */
    public Node selectedNode(String nodeID) {
        int i;

        for (i=0; i<nodes.size(); i++){
            if (nodes.get(i).getNodeID().equals(nodeID)){
               // System.out.println(nodes.get(i).getNameLong() + " " +nodes.get(i).getNodeID());
                return nodes.get(i);
            }
        }
        //if not in node
        Node badNode = null;
        return badNode;
    }



    /******************************************************
     *
     * Drawing on Map
     *
     ******************************************************/


    /**
     * Returns the distance of the path
     * @param path
     * @return
     */
    public double pathLength(ArrayList<Node> path){
        double length =0.0;
        for(int i =0; i < path.size()-1; i++){
            length += Math.sqrt((Math.pow((path.get(i).getxPos()-path.get(i+1).getxPos()),2)) +
                    (Math.pow((path.get(i).getyPos()-path.get(i+1).getyPos()),2)));
        }
        return  length;
    }

    /**
     * Creates a list of bathrooms from the array list passed in
     * @param roomNodes is an array list of nodes
     * @return an array list of nodes for bathrooms
     */
    public ArrayList<Node> closeBathroom(ArrayList<Node> roomNodes) {
        ArrayList<Node> bathNodes = new ArrayList<Node>();
        for(int i = 0; i < roomNodes.size(); i++){
            Node room = roomNodes.get(i);
            if(room.getType().equals("REST")){
                bathNodes.add(room);
            }
        }
        return bathNodes;
    }

    /**
     * Creates a list of exists from the array list passed in
     * @param roomNodes is an array list of nodes
     * @return an array list of nodes for exits
     */
    public ArrayList<Node> closeExit(ArrayList<Node> roomNodes) {
        ArrayList<Node> exitNodes = new ArrayList<Node>();
        for(int i = 0; i < roomNodes.size(); i++){
            Node room = roomNodes.get(i);
            if(room.getType().equals("STAI") || room.getType().equals("ELEV")){
                exitNodes.add(room);
            }
        }
        return exitNodes;
    }

    /**
     * Creates a list of food places from the array list passed in
     * @param roomNodes is an array list of nodes
     * @return an array list of nodes for food places
     */
    public ArrayList<Node> closeFood(ArrayList<Node> roomNodes) {
        ArrayList<Node> foodNodes = new ArrayList<Node>();
        for(int i = 0; i < roomNodes.size(); i++){
            Node room = roomNodes.get(i);
            if(room.getType().equals("FOOD")){
                foodNodes.add(room);
            }
        }
        return foodNodes;
    }




    /**
     * Opens the Email drawer
     */
    public void openEmailDrawer(){
        if(emailDrawer.isHidden()){
            emailDrawer.open();
            emailDrawer.setMinWidth(100);
            emailDrawer.setMinHeight(100);
            emailDrawer.setDisable(false);
            emailDrawer.setVisible(true);
        } else {
            emailDrawer.close();
            emailDrawer.setMinWidth(0);
            emailDrawer.setDisable(true);
            emailDrawer.setVisible(false);
        }
    }


    ProgressArrows arrows = new ProgressArrows();


    /**
     * Displays the inputted node to the screen
     * @param n
     */
    public void showSelection(Node n) {
        if(backImagePane.getChildren().contains(selectedLocation))
            backImagePane.getChildren().remove(selectedLocation);

        if(n.getFloor() == floorMaps.getCurrFloor()) {
            TransPoint tp1;
            tp1 = translateCoord(n);
            selectedLocation.setLayoutX(tp1.getTx() - 15);
            selectedLocation.setLayoutY(tp1.getTy() - 15);
            backImagePane.getChildren().add(selectedLocation);
        }
    }

    /**
     * Removes node circle from screen
     */
    public void removeSelection(){
        backImagePane.getChildren().remove(selectedLocation);
    }


    /**
     * Takes an array list of nodes and draws it on the map
     * @param path
     */
    public void drawPath(ArrayList<Node> path) {

        TransPoint tp;
        TransPoint finalPoint = null;  // tp to hold translated x and y coords,
                                    // finalPoint to hold the final point for animation polyline drawing
        ArrayList<TransPoint> pta = new ArrayList<>();  // holds coords for nodes on path

        // removes old paths and buttons before drawing new
        backImagePane.getChildren().removeAll(drawnPath);
        backImagePane.getChildren().removeAll(transList);
        backImagePane.getChildren().removeAll(labelList);
        backImagePane.getChildren().remove(starLabel);

        hboxProgress.getChildren().removeAll(arrows.getStackList());


        // clears list
        transList.clear();
        labelList.clear();
        starLabel = new Label();
        arrows = new ProgressArrows(path, floorMaps, hboxProgress, this, arrows.getCurrSelected());

        // Arraylist to hold lines to be drawn
        drawnPath = new ArrayList<Line>();

        // translates all nodes to new coords for when map moves
        for(Node nodeToTranslate : path){
            tp = translateCoord(nodeToTranslate);
            pta.add(tp);
        }

        // add label to start location
        if(floorMaps.getCurrFloor() == path.get(0).getFloor()) {
            starLabel.setText("    Start: " + path.get(0).getNameLong() + " ");
            starLabel.setPrefHeight(20);
            starLabel.setLayoutX(pta.get(0).getTx());
            starLabel.setLayoutY(pta.get(0).getTy() - 10);
            starLabel.setTextFill(Color.WHITE);
            starLabel.setStyle("-fx-background-color: #3a3b3b;");
        }

        // add label to end location
        int lastIndex = pta.size() - 1;
        if(floorMaps.getCurrFloor() == path.get(lastIndex).getFloor()) {
           // System.out.println(pta.get(lastIndex) + " " + path.get(lastIndex) + " " + path.get(lastIndex).getNameLong());
            starLabel.setText("    End: " + path.get(lastIndex).getNameLong() + " ");
            starLabel.setPrefHeight(20);
            starLabel.setLayoutX(pta.get(lastIndex).getTx());
            starLabel.setLayoutY(pta.get(lastIndex).getTy() - 10);
            starLabel.setTextFill(Color.BLACK);
            starLabel.setStyle("-fx-background-color: #c97b24;");
        }

        //resetting transitions
        transitions = new ArrayList<>();
        Polyline lineData = new Polyline();
        ArrayList<Double> lineDataPoints = new ArrayList<>();

        // goes through to determine which lines to show, in regards to the current floor
        for(int i=0; i < pta.size()-1; i++) {
            TransPoint startpoint = pta.get(i);
            TransPoint endpoint = pta.get(i + 1);

            // if both points are on the current floor, draws line
            if ((startpoint.getFloor() == floorMaps.getCurrFloor()) && (endpoint.getFloor() == floorMaps.getCurrFloor())) {
                Line l = new Line(startpoint.getTx(), startpoint.getTy(), endpoint.getTx(), endpoint.getTy());
                l.setStroke(Color.PURPLE);
                l.setStrokeWidth(6);
                drawnPath.add(l);
            }
            /**This "function" runs when the path needs a button to show a tranistion to a different floor
             *  it adds a button with an arrow in the corresponding direction, as well as a
             *  label at the node
             *
             */
            else if(startpoint.getFloor() != endpoint.getFloor()){
                //initializes button, color, and position ------------------
                Button transButt = new Button();
                Image arrow;
                ImageView arrowView;
                if(runningFromIntelliJ()) {
                    arrow = new Image("ButtonImages/upArrow.png");
                } else {
                    arrow = new Image("ButtonImages/upArrow.png");
                }
                arrowView = new ImageView(arrow);

                int size = 22;
                arrowView.setFitWidth(size);
                arrowView.setFitHeight(size);

                double buttX = startpoint.getTx();
                double buttY = startpoint.getTy();

                transButt.setLayoutX(buttX - (size / 2));
                transButt.setLayoutY(buttY - (size / 2));
//                transButt.setOpacity(.8);
                transButt.setMaxSize(size + 6,size + 6);
                transButt.setMinSize(size + 6,size + 6);

                // ---------------------------------------------------------------

                // converts floor values from db to index so it's easier to work with
                int endfloor = endpoint.getFloor();
                int startfloor = startpoint.getFloor();
                int thisfloor = floorMaps.getCurrFloor();


                // chooses floor to transition too, based on whichever point is not on the current floor
                int goToFloor;
                if(thisfloor == endfloor)
                    goToFloor = startfloor;
                else
                    goToFloor = endfloor;

                // for printing which floor to go to
                String goToString = String.valueOf(floorMaps.indexFloorToDB(goToFloor));
                if(goToString.equals("-2"))
                    goToString = "L2";
                if(goToString.equals("-1"))
                    goToString = "L1";

                // creates label to appear next to button
                Label transLabel = new Label( "    " + path.get(i).getNameShort() + " to floor " + goToString +  " ");
                transLabel.setPrefHeight(size);
                transLabel.setLayoutX(buttX);
                transLabel.setLayoutY(buttY - size/2 + 3);
                transLabel.setTextFill(Color.WHITE);

                // only add button if it is needed
                boolean needButton = false;

                if((endfloor > thisfloor  || startfloor > thisfloor) && (endfloor == thisfloor || startfloor == thisfloor)){
                    transButt.setStyle("-fx-base: #0d952a;" + "-fx-background-radius: 100%;" + "-fx-focus-color: transparent;");
                    transLabel.setStyle("-fx-background-color: #0d952a;");
                    needButton = true;
                }
                else if((endfloor < thisfloor || startfloor < thisfloor) && (endfloor == thisfloor || startfloor == thisfloor)){
                    transButt.setStyle("-fx-base: #ea2f5c;" + "-fx-background-radius: 100%;" + "-fx-focus-color: transparent;");
                    transLabel.setStyle("-fx-background-color: #ea2f5c;");
                    arrowView.setRotate(180);
                    needButton = true;
                }
                // action event to transition floor
                transButt.setOnAction((e)-> {
                    floorMaps.updateFloorMap(goToFloor);
                    drawPath(queuedPath);
                    updateDrawings();

                } );

                transButt.setGraphic(arrowView);

                if(needButton) {
                    transList.add(transButt);
                    labelList.add(transLabel);
                }
            }
            if(startpoint.getFloor() == endpoint.getFloor()) {
                //initializes button, color, and position ------------------
                lineDataPoints.add(startpoint.getTx());
                lineDataPoints.add(startpoint.getTy());
            }else{
                //for path animation
                int lineFloor = startpoint.getFloor();
//                System.out.println("new floor found: "+lineFloor);
                lineDataPoints.add(startpoint.getTx());
                lineDataPoints.add(startpoint.getTy());

                if(lineDataPoints.size()<=2){
                    System.out.println("Size less than 2, generating in-place line");
                    lineDataPoints.add(startpoint.getTx());
                    lineDataPoints.add(startpoint.getTy());
                }

                lineData.getPoints().addAll(lineDataPoints);
                transitions.add(new TransitionData(lineFloor, lineData));
                lineDataPoints = new ArrayList<>();
                lineData = new Polyline();
            }
            finalPoint = endpoint;
        }
        //tying up the end of the transitioning path
        if(finalPoint != null){
            lineDataPoints.add(finalPoint.getTx());
            lineDataPoints.add(finalPoint.getTy());
        }

        if(lineDataPoints.size()<=2){
            lineDataPoints.add(finalPoint.getTx());
            lineDataPoints.add(finalPoint.getTy());
        }

        lineData.getPoints().addAll(lineDataPoints);
       // System.out.println("pta size:" + pta.size());
        transitions.add(new TransitionData(pta.get(pta.size()-1).getFloor(), lineData));
        lineDataPoints = new ArrayList<>();
        lineData = new Polyline();
        TextInstructions(path);

        // adds path and buttons to pane
        backImagePane.getChildren().addAll(drawnPath);
        backImagePane.getChildren().addAll(labelList);
        backImagePane.getChildren().add(starLabel);
        backImagePane.getChildren().addAll(transList);
        arrows.addArrows();


    }


    /**
     * Updates the path with manipulation of the screen
     */
    public void updateDrawings(){
        //highlightSelected(floorMaps.getCurrFloor());
        noder.removeAllNodes();
        noder.displayNodes(floorMaps.getCurrFloor(), excludedTypesFromNodes, floorMaps.getIs2D());

        if(backImagePane.getChildren().contains(manImage))
            backImagePane.getChildren().remove(manImage);

//        if(floorMaps.getCurrFloor() == youHere.getFloor()){
//            initYouAreHere();
//        }
        if(isPathDisplayed) {
            drawPath(queuedPath);
        }else {
            clearPath();
        }

        if (isSelected)
            showSelection(curSelected);

    }
//
//    public void highlightSelected(int floor){
//        switch(floor){
//            case -2:
//                floorL21.setStyle();
//        }
//    }

    /**
     * Clear path drawing
     */
    public void clearPath(){
        backImagePane.getChildren().removeAll(drawnPath);
        drawnPath = new ArrayList<>();
    }


    /**
     * Creates a You Are Here Man on the given node
     * @param n the node for the man to be placed on
     */
    public void drawMan(Node n) {
        TransPoint tp;
        tp = translateCoord(n);

        manImage.setFitHeight(30);
        manImage.setFitWidth(30);
        manImage.setX(tp.getTx() - 16);
        manImage.setY(tp.getTy() - 16);
        backImagePane.getChildren().add(manImage);
    }



    /******************************************************
     *
     * Floor-Image-Setting Manipulation
     *
     ******************************************************/

    public void toggle3D(){
        floorMaps.setIs2D(dToggle.isSelected());
        floorMaps.updateFloorMap(floorMaps.getCurrFloor());

        if(dToggle.isSelected()) {
            dToggle.setText("3D");
            dToggle.setStyle("-fx-base: #0067B1;" + "-fx-background-radius: 50%;");
        }
        else {

            dToggle.setText("2D");
            dToggle.setStyle("-fx-base: #ca2b24;" + "-fx-background-radius: 50%;");
        }
        dToggle.setTextFill(Color.WHITE);
        updateDrawings();
    }




    /******************************************************
     *
     * Screen Changes
     *
     ******************************************************/

    /**
     * Goes to the Welcome Screen when called
     * @param actionEvent Takes an action event that says it has been called
     */
    public void goToWelcomeCondition(javafx.event.ActionEvent actionEvent){

        //checks to see if a user is an admin
        if(User.getUser().getLevelAccess() > 0){
            try {
                FXMLLoader welcomeLoader;
                if (runningFromIntelliJ()) {
                    welcomeLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/AdminHomeScreen.fxml"));
                } else {
                    welcomeLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/AdminHomeScreen.fxml"));
                }
                Parent welcomePane = welcomeLoader.load();
                Stage primaryStage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
                Scene welcomeScene = sdUtil.prodAndBindScene(welcomePane, primaryStage);
                welcomeScene.getStylesheets().addAll("Stylesheet.css", "StyleMenus.css");
                primaryStage.setScene(welcomeScene);

            } catch (IOException io) {
                System.out.println("errWelS");
                io.printStackTrace();
            }
        }
        //if user isn't an admin, go to welocome screen
        else {
            try {
                FXMLLoader welcomeLoader;
                if (runningFromIntelliJ()) {
                    welcomeLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/WelcomeScreen.fxml"));
                } else {
                    welcomeLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/WelcomeScreen.fxml"));
                }
                Parent welcomePane = welcomeLoader.load();
                Stage primaryStage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
                Scene welcomeScene = sdUtil.prodAndBindScene(welcomePane, primaryStage);
                welcomeScene.getStylesheets().addAll("Stylesheet.css", "StyleMenus.css");
                primaryStage.setScene(welcomeScene);

                User user = User.getUser();
                user.setLevelAccess(0);
            } catch (IOException io) {
                System.out.println("errWelS");
                io.printStackTrace();
            }
        }
    }

    /******************************************************
     *
     * Functions
     *
     ******************************************************/

    //TODO: Change color of floor button that is selected

    /**
     * Function to exchange the text between
     * starting and ending node text fields
     */
    public void exchangeTextFields(){
        String start = startingNodeField.getText();
        String end = endingNodeField.getText();
        String temp = end;
        endingNodeField.setText(start);
        startingNodeField.setText(temp);
        temp = "";
        updatePath();
    }


    /**
     * Takes in a node and returns a transpoint object
     * @param n
     * @return
     */
    public TransPoint translateCoord(Node n){
        Parent currentParent = backImage.getParent();

        double imageWidth = backImage.getBoundsInLocal().getWidth()*backImage.getScaleX();;
        double imageHeight = backImage.getBoundsInLocal().getHeight()*backImage.getScaleY();
        double offsetX = backImage.getLayoutX() - (backImage.getScaleX() - 1) * backImage.getBoundsInLocal().getWidth()/2;
        double offsetY = backImage.getLayoutY() - (backImage.getScaleY() - 1) * backImage.getBoundsInLocal().getHeight()/2;

        while((currentParent != null) && !(currentParent instanceof AnchorPane)){
            offsetX += currentParent.getLayoutX();
            offsetY += currentParent.getLayoutY();
            currentParent = currentParent.getParent();
        }

        double newX, newY;

        if(floorMaps.getIs2D()){
            newX = n.getxPos() * imageWidth / 5000.0 + offsetX ;
            newY = n.getyPos() * imageHeight / 3400.0 + offsetY ;
        } else {
            newX = n.getxPos3D() * imageWidth / 5000.0 + offsetX;
            newY = n.getyPos3D() * imageHeight / 2774.0 + offsetY;
        }

        return new TransPoint(newX,newY,n.getFloor());
    }



    /**
     * Get type node from text in search fields
     * @param start Starting node textfield
     * @param end Ending node textfield
     */
    public void getNodeFromTextFields(TextField start, TextField end) {
            String[] valuesStart = start.getText().split(",");
            String[] valuesEnd = end.getText().split(",");
            //if text reads a node
            if (valuesStart.length == 2 && valuesEnd.length==2) {
                Node startingNode = selectedNode(valuesStart[1]);
                Node endingNode = selectedNode(valuesEnd[1]);
                if ((startingNode != null) && (endingNode !=null)) {
                    showSelection(startingNode);
                    showSelection(endingNode);
                    startNode = startingNode;
                    curSelected = endingNode;
                    isSelected=true;
                } else {
                    System.out.println("BadNode");
                }
            }
    }


    /**
     * Function to switch to floor 3
     */
    public void buttonFloor3(){
        floorMaps.updateFloorMap(3);
        updateDrawings();
    }

    /**
     * Function to switch to floor 2
     */
    public void buttonFloor2(){
        floorMaps.updateFloorMap(2);
        updateDrawings();
    }

    /**
     * Function to switch to floor 1
     */
    public void buttonFloor1(){
        floorMaps.updateFloorMap(1);
        updateDrawings();
    }

    /**
     * Function to switch to floor L1
     */
    public void buttonFloorL1(){
        floorMaps.updateFloorMap(-1);
        updateDrawings();
    }

    /**
     * Function to switch to floor L2
     */
    public void buttonFloorL2(){
        floorMaps.updateFloorMap(-2);
        updateDrawings();
    }

    /**
     * Takes a snapshot of the anchorPane and sends it by email with text instructions
     * @param email
     */
    public void sendEmailByPath(String email){
        if (!queuedPath.isEmpty()) {
            System.out.println("Files count:"+ allFiles.size());
            PathInstructions x = new PathInstructions();
            //String file = x.captureAndSaveDisplay(backImagePane, vbox.getWidth(), vbox.getHeight());
            ArrayList<String> instructions = TextInstructions(queuedPath);
            String textDirections = x.buildString(instructions);
            x.generatePathEmail(allFiles, textDirections, email);
        }else {
            System.out.println("No path found");
        }
    }



    /**
     * Sets the listener to the pathfinding scene
     * @param s
     */
    public void setListen(Stage s){
        s.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                drawPath(queuedPath);
            }
        });
        s.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                drawPath(queuedPath);
            }
        });
    }



    /******************************************************
     *
     * Text Instructions
     *
     ******************************************************/

    /**
     * returns text instructions to a given path
     * @param
     */
    public ArrayList<String> TextInstructions(ArrayList<Node> nodes) {
        ArrayList<String> Answer = new ArrayList<String>();
        //For determining directions by feet
        double pixelsTraveledX = 0.0;
        double pixelsTraveledY = 0.0;
        double feetTraveled = 0;
        String floor;
        if(nodes.size()<2){

            return Answer;
        }

        Node AA = nodes.get(0);
        Node BB = nodes.get(1);

        if(AA.getType().equals(BB.getType()) &&
                (AA.getType().equals("ELEV") || AA.getType().equals("STAI"))){
            if(BB.getFloor() > 0){
                floor = String.valueOf(BB.getFloor());
            }else if (BB.getFloor() == -1){
                floor = "L1";
            }else{
                floor = "L2";
            }
            Answer.add("Go to floor " + floor + " using " + BB.getNameLong());
        }else{
            Answer.add("Go towards " + BB.getNameLong());
        }

        for(int i=2;i<nodes.size();i++){
            Node A = nodes.get(i-2);
            Node B = nodes.get(i-1);
            Node C = nodes.get(i);

            if(B.getType().equals(C.getType()) &&
                    (B.getType().equals("ELEV") || B.getType().equals("STAI"))){
                if(C.getFloor() > 0){
                    floor = String.valueOf(C.getFloor());
                }else if (C.getFloor() == -1){
                    floor = "L1";
                }else{
                    floor = "L2";
                }
                Answer.add("Go to floor " + floor + " using " + C.getNameLong());
                continue;
            }

            Double v1x = B.getxPos() - A.getxPos();
            Double v1y = B.getyPos() - A.getyPos();
            Double v2x = C.getxPos() - B.getxPos();
            Double v2y = C.getyPos() - B.getyPos();

            double alpha = getAngle(v1x,v1y,v2x,v2y);
            double beta = getDir(v1x,v1y,v2x,v2y);
            //System.out.println(alpha + " <==============");
            String direction = AngleToText(alpha,beta);
            pixelsTraveledX += Math.abs(v1x);
            pixelsTraveledY += Math.abs(v1y);
            if(!direction.equals("Go straight")) {
                double pixelsTraveled = Math.sqrt(pixelsTraveledX * pixelsTraveledX + pixelsTraveledY * pixelsTraveledY);
                feetTraveled = (pixelsTraveled / ROUGH_PIXELS_PER_FOOT);
                if(feetTraveled > 10) {
                    //Round the distance for non single digit values
                    feetTraveled = Math.round(feetTraveled / 5) * 5;
                }
                Answer.add("Continue for " + (int)feetTraveled + " feet");
                if(i != nodes.size() - 1){
                    Answer.add(direction);
                }else {
                    Answer.add(direction + " to " + C.getNameLong());
                }
                pixelsTraveledX = 0;
                pixelsTraveledY = 0;
            }
        }
        for(int i = 0; i < Answer.size(); i++){
           // System.out.println(Answer.get(i));
        }
        return Answer;
    }

    private String AngleToText(double alpha,double beta) {
        if(Math.abs(alpha) < 0.5) return "Go straight";
        if(beta < 0) return "Turn left";
        if(beta > 0) return "Turn right";

        return "OK";
    }

    private double getAngle(Double v1x, Double v1y, Double v2x, Double v2y) {
        Double top = v1x*v2x + v1y*v2y;
        Double bot = sqrt(v1x*v1x + v1y*v1y)*sqrt(v2x*v2x + v2y*v2y);
        return acos(top/bot);
    }

    private double getDir(Double v1x, Double v1y, Double v2x, Double v2y) {
        return v1x*v2y-v1y*v2x;
    }



    //Runs all the transitions queued up in the transitions Arraylist
    public void runTransitions() {
        if(!isSelected){
            return;
        }


        if(!emailDrawer.isHidden()){
            openEmailDrawer();
        }
        //locking zoom
//        initZoom();
        backImage.setScaleX(1.4);
        backImage.setScaleY(1.4);
        zoom.centerScroll();
        updateDrawings();

        System.out.println("starting animation");

        if(transitions.size()>0){
            floorMaps.updateFloorMap(transitions.get(0).getFloor());
            updateDrawings();
        }

        SequentialTransition allTransitions = new SequentialTransition();

        Circle movingPart = new Circle(10.0f);
        movingPart.setFill(Color.PURPLE);
        backImagePane.getChildren().add(movingPart);


        transitions.add(null);
        for (int i = 0; i < transitions.size()-1; i++) {
            TransitionData curTrans = transitions.get(i);
            System.out.println("Transitions Count: " + transitions.size());
            System.out.println("curTrans Floor: " + curTrans.getFloor());
            System.out.println("Points: " + curTrans.getPathData().getPoints());
            //  System.out.println("Length: " + curTrans.getTransitionLength());
            //floorChoice.getSelectionModel().select(dbToIndex(curTrans.getFloor()));
            //drawPath(queuedPath);
            Polyline path = curTrans.getPathData();
            PathTransition transition = new PathTransition();
            transition.setDuration(Duration.seconds(curTrans.getCalcDuration()));
            transition.setNode(movingPart);
            transition.setPath(path);
            transition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
            if(transitions.get(i+1)  != null ) {
                System.out.println("Grabbing next transition floor selection");
                TransitionData nextData = transitions.get(i+1);
                transition.setOnFinished((e) -> {
                    getSnap();
                    floorMaps.updateFloorMap(nextData.getFloor());
                    updateDrawings();
                }
                );
            }
            else{
                transition.setOnFinished((e)-> {
                    //for emailing
                    getSnap();
                    backImagePane.getChildren().remove(movingPart);
                    openEmailDrawer();
                });

            }
            allTransitions.getChildren().add(transition);

        }
        allTransitions.play();
        System.out.println("Play has been run");


        Runnable r = new Runnable() {
            public void run() {
                while(true) {
                    isUIDisabled(true);


                    //during animation
                    if(allTransitions.getStatus() != Animation.Status.RUNNING){
                        System.out.println("breaking");
                        break;
                    }

                }
                System.out.println("STOP");
                if(queuedPath.get(queuedPath.size()-1).getType().equals("EXIT")){
                    Platform.runLater(new Runnable() {
                        @Override public void run() {
                            AlertBox.display("Weather",null);
                        }
                    });
                }
                isUIDisabled(false);
                return;
            }
        };

        new Thread(r).start();

    }

    //gets the snapshot of the floor
    public void getSnap(){
        SnapData file = captureAndSaveDisplay(backImagePane, vbox.getWidth(), vbox.getHeight(), imageNumber);
        allFiles.add(file);
        imageNumber++;
    }

    //disables all buttons for the animation
    public void isUIDisabled(boolean b){
        dToggle.setDisable(b);
        floorL21.setDisable(b);
        floorL11.setDisable(b);
        floor11.setDisable(b);
        floor21.setDisable(b);
        floor31.setDisable(b);
        zoom.setLocked(b);
        startingNodeField.setDisable(b);
        exchange.setDisable(b);
        endingNodeField.setDisable(b);
        if(b){
            zoom.disableScroll();
        }
        else{
            zoom.enableScroll();
        }
    }

    //adds the images to the allfiles arraylist





    @Override
    public void insertUpdate(DocumentEvent e) {
        updatePath();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        updatePath();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        updatePath();
    }
}

