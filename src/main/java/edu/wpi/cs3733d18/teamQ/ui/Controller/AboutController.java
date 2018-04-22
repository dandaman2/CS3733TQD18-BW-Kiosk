package edu.wpi.cs3733d18.teamQ.ui.Controller;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class AboutController {

   public static void displayAboutPage() {
       Stage window = new Stage();

       // Format the window
       window.setTitle("About Page");
       Label label = new Label("About: Our Application");
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

       Label teamName = new Label("The Quenched Quinotaurs:");
       teamName.setStyle("-fx-font-size: 15pt;");
       Label teamMembers = new Label("Lead Software Engineer: Dan Duff\n" +
               "Algorithms Lead: Nugzar Chkhaidze\n" +
               "Database Lead - James Kenney\n" +
               "Assistant Lead Software Engineers:\n" +
               "Norman Delorey, Andrew DeRusha, Jesse d'Almeida, Aleksander Ibro\n" +
               "Project Manager: Aleksander Ibro\n" +
               "Scrum Master: Maggie Goodwin\n" +
               "Product Owner: Andrew DeRusha\n" +
               "Test Engineer: Norman Delorey\n" +
               "Documentation Analyst: Sarah Armstrong\n" +
               "Software Engineers: Krysta Murdy\n" +
               "Team Coach: Andrew Rottier\n" +
               "Professor Wilson Wong\n" +
               "Worcester Polytechnic Institute Computer Science Department");
       teamMembers.setStyle("-fx-font-size: 12pt;");
       teamMembers.setTextAlignment(TextAlignment.CENTER);

       Label thanksTitle = new Label("Special Thanks:");
       thanksTitle.setStyle("-fx-font-size: 15pt;");
       Label thanksText = new Label("Thank you to Brigham and Women’s Faulkner Hospital and Andrew\n" +
               "Shinn for their time and input on this application.");
       thanksText.setStyle("-fx-font-size: 12pt;");
       thanksText.setTextAlignment(TextAlignment.CENTER);

       Label copyState = new Label("The Brigham & Women’s Hospital maps and data used in this application\n" +
               "are copyrighted and provided for the sole use of\n" +
               "educational purposes");
       copyState.setStyle("-fx-font-size: 12pt;");
       copyState.setTextAlignment(TextAlignment.CENTER);


       // Formatting for the admin login
       GridPane grid = new GridPane();
       grid.setHgap(10);
       grid.setVgap(12);
       grid.setAlignment(Pos.CENTER); // Centers the grid in the VBox
       Region reg1 = new Region();
       Region reg2 = new Region();
       Region reg3 = new Region();
       Region reg4 = new Region();
       Region reg5 = new Region();
       Region reg6 = new Region();
       Region reg7 = new Region();
       Region reg8 = new Region();

       // Filling a grid pane that holds the username and password fields
       grid.add(reg1, 0, 0);
       grid.add(reg2, 1, 0);
       grid.add(teamName, 0, 1);
       grid.add(teamMembers, 0, 2);

       grid.add(reg3, 0, 3);
       grid.add(reg4, 1, 3);
       grid.add(thanksTitle, 0, 4);
       grid.add(thanksText, 0, 5);

       grid.add(reg5, 0, 6);
       grid.add(reg6, 1, 6);
       grid.add(reg7, 1, 7);
       grid.add(reg8, 1, 7);
       grid.add(copyState, 0, 7);

       // Filling a Vbox that holds the main message the grid and the submit button
       vb.getChildren().addAll(label, grid, aPane);


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

