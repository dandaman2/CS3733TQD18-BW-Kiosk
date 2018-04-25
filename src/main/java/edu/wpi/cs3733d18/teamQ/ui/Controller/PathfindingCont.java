package edu.wpi.cs3733d18.teamQ.ui.Controller;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXTreeView;
import edu.wpi.cs3733d18.teamQ.pathfinding.*;
import edu.wpi.cs3733d18.teamQ.ui.ArrowShapes.BreadCrumber;
import edu.wpi.cs3733d18.teamQ.ui.*;
import edu.wpi.cs3733d18.teamQ.ui.ProxyMaps.FloorMaps;
import edu.wpi.cs3733d18.teamQ.ui.ProxyMaps.IMap;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.ExtractedResult;
import sun.reflect.generics.tree.Tree;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static edu.wpi.cs3733d18.teamQ.manageDB.DatabaseSystem.getNodes;
import static edu.wpi.cs3733d18.teamQ.manageDB.DatabaseSystem.runningFromIntelliJ;
import static edu.wpi.cs3733d18.teamQ.ui.PathData.*;
import static edu.wpi.cs3733d18.teamQ.ui.PathInstructions.captureAndSaveDisplay;
import static java.lang.Math.acos;
import static java.lang.Math.sqrt;
import static javafx.geometry.HPos.LEFT;
import static javafx.geometry.HPos.RIGHT;
import static javafx.scene.input.KeyCode.U;

public class PathfindingCont extends JPanel implements Initializable, IZoomableCont, DocumentListener {

    //Path Inputs
    @FXML
    GridPane gridTop;

    private AutoCompleteTextField startingNodeField;

    private AutoCompleteTextField endingNodeField;

    @FXML
    private JFXButton exchange;

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

    @FXML
    JFXButton infoButt;

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

    private ImageView movingPart;

    @FXML
    private ToggleButton playButton;

    @FXML
    private Label floorLabel;

    @FXML
    private JFXComboBox<String> gifSelector;

    private JFXTreeView<String> textTree;

    @FXML
    private JFXButton textBtn;

    @FXML
    private JFXDrawer treeDrawer;

    @FXML
    private HBox topHBox;

    //Scrolling and zooming functionality
    @FXML
    private ScrollPane imageScroller;
    final DoubleProperty zoomProperty = new SimpleDoubleProperty(1);
    @FXML
    private AnchorPane outerAnchor;
    final double SCALE_DELTA = 1.1;
    public double SCALE_TOTAL = 1;


    //the animated path and ant variables
    ArrayList<Timeline> antTimeLines = new ArrayList<>();
    ArrayList<Polyline> antPaths = new ArrayList<>();
    SequentialTransition allTransitions;

    //animation variables
    public ArrayList<TransitionData> transitions = new ArrayList<>();
    private ScreenUtil sdUtil = new ScreenUtil();
    ArrayList<Line> drawnPath = new ArrayList<>();
    ImageView selectedLocation;
    Node curSelected;
    Node youHere;
    Node cameraNode;
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
    private BreadCrumber breadCrumb;


    //Strings used to see if change was made
    String sTempStart = "";
    String sTempEnd = "";


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
        textTree = new JFXTreeView<String>();
        textTree.setVisible(false);
        textTree.setMouseTransparent(true);
        textBtn.setVisible(false);

        treeDrawer.setMouseTransparent(true);

        Image close;
        if(runningFromIntelliJ()) {
            close = new Image("/ButtonImages/down-book.png");
        } else{
            close = new Image("ButtonImages/down-book.png");
        }
        ImageView closeView = new ImageView(close);
        textBtn.setGraphic(closeView);

        treeDrawer.close();
        treeDrawer.setSidePane(textTree);

        nodes = getNodes();

        //list of nodes from running algorithm
        queuedPath = new ArrayList<>();

        //get array of all nodes
        //make an array of string that are gotten from the nodes
        //once user selects string, go through array of nodes to find the appropriate node
        youHere = user.getNode("GELEV00N02");
        startNode = youHere;

        initializeButtons();
        initializeTF();
        initAutoComp(nodes);

        initScroll();
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
        noder.displayNodes(getCurrFloor(), excludedTypesFromNodes, floorMaps.getIs2D());
        initYouAreHere();

        breadCrumb = new BreadCrumber(floorMaps, hboxProgress, this);
        initFloorLabel();
        initGifSelector();
        //new TimeoutData().initTimer(screenBinding);
        //initTimer();
        user = User.getUser();

        if(user.getPathType()!=null){
            getNearType(user.getPathType());
        }

        textTree.setVisible(false);
        textTree.setMouseTransparent(true);

        infoButt.setOnAction(e -> InfoController.displayPathInfoPage());
        infoButt.setStyle("-fx-background-radius: 50%; -fx-font-size: 20;");
        ImageView infoView = new ImageView(new Image("ButtonImages/info_symbol.png"));
        infoView.setFitHeight(60);
        infoView.setFitWidth(60);
        infoButt.setText("");
        infoButt.setGraphic(infoView);
    }


    /******************************************************
     *
     * Initializers
     *
     ******************************************************/

    /**
     * initializes the scrolling and panning of the pathfinding map
     */
    public void initScroll(){

        imageScroller.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (event.getDeltaY() == 0) {
                    return;
                }
                double scaleFactor
                        = (event.getDeltaY() > 0)
                        ? SCALE_DELTA
                        : 1 / SCALE_DELTA;
                if (scaleFactor * SCALE_TOTAL >= 1) {
                    Bounds viewPort = imageScroller.getViewportBounds();
                    Bounds contentSize = backImagePane.getBoundsInParent();

                    double centerPosX = (contentSize.getWidth() - viewPort.getWidth()) * imageScroller.getHvalue() + viewPort.getWidth() / 2;
                    double centerPosY = (contentSize.getHeight() - viewPort.getHeight()) * imageScroller.getVvalue() + viewPort.getHeight() / 2;

                    backImagePane.setScaleX(backImagePane.getScaleX() * scaleFactor);
                    backImagePane.setScaleY(backImagePane.getScaleY() * scaleFactor);
                    SCALE_TOTAL *= scaleFactor;

                    double newCenterX = centerPosX * scaleFactor;
                    double newCenterY = centerPosY * scaleFactor;

                    imageScroller.setHvalue((newCenterX - viewPort.getWidth()/2) / (contentSize.getWidth() * scaleFactor - viewPort.getWidth()));
                    imageScroller.setVvalue((newCenterY - viewPort.getHeight()/2) / (contentSize.getHeight() * scaleFactor  -viewPort.getHeight()));
                }

            }
        });
    }

    private String gifPath;
    public void initGifSelector(){
        ObservableList options = FXCollections.observableArrayList();
        options.addAll("Default", "Spider-Man", "Puppy", "Zombie", "Nyan Cat");
        gifSelector.getItems().addAll(options);
        gifSelector.setStyle("-fx-background-color: #ffffff;");

        gifSelector.getSelectionModel()
                .selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
                                 @Override
                                 public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                                     switch(newValue.intValue()){
                                         case 0:
                                             gifPath = "Gifs/circle.png";
                                             break;
                                         case 1:
                                             gifPath = "Gifs/spiderman.gif";
                                             break;
                                         case 2:
                                             gifPath = "Gifs/dog.gif";
                                             break;
                                         case 3:
                                             gifPath = "Gifs/zombie.gif";
                                             break;
                                         case 4:
                                             gifPath = "Gifs/nyan.gif";
                                             break;
                                     }

                                     movingPart = new ImageView(new Image(gifPath));
                                     movingPart.setPreserveRatio(true);

                                     if(gifPath.equals("Gifs/circle.png"))
                                         movingPart.setFitWidth(40);
                                     else
                                         movingPart.setFitWidth(100);

                                 }
                             }
                );
        gifSelector.getSelectionModel().select(0);
    }

    private FadeTransition fadeIn = new FadeTransition( Duration.millis(1500));

    public void initFloorLabel() {
        floorLabel.setVisible(false);
        floorLabel.setStyle("-fx-text-fill: #818181;" + "-fx-font-size: 500;" + "-fx-font-weight: 700;");

        fadeIn.setNode(floorLabel);
        fadeIn.setFromValue(0.75);
        fadeIn.setToValue(0.0);
        fadeIn.setCycleCount(1);
        fadeIn.setAutoReverse(false);
    }

    private void floorTransition() {
        floorLabel.setText(floorMaps.currFloorString());
        floorLabel.setVisible(true);
        fadeIn.playFromStart();
    }

    private void initializeTF(){
        startingNodeField = new AutoCompleteTextField();
        startingNodeField.setPromptText("Start Location");
        startingNodeField.setText("");
        startingNodeField.setFont(Font.font("Georgia", 20));
        startingNodeField.setStyle("-fx-text-inner-color: white;");
        startingNodeField.setStyle("-fx-background-color: #FFFFFF;");
        startingNodeField.setMinWidth(300);
        topHBox.getChildren().add(startingNodeField);
        startingNodeField.toBack();
        startingNodeField.setOnMousePressed(event -> updatePath());
        startingNodeField.setOnKeyPressed(event -> updateFilterStart(startingNodeField.getText()));

        endingNodeField = new AutoCompleteTextField();
        endingNodeField.setPromptText("End Location");
        endingNodeField.setText("");
        endingNodeField.setStyle("-fx-text-inner-color: white;");
        endingNodeField.setStyle("-fx-background-color: #FFFFFF;");
        endingNodeField.setFont(Font.font("Georgia", 20));
        endingNodeField.setMinWidth(300);
        topHBox.getChildren().add(endingNodeField);
        endingNodeField.toFront();
        endingNodeField.setOnMousePressed(event -> updatePath());
        endingNodeField.setOnKeyPressed(event -> updateFilterStart(endingNodeField.getText()));

        exchange.setStyle("-fx-font-size: 30;");
        exchange.setMaxHeight(startingNodeField.getHeight());
        exchange.setPrefHeight(startingNodeField.getHeight());
        ImageView exchangeArrows = new ImageView(new Image("ButtonImages/exchange.png"));
        exchangeArrows.setFitWidth(40);
        exchangeArrows.setFitHeight(40);
        exchange.setText("⇄");

        exchange.setPadding(new Insets(0,0,0,0));
    }

    private void initializeTopBar(){
        topBar.setBackground(new Background(new BackgroundFill(Paint.valueOf("#012D5A"), new CornerRadii(0), null)));
    }

    private void initializeButtons(){
        Image info;
        if(runningFromIntelliJ()) {
            info = new Image("/ButtonImages/whiteHut.png");
        } else{
            info = new Image("ButtonImages/whiteHut.png");
        }
        ImageView infoView = new ImageView(info);
        infoView.setFitWidth(42);
        infoView.setFitHeight(40);
        homeButton.setGraphic(infoView);
        Image play;
        if(runningFromIntelliJ()) {
            play = new Image("/ButtonImages/play-icon.png");
        } else{
            play = new Image("ButtonImages/play-icon.png");
        }
        ImageView playView = new ImageView(play);
        infoView.setFitWidth(42);
        infoView.setFitHeight(40);
        playButton.setGraphic(playView);
        playButton.setVisible(false);
        playButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!oldValue){
                    Image pause;
                    if(runningFromIntelliJ()) {
                        pause = new Image("/ButtonImages/pause-icon.png");
                    } else{
                        pause = new Image("ButtonImages/pause-icon.png");
                    }
                    ImageView pauseView = new ImageView(pause);
                    playButton.setGraphic(pauseView);
                } else{
                    Image play;
                    if(runningFromIntelliJ()) {
                        play = new Image("/ButtonImages/play-icon.png");
                    } else{
                        play = new Image("ButtonImages/play-icon.png");
                    }
                    ImageView playView = new ImageView(play);
                    playButton.setGraphic(playView);
                }
            }
        });
    }

    /**
     * Initializes the autocomplete functionality of the search bar
     * @param nodeList
     */
    private void initAutoComp(ArrayList<Node> nodeList) {
        ArrayList<String> nodeIdentification = pUtil.getNameIdNode(nodeList);
        //startingNodeField.setOnKeyReleased(event -> updateFilterStart(startingNodeField.getText()));
        //endingNodeField.setOnKeyReleased(event -> updateFilterEnd(endingNodeField.getText()));
        //TextFields.bindAutoCompletion(startingNodeField, nodeIdentification);
        //TextFields.bindAutoCompletion(endingNodeField, nodeIdentification);
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
                    //startingNodeField.setText(youHere.getNameLong()+ ","+youHere.getNodeID());
                    updatePath();
                }else {
                    updatePath();
                }
            }
        });
    }

    /**
     * binds the image to its pane if needed
     */
    public void jankBind(){
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
        updateFloorMap(startingFloor);
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
            //startingNodeField.setText(youHere.getNameLong()+ ","+youHere.getNodeID());//TODO this line causes the field editing bug
        }

        if ((!sTempStart.equals(startingNodeField.getText())) || (!sTempEnd.equals(endingNodeField.getText()))){
            sTempStart = startingNodeField.getText();
            sTempEnd = endingNodeField.getText();
            if (!(endingNodeField.getText().isEmpty())) {
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
        backImagePane.getChildren().removeAll(antPaths);

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

        setToStart(queuedPath);
        drawPath(queuedPath);
        centerOnNode(queuedPath.get(0),1000);
        isPathDisplayed = true;
    }

    /**
     * Sets the user view to the center of the map on the starting floor
     * @param path The path to set the start view to
     */
    public void setToStart(ArrayList<Node> path){
        int floor = path.get(0).getFloor();
        updateFloorMap(floor);
//        zoom.centerScrollToPath(path,floor,floorMaps.getIs2D());
    }

    /**
     * Function to find the nearest location of the parsed type
     * @param Type Type of location to find
     */
    public void getNearType(String Type) {
        ArrayList<String> RestrictedTYPES = new ArrayList<String>(); // empty

        Graph g = new ByTypeSearch();
//        ArrayList<Node> floor2Nodes = new ArrayList<Node>();
//        Node n;
//        // isolates the nodes on the second floor and adds them to floor2Nodes array
//        for(int i = 0; i < user.getNodes().size(); i++){
//            n = user.getNodes().get(i);
//
//            if(n.getFloor() == 2){
//                floor2Nodes.add(n);
//            }
//        }
        g.init2(user.getNodes(),user.getEdges());

        youHere = user.getNode("GELEV00N02");
        queuedPath = g.findShortestPathByType(youHere,Type,RestrictedTYPES);

        if(Type.equals("BATH")){
            double bathLength = pathLength(queuedPath);
            Node bathNode = queuedPath.get(queuedPath.size()-1);
            queuedPath = g.findShortestPathByType(youHere,"REST",RestrictedTYPES);
            double restLength = pathLength(queuedPath);
            if(restLength<bathLength){
                curSelected = queuedPath.get(queuedPath.size()-1);
            } else{
                curSelected = bathNode;
            }
        } else {
            curSelected = queuedPath.get(queuedPath.size() - 1);
        }
        //drawPath(queuedPath);
        //isPathDisplayed = true;

        startingNodeField.setText(youHere.getNameLong()+ ","+youHere.getNodeID());
        endingNodeField.setText(curSelected.getNameLong()+ ","+curSelected.getNodeID());
    }

    /**
     * Finds the nearest exit vis only stairs
     */
    public void findExitEmergency(int numBodies){
        ArrayList<String> RestrictedTYPES = new ArrayList<String>(); // empty
        RestrictedTYPES.add("ELEV");
        Graph g = new ByTypeSearch();
        g.init2(user.getNodes(),user.getEdges());

        //HARD CODED INFO
        cameraNode = user.getNode("AINFO001L2");
        String securityPhone = "5413994557";

        ArrayList<Node> pathExit = g.findShortestPathByType(cameraNode,"EXIT",RestrictedTYPES);
        System.out.println("Path exit size: " + pathExit.size());
        if(pathExit.size()<1){
            return;
        }
        ArrayList<Node>pathExitFlipped = new ArrayList<>();
        for(int i = pathExit.size()-1; i >=0; i--){
            pathExitFlipped.add(pathExit.get(i));
        }
        PathInstructions ins = new PathInstructions();
        System.out.println("Path exit "+ pathExitFlipped.size());
        String instructions = ins.buildString(TextInstructions(pathExitFlipped));
        System.out.println("Instructions: " + instructions);
        String location = pathExit.get(0).getNameLong();
        new Email().sendTextTo(instructions,securityPhone, ""+numBodies, location);

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


    BreadCrumber arrows = new BreadCrumber();


    /**
     * Displays the inputted node to the screen
     * @param n
     */
    public void showSelection(Node n) {
        if(backImagePane.getChildren().contains(selectedLocation))
            backImagePane.getChildren().remove(selectedLocation);

        if(n.getFloor() == getCurrFloor()) {
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
        Node endNode=null;

        // removes old paths and buttons before drawing new
        backImagePane.getChildren().removeAll(drawnPath);
        backImagePane.getChildren().removeAll(antPaths);
        backImagePane.getChildren().removeAll(transList);
        backImagePane.getChildren().removeAll(labelList);
        backImagePane.getChildren().remove(starLabel);
//        breadCrumb.removeArrows();
//        hboxProgress.getChildren().removeAll(breadCrumb.getArrowList());

        // clears list
        transList.clear();
        labelList.clear();
        starLabel = new Label();

        // Arraylist to hold lines to be drawn
        drawnPath = new ArrayList<Line>();

        // creates breadcrumbs
        breadCrumb.drawCrumbs(path);

        playButton.setVisible(true);

        // translates all nodes to new coords for when map moves
        for(Node nodeToTranslate : path){
            tp = translateCoord(nodeToTranslate);
            pta.add(tp);
        }

        // add label to start location
        if(getCurrFloor() == path.get(0).getFloor()) {
            starLabel.setText("    Start: " + path.get(0).getNameLong() + " ");
            starLabel.setPrefHeight(20);
            starLabel.setLayoutX(pta.get(0).getTx());
            starLabel.setLayoutY(pta.get(0).getTy() - 10);
            starLabel.setTextFill(Color.WHITE);
            starLabel.setStyle("-fx-background-color: #3a3b3b;");
        }

        // add label to end location
        int lastIndex = pta.size() - 1;
        if(getCurrFloor() == path.get(lastIndex).getFloor()) {
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
        ArrayList<Node> nodePath = new ArrayList<>();

        // goes through to determine which lines to show, in regards to the current floor
        for(int i=0; i < pta.size()-1; i++) {
            TransPoint startpoint = pta.get(i);
            TransPoint endpoint = pta.get(i + 1);

            Node startingNode = path.get(i);
            Node endingNode = path.get(i+1);

            // if both points are on the current floor, draws line
            if ((startpoint.getFloor() == getCurrFloor()) && (endpoint.getFloor() == getCurrFloor())) {
                Line l = new Line(startpoint.getTx(), startpoint.getTy(), endpoint.getTx(), endpoint.getTy());
                l.setStroke(getPathColor());
                l.setStrokeWidth(getPathStrokeWidth());
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
                int thisfloor = getCurrFloor();


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
                transLabel.setLayoutX(buttX + size);
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
                    updateFloorMap(goToFloor);
                    drawPath(queuedPath);
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
                nodePath.add(startingNode);
            }else{

                /**********************************************************
                 * PATH ANIMATION
                 ************************************************************/
                int lineFloor = startpoint.getFloor();
//                System.out.println("new floor found: "+lineFloor);
                lineDataPoints.add(startpoint.getTx());
                lineDataPoints.add(startpoint.getTy());
                nodePath.add(startingNode);

                if(lineDataPoints.size()<=2){
                    System.out.println("Size less than 2, generating in-place line");
                    lineDataPoints.add(startpoint.getTx());
                    lineDataPoints.add(startpoint.getTy());
                    nodePath.add(startingNode);
                }

                lineData.getPoints().addAll(lineDataPoints);
                transitions.add(new TransitionData(lineFloor, lineData, nodePath));
                lineDataPoints = new ArrayList<>();
                lineData = new Polyline();
                nodePath = new ArrayList<>();
            }
            finalPoint = endpoint;
            endNode = endingNode;
        }
        //tying up the end of the transitioning path
        if(finalPoint != null){
            lineDataPoints.add(finalPoint.getTx());
            lineDataPoints.add(finalPoint.getTy());
            nodePath.add(endNode);
        }

        if(lineDataPoints.size()<=2){
            lineDataPoints.add(finalPoint.getTx());
            lineDataPoints.add(finalPoint.getTy());
            nodePath.add(endNode);
        }

        lineData.getPoints().addAll(lineDataPoints);
       // System.out.println("pta size:" + pta.size());
        transitions.add(new TransitionData(pta.get(pta.size()-1).getFloor(), lineData,nodePath));
        lineDataPoints = new ArrayList<>();
        lineData = new Polyline();
        nodePath = new ArrayList<>();
        TextInstructions(path);

        // adds path and buttons to pane
        backImagePane.getChildren().addAll(drawnPath);
        addAntz(transitions);
        backImagePane.getChildren().addAll(labelList);
        backImagePane.getChildren().add(starLabel);
        backImagePane.getChildren().addAll(transList);
        try {
            movingPart.toFront();
        }
        catch (NullPointerException p){
            System.out.println("MovingPart not on image yet");
            movingPart = new ImageView(new Image("dog.gif"));
            backImagePane.getChildren().add(movingPart);
            movingPart.toFront();

        }
        TreeItem<String> root = new TreeItem<String>("Directions");

        ArrayList<String> instructions = TextInstructions(queuedPath);

        int currentFloor = queuedPath.get(0).getFloor();
        String floorString;
        if(currentFloor==-1){
            floorString = "L1";
        } else if(currentFloor==-2){
            floorString = "L2";
        } else {
            floorString = String.valueOf(currentFloor);
        }
        TreeItem<String> floorLeaf = new TreeItem<String>("Floor "+floorString);
        floorLeaf.setExpanded(true);

        int instructionIndex = 0;
        for (int i = instructionIndex; i < instructions.size(); i++) {
            TreeItem<String> itemLeaf = new TreeItem<String>(instructions.get(i));
            floorLeaf.getChildren().add(itemLeaf);
            instructionIndex++;
            if(instructions.get(i).contains("floor")){
                break;
            }
        }

        root.getChildren().add(floorLeaf);
        for (Node n:queuedPath) {
            if(n.getFloor()!=currentFloor){
                currentFloor = n.getFloor();
                if(currentFloor==-1){
                    floorString = "L1";
                } else if(currentFloor==-2){
                    floorString = "L2";
                } else {
                    floorString = String.valueOf(currentFloor);
                }
                floorLeaf = new TreeItem<String>("Floor "+floorString);
                floorLeaf.setExpanded(true);
                for (int i = instructionIndex; i < instructions.size(); i++) {
                    TreeItem<String> itemLeaf = new TreeItem<String>(instructions.get(i));
                    floorLeaf.getChildren().add(itemLeaf);
                    instructionIndex++;
                    if(instructions.get(i).contains("floor")){
                        break;
                    }
                }
                root.getChildren().add(floorLeaf);
            }
        }
        /*for (String item:instructions) {
            TreeItem<String> leaf = new TreeItem<String>(item);
            root.getChildren().add(leaf);
        }*/
        root.setExpanded(true);
        textTree.setRoot(root);
        textTree.setVisible(true);
        textTree.setMouseTransparent(false);
        textBtn.setVisible(true);
    }

    /**
     * Adds the ants animations to the path
     * @param trans the transition data to get the polylines from
     */

    public void addAntz(ArrayList<TransitionData> trans){

        backImagePane.getChildren().removeAll(antPaths);
        antPaths = new ArrayList<>();

        for(Timeline t: antTimeLines){
            t.stop();
        }
        antTimeLines = new ArrayList<>();
        for(TransitionData t : trans){
            if(t.getFloor()==getCurrFloor()){
                dashLine(t.getPathData());

                System.out.println("XPos get0 " + t.getNodeShells().get(0).getxPos());
                System.out.println("XPos get1 " + t.getNodeShells().get(1).getxPos());

                if(t.getNodeShells().get(0).getxPos() > t.getNodeShells().get(1).getxPos()){
                    System.out.println("Flip");
                    movingPart.setScaleY(-1);
                    movingPart.setScaleX(1);
                }
                else {
                    System.out.println("UnFlip");
                    movingPart.setScaleY(1);
                    movingPart.setScaleX(1);
                }

            }
        }
        backImagePane.getChildren().addAll(antPaths);
        for(Timeline antline: antTimeLines){
            System.out.println("playing ants");
            antline.play();
        }


    }

    /**
     * sets a timeline-based moving line for the polyline
     * @param line
     */
    public void dashLine(Polyline line){
        line.getStrokeDashArray().setAll(getAntSpacing(),getAntSpacing());
        line.setStrokeWidth(getAntStrokeWidth());

        final double maxOffset =
                line.getStrokeDashArray().stream()
                        .reduce(
                                0d,
                                (a, b) -> a + b
                        );

        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.ZERO,
                        new KeyValue(
                                line.strokeDashOffsetProperty(),
                                maxOffset,
                                Interpolator.LINEAR
                        )
                ),
                new KeyFrame(
                        Duration.seconds(0.5),
                        new KeyValue(
                                line.strokeDashOffsetProperty(),
                               0,
                                Interpolator.LINEAR
                        )
                )
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        line.setStroke(getAntColor());
        antTimeLines.add(timeline);
        antPaths.add(line);

    }


    /**
     * Updates the path with manipulation of the screen
     */
    public void updateDrawings(){
        //highlightSelected(floorMaps.getCurrFloor());
        noder.removeAllNodes();
        noder.displayNodes(getCurrFloor(), excludedTypesFromNodes, floorMaps.getIs2D());

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
        backImagePane.getChildren().removeAll(antPaths);
        drawnPath = new ArrayList<>();
        textTree.setVisible(false);
        textTree.setMouseTransparent(true);
        textBtn.setVisible(false);
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
        updateFloorMap(getCurrFloor());

        if(dToggle.isSelected()) {
            dToggle.setText("3D");
            dToggle.setStyle("-fx-base: #0067B1;" + "-fx-background-radius: 50%;");
        }
        else {

            dToggle.setText("2D");
            dToggle.setStyle("-fx-base: #ca2b24;" + "-fx-background-radius: 50%;");
        }
        dToggle.setTextFill(Color.WHITE);

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
                welcomeScene.getStylesheets().addAll("Stylesheet.css", "StyleMenus.css","homeBackground.css");
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

    /**
     * updates the filter for the searching of the start node
     * @param text
     */
    private void updateFilterStart(String text) {
        ArrayList<String> nodeIdentification = pUtil.getNameIdNode(nodes);
        List<ExtractedResult> fuzzyFilter =  FuzzySearch.extractSorted(text, nodeIdentification);

        ArrayList<String> currentFilter = new ArrayList<String>();
        for(int i = 0; i < fuzzyFilter.size(); i++){
            ExtractedResult current = fuzzyFilter.get(i);
            //System.out.println(current.getString() + " - " + current.getScore() + " for " +text);

            currentFilter.add(current.getString());
        }

        //startingNodeField.getEntries().addAll(currentFilter);
    }

    /**
     * updates the filter for the searching of the end node
     * @param text
     */
    private void updateFilterEnd(String text) {
        ArrayList<String> nodeIdentification = pUtil.getNameIdNode(nodes);
        List<ExtractedResult> fuzzyFilter =  FuzzySearch.extractSorted(text, nodeIdentification);

        ArrayList<String> currentFilter = new ArrayList<String>();
        for(int i = 0; i < fuzzyFilter.size(); i++){
            ExtractedResult current = fuzzyFilter.get(i);
            //System.out.println(current.getString() + " - " + current.getScore() + " for " +text);

            currentFilter.add(current.getString());
        }

        //endingNodeField.getEntries().addAll(currentFilter);
    }


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
        if(start.getText().isEmpty()){
            startNode = youHere;
            if(!isSelected){
                start.setText("Current Location");
            }
            String[] valuesEnd = end.getText().split(",");
            //if text reads a node
            if (valuesEnd.length==2) {
                Node endingNode = selectedNode(valuesEnd[1]);
                if ((endingNode !=null)) {
                    showSelection(endingNode);
                    curSelected = endingNode;
                    isSelected=true;
                } else {
                    System.out.println("BadNode");
                }
            }
        } else {
            if(start.getText().equals("Current Location")){
                start.setText(youHere.getNameLong()+ ","+youHere.getNodeID());
            }
            String[] valuesStart = start.getText().split(",");
            String[] valuesEnd = end.getText().split(",");
            //if text reads a node
            if (valuesStart.length == 2 && valuesEnd.length == 2) {
                Node startingNode = selectedNode(valuesStart[1]);
                Node endingNode = selectedNode(valuesEnd[1]);
                if ((startingNode != null) && (endingNode != null)) {
                    showSelection(startingNode);
                    showSelection(endingNode);
                    startNode = startingNode;
                    curSelected = endingNode;
                    isSelected = true;
                } else {
                    System.out.println("BadNode");
                }
            }
        }
    }


    /**
     * Function to switch to floor 3
     */
    public void buttonFloor3(){
        updateFloorMap(3);
    }

    /**
     * Function to switch to floor 2
     */
    public void buttonFloor2(){
        updateFloorMap(2);
    }

    /**
     * Function to switch to floor 1
     */
    public void buttonFloor1(){
        updateFloorMap(1);
    }

    /**
     * Function to switch to floor L1
     */
    public void buttonFloorL1(){
        updateFloorMap(-1);
    }

    /**
     * Function to switch to floor L2
     */
    public void buttonFloorL2(){
        updateFloorMap(-2);
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


    /**
     * The toggling action for the play button
     */
    public void toggleRunAnimation(){
        if(playButton.isSelected()){
            runTransitions();
            Image pause;
            if(runningFromIntelliJ()) {
                pause = new Image("/ButtonImages/pause-icon.png");
            } else{
                pause = new Image("ButtonImages/pause-icon.png");
            }
            ImageView pauseView = new ImageView(pause);
            playButton.setGraphic(pauseView);
            //playButton.setText("STOP");
        }
        else{
            playButton.setText(null);
            Image play;
            if(runningFromIntelliJ()) {
                play = new Image("/ButtonImages/play-icon.png");
            } else{
                play = new Image("ButtonImages/play-icon.png");
            }
            ImageView playView = new ImageView(play);
            playButton.setGraphic(playView);
            allTransitions.stop();
            backImagePane.getChildren().remove(movingPart);
            isUIDisabled(false);

        }
    }


    //Runs all the transitions queued up in the transitions Arraylist
    public void runTransitions() {
        if(!isSelected){
            return;
        }

        if(!emailDrawer.isHidden()){
            openEmailDrawer();
        }
       // backImage.setScaleX(1.4);
       // backImage.setScaleY(1.4);
        updateDrawings();

        System.out.println("starting animation");

        if(transitions.size()>0){
            updateFloorMap(transitions.get(0).getFloor());
        }

        allTransitions = new SequentialTransition();
        SequentialTransition allTimelines = new SequentialTransition();

        backImagePane.getChildren().remove(movingPart);
        backImagePane.getChildren().add(movingPart);


        transitions.add(null);
        //centerScrollToPath(transitions.get(0).getNodeShells(),transitions.get(0).getFloor(),floorMaps.getIs2D(), false);
        for (int i = 0; i < transitions.size()-1; i++) {
            TransitionData curTrans = transitions.get(i);
//            System.out.println("Transitions Count: " + transitions.size());
//            System.out.println("curTrans Floor: " + curTrans.getFloor());
//            System.out.println("Points: " + curTrans.getPathData().getPoints());
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
                    updateFloorMap(nextData.getFloor());
                    //centerScrollToPath(nextData.getNodeShells(),nextData.getFloor(),floorMaps.getIs2D(), true);
                }
                );
            }
            else{
                transition.setOnFinished((e)-> {
                    //for emailing
                    getSnap();
                    backImagePane.getChildren().remove(movingPart);
                    playButton.setSelected(false);
                    openEmailDrawer();
                });

            }
            Timeline tl;
            if(i==0){
                tl = followAnimation(transition,transitions.get(i));
            }else{
                tl = followAnimation(transition,transitions.get(i));
            }
            ParallelTransition par = new ParallelTransition(transition,tl);
            allTransitions.getChildren().add(par);
           // allTransitions.getChildren().add(transition);

        }

        zoomTo(1.5);
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
        SnapData file = captureAndSaveDisplay(backImagePane, backImagePane.getWidth(), backImagePane.getHeight(), imageNumber);
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
        startingNodeField.setDisable(b);
        exchange.setDisable(b);
        endingNodeField.setDisable(b);
        gifSelector.setDisable(b);
    }


    //centers the scroller to the average path location
    public void centerScrollToPath(ArrayList<Node> path, int floor, boolean is2D, boolean isZoomed){

       double zoomOffset = (isZoomed) ? 0 : 100;
        double avgXCoord=0;
        double avgYCoord=0;
        double numNodes=0;
        for(Node n : path){
            if(n.getFloor() == floor){
                numNodes++;
                if(is2D){
                    avgXCoord+=n.getxPos();
                    avgYCoord+=n.getyPos();
                }
                else{
                    avgXCoord+=n.getxPos3D();
                    avgYCoord+=n.getyPos3D();
                }
            }
        }
        if(avgXCoord+avgYCoord+numNodes>0){
            double imgWidth = 5000;
            double imgHeight;
            if(is2D){
                imgHeight=3400;
            }
            else{
                imgHeight = 2774;
            }

            imageScroller.setVvalue((avgYCoord/numNodes/imgHeight)+zoomOffset);
            imageScroller.setHvalue(avgXCoord/numNodes/imgWidth);
            if(!isZoomed) {
                zoomTo(2.0);
            }

            System.out.println("x pos: " + avgXCoord);
            System.out.println("y pos: " + avgYCoord);
            System.out.println("H middle: " + (imageScroller.getHmax() - imageScroller.getHmin())/2);
            System.out.println("V middle: " + (imageScroller.getVmax() - imageScroller.getVmin())/2);
            System.out.println("Nodes: " + numNodes);
        }

    }

    /**
     * centers the scroll to follow the moving part
     */
    public void moveToPart(ImageView imgv){
        double imgWidth = 5000;
        double imgHeight;
        if(floorMaps.getIs2D()){
            imgHeight=3400;
        }
        else{
            imgHeight = 2774;
        }
        imageScroller.setVvalue((imgv.getX())/imgWidth);
        imageScroller.setHvalue((imgv.getY())/imgHeight);
    }

    /**
     * sets the zoom to the desired scale value
     */
    public void zoomTo(double scale){
        double scaleFactor
                = (scale>0)
                ? scale
                : 1 / scale;
        if (scaleFactor * SCALE_TOTAL >= 1) {
            Bounds viewPort = imageScroller.getViewportBounds();
            Bounds contentSize = backImagePane.getBoundsInParent();

            double centerPosX = (contentSize.getWidth() - viewPort.getWidth()) * imageScroller.getHvalue() + viewPort.getWidth() / 2;
            double centerPosY = (contentSize.getHeight() - viewPort.getHeight()) * imageScroller.getVvalue() + viewPort.getHeight() / 2;

            backImagePane.setScaleX(scale);
            backImagePane.setScaleY(scale);

            SCALE_TOTAL *= scaleFactor;

            double newCenterX = centerPosX * scaleFactor;
            double newCenterY = centerPosY * scaleFactor;

            imageScroller.setHvalue((newCenterX - viewPort.getWidth()/2) / (contentSize.getWidth() * scaleFactor - viewPort.getWidth()));
            imageScroller.setVvalue((newCenterY - viewPort.getHeight()/2) / (contentSize.getHeight() * scaleFactor  -viewPort.getHeight()));
        }
    }


    /**
     * Returns the current floor
     *
     */
    public int getCurrFloor(){
        return floorMaps.getCurrFloor();
    }

    /**
     * Changes the floor to the inputted floor integer
     * @param floor
     */
   public void updateFloorMap(int floor){
        boolean showLabel = (floor != floorMaps.getCurrFloor());
        floorMaps.updateFloorMap(floor);
        updateDrawings();
        highlightButton(floor);
        if(showLabel) {
            floorTransition();
        }
   }

    /**
     * centers the screen to the given node
     * @param n
     */
    public void centerOnNode(Node n, double millisDuration){
        double x = floorMaps.getIs2D() ? n.getxPos() : n.getxPos3D();
        double y = floorMaps.getIs2D() ? n.getyPos() : n.getyPos3D();

        //startMarker.setCenterX(x);
        //startMarker.setCenterY(y);
        double newHValue = Math.max((x - Screen.getPrimary().getBounds().getWidth() / 2), 0) / (5000 - Screen.getPrimary().getBounds().getWidth());
        double newVValue = Math.max((y - Screen.getPrimary().getBounds().getHeight() / 2), 0) / ((floorMaps.getIs2D() ? 3400 : 2776) - Screen.getPrimary().getBounds().getHeight());
        KeyValue xValue = new KeyValue(imageScroller.hvalueProperty(), newHValue);
        KeyValue yValue = new KeyValue(imageScroller.vvalueProperty(), newVValue);
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.getKeyFrames().addAll(new KeyFrame(Duration.ZERO,
                        new KeyValue(imageScroller.hvalueProperty(), imageScroller.getHvalue()),
                        new KeyValue(imageScroller.vvalueProperty(), imageScroller.getVvalue())),
                new KeyFrame(new Duration(millisDuration), xValue, yValue));
        timeline.play();
    }

    /**
     * centers the screen to follow the animation
     * @param transition
     * @return
     */
    public Timeline followAnimation(PathTransition transition, TransitionData data){

        //centerOnNode(data.getNodes().get(0),1);

        /**
         * Starting Pan
         */
        Node s = data.getNodes().get(0);
        double initX = floorMaps.getIs2D() ? s.getxPos() : s.getxPos3D();
        double initY = floorMaps.getIs2D() ? s.getyPos() : s.getyPos3D();

        double newInitHValue = Math.max((initX - Screen.getPrimary().getBounds().getWidth() / 2), 0) / (5000 - Screen.getPrimary().getBounds().getWidth());
        double newInitVValue = Math.max((initY - Screen.getPrimary().getBounds().getHeight() / 2), 0) / ((floorMaps.getIs2D() ? 3400 : 2776) - Screen.getPrimary().getBounds().getHeight());
        KeyValue xInitValue = new KeyValue(imageScroller.hvalueProperty(), newInitHValue);
        KeyValue yInitValue = new KeyValue(imageScroller.vvalueProperty(), newInitVValue);


        /**
         * Ending Pan
         */
        Node n = data.getNodes().get(data.getNodes().size()-1);
        double x = floorMaps.getIs2D() ? n.getxPos() : n.getxPos3D();
        double y = floorMaps.getIs2D() ? n.getyPos() : n.getyPos3D();


        double newHValue = Math.max((x - Screen.getPrimary().getBounds().getWidth() / 2), 0) / (5000 - Screen.getPrimary().getBounds().getWidth());
        double newVValue = Math.max((y - Screen.getPrimary().getBounds().getHeight() / 2), 0) / ((floorMaps.getIs2D() ? 3400 : 2776) - Screen.getPrimary().getBounds().getHeight());
        KeyValue xValue = new KeyValue(imageScroller.hvalueProperty(), newHValue);
        KeyValue yValue = new KeyValue(imageScroller.vvalueProperty(), newVValue);
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);

        timeline.getKeyFrames().addAll(new KeyFrame(Duration.ZERO,
                        new KeyValue(imageScroller.hvalueProperty(), imageScroller.getHvalue()),
                        new KeyValue(imageScroller.vvalueProperty(), imageScroller.getVvalue())),
                new KeyFrame(new Duration(10), xInitValue, yInitValue));

        timeline.getKeyFrames().addAll(new KeyFrame(Duration.ZERO,
                        new KeyValue(imageScroller.hvalueProperty(), imageScroller.getHvalue()),
                        new KeyValue(imageScroller.vvalueProperty(), imageScroller.getVvalue())),
                new KeyFrame(new Duration(data.getCalcDuration()*1000), xValue, yValue));
        return timeline;
    }

    /**
     * highlights the button of the selected floor
     * @param floor
     */
    public void highlightButton(int floor) {
        clearBtnBorders();

        switch (floor) {
            case -2: floorL21.setStyle("-fx-border-color: YELLOW;" + "-fx-border-width: 5");
            break;

            case -1: floorL11.setStyle("-fx-border-color: YELLOW;" + "-fx-border-width: 5");
            break;

            case 1: floor11.setStyle("-fx-border-color: YELLOW;" + "-fx-border-width: 5");
            break;

            case 2: floor21.setStyle("-fx-border-color: YELLOW;" + "-fx-border-width: 5");
            break;

            case 3: floor31.setStyle("-fx-border-color: YELLOW;" + "-fx-border-width: 5");
            break;
        }
    }


    /**
     * clears the borders of all the floor selector buttons
     */
    public void clearBtnBorders(){
        floorL21.setStyle(null);
        floorL11.setStyle(null);
        floor11.setStyle(null);
        floor21.setStyle(null);
        floor31.setStyle(null);

    }

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

    public void displayTextDrawer(ActionEvent actionEvent) {
        if (treeDrawer.isHidden()) {
            treeDrawer.open();
            treeDrawer.setMouseTransparent(false);
            Image open;
            if(runningFromIntelliJ()) {
                open = new Image("/ButtonImages/up-book.png");
            } else{
                open = new Image("ButtonImages/up-book.png");
            }
            ImageView openView = new ImageView(open);
            textBtn.setGraphic(openView);
        } else {
            treeDrawer.close();
            treeDrawer.setMouseTransparent(true);
            Image close;
            if(runningFromIntelliJ()) {
                close = new Image("/ButtonImages/down-book.png");
            } else{
                close = new Image("ButtonImages/down-book.png");
            }
            ImageView closeView = new ImageView(close);
            textBtn.setGraphic(closeView);
        }
    }
}

