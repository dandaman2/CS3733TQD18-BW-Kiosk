package edu.wpi.cs3733d18.teamQ.ui.Controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.cs3733d18.teamOapi.giftShop.GiftShop;
import edu.wpi.cs3733d18.teamQ.pathfinding.Node;
import edu.wpi.cs3733d18.teamQ.ui.Email;
import edu.wpi.cs3733d18.teamQ.ui.Requests.GiftRequest;
import edu.wpi.cs3733d18.teamQ.ui.Requests.InterpreterRequest;
import edu.wpi.cs3733d18.teamQ.ui.Requests.Request;
import edu.wpi.cs3733d18.teamQ.ui.Requests.SanitationRequest;
import edu.wpi.cs3733d18.teamQ.ui.User;
import edu.wpi.cs3733d18.teamQ2.ui.Controller.RequestController2;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.ExtractedResult;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import javax.naming.ServiceUnavailableException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;

import static edu.wpi.cs3733d18.teamQ.manageDB.DatabaseSystem.*;


public class  RequestController implements Initializable{

    @FXML
    private AnchorPane requestPane;

    //Drop Down Menu

    private ChoiceBox<String> interpreterCB = new ChoiceBox<String>();

    @FXML
    ChoiceBox<String> priorityLevel = new ChoiceBox<String>();

    //Text Fields
    @FXML
    private JFXTextField firstNameTF;
    @FXML
    private JFXTextField lastNameTF;

    AutoCompleteTextField roomLocationTF;
    @FXML
    private JFXTextField emailTF;
    @FXML
    private JFXTextField phoneNumberTF;
    //Fulfilled when Date fields
    @FXML
    private TextField whoFulfilled;

    private JFXTextArea sanitationDescription;


    //grids
    @FXML
    private GridPane grid1_1_1;

    //buttons
    @FXML
    private JFXButton clear;
    @FXML
    private JFXButton submitRequest;
    // submit button for pending request
    @FXML
    private JFXButton submit;
    @FXML
    JFXButton removePending;
    @FXML
    JFXButton removeFulFilled;
    @FXML
    JFXButton backBtn;
    @FXML
    JFXButton stat1;
    @FXML
    JFXButton stat2;
    @FXML
    JFXButton stat3;

    //request buttons
    @FXML
    JFXButton interpreterBtn;
    @FXML
    JFXButton sanitationBtn;
    @FXML
    JFXButton giftBtn;


    // description of pending request
    @FXML
    private Label fulfilledDescription;
    @FXML
    private Label errorLabel;

    //Pending Requests List
    @FXML
    private ListView<Request> pendingListView;
    @FXML
    private ListView<Request> fulfilledListView;


    //ScrollPane
    @FXML
    ScrollPane scrollPane;
    @FXML
    ScrollPane scrollPaneFulFill;

    //TreeTableView
    @FXML
    JFXTreeTableView<Request> treeTableViewPending;
    @FXML
    JFXTreeTableView<Request> treeTableViewFulfilled;

    //Date Picker
    @FXML
    DatePicker calendar;

    //Stats
    @FXML
    Tab statTab;
    @FXML
    Tab pendingTab;
    @FXML
    Tab fulfillTab;
    @FXML
    private GridPane statGrid;
    private PieChart pieChart;
    private PieChart pieChart1;
    private BarChart<String,Number> barChart;



    //Global Variables to monitor:------------

    //First Name of Person to wanted the request
    private String firstName;
    //Last Name of Person to wanted the request
    private String lastName;
    //email of Person to wanted the request
    private String email;
    //Phone Number of Person to wanted the request made
    private String phoneNumber;
    //Room that the Person to wanted the request
    private String roomName;


    //Global Variables for specific requests
    private String requestType;

    private String language = "English";

    //Initially sets priority to low
    private String priority = "Low";

    //Variables used for the stats
    private XYChart.Series totalSeries;
    private XYChart.Series pendingSeries;
    private XYChart.Series fulfilledSeries;


    //the user
    User user = User.getUser();


    // screenUtil object for request room searching
    ScreenUtil pUtil = new ScreenUtil();
    public ArrayList<Node> nodes;
    public ArrayList<String> currentFilter;


    //Initializes the scene
    public void initialize(URL url, ResourceBundle rb){

        setUpDropBox();
        setUpTF();
        setUpButtons();
        setUpLabels();
        setUpTreeTable();
        initPieCharts();
        initializeTab();

        nodes = getNodes();
        initAutoComp(nodes);

        initializePendingList();
        fulfilledListView.setItems(getFulfilledRequests());


        // display info of selected pending request in description box below
        treeTableViewPending.getSelectionModel().selectedItemProperty().addListener((v, oldRequest, newRequest) -> showRequestDescription(newRequest.getValue()));

        // display info of selected pending request in description box below
        treeTableViewFulfilled.getSelectionModel().selectedItemProperty().addListener((v, oldRequest, newRequest) -> showFulFillDescription(newRequest.getValue()));


        // Action for button to submit fields
        scrollPane.setStyle("-fx-font: 16px \"System\";");
        scrollPaneFulFill.setStyle("-fx-font: 16px \"System\";");

    }

    //Initialize Screen Methods---------------------------

    //initialize the drop down menus
    private void setUpDropBox() {
        //create ChoiceBox for an Interpreter request
        interpreterCB.getItems().add("English");
        interpreterCB.getItems().add("French");
        interpreterCB.getItems().add("Spanish");
        grid1_1_1.add(interpreterCB,0,8);
        interpreterCB.setValue("English");
        interpreterCB.setVisible(false);
        interpreterCB.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> displayRequestLanguage(newValue));


        //create ChoiceBox for priorityLevel
        priorityLevel.getItems().add("Low");
        priorityLevel.getItems().add("Medium");
        priorityLevel.getItems().add("High");
        priorityLevel.getItems().add("Critical");
        priorityLevel.setValue("Low");
        priorityLevel.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> displayRequestPriority(newValue));

    }

    //initialize the text fields for all requests
    private void setUpTF(){
        firstNameTF.setPromptText("Requester's First Name");
        firstNameTF.setText(null);

        lastNameTF.setPromptText("Requester's Last Name");
        lastNameTF.setText(null);

        roomLocationTF = new AutoCompleteTextField();
        roomLocationTF.setPromptText("Room Location");
        roomLocationTF.setText("");
        grid1_1_1.add(roomLocationTF,0,3);
        //roomLocationTF.setOnInputMethodTextChanged(event -> updateFilter(roomLocationTF.getText()));
        //roomLocationTF.setOnKeyPressed(event -> updateFilter(roomLocationTF.getText()));
        roomLocationTF.setOnKeyReleased(event -> updateFilter(roomLocationTF.getText()));
        //roomLocationTF.setOnKeyTyped(event -> updateFilter(roomLocationTF.getText()));

        emailTF.setPromptText("Email");
        emailTF.setText(null);

        phoneNumberTF.setPromptText("Phone Number");
        phoneNumberTF.setText(null);


        sanitationDescription = new JFXTextArea();
        grid1_1_1.add(sanitationDescription,0,8);
        sanitationDescription.setText(null);
        sanitationDescription.setVisible(false);
        sanitationDescription.setMinHeight(100);
        sanitationDescription.setPromptText("Enter Description ... ");
    }


    //Initialize buttons of request screen
    private void setUpButtons() {
        clear.setOnAction(e -> handleAction(e));
        //submit.setOnAction(e -> handleAction(e));

        clear.setBackground(new Background(new BackgroundFill(Paint.valueOf("#012D5A"), new CornerRadii(0), null)));
        clear.setStyle("-fx-text-fill: #FFFFFF;");
        clear.setRipplerFill(Paint.valueOf("#FFFFFF"));

        submitRequest.setBackground(new Background(new BackgroundFill(Paint.valueOf("#012D5A"), new CornerRadii(0), null)));
        submitRequest.setStyle("-fx-text-fill: #FFFFFF;");
        submitRequest.setRipplerFill(Paint.valueOf("#FFFFFF"));

        submit.setBackground(new Background(new BackgroundFill(Paint.valueOf("#012D5A"), new CornerRadii(0), null)));
        submit.setStyle("-fx-text-fill: #FFFFFF;");
        submit.setRipplerFill(Paint.valueOf("#FFFFFF"));
        submit.setOnAction(e -> submitFulFillRequest());

        removeFulFilled.setBackground(new Background(new BackgroundFill(Paint.valueOf("#012D5A"), new CornerRadii(0), null)));
        removeFulFilled.setStyle("-fx-text-fill: #FFFFFF;");
        removeFulFilled.setRipplerFill(Paint.valueOf("#FFFFFF"));

        removePending.setBackground(new Background(new BackgroundFill(Paint.valueOf("#012D5A"), new CornerRadii(0), null)));
        removePending.setStyle("-fx-text-fill: #FFFFFF;");
        removePending.setRipplerFill(Paint.valueOf("#FFFFFF"));

        backBtn.setBackground(new Background(new BackgroundFill(Paint.valueOf("#ECECEC"), new CornerRadii(0), null)));
        backBtn.setStyle("-fx-text-fill: #FFFFFF;");
        backBtn.setRipplerFill(Paint.valueOf("#FFFFFF"));

        Image info;
        if(runningFromIntelliJ()) {
            info = new Image("/ButtonImages/home.png");
        } else{
            info = new Image("ButtonImages/home.png");
        }
        ImageView infoView = new ImageView(info);
        infoView.setFitWidth(42);
        infoView.setFitHeight(40);
        backBtn.setGraphic(infoView);
        backBtn.setOnAction(e->goToAdminHome(e));


        stat1.setBackground(new Background(new BackgroundFill(Paint.valueOf("#012D5A"), new CornerRadii(0), null)));
        stat1.setStyle("-fx-text-fill: #FFFFFF;");
        stat1.setRipplerFill(Paint.valueOf("#FFFFFF"));
        stat1.setOnAction(e->showStat1(e));

        stat2.setBackground(new Background(new BackgroundFill(Paint.valueOf("#012D5A"), new CornerRadii(0), null)));
        stat2.setStyle("-fx-text-fill: #FFFFFF;");
        stat2.setRipplerFill(Paint.valueOf("#FFFFFF"));
        stat2.setOnAction(e->showStat2(e));


        stat3.setBackground(new Background(new BackgroundFill(Paint.valueOf("#012D5A"), new CornerRadii(0), null)));
        stat3.setStyle("-fx-text-fill: #FFFFFF;");
        stat3.setRipplerFill(Paint.valueOf("#FFFFFF"));
        stat3.setOnAction(e->showStat3(e));


        //Buttons on first tab
        Image interpreterIcon;
        if(runningFromIntelliJ()) {
            interpreterIcon = new Image("/ButtonImages/Interpreter_Icon3.png");
        } else{
            interpreterIcon = new Image("ButtonImages/Interpreter_Icon3.png");
        }
        ImageView interpreterView = new ImageView(interpreterIcon);
        interpreterView.setFitWidth(280);
        interpreterView.setFitHeight(180);

        interpreterBtn.setGraphic(interpreterView);
        interpreterBtn.setBackground(new Background(new BackgroundFill(Paint.valueOf("#012D5A"), new CornerRadii(0), null)));
        interpreterBtn.setStyle("-fx-text-fill: #FFFFFF;");
        interpreterBtn.setRipplerFill(Paint.valueOf("#FFFFFF"));
        interpreterBtn.setOnAction(e  -> displayRequestType("Interpreter"));


        Image sanitationIcon;
        if(runningFromIntelliJ()) {
            sanitationIcon = new Image("/ButtonImages/Sanitation_Icon3.png");
        } else{
            sanitationIcon = new Image("ButtonImages/Sanitation_Icon3.png");
        }
        ImageView sanitationView = new ImageView(sanitationIcon);
        sanitationView.setFitWidth(280);
        sanitationView.setFitHeight(180);

        sanitationBtn.setGraphic(sanitationView);
        sanitationBtn.setBackground(new Background(new BackgroundFill(Paint.valueOf("#012D5A"), new CornerRadii(0), null)));
        sanitationBtn.setStyle("-fx-text-fill: #FFFFFF;");
        sanitationBtn.setRipplerFill(Paint.valueOf("#FFFFFF"));
        sanitationBtn.setOnAction(e  -> displayRequestType("Sanitation"));


        Image giftIcon;
        if(runningFromIntelliJ()) {
            giftIcon = new Image("/ButtonImages/Gift_Icon3.png");
        } else{
            giftIcon = new Image("ButtonImages/Gift_Icon3.png");
        }
        ImageView giftView = new ImageView(giftIcon);
        giftView.setFitWidth(280);
        giftView.setFitHeight(180);

        giftBtn.setGraphic(giftView);
        giftBtn.setBackground(new Background(new BackgroundFill(Paint.valueOf("#012D5A"), new CornerRadii(0), null)));
        giftBtn.setStyle("-fx-text-fill: #FFFFFF;");
        giftBtn.setRipplerFill(Paint.valueOf("#FFFFFF"));
        giftBtn.setOnAction(e  -> displayRequestType("Gift"));

    }


    /**
     * highlights the button of the selected floor
     */
    public void highlightButton() {
        clearBtnBorders();

        switch (requestType) {
            case "Interpreter": interpreterBtn.setStyle("-fx-border-color: YELLOW;" + "-fx-border-width: 5;" + "-fx-text-fill: #FFFFFF;");
                break;

            case "Sanitation": sanitationBtn.setStyle("-fx-border-color: YELLOW;" + "-fx-border-width: 5;" + "-fx-text-fill: #FFFFFF;");
                break;

            case "Gift": giftBtn.setStyle("-fx-border-color: YELLOW;" + "-fx-border-width: 5;" + "-fx-text-fill: #FFFFFF;");
                break;

            default:
                break;
        }
    }


    /**
     * clears the borders of all the floor selector buttons
     */
    public void clearBtnBorders(){
        interpreterBtn.setStyle("-fx-text-fill: #FFFFFF;");
        sanitationBtn.setStyle("-fx-text-fill: #FFFFFF;");
        giftBtn.setStyle("-fx-text-fill: #FFFFFF;");
    }

    /**
     * displays the first stat
     */
    private void showStat1(ActionEvent e){
        pieChart.setVisible(true);
        pieChart1.setVisible(false);
        barChart.setVisible(false);
    }

    /**
     * displays the second stat
     */
    private void showStat2(ActionEvent e){
        pieChart.setVisible(false);
        pieChart1.setVisible(true);
        barChart.setVisible(false);
    }

    /**
     * displays the third stat
     */
    private void showStat3(ActionEvent e){
        pieChart.setVisible(false);
        pieChart1.setVisible(false);
        barChart.setVisible(true);
    }

    //initializes charts on tab click
    private void initializeTab(){
        statTab.setOnSelectionChanged(e -> statTabFunction());
        pendingTab.setOnSelectionChanged(e -> penidngTabFunction());
        fulfillTab.setOnSelectionChanged(e -> fulfillTabFunction());

    }

    /**
     * Initializes the functions of the statsTab
     */
    private void statTabFunction(){
        updateCharts();
    }

    /**
     * Initializes the functions of the penidngTab
     */
    private void penidngTabFunction(){
        clearCharts();
    }

    /**
     * Initializes the functions of the fulfillTab
     */
    private void fulfillTabFunction(){
        clearCharts();
    }

    /**
     * clears the charts
     */
    private void clearCharts(){
        pieChart.setVisible(false);
        pieChart1.setVisible(false);
        barChart.setVisible(false);

        //statGrid.getChildren().removeAll();
        System.out.println(statGrid.getChildren());
    }

    /**
     * gets the data for the charts
     */
    private void updateCharts(){
        pieChart.setVisible(true);

        ArrayList<Request> requests = getRequests();
        int interpreterReqs=0;
        int sanitationReqs=0;
        int emergReqs=0;
        int giftReqs=0;
        for (Request req:requests) {
            switch (req.getType()) {
                case "Interpreter":
                    interpreterReqs++;
                    break;
                case "Sanitation":
                    sanitationReqs++;
                    break;
                case "EMERGENCY":
                    emergReqs++;
                    break;
                case "Gift":
                    giftReqs++;
                    break;
            }
        }
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Interpreter", interpreterReqs),
                        new PieChart.Data("Sanitation", sanitationReqs),
                        new PieChart.Data("Emergency", emergReqs),
                        new PieChart.Data("Gift", giftReqs));
        pieChart.setTitle("Request Breakdown");
        pieChart.setData(pieChartData);

        int critical=0;
        int high=0;
        int medium=0;
        int low=0;
        for (Request req:requests) {
            switch (req.getPriority()) {
                case "Critical":
                    critical++;
                    break;
                case "High":
                    high++;
                    break;
                case "Medium":
                    medium++;
                    break;
                case "Low":
                    low++;
                    break;
                default:
                    break;
            }
        }
        ObservableList<PieChart.Data> pieChartData1 =
                FXCollections.observableArrayList(
                        new PieChart.Data("Critical", critical),
                        new PieChart.Data("High", high),
                        new PieChart.Data("Medium", medium),
                        new PieChart.Data("Low",low));
        pieChart1.setTitle("Priority Breakdown");
        pieChart1.setData(pieChartData1);


        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Type");
        yAxis.setLabel("Number");
        yAxis.setTickUnit(5);

        barChart.setTitle("Summary");
        int pendingInterpreter=0;
        int fulfilledInterpreter=0;
        int pendingSanitation=0;
        int fulfilledSanitation=0;
        int pendingEmerg=0;
        int fulfilledEmerg=0;
        int pendingGift=0;
        int fulfilledGift=0;

        for (Request req:requests) {
            switch (req.getType()) {
                case "Interpreter":
                    if(req.isFulfilled()==1){
                        fulfilledInterpreter++;
                    } else{
                        pendingInterpreter++;
                    }
                    break;
                case "Sanitation":
                    if(req.isFulfilled()==1){
                        fulfilledSanitation++;
                    } else{
                        pendingSanitation++;
                    }
                    break;
                case "EMERGENCY":
                    if(req.isFulfilled()==1){
                        fulfilledEmerg++;
                    } else{
                        pendingEmerg++;
                    }
                    break;
                case "Gift":
                    if(req.isFulfilled()==1){
                        fulfilledGift++;
                    } else {
                        pendingGift++;
                    }
            }
        }

        totalSeries.setName("Total");
        totalSeries.getData().add(new XYChart.Data("Interpreter", interpreterReqs));
        totalSeries.getData().add(new XYChart.Data("Sanitation", sanitationReqs));
        totalSeries.getData().add(new XYChart.Data("Emergency", emergReqs));
        totalSeries.getData().add(new XYChart.Data("Gift", giftReqs));

        pendingSeries.setName("Pending");
        pendingSeries.getData().add(new XYChart.Data("Interpreter", pendingInterpreter));
        pendingSeries.getData().add(new XYChart.Data("Sanitation", pendingSanitation));
        pendingSeries.getData().add(new XYChart.Data("Emergency", pendingEmerg));
        pendingSeries.getData().add(new XYChart.Data("Gift", pendingGift));

        fulfilledSeries.setName("Fulfilled");
        fulfilledSeries.getData().add(new XYChart.Data("Interpreter", fulfilledInterpreter));
        fulfilledSeries.getData().add(new XYChart.Data("Sanitation", fulfilledSanitation));
        fulfilledSeries.getData().add(new XYChart.Data("Emergency", fulfilledEmerg));
        fulfilledSeries.getData().add(new XYChart.Data("Gift", fulfilledGift));

        //Scene scene  = new Scene(bc,800,600);
        //barChart.getData().addAll(totalSeries, pendingSeries, fulfilledSeries);
        //barChart.setCategoryGap(40);
    }

    /**
     * Initializes the pie charts
     */
    private void initPieCharts(){
        pieChart = new PieChart();
        pieChart1 = new PieChart();


        ArrayList<Request> requests = getRequests();
        int interpreterReqs=0;
        int sanitationReqs=0;
        int emergReqs=0;
        int giftReqs=0;
        for (Request req:requests) {
            switch (req.getType()) {
                case "Interpreter":
                    interpreterReqs++;
                    break;
                case "Sanitation":
                    sanitationReqs++;
                    break;
                case "EMERGENCY":
                    emergReqs++;
                    break;
                case "Gift":
                    giftReqs++;
                    break;
            }
        }
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Interpreter", interpreterReqs),
                        new PieChart.Data("Sanitation", sanitationReqs),
                        new PieChart.Data("Emergency", emergReqs),
                        new PieChart.Data("Gift", giftReqs));
        pieChart.setTitle("Request Breakdown");
        pieChart.setData(pieChartData);

        int critical=0;
        int high=0;
        int medium=0;
        int low=0;
        for (Request req:requests) {
            switch (req.getPriority()) {
                case "Critical":
                    critical++;
                    break;
                case "High":
                    high++;
                    break;
                case "Medium":
                    medium++;
                    break;
                case "Low":
                    low++;
                    break;
                default:
                    break;
            }
        }
        ObservableList<PieChart.Data> pieChartData1 =
                FXCollections.observableArrayList(
                        new PieChart.Data("Critical", critical),
                        new PieChart.Data("High", high),
                        new PieChart.Data("Medium", medium),
                        new PieChart.Data("Low",low));
        pieChart1.setTitle("Priority Breakdown");
        pieChart1.setData(pieChartData1);


        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Type");
        yAxis.setLabel("Number");
        yAxis.setTickUnit(5);

        barChart = new BarChart<String, Number>(xAxis,yAxis);
        barChart.setTitle("Summary");
        int pendingInterpreter=0;
        int fulfilledInterpreter=0;
        int pendingSanitation=0;
        int fulfilledSanitation=0;
        int pendingEmerg=0;
        int fulfilledEmerg=0;
        int pendingGift=0;
        int fulfilledGift=0;

        for (Request req:requests) {
            switch (req.getType()) {
                case "Interpreter":
                    if(req.isFulfilled()==1){
                        fulfilledInterpreter++;
                    } else{
                        pendingInterpreter++;
                    }
                    break;
                case "Sanitation":
                    if(req.isFulfilled()==1){
                        fulfilledSanitation++;
                    } else{
                        pendingSanitation++;
                    }
                    break;
                case "EMERGENCY":
                    if(req.isFulfilled()==1){
                        fulfilledEmerg++;
                    } else{
                        pendingEmerg++;
                    }
                    break;
                case "Gift":
                    if(req.isFulfilled()==1){
                        fulfilledGift++;
                    } else{
                        pendingGift++;
                    }
            }
        }

        totalSeries = new XYChart.Series();
        totalSeries.setName("Total");
        totalSeries.getData().add(new XYChart.Data("Interpreter", interpreterReqs));
        totalSeries.getData().add(new XYChart.Data("Sanitation", sanitationReqs));
        totalSeries.getData().add(new XYChart.Data("Emergency", emergReqs));
        totalSeries.getData().add(new XYChart.Data("Gift", giftReqs));

        pendingSeries = new XYChart.Series();
        pendingSeries.setName("Pending");
        pendingSeries.getData().add(new XYChart.Data("Interpreter", pendingInterpreter));
        pendingSeries.getData().add(new XYChart.Data("Sanitation", pendingSanitation));
        pendingSeries.getData().add(new XYChart.Data("Emergency", pendingEmerg));
        pendingSeries.getData().add(new XYChart.Data("Gift", pendingGift));

        fulfilledSeries = new XYChart.Series();
        fulfilledSeries.setName("Fulfilled");
        fulfilledSeries.getData().add(new XYChart.Data("Interpreter", fulfilledInterpreter));
        fulfilledSeries.getData().add(new XYChart.Data("Sanitation", fulfilledSanitation));
        fulfilledSeries.getData().add(new XYChart.Data("Emergency", fulfilledEmerg));
        fulfilledSeries.getData().add(new XYChart.Data("Gift", fulfilledGift));

        //Scene scene  = new Scene(bc,800,600);
        barChart.getData().addAll(totalSeries, pendingSeries, fulfilledSeries);
        barChart.setCategoryGap(40);
        statGrid.add(pieChart,0,0);
        statGrid.add(pieChart1,0,0);
        statGrid.add(barChart,0,0);

        pieChart.setVisible(true);
        pieChart1.setVisible(false);
        barChart.setVisible(false);
    }

    //ScreenUtil object for resizing of loading images
    private ScreenUtil sdUtil = new ScreenUtil();

    /**
     * Goes to the Admin Home Screen when called
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

    //Initialize labels of request screen
    private void setUpLabels(){
        errorLabel.setVisible(false);
    }

    /**
     * returns the list of all pending requests
     * @return ObservableList<Request>
     */
    private ObservableList<Request> getPendingRequests(){
        ArrayList<Request> allRequests = getRequests();
        ArrayList<Request> pendingRequests = new ArrayList<Request>();
        for (Request request: allRequests) {
            if(request.isFulfilled()==0){
                System.out.println("Request is not fulfilled");
                pendingRequests.add(request);
            } else {
                System.out.println("R is fulfilled");
            }
        }
        ObservableList<Request> requests = FXCollections.observableArrayList(pendingRequests);
        return requests;
    }

    /**
     * returns the list of all fulfilled requests
     * @return ObservableList<Request>
     */
    private ObservableList<Request> getFulfilledRequests(){
        ArrayList<Request> allRequests = getRequests();
        ArrayList<Request> fullfilledRequests = new ArrayList<Request>();
        for (Request request: allRequests) {
            if(request.isFulfilled()==1){
                fullfilledRequests.add(request);
            }
        }
        ObservableList<Request> requests = FXCollections.observableArrayList(fullfilledRequests);
        return requests;
    }

    // Populates pending list with values from database (atm just dummy values)
    public void initializePendingList() {

        pendingListView.setItems(getPendingRequests());

        Text text = new Text("Description:");
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(text);

        Text text2 = new Text("Description:");
        scrollPaneFulFill.setFitToWidth(true);
        scrollPaneFulFill.setContent(text2);
    }


    /**
     * initializes the tree table view
     * @param
     * @return void
     */
    private void setUpTreeTable(){

        setUpTreeTable1();
        setUpTreeTable2();

    }

    /**
     * initializes the pending tree table view
     */
    private void setUpTreeTable1(){
        JFXTreeTableColumn<Request, String> idColumn = new JFXTreeTableColumn<>("ID");
        idColumn.setPrefWidth(150);
        idColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Request, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Request, String> param) {
                return param.getValue().getValue().requestIDProperty();
            }
        });
        JFXTreeTableColumn<Request, String> priorityColumn = new JFXTreeTableColumn<>("Priority");
        priorityColumn.setPrefWidth(150);
        priorityColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Request, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Request, String> param) {
                return param.getValue().getValue().priority();
            }
        });
        JFXTreeTableColumn<Request, String> typeColumn = new JFXTreeTableColumn<>("Type");
        typeColumn.setPrefWidth(150);
        typeColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Request, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Request, String> param) {
                return param.getValue().getValue().typeProperty();
            }
        });
        JFXTreeTableColumn<Request, String> roomColumn = new JFXTreeTableColumn<>("Room");
        roomColumn.setPrefWidth(300);
        roomColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Request, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Request, String> param) {
                return param.getValue().getValue().roomNameProperty();
            }
        });
        JFXTreeTableColumn<Request, String> lastNameColumn = new JFXTreeTableColumn<>("Patient Last Name");
        lastNameColumn.setPrefWidth(150);
        lastNameColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Request, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Request, String> param) {
                return param.getValue().getValue().lastNameProperty();
            }
        });

        ObservableList<Request> allRequests = FXCollections.observableArrayList(getPendingRequests());
        final TreeItem<Request> pendingRoot = new RecursiveTreeItem<Request>(allRequests, RecursiveTreeObject::getChildren);
        treeTableViewPending.getColumns().setAll(priorityColumn, idColumn, typeColumn, roomColumn, lastNameColumn);
        treeTableViewPending.setRoot(pendingRoot);
        treeTableViewPending.setShowRoot(false);
        treeTableViewPending.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
    }


    /**
     * initializes the fulfilled tree table view
     */
    private void setUpTreeTable2(){
        JFXTreeTableColumn<Request, String> idColumn = new JFXTreeTableColumn<>("ID");
        idColumn.setPrefWidth(150);
        idColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Request, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Request, String> param) {
                return param.getValue().getValue().requestIDProperty();
            }
        });
        JFXTreeTableColumn<Request, String> priorityColumn = new JFXTreeTableColumn<>("Priority");
        priorityColumn.setPrefWidth(150);
        priorityColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Request, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Request, String> param) {
                return param.getValue().getValue().priority();
            }
        });
        JFXTreeTableColumn<Request, String> typeColumn = new JFXTreeTableColumn<>("Type");
        typeColumn.setPrefWidth(150);
        typeColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Request, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Request, String> param) {
                return param.getValue().getValue().typeProperty();
            }
        });
        JFXTreeTableColumn<Request, String> roomColumn = new JFXTreeTableColumn<>("Room");
        roomColumn.setPrefWidth(300);
        roomColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Request, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Request, String> param) {
                return param.getValue().getValue().roomNameProperty();
            }
        });
        JFXTreeTableColumn<Request, String> lastNameColumn = new JFXTreeTableColumn<>("Patient Last Name");
        lastNameColumn.setPrefWidth(150);
        lastNameColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Request, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Request, String> param) {
                return param.getValue().getValue().lastNameProperty();
            }
        });

        ObservableList<Request> allRequests = FXCollections.observableArrayList(getFulfilledRequests());
        final TreeItem<Request> pendingRoot = new RecursiveTreeItem<Request>(allRequests, RecursiveTreeObject::getChildren);
        treeTableViewFulfilled.getColumns().setAll(priorityColumn, idColumn, typeColumn, roomColumn, lastNameColumn);
        treeTableViewFulfilled.setRoot(pendingRoot);
        treeTableViewFulfilled.setShowRoot(false);
        treeTableViewFulfilled.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
    }


    /**
     * Initializes the autocomplete functionality of the search bar
     * @param nodeList
     */
    private void initAutoComp(ArrayList<Node> nodeList) {
        ArrayList<String> nodeIdentification = pUtil.getNameIdNode(nodeList);

        //TextFields.bindAutoCompletion(roomLocationTF, nodeIdentification);
    }


    /**
     * updates the filter for the searching
     * @param text
     */
    private void updateFilter(String text) {
        ArrayList<String> nodeIdentification = pUtil.getNameIdNode(nodes);
        List<ExtractedResult> fuzzyFilter =  FuzzySearch.extractSorted(text, nodeIdentification);

        currentFilter = new ArrayList<String>();
        for(int i = 0; i < fuzzyFilter.size(); i++){
            ExtractedResult current = fuzzyFilter.get(i);
            //System.out.println(current.getString() + " - " + current.getScore() + " for " +text);

            currentFilter.add(current.getString());
        }

        roomLocationTF.getEntries().addAll(currentFilter);
    }

    //Helper Methods-----------------------------------------------------------------------------------------------



    // if the request passed in is not null, this displays the description of the request in the given description pane
    private void showRequestDescription(Request request) {
        if (request != null) {
            // set the text of description to be the description of the request
            Text text = new Text(request.showDescription());
            scrollPane.setFitToWidth(true);
            scrollPane.setContent(text);

            //pendingDescription.setText(request.showDescription());
        } else {
            // if the request is null, reset the label to say Description
            Text text = new Text("Description:");
            scrollPane.setFitToWidth(true);
            scrollPane.setContent(text);
        }
    }

    // if the request passed in is not null, this displays the description of the request in the given description scroll pane
    private void showFulFillDescription(Request request) {
        if (request != null) {
            // set the text of description to be the description of the request
            Text text = new Text(request.showDescription());
            scrollPaneFulFill.setFitToWidth(true);
            scrollPaneFulFill.setContent(text);

            //pendingDescription.setText(request.showDescription());
        } else {
            // if the request is null, reset the label to say Description
            Text text = new Text("Description:");
            scrollPaneFulFill.setFitToWidth(true);
            scrollPaneFulFill.setContent(text);
        }
    }


    // when submit is clicked, takes selected list view item and the text in the name and date fields and moves the item to fulfilled
    //// with additional information (whoFulfilled, dateFulfilled)
    private void submitFulFillRequest() {
        try {
            String name = whoFulfilled.getText();


            //Request r = pendingListView.getSelectionModel().getSelectedItem();
            Request r;
            if(treeTableViewPending.getSelectionModel().selectedItemProperty().getValue() != null) {
                r = treeTableViewPending.getSelectionModel().getSelectedItem().getValue();
                hideError();
            }
            else{
                errorLabel.setText("[No Request Selected]");
                errorLabel.setVisible(true);
                return;
            }

            // initialize fields as variable to set in an object later

            //determines if text field for whoFulFilled is filled in
            if(name == null || whoFulfilled.getText().isEmpty()){
                errorLabel.setText("[Employee Name not Entered]");
                errorLabel.setVisible(true);
                System.out.println("Invalid Employee Name");
                return;
            }

            //determines if there is a request to fulfill
            if(r == null){
                errorLabel.setText("[No Request Selected to Fulfill]");
                errorLabel.setVisible(true);
                System.out.println("No Request to fulfill");
                return;
            }


            LocalDate date = calendar.getValue();
            GregorianCalendar.from(date.atStartOfDay(ZoneId.systemDefault()));
            GregorianCalendar dayFulfilled = GregorianCalendar.from(date.atStartOfDay(ZoneId.systemDefault()));
            dayFulfilled.set(GregorianCalendar.MONTH,GregorianCalendar.MONTH+1);

            // sets the variables to the selected request object
            r.setDate(dayFulfilled);
            r.setFulfilled(1); // request is now fulfilled
            r.setNameWhoFulfilled(name);

            // remove the object from the pending list
            Request selectedItem = fulfilledListView.getSelectionModel().getSelectedItem();
            fulfilledListView.getItems().remove(selectedItem);
            //pendingListView.getItems().remove(r);
            editRequest(r);

            Email fulfilledRequestEmail = new Email();
            fulfilledRequestEmail.sendRequestEmail(r);
            fulfilledRequestEmail.sendRequestText(r);

            final TreeItem<Request> pendingRoot = new RecursiveTreeItem<Request>(getPendingRequests(), RecursiveTreeObject::getChildren);
            if(pendingRoot.getValue() != null) {
                treeTableViewPending.setRoot(pendingRoot);
            }
            treeTableViewPending.setShowRoot(false);


            //pendingListView.setItems(getPendingRequests());
            //requestDB.remove(r);

            //clears values from text fields
            whoFulfilled.clear();

            // adds the object on the fulfilled list
            fulfilledListView.setItems(getFulfilledRequests());
            final TreeItem<Request> fulFilledRoot = new RecursiveTreeItem<Request>(getFulfilledRequests(), RecursiveTreeObject::getChildren);
            if(fulFilledRoot != null) {
                treeTableViewFulfilled.setRoot(fulFilledRoot);
                treeTableViewFulfilled.setShowRoot(false);
            }

            Text text = new Text("Description:");
            scrollPane.setContent(text);
            hideError();

        }catch (NumberFormatException n){
            errorLabel.setText("[Date Entered Improperly (MM DD YYYY)]");
            errorLabel.setVisible(true);
            System.out.println("Invalid date ");
        }

    }

    //displays the text fields that are dependent of the type of request
    private void displayRequestType(String type){

        switch (type){
            //Interpreter
            case "Interpreter":
                resetDefault();
                requestType = "Interpreter";
                highlightButton();
                interpreterCB.setVisible(true);
                break;
            //Sanitation
            case "Sanitation":
                resetDefault();
                requestType = "Sanitation";
                highlightButton();
                sanitationDescription.setVisible(true);
                break;
            //Gift
            case "Gift":
                resetDefault();
                requestType = "Gift";
                highlightButton();
                runAPI();
                break;

            default:
        }
    }

    //displays the language that interpreter is needed for request
    private void displayRequestLanguage(String lang){

        switch (lang){
            //English
            case "English":
                language = "English";
                break;
            //French
            case "French":
                language = "French";
                break;
            //Spanish
            case "Spanish":
                language = "Spanish";
                break;
            default:
        }
    }

    //displays the priority that requests need
    private  void displayRequestPriority(String level){

        switch (level){
            //Low
            case "Low":
                priority = "Low";
                break;
            //Medium
            case "Medium":
                priority = "Medium";
                break;
            //High
            case "High":
                priority = "High";
                break;
            //Critical
            case "Critical":
                priority = "Critical";
                break;
            default:
        }
    }

    //resets the default scene to the Request Screen
    private void resetDefault(){
        interpreterCB.setVisible(false);
        sanitationDescription.setVisible(false);

        language = null;
    }


    //handles button responses
    public void handleAction(ActionEvent event){

        if (event.getSource() == clear) {
            resetTextFields();
        }

        if (event.getSource() == submitRequest) {
            //values from textfields

            firstName = firstNameTF.getText();
            lastName = lastNameTF.getText();
            email = emailTF.getText();
            phoneNumber = phoneNumberTF.getText();
            roomName = roomLocationTF.getText();

            //determines if all the text fields are filled before allowing submit
            if(firstName ==null || lastNameTF == null || email == null || phoneNumber == null || roomName == null){
                errorLabel.setText("[Please fill in all sections before Submitting Request]");
                errorLabel.setVisible(true);
                System.out.println("No Null");
                return;
            }

            //determines if all the text fields are filled before allowing submit
            if(firstNameTF.getText().isEmpty() || lastNameTF.getText().isEmpty() || emailTF.getText().isEmpty() ||
                    phoneNumberTF.getText().isEmpty() || roomLocationTF.getText().isEmpty()){

                errorLabel.setText("[Please fill in all sections before Submitting Request]");
                errorLabel.setVisible(true);
                System.out.println("No Empty");
                return;
            }


            int num = getNewRequestID();

            if(requestType == "Interpreter" ){

                language = interpreterCB.getValue();

                //create interpreter request object
                InterpreterRequest interReq = new InterpreterRequest(firstName, lastName, email, phoneNumber, roomName, language);
                interReq.setPriority(priority);

                //add interpreter request to list
                addRequest(interReq);

                Email requestEmail = new Email();
                requestEmail.sendRequestEmail(interReq);
                requestEmail.sendRequestText(interReq);

                pendingListView.setItems(getPendingRequests());
                final TreeItem<Request> pendingRoot = new RecursiveTreeItem<Request>(getPendingRequests(), RecursiveTreeObject::getChildren);
                treeTableViewPending.setRoot(pendingRoot);
                treeTableViewPending.setShowRoot(false);

                //reset values in text fields after submitting
                System.out.println("Sent!");
                resetTextFields();
                hideError();
                clearBtnBorders();
            }
            else if(requestType == "Sanitation"){

                //check to see if description text field is set
                if(sanitationDescription.getText() == null || sanitationDescription.getText().isEmpty()){
                    errorLabel.setText("[Please fill in all sections before Submitting Request]");
                    errorLabel.setVisible(true);
                    return;
                }

                //create interpreter request object
                String description = sanitationDescription.getText();
                SanitationRequest sanReq = new SanitationRequest(firstName, lastName, email, phoneNumber, roomName, description);
                sanReq.setPriority(priority);

                //add interpreter request to list
                addRequest(sanReq);
                pendingListView.setItems(getPendingRequests());
                final TreeItem<Request> pendingRoot = new RecursiveTreeItem<Request>(getPendingRequests(), RecursiveTreeObject::getChildren);
                treeTableViewPending.setRoot(pendingRoot);
                treeTableViewPending.setShowRoot(false);

                Email requestEmail = new Email();
                requestEmail.sendRequestEmail(sanReq);
                requestEmail.sendRequestText(sanReq);

                //reset values in text fields after submitting
                System.out.println("Sent!");
                resetTextFields();
                hideError();
                clearBtnBorders();
            }
        }
    }




    //clears the text fields for the request creation
    private void resetTextFields(){
        firstNameTF.setText(null);
        lastNameTF.setText(null);
        roomLocationTF.setText("");
        emailTF.setText(null);
        phoneNumberTF.setText(null);
        sanitationDescription.setText(null);
    }

    //Hide error
    private void hideError(){
        errorLabel.setVisible(false);
        errorLabel.setText("[Error Label]");
    }


    /**
     * removes a request from the pending list
     */
    public void removePending(){

        Request r;
        if(treeTableViewPending.getSelectionModel().selectedItemProperty().getValue() != null) {
            r = treeTableViewPending.getSelectionModel().getSelectedItem().getValue();
            removeRequest(r);

            final TreeItem<Request> pendingRoot = new RecursiveTreeItem<Request>(getPendingRequests(), RecursiveTreeObject::getChildren);
            treeTableViewPending.setRoot(pendingRoot);
            treeTableViewPending.setShowRoot(false);

            Text text = new Text("Description:");
            scrollPane.setContent(text);
            hideError();
        }

    }

    /**
     * removes a request from the fulfilled list
     */
    public void removeFulFilled(){

        Request r;
        if(treeTableViewFulfilled.getSelectionModel().selectedItemProperty().getValue() != null) {
            r = treeTableViewFulfilled.getSelectionModel().getSelectedItem().getValue();
            removeRequest(r);

            final TreeItem<Request> fulfulledRoot = new RecursiveTreeItem<Request>(getFulfilledRequests(), RecursiveTreeObject::getChildren);
            if(fulfulledRoot.getValue() != null) {
                treeTableViewFulfilled.setRoot(fulfulledRoot);
            }
            treeTableViewFulfilled.setShowRoot(false);

            Text text = new Text("Description:");
            scrollPaneFulFill.setContent(text);

            hideError();
        }

    }

    public void didMove(){
        System.out.println("Reeeeequesttttttt");
    }


    //runAPI
    public void runAPI() {
        GiftShop giftShop = new GiftShop();
        GiftRequest giftRequest = new GiftRequest("NA","NA",null,null,user.getNode("GELEV00N02").getNameLong());
        giftRequest.setPriority("Medium");
        giftRequest.setFulfilled(0);
        addRequest(giftRequest);
        pendingListView.setItems(getPendingRequests());
        final TreeItem<Request> pendingRoot = new RecursiveTreeItem<Request>(getPendingRequests(), RecursiveTreeObject::getChildren);
        treeTableViewPending.setRoot(pendingRoot);
        treeTableViewPending.setShowRoot(false);
        giftShop.run(0, 0, 1900, 1000, (String)null, "Path A", (String)null);
    }

//    //runAPI
//    public void runAPI() throws IOException {
//        RequestController2 requestController2 = new RequestController2();
//        try {
//            requestController2.run(0, 0, 1900, 1000, (String)null, (String)null, (String)null);
//        } catch (ServiceUnavailableException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}

