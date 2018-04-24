package edu.wpi.cs3733d18.teamQ.ui.Controller;


import edu.wpi.cs3733d18.teamQ.ui.Employee;
import edu.wpi.cs3733d18.teamQ.ui.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.beans.EventHandler;
import java.io.IOException;

import static edu.wpi.cs3733d18.teamQ.manageDB.DatabaseSystem.getEmployee;
import static edu.wpi.cs3733d18.teamQ.manageDB.DatabaseSystem.runningFromIntelliJ;

public class QuickSearchController {
    /**
     * Creates a formatted popup window that can be formatted differently based on what the title is
     */

    public static void display(){
        Stage window = new Stage();

        // Format the window
        window.setTitle("Quick Search");
        Label label = new Label("Pick a Destination");
        label.setStyle("-fx-font-size: 27pt;");
        BorderPane layout = new BorderPane(); // Implements a Border Pane (splits screen into 5 sections)
        HBox hb = addHBox();
        hb.setStyle("-fx-background-color: #012D5A;");
        layout.setTop(hb);
        VBox vb = addVBox();
        layout.setCenter(vb);
        vb.setAlignment(Pos.CENTER);
        AnchorPane aPane = addAnchorPane();
        HBox hb2 = addHBox();
        hb2.setStyle("-fx-background-color: #012D5A;");
        layout.setBottom(hb2);


        //gets nearest bathroom
        Button bathButton = new Button("Bathroom");
        bathButton.setStyle("-fx-font-size: 15pt; -fx-border-color: #F6BD38; -fx-base: #012D5A;");
        bathButton.setMaxWidth(200.0);
        bathButton.setOnAction(new javafx.event.EventHandler<ActionEvent>() {
            private PathfindingCont pathCont = new PathfindingCont();
            @Override
            public void handle(ActionEvent e) {
                User user = User.getUser();
                Stage primaryStage = user.getPrimaryStage();
                FXMLLoader pathfindingLoader;
                if(runningFromIntelliJ()) {
                    pathfindingLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/pathfindingView.fxml"));
                } else{
                    pathfindingLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/pathfindingView.fxml"));
                }
                Parent pathfindingPane = null;
                try{
                    pathfindingPane = pathfindingLoader.load();
                }
                catch(IOException io){
                    io.printStackTrace();
                }
                ScreenUtil sdUtil = new ScreenUtil();
                Scene pathfindingScene = sdUtil.prodAndBindScene(pathfindingPane, primaryStage);
                pathfindingScene.getStylesheets().addAll("Stylesheet.css");
                primaryStage.setScene(pathfindingScene);
                pathCont = pathfindingLoader.getController();
                pathCont.setListen(primaryStage);
                pathCont.getNearType("BATH");
                window.close();
            }
        });

        //gets nearest exit
        Button exitButton = new Button("Exit");
        exitButton.setStyle("-fx-font-size: 15pt; -fx-border-color: #F6BD38; -fx-base: #012D5A;");
        exitButton.setMaxWidth(200.0);
        exitButton.setOnAction(new javafx.event.EventHandler<ActionEvent>() {
            private PathfindingCont pathCont = new PathfindingCont();
            @Override
            public void handle(ActionEvent e) {
                User user = User.getUser();
                Stage primaryStage = user.getPrimaryStage();
                FXMLLoader pathfindingLoader;
                if(runningFromIntelliJ()) {
                    pathfindingLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/pathfindingView.fxml"));
                } else{
                    pathfindingLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/pathfindingView.fxml"));
                }
                Parent pathfindingPane = null;
                try{
                    pathfindingPane = pathfindingLoader.load();
                }
                catch(IOException io){
                    io.printStackTrace();
                }
                ScreenUtil sdUtil = new ScreenUtil();
                Scene pathfindingScene = sdUtil.prodAndBindScene(pathfindingPane, primaryStage);
                pathfindingScene.getStylesheets().addAll("Stylesheet.css");
                primaryStage.setScene(pathfindingScene);
                pathCont = pathfindingLoader.getController();
                pathCont.setListen(primaryStage);
                pathCont.getNearType("EXIT");
                window.close();
            }
        });

        ////gets nearest food
        Button foodButton = new Button("Food");
        foodButton.setStyle("-fx-font-size: 15pt; -fx-border-color: #F6BD38; -fx-base: #012D5A;");
        foodButton.setMaxWidth(200.0);
        foodButton.setOnAction(new javafx.event.EventHandler<ActionEvent>() {
            private PathfindingCont pathCont = new PathfindingCont();
            @Override
            public void handle(ActionEvent e) {
                User user = User.getUser();
                Stage primaryStage = user.getPrimaryStage();
                FXMLLoader pathfindingLoader;
                if(runningFromIntelliJ()) {
                    pathfindingLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/pathfindingView.fxml"));
                } else{
                    pathfindingLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/pathfindingView.fxml"));
                }
                Parent pathfindingPane = null;
                try{
                    pathfindingPane = pathfindingLoader.load();
                }
                catch(IOException io){
                    io.printStackTrace();
                }
                ScreenUtil sdUtil = new ScreenUtil();
                Scene pathfindingScene = sdUtil.prodAndBindScene(pathfindingPane, primaryStage);
                pathfindingScene.getStylesheets().addAll("Stylesheet.css");
                primaryStage.setScene(pathfindingScene);
                pathCont = pathfindingLoader.getController();
                pathCont.setListen(primaryStage);
                pathCont.getNearType("RETL");
                window.close();
            }
        });

        //gets nearest info
        Button infoButton = new Button("Info Desk");
        infoButton.setStyle("-fx-font-size: 15pt; -fx-border-color: #F6BD38; -fx-base: #012D5A;");
        infoButton.setMaxWidth(200.0);
        infoButton.setOnAction(new javafx.event.EventHandler<ActionEvent>() {
            private PathfindingCont pathCont = new PathfindingCont();
            @Override
            public void handle(ActionEvent e) {
                User user = User.getUser();
                Stage primaryStage = user.getPrimaryStage();
                FXMLLoader pathfindingLoader;
                if(runningFromIntelliJ()) {
                    pathfindingLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/pathfindingView.fxml"));
                } else{
                    pathfindingLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/pathfindingView.fxml"));
                }
                Parent pathfindingPane = null;
                try{
                    pathfindingPane = pathfindingLoader.load();
                }
                catch(IOException io){
                    io.printStackTrace();
                }
                ScreenUtil sdUtil = new ScreenUtil();
                Scene pathfindingScene = sdUtil.prodAndBindScene(pathfindingPane, primaryStage);
                pathfindingScene.getStylesheets().addAll("Stylesheet.css");
                primaryStage.setScene(pathfindingScene);
                pathCont = pathfindingLoader.getController();
                pathCont.setListen(primaryStage);
                pathCont.getNearType("INFO");
                window.close();
            }
        });

        //gets nearest elev
        Button elevButton = new Button("Elevator");
        elevButton.setStyle("-fx-font-size: 15pt; -fx-border-color: #F6BD38; -fx-base: #012D5A;");
        elevButton.setMaxWidth(200.0);
        elevButton.setOnAction(new javafx.event.EventHandler<ActionEvent>() {
            private PathfindingCont pathCont = new PathfindingCont();
            @Override
            public void handle(ActionEvent e) {
                User user = User.getUser();
                Stage primaryStage = user.getPrimaryStage();
                FXMLLoader pathfindingLoader;
                if(runningFromIntelliJ()) {
                    pathfindingLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/pathfindingView.fxml"));
                } else{
                    pathfindingLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/pathfindingView.fxml"));
                }
                Parent pathfindingPane = null;
                try{
                    pathfindingPane = pathfindingLoader.load();
                }
                catch(IOException io){
                    io.printStackTrace();
                }
                ScreenUtil sdUtil = new ScreenUtil();
                Scene pathfindingScene = sdUtil.prodAndBindScene(pathfindingPane, primaryStage);
                pathfindingScene.getStylesheets().addAll("Stylesheet.css", "StylePath.css");
                primaryStage.setScene(pathfindingScene);
                pathCont = pathfindingLoader.getController();
                pathCont.setListen(primaryStage);
                pathCont.getNearType("ELEV");
                window.close();
            }
        });

        //gets nearest stair
        Button stairButton = new Button("Stairs");
        stairButton.setStyle("-fx-font-size: 15pt; -fx-border-color: #F6BD38; -fx-base: #012D5A;");
        stairButton.setMaxWidth(200.0);
        stairButton.setOnAction(new javafx.event.EventHandler<ActionEvent>() {
            private PathfindingCont pathCont = new PathfindingCont();
            @Override
            public void handle(ActionEvent e) {
                User user = User.getUser();
                Stage primaryStage = user.getPrimaryStage();
                FXMLLoader pathfindingLoader;
                if(runningFromIntelliJ()) {
                    pathfindingLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/pathfindingView.fxml"));
                } else{
                    pathfindingLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/pathfindingView.fxml"));
                }
                Parent pathfindingPane = null;
                try{
                    pathfindingPane = pathfindingLoader.load();
                }
                catch(IOException io){
                    io.printStackTrace();
                }
                ScreenUtil sdUtil = new ScreenUtil();
                Scene pathfindingScene = sdUtil.prodAndBindScene(pathfindingPane, primaryStage);
                pathfindingScene.getStylesheets().addAll("Stylesheet.css", "StylePath.css");
                primaryStage.setScene(pathfindingScene);
                pathCont = pathfindingLoader.getController();
                pathCont.setListen(primaryStage);
                pathCont.getNearType("STAI");
                window.close();
            }
        });

        // Formatting for the admin login
        GridPane grid = new GridPane();
        grid.setHgap(5);
        grid.setVgap(3);
        grid.setAlignment(Pos.CENTER); // Centers the grid in the VBox


        // Filling a Vbox that holds the main message the grid and the submit button
        vb.getChildren().addAll(label, grid, aPane);
        vb.getChildren().addAll(bathButton, exitButton, foodButton, infoButton, elevButton, stairButton);

        // Creates the scene and sets the scene to the window
        Scene scene = new Scene(layout);
        window.setScene(scene);

        // Means the window has to be closed before the user can interact with other windows again
        window.showAndWait();
    }

    public void goToPathfinding(ActionEvent e){

    }



    /**
     * Creates and formats an HBox
     * @return a new HBox
     */
    public static HBox addHBox() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #336699;");
        return hbox;
    }

    /**
     * Creates and formats a VBox
     * @return a new VBox
     */
    public static VBox addVBox() {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);
        return vbox;
    }

    /**
     * Creates a new AnchorPane
     * @return a new AnchorPane
     */
    public static AnchorPane addAnchorPane() {
        AnchorPane anchorpane = new AnchorPane();
        return anchorpane;
    }
}
