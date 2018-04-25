package edu.wpi.cs3733d18.teamQ.ui.Controller;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733d18.teamQ.ui.Admin_Login.FaceRecognition;
import edu.wpi.cs3733d18.teamQ.ui.Requests.EmergencyRequest;
import edu.wpi.cs3733d18.teamQ.ui.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static edu.wpi.cs3733d18.teamQ.manageDB.DatabaseSystem.addRequest;
import static edu.wpi.cs3733d18.teamQ.manageDB.DatabaseSystem.runningFromIntelliJ;


public class WelcomeController implements Initializable {

    @FXML
    private JFXButton hoursButt;

    @FXML
    private JFXButton directButt;

    @FXML
    private JFXButton searchButt;

    @FXML
    private JFXButton adminButt;

    @FXML
    private JFXButton EmergencyBtn;

    @FXML
    private ImageView bwhSeal;

    @FXML
    private ImageView bwhLogo;

    @FXML
    private AnchorPane sealPane;

    @FXML
    private StackPane stackPane;

    @FXML
    private VBox buttBox;

    @FXML
    private Button aboutButton;


    //ScreenUtil object for resizing of loading images
    private ScreenUtil sdUtil = new ScreenUtil();

    /**
     * Initializes the Welcome Screen
     * @param url Takes a URL
     * @param rb Takes a ResourceBundle
     */
    public void initialize(URL url, ResourceBundle rb) {
        Image logo;
        Image seal;
        if(runningFromIntelliJ()) {
            logo = new Image("bwhLogo.png");
            seal = new Image("bwhCrest.png");
        } else{
            logo = new Image("bwhLogo.png");
            seal = new Image("bwhCrest.png");
        }

//        Button face = new Button("Face Recognition");
//        face.setStyle("-fx-font-size: 15pt;");
//        face.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                FaceRecognition.getInstance().getGesture();
//
//            }
//        });

        buttBox.getChildren().addAll();

        // info button icon
        Image info;
        if(runningFromIntelliJ()) {
            info = new Image("/ButtonImages/info_symbol.png");
        } else{
            info = new Image("ButtonImages/info_symbol.png");
        }
        ImageView infoView = new ImageView(info);
        infoView.setFitWidth(42);
        infoView.setFitHeight(40);
        aboutButton.setPrefWidth(43);
        aboutButton.setPrefHeight(41);
        aboutButton.setGraphic(infoView);

        // background of welcome screen
        //bwhLogo.setImage(logo);
        //bwhSeal.setImage(seal);

        // Sets the action of buttons
        aboutButton.setOnAction(e -> AboutController.displayAboutPage());
        adminButt.setOnAction(e -> AlertBox.display("Admin Login", "Please input your username and password."));
//        directButt.setDisableVisualFocus(true);
        hoursButt.setOnAction(e -> HoursController.display());
        searchButt.setOnAction(e -> QuickSearchController.display());

    }

    //ScreenUtil object for resizing of loading images
    private PathfindingCont pathCont = new PathfindingCont();

    /**
     * Will open the main pathCont map
     * @param e Takes an ActionEvent
     */
    public void pathClick(javafx.event.ActionEvent e){
        User user = User.getUser();
        user.setPathType(null);
        try {
            FXMLLoader pathfindingload = new FXMLLoader(getClass().getResource("/fxmlFiles/PathfindingScreen.fxml"));
            Parent pathfindingParent = pathfindingload.load();
            Stage primaryStage = (Stage)((Node)e.getSource()).getScene().getWindow();
            Scene pathfindingScene = sdUtil.prodAndBindScene(pathfindingParent, primaryStage);
            pathfindingScene.getStylesheets().addAll("Stylesheet.css", "StylePath.css");
            primaryStage.setScene(pathfindingScene);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Makes an emergency request from the home screen
     */
    public void emergencyRequest(){
        int bodies = FaceRecognition.getInstance().detectBody();
        if(bodies>0){
            //FaceRecognition.getInstance().detectBody()>0
            new PathfindingCont().findExitEmergency(bodies);
        }
        EmergencyRequest emergencyRequest = new EmergencyRequest("NA", "NA", "NA", "NA", User.getUser().getNodeLocation());
        emergencyRequest.setPriority("Critical");
        emergencyRequest.setType("EMERGENCY");
        addRequest(emergencyRequest);
        AlertBox.display("Emergency", "Help is on the way!");
    }
}
