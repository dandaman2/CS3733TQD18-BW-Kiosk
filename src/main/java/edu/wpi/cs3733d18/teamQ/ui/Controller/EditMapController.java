package edu.wpi.cs3733d18.teamQ.ui.Controller;

import com.jfoenix.controls.*;
import edu.wpi.cs3733d18.teamQ.ui.LineEdge;
import edu.wpi.cs3733d18.teamQ.pathfinding.Edge;
import edu.wpi.cs3733d18.teamQ.pathfinding.Node;
import edu.wpi.cs3733d18.teamQ.ui.*;
import edu.wpi.cs3733d18.teamQ.ui.ProxyMaps.FloorMaps;
import edu.wpi.cs3733d18.teamQ.ui.ProxyMaps.IMap;
import edu.wpi.cs3733d18.teamQ.ui.User;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static edu.wpi.cs3733d18.teamQ.manageDB.DatabaseSystem.runningFromIntelliJ;
import static java.lang.Character.isLetter;
//import static edu.wpi.cs3733d18.teamQ.manageDB.DatabaseSystem.*;



public class EditMapController implements Initializable, IZoomableCont {
    //Top bar items
    @FXML
    private TextField searchNodeEdge;

    @FXML
    GridPane editMapSide;

    // Center menu items
    @FXML
    private Label nameLabel;
    @FXML
    private Label nameMesgLabel;

    @FXML
    private JFXTextField nameField;

    @FXML
    private Button submitButton;

    @FXML
    private Label x2dNode_toEdgeLabel;

    @FXML
    JFXTextField x2dNode_toEdgeField;

    @FXML
    private Label y2dNode_fromEdgeLabel;

    @FXML
    JFXTextField y2dNode_fromEdgeField;

    @FXML
    private JFXButton guessBtn;

    @FXML
    private Label x3dLabel;

    @FXML
    private JFXTextField x3dField;

    @FXML
    private Label y3dLabel;

    @FXML
    private JFXTextField y3dField;

    @FXML
    private Label typeLabel;

    @FXML
    JFXTextField typeField;

    @FXML
    private JFXButton removeButton;

    @FXML
    private JFXButton addButton;

    @FXML
    private JFXButton clearButton;

    @FXML
    private JFXButton betweenFloorEdges;

    @FXML
    JFXButton backBtn;

    //Selector dropdown

    @FXML
    private ComboBox<String> selector;
    ObservableList selectionOptions = FXCollections.observableArrayList();
    String mode = "";

    @FXML
    private ChoiceBox<String> floorChoice;

    @FXML
    private ChoiceBox<String> algChoice;

    //text field for search bar
    @FXML
    private TextField searchMenu;

    //search button
    @FXML
    Button searchButton;

    //global list of nodes and edges
    //private ArrayList<Node> nodes;
    //private ArrayList<Edge> edges;

    //for top menu
    @FXML
    JFXHamburger editMapHamburger;

    @FXML
    JFXDrawer editMapSideDrawer;

    @FXML
    private AnchorPane leftPane;

    @FXML
    private ImageView curFloor2D;

    @FXML
    private AnchorPane mapFrame2D;

    @FXML
    private VBox vbox2d;

    @FXML
    private ImageView curFloor3D;

    @FXML
    private AnchorPane mapFrame3D;

    @FXML
    private VBox vbox3d;

    @FXML
    private SplitPane mapSplitter;

    @FXML
    private JFXToggleButton heatToggle;

    @FXML
    private ToggleButton dToggle;

    @FXML
    JFXButton zoomInButt;

    @FXML
    JFXButton zoomOutButt;

    @FXML
    private AnchorPane mapPane;

    @FXML
    private ScrollPane twoDScroll;

    @FXML
    private ScrollPane threeDScroll;


    double orgPosXCirc3D;
    double orgPosYCirc3D;

    double orgPosXCirc;
    double orgPosYCirc;

    ArrayList<CircleNode> threeDPoints = new ArrayList<>();
    ArrayList<CircleNode> twoDPoints = new ArrayList<>();

    CircleNode curSel1 = new CircleNode();
    CircleNode curSel2 = new CircleNode();

    CircleNode otherSel1 = new CircleNode();
    CircleNode otherSel2 = new CircleNode();

//    ArrayList<Line> drawn3DEdges = new ArrayList<>();
//    ArrayList<Line> drawn2DEdges = new ArrayList<>();


    ArrayList<LineEdge> drawn3DEdges = new ArrayList<>();
    ArrayList<LineEdge> drawn2DEdges = new ArrayList<>();

    MapEditUtil editUtil = new MapEditUtil(this);
    IMap floorMap2D;
    IMap floorMap3D;

    //private int floor = 2;

    private Color nodeColor = Color.RED;
    private Color edgeColor = Color.BLUE;

    private Color select1Color = Color.GREEN;
    private Color select2Color = Color.YELLOWGREEN;

    private int startingFloor = 1;

    private final double zoomDelta = 1.1;

    User user;

    /**
     * Initializes the edit map screen
     * @param url
     * @param rb
     */
    public void initialize(URL url, ResourceBundle rb) {
//        initAutoComp(nodes, edges);
        long startTime = System.currentTimeMillis();
        //nodes = getNodes();
        //edges = getEdges();
        user = User.getUser();
        //nodes = user.getNodes();
        //edges = user.getEdges();
        long curTime = System.currentTimeMillis();
        System.out.println(curTime - startTime + " get nodes, edges");
        //System.out.println(System.currentTimeMillis());

        curTime = System.currentTimeMillis();
        System.out.println(curTime - startTime + " init drawer");
        startTime = curTime;

        floorMap2D = new FloorMaps(user.getMaps3D(), user.getMaps2D(), curFloor2D, startingFloor, true);
        floorMap3D = new FloorMaps(user.getMaps3D(), user.getMaps2D(), curFloor3D, startingFloor, false);
        curTime = System.currentTimeMillis();
        System.out.println(curTime - startTime + " get maps");
        startTime = curTime;

        initImage();
        curTime = System.currentTimeMillis();
        System.out.println(curTime - startTime + " init image");
        startTime = curTime;

        initClick();
        curTime = System.currentTimeMillis();
        System.out.println(curTime - startTime + " init click");
        startTime = curTime;

//      updateDrawings();
        curTime = System.currentTimeMillis();
        System.out.println(curTime - startTime + " draw");
        startTime = curTime;

        initSelectionOptions();
        curTime = System.currentTimeMillis();
        System.out.println(curTime - startTime + " init select options");
        startTime = curTime;

        initFloorSelector();
        curTime = System.currentTimeMillis();
        System.out.println(curTime - startTime + " init floor select");

        initAlgSelector();
        setUpButtons();
    }

    /**
     * initializes the buttons
     */
    private void setUpButtons() {
        addButton.setBackground(new Background(new BackgroundFill(Paint.valueOf("#012D5A"), new CornerRadii(0), null)));
        addButton.setStyle("-fx-text-fill: #FFFFFF;");
        addButton.setRipplerFill(Paint.valueOf("#FFFFFF"));

        removeButton.setBackground(new Background(new BackgroundFill(Paint.valueOf("#012D5A"), new CornerRadii(0), null)));
        removeButton.setStyle("-fx-text-fill: #FFFFFF;");
        removeButton.setRipplerFill(Paint.valueOf("#FFFFFF"));

        betweenFloorEdges.setBackground(new Background(new BackgroundFill(Paint.valueOf("#012D5A"), new CornerRadii(0), null)));
        betweenFloorEdges.setStyle("-fx-text-fill: #FFFFFF;");
        betweenFloorEdges.setRipplerFill(Paint.valueOf("#FFFFFF"));

        clearButton.setBackground(new Background(new BackgroundFill(Paint.valueOf("#012D5A"), new CornerRadii(0), null)));
        clearButton.setStyle("-fx-text-fill: #FFFFFF;");
        clearButton.setRipplerFill(Paint.valueOf("#FFFFFF"));

        backBtn.setBackground(new Background(new BackgroundFill(Paint.valueOf("#ECECEC"), new CornerRadii(0), null)));
        backBtn.setStyle("-fx-text-fill: #FFFFFF;");
        backBtn.setRipplerFill(Paint.valueOf("#FFFFFF"));

        guessBtn.setBackground(new Background(new BackgroundFill(Paint.valueOf("#012D5A"), new CornerRadii(0), null)));
        guessBtn.setStyle("-fx-text-fill: #FFFFFF;");
        guessBtn.setRipplerFill(Paint.valueOf("#FFFFFF"));

        Image info;
        if (runningFromIntelliJ()) {
            info = new Image("/ButtonImages/home.png");
        } else {
            info = new Image("ButtonImages/home.png");
        }
        ImageView infoView = new ImageView(info);
        infoView.setFitWidth(42);
        infoView.setFitHeight(40);
        backBtn.setGraphic(infoView);
        backBtn.setPrefWidth(editMapSide.getPrefWidth() + 48);

        backBtn.setOnAction(e -> goToAdminHome(e));

        dToggle.setText("2D");
        dToggle.setStyle("-fx-base: #CA2B24;" + "-fx-background-radius: 50%;");


        zoomInButt.setBackground(new Background(new BackgroundFill(Paint.valueOf("#012D5A"), new CornerRadii(0), null)));
        zoomInButt.setStyle("-fx-text-fill: #FFFFFF;");
        zoomInButt.setRipplerFill(Paint.valueOf("#FFFFFF"));

        zoomOutButt.setBackground(new Background(new BackgroundFill(Paint.valueOf("#012D5A"), new CornerRadii(0), null)));
        zoomOutButt.setStyle("-fx-text-fill: #FFFFFF;");
        zoomOutButt.setRipplerFill(Paint.valueOf("#FFFFFF"));

        zoomInButt.setPrefWidth(dToggle.getPrefWidth());
        zoomOutButt.setPrefWidth(dToggle.getPrefWidth());
    }

    private void displayHeat(CircleNode cn){
        double maxHits = Math.max(user.getMaxHits(),1);
        double scale = 255.0/maxHits;
        double sizeScale = 10.0/maxHits;
        int heatColor1;
        int heatColor2;
        double nodeSize;
        if(heatToggle.isSelected()) {
            heatColor1 = (int) (cn.getNode().getHits() * scale);
            String heatString1 = Integer.toHexString(heatColor1);
            if (heatColor1 <= 15) {
                heatString1 = "0" + heatString1;
            }

            heatColor2 = (int) (255 - (cn.getNode().getHits() * scale));
            String heatString2 = Integer.toHexString(heatColor2);
            if (heatColor2 <= 15) {
                heatString2 = "0" + heatString2;
            }
            nodeSize = (cn.getNode().getHits() * sizeScale) + 7;
            //cn.setStyle("-fx-fill: #" + heatString1 + "00" + heatString2 + ";");
            cn.setFill(Color.rgb(heatColor1,0,heatColor2));
            cn.setRadius(nodeSize);
        }
        else {
            cn.setFill(nodeColor);
            cn.setRadius(10);
        }
    }

//    private Color getHeatColor(CircleNode){
//        double maxHits = Math.max(user.getMaxHits(),1);
//        double scale = 255.0/maxHits;
//        double sizeScale = 10.0/maxHits;
//        double heatColor1;
//        double heatColor2;
//        double nodeSize;
//        if(heatToggle.isSelected()) {
//            heatColor1 = cn.getNode().getHits() * scale;
//
//            heatColor2 = 255 - (cn.getNode().getHits() * scale);
//    }

    public void estimateCoords(){
        boolean xy2DFilled = false;
        boolean xy3DFilled = false;
        if ((x2dNode_toEdgeField.getText() != null && (!x2dNode_toEdgeField.getText().trim().isEmpty())) &&
                (y2dNode_fromEdgeField.getText() != null && (!y2dNode_fromEdgeField.getText().trim().isEmpty()))) {
            xy2DFilled = true;
        }
        if ((x3dField.getText() != null && (!x3dField.getText().trim().isEmpty())) &&
                (y3dField.getText() != null && (!y3dField.getText().trim().isEmpty()))) {
            xy3DFilled = true;
        }
        if(xy2DFilled && xy3DFilled){
            return;
        }else if(xy2DFilled){
            double x2d = Double.parseDouble(x2dNode_toEdgeField.getText());
            double y2d = Double.parseDouble(y2dNode_fromEdgeField.getText());
            //do 2D -> 3D conversion
            x3dField.setText(String.valueOf((double)(int)(811.884345781074+(0.728957626276976*x2d)+(-0.350003321064494*y2d))));
            y3dField.setText(String.valueOf((double)(int)(334.546412113387+(0.260872086377462*x2d)+(0.530956818761984*y2d))));
        } else if(xy3DFilled){
            double x3d = Double.parseDouble(x3dField.getText());
            double y3d = Double.parseDouble(y3dField.getText());
            //do 3D -> 2D conversion
            x2dNode_toEdgeField.setText(String.valueOf((double)(int)(-1130.52054675626+(1.10927201797229*x3d)+(0.723162976460627*y3d))));
            y2dNode_fromEdgeField.setText(String.valueOf((double)(int)(-31.9109016044863+(-0.545661628811575*x3d)+(1.50284857905834*y3d))));
        }
    }

    private double maxZoom = 2;
    private double minZoom = .5;
    private double newScale;

    public void zoomIn(){
        newScale = curFloor2D.getScaleX() * zoomDelta;

        if (newScale > maxZoom)
            newScale = maxZoom;

        curFloor2D.setScaleY(newScale);
        curFloor2D.setScaleX(newScale);
        curFloor3D.setScaleY(newScale);
        curFloor3D.setScaleX(newScale);

        updateDrawings();
    }

    public void zoomOut(){
        newScale = curFloor2D.getScaleX() *(1 / zoomDelta);
        if (newScale < minZoom)
            newScale = minZoom;

        curFloor2D.setScaleY(newScale);
        curFloor2D.setScaleX(newScale);
        curFloor3D.setScaleY(newScale);
        curFloor3D.setScaleX(newScale);

        updateDrawings();
    }

    //ScreenUtil object for resizing of loading images
    private ScreenUtil sdUtil = new ScreenUtil();

    /**
     * Goes to the Welcome Screen when called
     * @param actionEvent Takes an action event that says it has been called
     */
    public void goToAdminHome(javafx.event.ActionEvent actionEvent){
        try{
            //this line will execute immediately, not waiting for your task to complete
            FXMLLoader adminLoader;
            if(runningFromIntelliJ()) {
                adminLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/AdminHomeScreen.fxml"));
            } else{
                adminLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/AdminHomeScreen.fxml"));
            }
            Parent adminPane = adminLoader.load();
            Stage primaryStage = (Stage)((javafx.scene.Node)actionEvent.getSource()).getScene().getWindow();
            Scene adminScene = sdUtil.prodAndBindScene(adminPane, primaryStage);
            adminScene.getStylesheets().addAll("Stylesheet.css", "StyleMenus.css");
            primaryStage.setScene(adminScene);
        }
        catch(IOException io){
            System.out.println("errWelS");
            io.printStackTrace();
        }
    }

    /**
     *
     */
    public void toggleMap(){
        //floorMaps.setIs2D(dToggle.isSelected());
        //floorMaps.updateFloorMap(floorMaps.getCurrFloor());

        if(dToggle.isSelected()) {
            dToggle.setText("3D");
            dToggle.setStyle("-fx-base: #0067B1;" + "-fx-background-radius: 50%;");
            double target = 1;
            KeyValue keyValue = new KeyValue(mapSplitter.getDividers().get(0).positionProperty(), target);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(250), keyValue));
            timeline.play();
        }
        else {
            dToggle.setText("2D");
            dToggle.setStyle("-fx-base: #CA2B24;" + "-fx-background-radius: 50%;");
            double target = 0;
            KeyValue keyValue = new KeyValue(mapSplitter.getDividers().get(0).positionProperty(), target);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(250), keyValue));
            timeline.play();
        }
    }

    /**
     * Redraws everything
     */
    public void updateDrawings(){
        displayNodesOnFloor();
        displayEdges();
    }

    /**
     *
     */
    public void initImage() {
        double startScale = .5;
        floorMap2D.updateFloorMap(2);
        floorMap3D.updateFloorMap(2);

        twoDScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        twoDScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        threeDScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        threeDScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        curFloor2D.setScaleY(startScale);
        curFloor2D.setScaleX(startScale);
        twoDScroll.setHvalue((twoDScroll.getHmax() - twoDScroll.getHmin()) / 2.0);
        twoDScroll.setVvalue((twoDScroll.getVmax() - twoDScroll.getVmin()) / 2.0);


        curFloor3D.setScaleY(startScale);
        curFloor3D.setScaleX(startScale);
        threeDScroll.setHvalue((threeDScroll.getHmax() - threeDScroll.getHmin()) / 2.0);
        threeDScroll.setVvalue((threeDScroll.getVmax() - threeDScroll.getVmin()) / 2.0);


    }

    /**
     *
     */
    public void initFloorSelector(){
        ObservableList options = FXCollections.observableArrayList();
        options.addAll("Lower Level 2", "Lower Level 1", "Floor 1", "Floor 2", "Floor 3");
        floorChoice.getItems().addAll(options);

        floorChoice.getSelectionModel()
                .selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
                                 @Override
                                 public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                                     floorMap2D.updateFloorMap(floorMap2D.indexFloorToDB(newValue.intValue()));
                                     floorMap3D.updateFloorMap(floorMap3D.indexFloorToDB(newValue.intValue()));
                                     updateDrawings();
                                 }
                             }
                );
        floorChoice.getSelectionModel().select(2);
    }

    /**
     *
     */
    public void initAlgSelector(){
        algChoice.getItems().addAll("A*", "BFS", "DFS", "Dijkstra", "Best-first search");
        algChoice.getSelectionModel().select(user.getSelectedAlg());

        algChoice.getSelectionModel()
                .selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
                                 @Override
                                 public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                                     user.setSelectedAlg(newValue.intValue());
                                     System.out.println(newValue.toString());
                                 }
                             }
                );

    }

    /**
     * Initializes the possible option selections
     */
    public void initSelectionOptions() {
        selector.setStyle("-fx-font: 16px \"System\";");
        selectionOptions.removeAll(selectionOptions);
        selector.setTooltip(new Tooltip("Select an Editing Option"));
        String en = "Edit Nodes";
        String ee = "Edit Edges";

        selectionOptions.addAll(en, new Separator(), ee);
        selector.getItems().addAll(selectionOptions);

        //applies a listener to update the selection settings immediately
        selector.getSelectionModel()
                .selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        Object selected = selectionOptions.get(newValue.intValue());
                        if (!(selected instanceof Separator)) {
                            loadSelection((String) selected);
                            System.out.println("Selected " + selected);
//                            updateDrawings();
                            //curSel1.setFill(nodeColor);
                            //curSel2.setFill(nodeColor);
//                            if(heatToggle.isSelected()){
//                                displayHeat(curSel1);
//                                displayHeat(curSel2);
//                            } else {
//                                curSel1.setFill(nodeColor);
//                                curSel2.setFill(nodeColor);
//                            }
                            if(curSel1.getNode()!=null){
                                displayHeat(curSel1);
                            }
                            if(curSel2.getNode()!=null){
                                displayHeat(curSel2);
                            }
                            curSel1 = new CircleNode();
                            curSel2 = new CircleNode();
//                            saveToDatabase();
//                            updateDrawings();
                            clearAllFields();
                        }
                    }
                });
        selector.getSelectionModel().selectFirst();
        loadSelection("Edit Nodes");
        //curSel1.setFill(nodeColor);
        //curSel2.setFill(nodeColor);
//        if(heatToggle.isSelected()){
//            displayHeat(curSel1);
//            displayHeat(curSel2);
//        } else {
//            curSel1.setFill(nodeColor);
//            curSel2.setFill(nodeColor);
//        }
        if(curSel1.getNode()!=null){
            displayHeat(curSel1);
        }
        if(curSel2.getNode()!=null){
            displayHeat(curSel2);
        }
        curSel1 = new CircleNode();
        curSel2 = new CircleNode();
//        saveToDatabase();
//        updateDrawings();
    }

    /**
     * Loads the selected editing option
     * @param selection
     */
    public void loadSelection(String selection) {
        if (selection == null) {
            System.out.println("Bad Selection, reselect");
        } else {
            switch (selection) {

                case "Edit Nodes":
                    resetVoid();
                    mode = "en";
                    initEditNode();
                    break;

                case "Edit Edges":
                    resetVoid();
                    mode = "ee";
                    initEditEdge();
                    break;
            }
        }
    }

    /**
     * Resers fiels in edit screen
     */
    public void resetVoid() {
        nameLabel.setText("");
        nameLabel.setVisible(false);
        nameField.setVisible(false);

        x2dNode_toEdgeLabel.setText("");
        x2dNode_toEdgeLabel.setVisible(false);
        x2dNode_toEdgeField.setVisible(false);

        y2dNode_fromEdgeLabel.setText("");
        y2dNode_fromEdgeLabel.setVisible(false);
        y2dNode_fromEdgeField.setVisible(false);

        x3dLabel.setText("");
        x3dLabel.setVisible(false);
        x3dField.setVisible(false);

        y3dLabel.setText("");
        y3dLabel.setVisible(false);
        y3dField.setVisible(false);

        typeLabel.setText("");
        typeLabel.setVisible(false);
        typeField.setVisible(false);
    }

    //clears the fields of the editor menu
    public void clearAllFields(){
        nameField.setText("");
        //nodeIdField.setText("");
        x2dNode_toEdgeField.setText("");
        y2dNode_fromEdgeField.setText("");
        x3dField.setText("");
        y3dField.setText("");
        typeField.setText("");
    }

    /**
     * The node label framwork
     */
    public void nodeLabelSetup() {
        nameLabel.setText("Node Name");
        x2dNode_toEdgeLabel.setText("2D X-Coordinate");
        y2dNode_fromEdgeLabel.setText("2D Y-Coordinate");
        x3dLabel.setText("3D X-Coordinate");
        y3dLabel.setText("3D Y-Coordinate");
        typeLabel.setText("Node Type");
    }

    /**
     * The add node framework
     */
    public void initEditNode() {
        nameLabel.setText("Node Name:");
        nameLabel.setVisible(true);
        nameField.setVisible(true);

        x2dNode_toEdgeLabel.setText("2D x-coordinate:");
        x2dNode_toEdgeLabel.setVisible(true);
        x2dNode_toEdgeField.setVisible(true);

        y2dNode_fromEdgeLabel.setText("2D y-coordinate:");
        y2dNode_fromEdgeLabel.setVisible(true);
        y2dNode_fromEdgeField.setVisible(true);

        x3dLabel.setText("3D x-coordinate:");
        x3dLabel.setVisible(true);
        x3dField.setVisible(true);

        y3dLabel.setText("3D y-coordinate:");
        y3dLabel.setVisible(true);
        y3dField.setVisible(true);

        typeLabel.setText("Node Type:");
        typeLabel.setVisible(true);
        typeField.setVisible(true);

        betweenFloorEdges.setVisible(false);

        addButton.setText("Add Node");
        removeButton.setText("Remove Node");
    }

    /**
     * The add edge label framework
     */
    public void initEditEdge() {
        x2dNode_toEdgeLabel.setText("To Node:");
        x2dNode_toEdgeLabel.setVisible(true);
        x2dNode_toEdgeField.setVisible(true);

        y2dNode_fromEdgeLabel.setText("From Node:");
        y2dNode_fromEdgeLabel.setVisible(true);
        y2dNode_fromEdgeField.setVisible(true);

        betweenFloorEdges.setVisible(true);

        addButton.setText("Add Edge");
        removeButton.setText("Remove Edge");
    }

    /**
     * Initializes the autocomplete functionality of the search bar
     * @param nodeList
     * @param edgeList
     */
    private void initAutoComp(ArrayList<Node> nodeList, ArrayList<Edge> edgeList) {
        ArrayList<String> nodeIdentification = getNameIdNode(nodeList);
        ArrayList<String> edgeIdentification = getNameIdEdge(edgeList);

        ArrayList<String> nodeEdge = new ArrayList<String>(nodeIdentification);
        nodeEdge.addAll(edgeIdentification);

        TextFields.bindAutoCompletion(searchMenu, nodeEdge);
    }

    /**
     * Search function for edit map
     */
    public void searchResult() {
        if (!searchMenu.getText().isEmpty()) {
            String[] values = searchMenu.getText().split(",");

            //if text reads an edge
            if (values.length == 1) {
                System.out.println(values[0]);
                Edge possibleEdge = selectedEdge(values[0]);

                if (possibleEdge != null) {
                    //todo: do something with possibleNode;
                } else {
                    System.out.println("BadEdge");
                }
            }

            //if text reads a node
            if (values.length == 2) {
                System.out.println(values[1]);
                Node possibleNode = selectedNode(values[1]);

                if (possibleNode != null) {
                    //todo: do something with possibleNode;
                } else {
                    System.out.println("BadNode");
                }
            }
        }
    }

    /**
     * Returns node if node with inputted id exists
     * @param nodeID
     * @return
     */
    public Node selectedNode(String nodeID) {
        int i;
        ArrayList<Node> nodes = user.getNodes();
        for (i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).getNodeID().equals(nodeID)) {
                System.out.println(nodes.get(i).getNameLong() + " " + nodes.get(i).getNodeID());
                return nodes.get(i);
            }
        }
        //if not in node
        Node badNode = null;
        return badNode;
    }

    /**
     * Returns edge if edge with inputted id exists
     * @param edgeID
     * @return
     */
    public Edge selectedEdge(String edgeID) {
        int i;
        ArrayList<Edge> edges = user.getEdges();
        for (i = 0; i < edges.size(); i++) {
            if (edges.get(i).getEdgeID().equals(edgeID)) {
                System.out.println(edges.get(i).getEdgeID());
                return edges.get(i);
            }
        }
        //if not in node
        Edge badEdge = null;
        return badEdge;
    }

    /**
     *
     * @return
     */
    public int getMaxID() {
        int maxID = 0;
        String nodeNumString;
        int idNum;
        ArrayList<Node> nodes = user.getNodes();
        for (Node n : nodes) {
            nodeNumString = n.getNodeID().substring(5, 8);
            if (!isLetter(nodeNumString.charAt(nodeNumString.length() - 1))) {
                idNum = Integer.parseInt(nodeNumString);
                if (idNum > maxID) {
                    maxID = idNum;
                }
            }
        }
        return maxID;
    }

    /**
     *
     * @param type
     * @return
     */
    public String generateID(String type) {
        System.out.println("into ID");
        int nextID;
        nextID = getMaxID();
        nextID++;
        String id = String.format("%s%s%03d%02d", "Q", type, nextID, 2);
        System.out.println("out of ID");
        return id;
    }

    /**
     *
     * @param ID
     * @param type
     * @return
     */
    public String generateLongName(String ID, String type) {
        String nodeNumString = ID.substring(5, 8);
        int nodeNum = Integer.parseInt(nodeNumString);
        String nodeType;
        switch (type) {
            case "HALL":
                nodeType = "Hallway Node";
                break;
            case "REST":
                nodeType = "Bathroom Node";
                break;
            case "STAI":
                nodeType = "Stairway Node";
                break;
            case "INFO":
                nodeType = "Info Node";
                break;
            default:
                nodeType = "Node";//method should not be used in this case
                break;
        }
        return nodeType + " " + nodeNum + " Floor " + floorMap2D.getCurrFloor();
    }

    /**
     * Generate a node based off of the minimal information needed
     * @param xPos
     * @param yPos
     * @param type
     * @param shortName
     * @param xPos3D
     * @param yPos3D
     * @param floor
     * @return
     */
    public Node createNode(double xPos, double yPos, String type, String shortName, double xPos3D, double yPos3D, int floor) {

        System.out.println("The floor:" + floor);
        String id = generateID(type);
        System.out.println(id);
        String longName = generateLongName(id, type);
        System.out.println(longName);

        return new Node(id, xPos, yPos, floor, "Shapiro", type, longName, shortName, "Q", xPos3D, yPos3D, 0.0, true);
    }

    /**
     * Generates ArrayList of ID and names for Autocomplete
     * @param node
     * @return
     */
    public ArrayList<String> getNameIdNode(ArrayList<Node> node) {
        int i;
        ArrayList<String> idName = new ArrayList<String>();
        for (i = 0; i < node.size(); i++) {
            idName.add(node.get(i).getNameLong() + "," + node.get(i).getNodeID());
        }
        return idName;
    }

    /**
     * Generates ArrayList of ID and names for Autocomplete edge
     * @param edge
     * @return
     */
    public ArrayList<String> getNameIdEdge(ArrayList<Edge> edge) {
        int i;
        ArrayList<String> idName = new ArrayList<String>();
        for (i = 0; i < edge.size(); i++) {
            idName.add(edge.get(i).getEdgeID());
        }
        return idName;
    }


    //Button Controls------------------------

    public String generateEdgeID(Node n1, Node n2) {
        return n1.getNodeID() + "_" + n2.getNodeID();
    }

    @FXML
    public void handleAction(ActionEvent e) {

        if (e.getSource() == clearButton) {
            System.out.println("Clear");
            resetTextFields();
        }
        if (e.getSource() == addButton) {
            // saveToDatabase();
            System.out.println("Confirm");
            try {
                //get mod of selector
                if (mode.equals("en")) {
                    //saveToSingleton();
                    System.out.println("Edit Node");
                    String name = nameField.getText();
//                    String id = nodeIdField.getText();
                    try {
                        double x2d = Double.parseDouble(x2dNode_toEdgeField.getText());
                        double y2d = Double.parseDouble(y2dNode_fromEdgeField.getText());
                        double x3d = Double.parseDouble(x3dField.getText());
                        double y3d = Double.parseDouble(y3dField.getText());
                        String type = typeField.getText();

                        System.out.println(type);

                        System.out.println("Created node");
                        Node holder = createNode(x2d, y2d, type, name, x3d, y3d, floorMap2D.getCurrFloor());
                        CircleNode nta = generate3DCircleNode(holder);
                        user.addNodeSingleton(holder);
                        //addNodeSingleton(holder);
                        updateDrawings();

                        System.out.println("Node Added");
                        //resetVoid();
                    }
                    catch(NumberFormatException nf){
                        System.out.println("no validity in fields");
                    }
                }
                if (mode.equals("ee")) {
                    //saveToSingleton();
                    String nodeID1 = curSel1.getNode().getNodeID();
                    String nodeID2 = curSel2.getNode().getNodeID();
                    if (nodeID1.equals(nodeID2)) {
                        System.out.println("node ids are same, not adding edge");
                    } else {
                        Node node1 = user.getNode(nodeID1); //getNode(nodeID1);
                        Node node2 = user.getNode(nodeID2);
                        System.out.println(nodeID1 + " " + nodeID2);
                        Edge edge = new Edge(generateEdgeID(node1, node2), node1, node2, node1.calcDistance(node2),true);
                        mapFrame3D.getChildren().removeAll(drawn3DEdges);
                        mapFrame2D.getChildren().removeAll(drawn2DEdges);
                        user.addEdgeSingleton(edge);
                        updateDrawings();
                        System.out.println("Edge added");
                    }
                }
            } catch (NullPointerException n) {
                System.out.println("Null pointer in node select");
                //do nothing
            }
        }
    }

    /**
     * Clears all text fields
     */
    public void resetTextFields() {
        nameField.setText(null);
        x2dNode_toEdgeField.setText(null);
        y2dNode_fromEdgeField.setText(null);
        x3dField.setText(null);
        x3dField.setText(null);
        y3dField.setText(null);

    }

    /**
     * Initializes clicking on the map
     */
    public void initClick() {
        editUtil.initClick(curFloor3D,x3dField,y3dField);
        editUtil.initClick(curFloor2D,x2dNode_toEdgeField,y2dNode_fromEdgeField);
    }


    /**
     * Displays the nodes onto the 3d and 2d map of the current floor
     */
    public void displayNodesOnFloor() {
        ArrayList<Node> floorNodes = user.getFloorNodes(floorMap2D.getCurrFloor()); //getNodesByFloor(floorMap2D.getCurrFloor());

        //for 3D
        mapFrame3D.getChildren().removeAll(threeDPoints);
        threeDPoints = new ArrayList<>();

        //for 2D
        mapFrame2D.getChildren().removeAll(twoDPoints);
        twoDPoints = new ArrayList<>();

        double maxHits = user.getMaxHits();
        double scale = 255.0/maxHits;
        double heatColor1;
        double heatColor2;
        double sizeScale = 10.0/maxHits;
        double nodeSize;
        //TransPoint tp;
        for (Node n : floorNodes) {
            CircleNode circle3d = generate3DCircleNode(n);
            CircleNode circle2d = generate2DCircleNode(n);

            /*if(heatToggle.isSelected()) {
                heatColor1 = n.getHits() * scale;
                String heatString1 = Integer.toHexString((int) heatColor1);
                if (heatColor1 <= 15) {
                    heatString1 = "0" + heatString1;
                }

                heatColor2 = 255 - (n.getHits() * scale);
                String heatString2 = Integer.toHexString((int) heatColor2);
                if (heatColor2 <= 15) {
                    heatString2 = "0" + heatString2;
                }
                nodeSize = (n.getHits()*sizeScale)+7;
                circle2d.setStyle("-fx-fill: #" + heatString1 + "00" + heatString2 + ";");
                circle2d.setRadius(nodeSize);
                circle3d.setStyle("-fx-fill: #" + heatString1 + "00" + heatString2 + ";");
                circle3d.setRadius(nodeSize);
            }*/
            displayHeat(circle2d);
            displayHeat(circle3d);

            threeDPoints.add(circle3d);
            twoDPoints.add(circle2d);
            //saveToSingleton(circle3d, circle2d);
        }

        mapFrame3D.getChildren().addAll(threeDPoints);
        mapFrame2D.getChildren().addAll(twoDPoints);
    }


    /**
     * Creates a dragable node on the editing screen
     * @param n
     * @return
     */
    public CircleNode generate3DCircleNode(Node n) {
        TransPoint tp;
        tp = translateCoord(n, curFloor3D, false);

        CircleNode circle = new CircleNode(n, 3);
        circle.setCenterX(tp.getTx());
        circle.setCenterY(tp.getTy());
        circle.setRadius(10.0f);
        //circle.setFill(nodeColor);
        displayHeat(circle);
        //circle.setFill(Color.rgb(0,0,0));

        circle.setCursor(Cursor.HAND);

        circle.setOnMousePressed((t) -> {

            if(mode.equals("en")){
                resetNodeColoring();
                //curSel1.setFill(nodeColor);
                if(curSel1.getNode()!=null){
                    displayHeat(curSel1);
                }
                curSel1 = circle;
                curSel1.setFill(select1Color);
                hlOther(curSel1);

                setAddNodeFields(circle,3);
            }
            if(mode.equals("ee")) {
                if (isSameDem(curSel1, circle) && isSameDem(curSel2, circle)) {

                    try {
                        resetNodeColoring();
                        //curSel1.setFill(nodeColor);
                        if(curSel1.getNode()!=null){
                            displayHeat(curSel1);
                        }
                        curSel1 = curSel2;
                        curSel1.setFill(select1Color);
                        curSel2 = circle;
                        curSel2.setFill(select1Color);
                        hlOther2(curSel1,curSel2);

                        y2dNode_fromEdgeField.setText(curSel1.getNode().getNameLong());
                        x2dNode_toEdgeField.setText(curSel2.getNode().getNameLong());
                    } catch (NullPointerException npe) {
                        System.out.println("npe for selecting 2 nodes");
                    }
                }
                else{
                    resetNodeColoring();
                    //curSel1.setFill(nodeColor);
                    //curSel2.setFill(nodeColor);
                    if(curSel1.getNode()!=null){
                        displayHeat(curSel1);
                    }
                    if(curSel2.getNode()!=null){
                        displayHeat(curSel2);
                    }
                    curSel1 = circle;
                    curSel1.setFill(select1Color);
                    curSel2 = curSel1;
                    hlOther2(curSel1,curSel2);

                    y2dNode_fromEdgeField.setText(curSel1.getNode().getNameLong());
                    x2dNode_toEdgeField.setText(curSel2.getNode().getNameLong());
                }
            }


            orgPosXCirc3D = t.getX();
            orgPosYCirc3D = t.getY();

            CircleNode c = (CircleNode) (t.getSource());
            c.toFront();


        });
        circle.setOnMouseDragged((t) -> {
            threeDScroll.setPannable(false);
            double offsetX = t.getX() - orgPosXCirc3D;
            double offsetY = t.getY() - orgPosYCirc3D;

            CircleNode c = (CircleNode) (t.getSource());

            c.setCenterX(c.getCenterX() + offsetX);
            c.setCenterY(c.getCenterY() + offsetY);

            orgPosXCirc3D = t.getX();
            orgPosYCirc3D = t.getY();

            if (mode.equals("en")) {
                setAddNodeFields(circle,3);
            }
        });

        circle.setOnMouseReleased((t)->{
            System.out.println("saving node 3d");
            threeDScroll.setPannable(true);
            saveToSingleton(circle, 3);
        });

        return circle;
    }

    /**
     *
     * @param n
     * @return
     */
    public CircleNode generate2DCircleNode(Node n) {
        TransPoint tp;
        tp = translateCoord(n, curFloor2D, true);
        CircleNode circle = new CircleNode(n,2);

        circle.setCenterX(tp.getTx());
        circle.setCenterY(tp.getTy());
        circle.setRadius(10.0f);
        //circle.setFill(nodeColor);
        //circle.setFill(Color.rgb(0,0,0));
        displayHeat(circle);

        circle.setCursor(Cursor.HAND);

        circle.setOnMousePressed((t) -> {
//            orgPosXCirc=0;
//            orgPosYCirc=0;

            if(mode.equals("en")){
                resetNodeColoring();
                if(curSel1.getNode()!=null){
                    displayHeat(curSel1);
                }
                //curSel1.setFill(Color.rgb(0,0,0));
                //displayHeat(curSel1);
                curSel1 = circle;
                curSel1.setFill(select1Color);
                hlOther(curSel1);

                setAddNodeFields(circle, 2);
            }
            if(mode.equals("ee")) {
                if (isSameDem(curSel1, circle) && isSameDem(curSel2, circle)) {
                    System.out.println("Same dim");
                    try {
                        resetNodeColoring();
                        //curSel1.setFill(nodeColor);
                        if(curSel1.getNode()!=null){
                            displayHeat(curSel1);
                        }
                        curSel1 = curSel2;
                        curSel1.setFill(select1Color);
                        curSel2 = circle;
                        curSel2.setFill(select1Color);
                        hlOther2(curSel1,curSel2);

                        y2dNode_fromEdgeField.setText(curSel1.getNode().getNameLong());
                        x2dNode_toEdgeField.setText(curSel2.getNode().getNameLong());
                    } catch (NullPointerException npe) {
                        System.out.println("npe for selecting 2 nodes 2d");
                    }
                } else {
                    System.out.println("diff dim");
                    resetNodeColoring();
                    //curSel1.setFill(nodeColor);
                    //curSel2.setFill(nodeColor);
                    if(curSel1.getNode()!=null){
                        displayHeat(curSel1);
                    }
                    if(curSel2.getNode()!=null){
                        displayHeat(curSel2);
                    }
                    curSel1 = circle;
                    curSel1.setFill(select1Color);
                    curSel2 = curSel1;
                    hlOther2(curSel1,curSel2);

                    y2dNode_fromEdgeField.setText(curSel1.getNode().getNameLong());
                    x2dNode_toEdgeField.setText(curSel2.getNode().getNameLong());
                }

            }
            orgPosXCirc = t.getX();
            orgPosYCirc = t.getY();

            CircleNode c = (CircleNode) (t.getSource());
            c.toFront();


        });

        circle.setOnMouseDragged((t) -> {
            twoDScroll.setPannable(false);
            System.out.println("dragging 2d node");
            double offsetX = t.getX() - orgPosXCirc;
            double offsetY = t.getY() - orgPosYCirc;

            CircleNode c = (CircleNode) (t.getSource());

            c.setCenterX(c.getCenterX() + offsetX);
            c.setCenterY(c.getCenterY() + offsetY);

            orgPosXCirc = t.getX();
            orgPosYCirc = t.getY();

            if (mode.equals("en")) {
                setAddNodeFields(circle, 2);
            }

        });

        circle.setOnMouseReleased((t)->{
            twoDScroll.setPannable(true);
            System.out.println("saving node 2d");
            saveToSingleton(circle, 2);
        });


        return circle;
    }

    /**
     * Draws the edges between all the nodes
     */
    /**
     * Takes in a node and returns a transpoint object
     * @param n
     * @return
     */
    public TransPoint translateCoord(Node n, ImageView view, boolean is2D){
        Parent currentParent = view.getParent();

        double imageWidth = view.getBoundsInLocal().getWidth()*view.getScaleX();;
        double imageHeight = view.getBoundsInLocal().getHeight()*view.getScaleY();
        double offsetX = view.getLayoutX() - (view.getScaleX() - 1) * view.getBoundsInLocal().getWidth()/2;
        double offsetY = view.getLayoutY() - (view.getScaleY() - 1) * view.getBoundsInLocal().getHeight()/2;

        while((currentParent != null) && !(currentParent instanceof AnchorPane)){
            offsetX += currentParent.getLayoutX();
            offsetY += currentParent.getLayoutY();
            currentParent = currentParent.getParent();
        }

        double newX, newY;

        if(is2D){
            newX = n.getxPos() * imageWidth / 5000.0 + offsetX ;
            newY = n.getyPos() * imageHeight / 3400.0 + offsetY ;
        } else {
            newX = n.getxPos3D() * imageWidth / 5000.0 + offsetX;
            newY = n.getyPos3D() * imageHeight / 2774.0 + offsetY;
        }

        return new TransPoint(newX,newY,n.getFloor());
    }

    //reverts a transnode action
    public TransPoint revertTransNode(double x, double y, int mode){

        if(mode==2) {
            ImageView view = curFloor2D;
            Parent currentParent = view.getParent();
            double imageWidth = view.getBoundsInLocal().getWidth() * view.getScaleX();

            double imageHeight = view.getBoundsInLocal().getHeight() * view.getScaleY();
            double offsetX = view.getLayoutX() - (view.getScaleX() - 1) * view.getBoundsInLocal().getWidth() / 2;
            double offsetY = view.getLayoutY() - (view.getScaleY() - 1) * view.getBoundsInLocal().getHeight() / 2;

            while ((currentParent != null) && !(currentParent instanceof AnchorPane)) {
                offsetX += currentParent.getLayoutX();
                offsetY += currentParent.getLayoutY();
                currentParent = currentParent.getParent();
            }
            double fixedX = 5000.0*(x-offsetX)/imageWidth;
            double fixedY = 3400.0*(y-offsetY)/imageHeight;
            return new TransPoint(fixedX,fixedY,0);
        } else{
            ImageView view = curFloor3D;
            Parent currentParent = view.getParent();
            double imageWidth = view.getBoundsInLocal().getWidth() * view.getScaleX();

            double imageHeight = view.getBoundsInLocal().getHeight() * view.getScaleY();
            double offsetX = view.getLayoutX() - (view.getScaleX() - 1) * view.getBoundsInLocal().getWidth() / 2;
            double offsetY = view.getLayoutY() - (view.getScaleY() - 1) * view.getBoundsInLocal().getHeight() / 2;

            while ((currentParent != null) && !(currentParent instanceof AnchorPane)) {
                offsetX += currentParent.getLayoutX();
                offsetY += currentParent.getLayoutY();
                currentParent = currentParent.getParent();
            }
            double fixedX = 5000.0*(x-offsetX)/imageWidth;
            double fixedY = 2774.0*(y-offsetY)/imageHeight;
            return new TransPoint(fixedX,fixedY,0);
        }
    }


    //draws the edges between all the nodes
    public void displayEdges() {

        mapFrame3D.getChildren().removeAll(drawn3DEdges);
        mapFrame2D.getChildren().removeAll(drawn2DEdges);

        drawn3DEdges = editUtil.SetEdges(floorMap3D.getCurrFloor(),3, heatToggle.isSelected());
        mapFrame3D.getChildren().addAll(drawn3DEdges);

        drawn2DEdges = editUtil.SetEdges(floorMap2D.getCurrFloor(),2, heatToggle.isSelected());
        mapFrame2D.getChildren().addAll(drawn2DEdges);
    }

    //allows for dynamic edge adding
    public void multiFloorEdgeAdd(){
        AlertBox.display("Add a Custom Edge", "Add a Custom Edge");
        updateDrawings();
    }

    private void saveToSingleton(CircleNode circle, int mode) {
        Node nodeToEdit = circle.getNode();
        TransPoint reverseTrans;
        if(mode==2){
            reverseTrans = revertTransNode(circle.getCenterX(),circle.getCenterY(),2);
            nodeToEdit.setxPos(reverseTrans.getTx());
            nodeToEdit.setyPos(reverseTrans.getTy());
        } else if(mode==3) {
            reverseTrans =revertTransNode(circle.getCenterX(),circle.getCenterY(),3);
            nodeToEdit.setxPos3D(reverseTrans.getTx());
            nodeToEdit.setyPos3D(reverseTrans.getTy());
        }
        user.editNodeSingleton(nodeToEdit);
    }

    private void saveToSingleton(CircleNode circle3d, CircleNode circle2d) {
        //for 3d
        Node nodeToEdit = circle3d.getNode();
        nodeToEdit.setxPos3D(circle3d.getCenterX());
        nodeToEdit.setyPos3D(circle3d.getCenterY());
        nodeToEdit.setxPos(circle2d.getCenterX());
        nodeToEdit.setyPos(circle2d.getCenterY());
        user.editNodeSingleton(nodeToEdit);
    }

    //removes the selected node or edge from the map
    public void removeFromMap(){
        if(mode.equals("en")) {
            Node tbr = curSel1.getNode();
            ArrayList<Edge> edgesToRemove = user.getNodeEdges(tbr); //getEdgesByNode(tbr);

            //for 3d
            mapFrame3D.getChildren().removeAll(drawn3DEdges);
            mapFrame3D.getChildren().removeAll(threeDPoints);

            //for 2d
            mapFrame2D.getChildren().removeAll(drawn2DEdges);
            mapFrame2D.getChildren().removeAll(twoDPoints);

            for (Edge e : edgesToRemove) {
                user.removeEdgeSingleton(e);
                //removeEdgeSingleton(e);
            }
            user.removeNodeSingleton(tbr);
            //removeNodeSingleton(tbr);
            updateDrawings();

        }
        if(mode.equals("ee")){
            Edge edgeToRemove = editUtil.getCurSel().getEdge();
            user.removeEdgeSingleton(edgeToRemove);
            System.out.println("EDGE REMOVED");
            updateDrawings();
        }



    }

    public Edge getEdgeFromNodes(Node node1, Node node2){
        ArrayList<Edge> edges1 = user.getNodeEdges(node1); //getEdgesByNode(node1);
        ArrayList<Edge> edges2 = user.getNodeEdges(node2); //getEdgesByNode(node2);
        System.out.println("REMOVING EDGE");
        for(Edge e1: edges1){
            for(Edge e2 : edges2){
                if(e2.getEdgeID().equals(e1.getEdgeID())){
                    return e1;
                }
            }
        }

        return null;
    }

    /**
     * Tests to see if a and b are in the same dimension
     * @param a
     * @param b
     * @return
     */
    public boolean isSameDem(CircleNode a, CircleNode b){
        if((a.getDimension() == b.getDimension())||(a.getDimension() == 0 ) || (b.getDimension()== 0)){
            return true;
        }
        else{
            return false;
        }

    }



    //highlights the selected node in the opposite dimensional region
    public void hlOther(CircleNode circNode){
        int dem = circNode.getDimension();
        if(dem == 3){
            for(CircleNode cn : twoDPoints){
                //cn.setFill(nodeColor);
                //cn.setFill(Color.rgb(0,0,0));
            }
            for(CircleNode cn: twoDPoints){
                if(cn.getNode().getNodeID().equals(circNode.getNode().getNodeID())){
                    cn.setFill(select2Color);
                }
                else {
                    //cn.setFill(nodeColor);
                    //cn.setFill(Color.rgb(0,0,0));
                    displayHeat(cn);
                }

            }
        }
        if(dem == 2){
            for(CircleNode cn : threeDPoints){
                //cn.setFill(nodeColor);
                //cn.setFill(Color.rgb(0,0,0));
            }
            for(CircleNode cn: threeDPoints){
                if(cn.getNode().getNodeID().equals(circNode.getNode().getNodeID())){
                    cn.setFill(select2Color);
                }
                else {
                    //cn.setFill(nodeColor);
                    //cn.setFill(Color.rgb(0,0,0));
                    displayHeat(cn);
                }

            }
        }
    }



    //highlights the selected nodes in the opposite dimensional image
    public void hlOther2(CircleNode cn1, CircleNode cn2){
        int dem = cn1.getDimension();
        if(dem == 3){
            for(CircleNode cn : twoDPoints){
                //cn.setFill(nodeColor);
            }
            for(CircleNode cn: twoDPoints){
                if(cn.getNode().getNodeID().equals(cn1.getNode().getNodeID())){
                    cn.setFill(select2Color);
                }
                else if(cn.getNode().getNodeID().equals(cn2.getNode().getNodeID())){
                    cn.setFill(select2Color);
                }
                else {
                    //cn.setFill(nodeColor);
                    //cn.setFill(Color.rgb(0,0,0));
                    displayHeat(cn);
                }

            }
        }
        if(dem == 2){
            for(CircleNode cn : threeDPoints){
                //cn.setFill(nodeColor);
            }
            for(CircleNode cn: threeDPoints){
                if(cn.getNode().getNodeID().equals(cn1.getNode().getNodeID())){
                    cn.setFill(select2Color);
                }
                else if(cn.getNode().getNodeID().equals(cn2.getNode().getNodeID())){
                    cn.setFill(select2Color);
                }
                else {
                    //cn.setFill(nodeColor);
                    //cn.setFill(Color.rgb(0,0,0));
                    displayHeat(cn);
                }

            }
        }
    }

    //Resets all the node coloring to the default node color
    public void resetNodeColoring(){
        for(CircleNode cn : threeDPoints){
            //cn.setFill(nodeColor);
            //cn.setFill(Color.rgb(255,255,255));
            displayHeat(cn);
        }
        for(CircleNode cn : twoDPoints){
            //cn.setFill(nodeColor);
            //cn.setFill(Color.rgb(255,255,255));
            displayHeat(cn);
        }
    }

    //fills the fields for the node that is selected
    public void setAddNodeFields(CircleNode circle, int dem){
        nameField.setText(circle.getNode().getNameLong());
        if(dem == 3) {
            x3dField.setText("" + circle.getCenterX() + "");
            y3dField.setText("" + circle.getCenterY() + "");
            typeField.setText(circle.getNode().getType());
            for(CircleNode cn: twoDPoints){
                if(cn.getNode().getNodeID().equals(circle.getNode().getNodeID())){
                    x2dNode_toEdgeField.setText("" + cn.getCenterX() + "");
                    y2dNode_fromEdgeField.setText("" + cn.getCenterY() + "");
                }
            }

        }
        if(dem == 2) {
            x2dNode_toEdgeField.setText("" + circle.getCenterX() + "");
            y2dNode_fromEdgeField.setText("" + circle.getCenterY() + "");
            typeField.setText(circle.getNode().getType());
            for(CircleNode cn: threeDPoints){
                if(cn.getNode().getNodeID().equals(circle.getNode().getNodeID())){
                    x3dField.setText("" + cn.getCenterX() + "");
                    y3dField.setText("" + cn.getCenterY() + "");
                }
            }

        }


    }

}
