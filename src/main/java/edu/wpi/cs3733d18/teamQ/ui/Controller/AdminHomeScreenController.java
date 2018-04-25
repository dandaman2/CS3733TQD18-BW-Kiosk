package edu.wpi.cs3733d18.teamQ.ui.Controller;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733d18.teamQ.ui.Admin_Login.FaceRecognition;
import edu.wpi.cs3733d18.teamQ.ui.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static edu.wpi.cs3733d18.teamQ.manageDB.DatabaseSystem.runningFromIntelliJ;


public class AdminHomeScreenController implements Initializable {

    @FXML
    JFXButton directionBtn;

    @FXML
    JFXButton requestBtn;

    @FXML
    JFXButton editMapBtn;

    @FXML
    JFXButton employeeEdit;

    @FXML
    JFXButton logOut;

    @FXML
    JFXButton addFaceButton;

    @FXML
    Label addedFaceLabel;

    @FXML
    private AnchorPane adminScreenPane;


    //the user
    User user = User.getUser();

    public void initialize(URL url, ResourceBundle rb) {
        setUpButtons();
        setUpScrollText();
    }

    //initializes the buttons
    private void setUpButtons(){
        directionBtn.setDisableVisualFocus(true);

        directionBtn.setVisible(true);
        requestBtn.setVisible(true);
        editMapBtn.setVisible(true);
        employeeEdit.setVisible(true);
        logOut.setVisible(true);
        addFaceButton.setVisible(true);

        directionBtn.setMouseTransparent(false);
        requestBtn.setMouseTransparent(false);
        editMapBtn.setMouseTransparent(false);
        employeeEdit.setMouseTransparent(false);
        logOut.setMouseTransparent(false);
        addFaceButton.setMouseTransparent(false);

        //if user is not an admin, then they can't access edit map or employee screens
        if(User.getUser().getLevelAccess() < 2){
            editMapBtn.setVisible(false);
            employeeEdit.setVisible(false);
            editMapBtn.setMouseTransparent(true);
            employeeEdit.setMouseTransparent(true);
            addFaceButton.setMouseTransparent(false);
        }

        directionBtn.setOnAction(e -> goToPathfinding(e));
        requestBtn.setOnAction(e -> goToRequest(e));
        editMapBtn.setOnAction(e -> goToEditMap(e));
        employeeEdit.setOnAction(e -> goToEmployeeEdit(e));
        logOut.setOnAction(e -> goToWelcome(e));
        addFaceButton.setOnAction(e ->runFace(e));
    }

    //initializes scrolling text
    private void setUpScrollText(){
        //JLabel label = new JLabel();
    }



    //Helper Functions---------------------------------

    //ScreenUtil object for resizing of loading images
    private ScreenUtil sdUtil = new ScreenUtil();
    PathfindingCont pathCont;

    /**
     * Goes to the Pathfinding Screen when called
     * @param actionEvent Takes an action event that says it has been called
     */
    public void goToPathfinding(javafx.event.ActionEvent actionEvent){
        try{
            FXMLLoader pathfindingLoader;
            if(runningFromIntelliJ()) {
                pathfindingLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/PathfindingScreen.fxml"));
            } else{
                pathfindingLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/PathfindingScreen.fxml"));
            }
            Parent pathfindingPane = pathfindingLoader.load();
            Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            Scene pathfindingScene = sdUtil.prodAndBindScene(pathfindingPane, primaryStage);
            pathfindingScene.getStylesheets().addAll("Stylesheet.css", "StylePath.css");
            pathCont = pathfindingLoader.getController();
            pathCont.setListen(primaryStage);
            primaryStage.setScene(pathfindingScene);
        }
        catch(IOException io){
            System.out.println("errPath");
            io.printStackTrace();
        }
    }

    /**
     * Goes to the IMap Editing Screen when called
     * @param actionEvent Takes an action event that says it has been called
     */
    public void goToEditMap(javafx.event.ActionEvent actionEvent){
        try{
            FXMLLoader editMapLoader;
            if(runningFromIntelliJ()) {
                editMapLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/EditMap.fxml"));
            } else{
                editMapLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/EditMap.fxml"));
            }
            Parent editMapPane = editMapLoader.load();
            Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            Scene editMapScene = sdUtil.prodAndBindScene(editMapPane, primaryStage);
            editMapScene.getStylesheets().addAll("Stylesheet.css", "StylePath.css");
            primaryStage.setScene(editMapScene);
        }
        catch(IOException io){
            System.out.println("errEdit");
            io.printStackTrace();
        }
    }

    public void runFace(javafx.event.ActionEvent actionEvent){
        String temp =FaceRecognition.getInstance().addFace();
        if (temp==null){
//            addedFaceLabel.setText("Face ID setup failed!");
//            addedFaceLabel.setVisible(true);
        }else {
//            addedFaceLabel.setText("Face ID configured successfully!");
//            addedFaceLabel.setVisible(true);
        }
    }

    /**
     * Goes to the Request Screen when called
     * @param actionEvent Takes an action event that says it has been called
     */
    public void goToRequest(javafx.event.ActionEvent actionEvent){
        try{

            FXMLLoader requestLoader;
            if(runningFromIntelliJ()) {
                requestLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/RequestScreen.fxml"));
            } else{
                requestLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/RequestScreen.fxml"));
            }
            Parent requestPane = requestLoader.load();
            Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            Scene requestScene = sdUtil.prodAndBindScene(requestPane, primaryStage);
            primaryStage.setScene(requestScene);
        }
        catch(IOException io){
            System.out.println("errReqS");
            io.printStackTrace();
        }
    }

    /**
     * Goes to the Welcome Screen when called
     * @param actionEvent Takes an action event that says it has been called
     */
    public void goToWelcome(javafx.event.ActionEvent actionEvent){
        try{
            User user = User.getUser();
            user.saveToDB();
            user.setLevelAccess(0);
            user.setCurrentUser(null);
            Runnable r = new Runnable() {
                public void run() {
                    user.saveToDB();
                }
            };

            //new Thread(r).start();
            //this line will execute immediately, not waiting for your task to complete
            FXMLLoader welcomeLoader;
            if(runningFromIntelliJ()) {
                welcomeLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/WelcomeScreen.fxml"));
            } else{
                welcomeLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/WelcomeScreen.fxml"));
            }
            Parent welcomePane = welcomeLoader.load();
            Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            Scene welcomeScene = sdUtil.prodAndBindScene(welcomePane, primaryStage);
            welcomeScene.getStylesheets().addAll("Stylesheet.css", "StyleMenus.css", "homeBackground.css");
            primaryStage.setScene(welcomeScene);
        }
        catch(IOException io){
            System.out.println("errWelS");
            io.printStackTrace();
        }
    }

    /**
     * Goes to the Edit Employee Screen when called
     * @param actionEvent Takes an action event that says it has been called
     */
    public void goToEmployeeEdit(javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader employeeLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/EmployeeEditController.fxml"));
            if (runningFromIntelliJ()) {
                employeeLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/EmployeeEditController.fxml"));
            } else {
                employeeLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/EmployeeEditController.fxml"));
            }
            Parent employeePane = employeeLoader.load();
            Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene employeeScene = sdUtil.prodAndBindScene(employeePane, primaryStage);
            primaryStage.setScene(employeeScene);
        } catch (IOException io) {
            System.out.println("errWelS");
            io.printStackTrace();
        }
    }
}
