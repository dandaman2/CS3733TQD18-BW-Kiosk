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
import javafx.scene.layout.GridPane;
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
    JFXButton EmergencyBtn;

    @FXML
    private JFXButton aboutButton;

    @FXML
    private AnchorPane masterPane;

    @FXML
    ImageView gifPane;

    //Global user
    User user = User.getUser();


    //ScreenUtil object for resizing of loading images
    private ScreenUtil sdUtil = new ScreenUtil();

    /**
     * Initializes the Welcome Screen
     * @param url Takes a URL
     * @param rb Takes a ResourceBundle
     */
    public void initialize(URL url, ResourceBundle rb) {

        // background of welcome screen
        //bwhLogo.setImage(logo);
        //bwhSeal.setImage(seal);

        initializeGif();


        // Sets the action of buttons
        Image loginIcon;
        if(runningFromIntelliJ()) {
            loginIcon = new Image("/ButtonImages/login.png");
        } else{
            loginIcon = new Image("ButtonImages/login.png");
        }
        ImageView loginView = new ImageView(loginIcon);
        loginView.setFitWidth(50);
        loginView.setFitHeight(50);

        adminButt.setGraphic(loginView);
        adminButt.setDisableVisualFocus(true);

        aboutButton.setOnAction(e -> AboutController.displayAboutPage());
        adminButt.setOnAction(e -> AlertBox.display("Admin Login", "Please input your username and password."));
        directButt.setDisableVisualFocus(true);
        hoursButt.setOnAction(e -> HoursController.display());
        searchButt.setOnAction(e -> QuickSearchController.display());

    }

    //ScreenUtil object for resizing of loading images
    private PathfindingCont pathCont = new PathfindingCont();


    /**
     * initializes which gif to play
     */
    private void initializeGif(){
        int gifNum = user.getGifIndex();
        System.out.println("GifNum: " +gifNum);

        if(gifNum == 0){
            Image gif1;
            if(runningFromIntelliJ()) {
                gif1 = new Image("/Gif1.gif");
            } else{
                gif1 = new Image("/Gif1.gif");
            }

            gifPane.setImage(gif1);
            user.setGifIndex(1);
        }
        else if(gifNum == 1){
            Image gif2;
            if(runningFromIntelliJ()) {
                gif2 = new Image("/Gif2.gif");
            } else{
                gif2 = new Image("/Gif2.gif");
            }

            gifPane.setImage(gif2);
            user.setGifIndex(2);
        }
        else if(gifNum == 2){
            Image gif3;
            if(runningFromIntelliJ()) {
                gif3 = new Image("/Gif3.gif");
            } else{
                gif3 = new Image("/Gif3.gif");
            }

            gifPane.setImage(gif3);
            user.setGifIndex(3);
        }
        else if(gifNum == 3){
            Image gif4;
            if(runningFromIntelliJ()) {
                gif4 = new Image("/Gif4.gif");
            } else{
                gif4 = new Image("/Gif4.gif");
            }

            gifPane.setImage(gif4);
            user.setGifIndex(4);
        }
        else {
            Image gif5;
            if(runningFromIntelliJ()) {
                gif5 = new Image("/Gif5.gif");
            } else{
                gif5 = new Image("/Gif5.gif");
            }

            gifPane.setImage(gif5);
            user.setGifIndex(0);
        }
    }




    /**
     * Will open the main pathCont map
     * @param e Takes an ActionEvent
     */
    public void pathClick(javafx.event.ActionEvent e){
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
