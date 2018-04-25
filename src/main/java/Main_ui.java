//package edu.wpi.cs3733d18.teamQ;

import edu.wpi.cs3733d18.teamQ.manageDB.DatabaseSystem;
import edu.wpi.cs3733d18.teamQ.ui.Controller.ScreenUtil;
import edu.wpi.cs3733d18.teamQ.ui.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static edu.wpi.cs3733d18.teamQ.manageDB.DatabaseSystem.*;

public class Main_ui extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{

        //create user
        User user = User.getUser();
        user.setPrimaryStage(primaryStage);
        user.initLoadMaps();
        user.setNodes(getNodes());
        user.setEdges(getEdges());
        //user.startTimer();

        //Loader broken-down to allow static controller distribution
        FXMLLoader welcomeLoader;
        if(runningFromIntelliJ()) {
            welcomeLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/WelcomeScreen.fxml"));
        } else{
            welcomeLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/WelcomeScreen.fxml"));
        }
        Parent welcomePane = welcomeLoader.load();
        Scene welcomeScene = new Scene(welcomePane,722,604);

        welcomeScene.getStylesheets().addAll("Stylesheet.css", "StyleMenus.css");

        //preparing the starting stage
        primaryStage.setMinWidth(735);
        primaryStage.setMinHeight(645);
        primaryStage.setTitle("Iteration 3 Kiosk");
        primaryStage.setScene(welcomeScene);

        //primaryStage.setFullScreen(true);
        primaryStage.setMaximized(true);

        user.setPrimaryStage(primaryStage);
        user.setWelcomeScene(welcomeScene);
        primaryStage.show();

        setUpTimer();

    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        DatabaseSystem.initializeDb();
//        Node nodes = getNode("BCONF00102");
//        System.out.println(nodes);
//        editNodeType(nodes.get(0), "CHANGED");
        //ArrayList<Edge> edges= getEdges();
        //ArrayList<Node> nodes= getNodes();
//        System.out.println(edges.get(0).getDistance() + edges.get(0).getEdgeID());
//        System.out.println(nodes.get(0).getNodeID());
        //exportNodeToCSV();
        //exportEdgeToCSV();

        launch(args);

        User user = User.getUser();
        user.saveToDB();
    }


    //ScreenUtil object for resizing of loading images
    private ScreenUtil sdUtil = new ScreenUtil();
    /**
     * initializes the timer
     */
    private void setUpTimer(){

        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                User user = User.getUser();

                System.out.println("thread is running...");
                double xMouse = MouseInfo.getPointerInfo().getLocation().getX();
                double yMouse = MouseInfo.getPointerInfo().getLocation().getY();

                double xMouseNew;
                double yMouseNew;

                int time = user.getTime();
                while (time > 0) {
                    xMouseNew = MouseInfo.getPointerInfo().getLocation().getX();
                    yMouseNew = MouseInfo.getPointerInfo().getLocation().getY();

                    if (xMouse != xMouseNew || yMouse != yMouseNew) {
                        time = user.getTime();

                        xMouse = MouseInfo.getPointerInfo().getLocation().getX();
                        yMouse = MouseInfo.getPointerInfo().getLocation().getY();
                        continue;
                    }

                    time--;
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(time);
                }


                Platform.runLater(new Runnable(){
                    @Override
                    public void run () {
                        User user = User.getUser();
                        user.setLevelAccess(0);
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
                        Parent welcomePane = null;
                        try {
                            welcomePane = welcomeLoader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Stage primaryStage = user.getPrimaryStage();
                        Scene welcomeScene = sdUtil.prodAndBindScene(welcomePane, primaryStage);
                        welcomeScene.getStylesheets().addAll("Stylesheet.css", "StyleMenus.css");
                        primaryStage.setScene(welcomeScene);
                    }
                });
            }
        };

        Timer timer = new Timer("MyTimer");//create a new Timer
        timer.scheduleAtFixedRate(timerTask, 10, 100);

    }
}
