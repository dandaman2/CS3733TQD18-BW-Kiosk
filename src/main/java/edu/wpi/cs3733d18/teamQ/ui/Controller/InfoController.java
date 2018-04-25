package edu.wpi.cs3733d18.teamQ.ui.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class InfoController {

        public static void displayEditInfoPage() {
            Stage window = new Stage();

            Insets noPadding = new Insets(0, 0, 0, 0);
            // Format the window
            window.setTitle("Edit Map Info");
            Label label = new Label("How To Use Edit Map Screen");
            label.setFont(Font.font("Georgia", 30));
            BorderPane layout = new BorderPane(); // Implements a Border Pane (splits screen into 5 sections)
            HBox hb = addHBox();
            hb.setStyle("-fx-background-color: #012D5A;");
            layout.setTop(hb);
            VBox vb = addVBox();
            layout.setCenter(vb);
            vb.setAlignment(Pos.TOP_CENTER);
            AnchorPane aPane = addAnchorPane();
            HBox hb2 = addHBox();
            hb2.setStyle("-fx-background-color: #012D5A;");
            layout.setBottom(hb2);

            Label pathDirection = new Label("Use this screen to edit the map nodes and edges, on both 2D and 3D maps. \n\n" +
                                            "Add a new node by clicking anywhere on either map.\n\n"+
                                            "Can edit by click on and dragging nodes and edges on both maps.\n\n"+
                                            "Can also edit the maps using the fields in the side bar.\n\n"+
                                            "Can toggle between the 2D and 3D map with button in top left corner.\n\n"+
                                            "Change the search algorithm used to find the shortest path in the drop down. \n\n" +
                                            "Change the floor in the drop down. \n\n" +
                                            "Edit the timeout limit for the application to return to the welcome screen in the drop down.\n\n"+
                                            "Can view nodes that are most popularly used by hitting heat map switch.\n\n"+
                                            "Press the home button to return to the admin home screen.\n");
            pathDirection.setFont(Font.font("Georgia", 12));


            // Filling a Vbox that holds the main message the grid and the submit button
            vb.getChildren().addAll(label, pathDirection, aPane);

            // Creates the scene and sets the scene to the window
            Scene scene = new Scene(layout);
            window.setScene(scene);

            // Means the window has to be closed before the user can interact with other windows again
            window.showAndWait();
        }

    public static void displayPathInfoPage() {
        Stage window = new Stage();

        Insets noPadding = new Insets(0, 0, 0, 0);
        // Format the window
        window.setTitle("Directions Page Info");
        Label label = new Label("How To Use Directions Screen");
        label.setFont(Font.font("Georgia", 30));
        BorderPane layout = new BorderPane(); // Implements a Border Pane (splits screen into 5 sections)
        HBox hb = addHBox();
        hb.setStyle("-fx-background-color: #012D5A;");
        layout.setTop(hb);
        VBox vb = addVBox();
        layout.setCenter(vb);
        vb.setAlignment(Pos.TOP_CENTER);
        AnchorPane aPane = addAnchorPane();
        HBox hb2 = addHBox();
        hb2.setStyle("-fx-background-color: #012D5A;");
        layout.setBottom(hb2);

        Label pathDirection = new Label("Use this screen to edit the map nodes and edges, on both 2D and 3D maps. \n\n" +
                "Add a new node by clicking anywhere on either map.\n\n"+
                "Can edit by click on and dragging nodes and edges on both maps.\n\n"+
                "Can also edit the maps using the fields in the side bar.\n\n"+
                "Can toggle between the 2D and 3D map with button in top left corner.\n\n"+
                "Change the search algorithm used to find the shortest path in the drop down. \n\n" +
                "Change the floor in the drop down. \n\n" +
                "Edit the timeout limit for the application to return to the welcome screen in the drop down.\n\n"+
                "Can view nodes that are most popularly used by hitting heat map switch.\n\n"+
                "Press the home button to return to the admin home screen.\n");
        pathDirection.setFont(Font.font("Georgia", 12));


        // Filling a Vbox that holds the main message the grid and the submit button
        vb.getChildren().addAll(label, pathDirection, aPane);

        // Creates the scene and sets the scene to the window
        Scene scene = new Scene(layout);
        window.setScene(scene);

        // Means the window has to be closed before the user can interact with other windows again
        window.showAndWait();
    }

    public static void displayReqInfoPage() {
        Stage window = new Stage();

        Insets noPadding = new Insets(0, 0, 0, 0);
        // Format the window
        window.setTitle("Request Screen Info");
        Label label = new Label("How To Use Request Screen");
        label.setFont(Font.font("Georgia", 30));
        BorderPane layout = new BorderPane(); // Implements a Border Pane (splits screen into 5 sections)
        HBox hb = addHBox();
        hb.setStyle("-fx-background-color: #012D5A;");
        layout.setTop(hb);
        VBox vb = addVBox();
        layout.setCenter(vb);
        vb.setAlignment(Pos.TOP_CENTER);
        AnchorPane aPane = addAnchorPane();
        HBox hb2 = addHBox();
        hb2.setStyle("-fx-background-color: #012D5A;");
        layout.setBottom(hb2);

        Label pathDirection = new Label("Use this screen to edit the map nodes and edges, on both 2D and 3D maps. \n\n" +
                "Add a new node by clicking anywhere on either map.\n\n"+
                "Can edit by click on and dragging nodes and edges on both maps.\n\n"+
                "Can also edit the maps using the fields in the side bar.\n\n"+
                "Can toggle between the 2D and 3D map with button in top left corner.\n\n"+
                "Change the search algorithm used to find the shortest path in the drop down. \n\n" +
                "Change the floor in the drop down. \n\n" +
                "Edit the timeout limit for the application to return to the welcome screen in the drop down.\n\n"+
                "Can view nodes that are most popularly used by hitting heat map switch.\n\n"+
                "Press the home button to return to the admin home screen.\n");
        pathDirection.setFont(Font.font("Georgia", 12));


        // Filling a Vbox that holds the main message the grid and the submit button
        vb.getChildren().addAll(label, pathDirection, aPane);

        // Creates the scene and sets the scene to the window
        Scene scene = new Scene(layout);
        window.setScene(scene);

        // Means the window has to be closed before the user can interact with other windows again
        window.showAndWait();
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
