//package edu.wpi.cs3733d18.teamQ;

import edu.wpi.cs3733d18.teamQ.manageDB.DatabaseSystem;
import edu.wpi.cs3733d18.teamQ.ui.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
        primaryStage.show();

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


}
