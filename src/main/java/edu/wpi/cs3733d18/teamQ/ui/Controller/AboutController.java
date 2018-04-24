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
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class AboutController {

   public static void displayAboutPage() {
       Stage window = new Stage();

       Insets noPadding = new Insets(0, 0, 0, 0);
       // Format the window
       window.setTitle("About Page");
       Label label = new Label("About: Our Application");
//       label.setPadding(noPadding);
       Label label2 = new Label("Credit");
//       label2.setPadding(noPadding);
       label.setStyle("-fx-font-size: 27pt;");
       label2.setStyle("-fx-font-size: 27pt;");
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

       Label teamName = new Label("The Quenched Quinotaurs:");
//       teamName.setPadding(noPadding);
       teamName.setStyle("-fx-font-size: 15pt;");
       Label teamMembers = new Label("Lead Software Engineer: Dan Duff");
//       teamMembers.setPadding(noPadding);
       Label teamMembers1 = new Label("Algorithms Lead: Nugzar Chkhaidze");
//       teamMembers1.setPadding(noPadding);
       Label teamMembers2 = new Label("Database Lead - James Kenney");
//       teamMembers2.setPadding(noPadding);
       Label teamMembers3 = new Label("Assistant Lead Software Engineers:");
//       teamMembers3.setPadding(noPadding);
       Label teamMembers4 = new Label("Norman Delorey, Andrew DeRusha, Jesse d'Almeida, Aleksander Ibro");
       Label teamMembers5 = new Label("Scrum Master: Maggie Goodwin");
       Label teamMembers6 = new Label("Product Owner: Andrew DeRusha");
       Label teamMembers7 = new Label("Test Engineer: Norman Delorey");
       Label teamMembers8 = new Label("Documentation Analyst: Sarah Armstrong");
       Label teamMembers9 = new Label("Software Engineers: Krysta Murdy");
       Label teamMembers10 = new Label("Team Coach: Andrew Rottier");
       Label teamMembers11 = new Label("Professor Wilson Wong");
       Label teamMembers12 = new Label("Worcester Polytechnic Institute Computer Science Department");

       //teamMembers.setStyle("-fx-font-size: 12pt;");
       teamMembers.setTextAlignment(TextAlignment.LEFT);

       Label thanksTitle = new Label("Special Thanks:");
       thanksTitle.setStyle("-fx-font-size: 15pt;");

       Label thanksText = new Label("Thank you to Brigham and Women’s Faulkner Hospital and Andrew Shinn for their time and input\n" +
               "on this application.");
       thanksText.setStyle("-fx-font-size: 12pt;");
       thanksText.setTextAlignment(TextAlignment.CENTER);

       Label copyState = new Label("The Brigham & Women’s Hospital maps and data used in this application are copyrighted and provided for the sole\n" +
               "use of educational purposes.");
       copyState.setStyle("-fx-font-size: 10pt;");
       copyState.setTextAlignment(TextAlignment.CENTER);

       Label apiName = new Label("Team APIs Used:");
       apiName.setStyle("-fx-font-size: 15pt;");
       Label apiExName = new Label("External APIs Used:");
       apiExName.setStyle("-fx-font-size: 15pt;");
       Label teamApiUsed = new Label("Team O's Gift service request API");
       Label apiUsed = new Label("Yahoo Weather Java API");
       Label apiUsed1 = new Label("Jfoenix");
       Label apiUsed2 = new Label("Javax Mail");
       Label apiUsed3 = new Label("Activation");
       Label apiUsed4 = new Label("Junit");
       Label apiUsed5 = new Label("Fuzzywuzzy");
       Label apiUsed6 = new Label("Derby");
       Label apiUsed7 = new Label("Derbyclient");
       Label apiUsed8 = new Label("Derbytools");
       Label apiUsed9 = new Label("Controlsfx");
       Label apiUsed10 = new Label("Hamcrest-core");
       Label apiUsed11 = new Label("Jbcrypt");
       Label apiUsed12 = new Label("Slf4j-api");
       Label apiUsed13 = new Label("Slf4j-simple");
       //apiUsed.setStyle("-fx-font-size: 12pt;");
       //teamApiUsed.setStyle("-fx-font-size: 12pt;");
       apiUsed.setTextAlignment(TextAlignment.LEFT);

       // Formatting for the admin login
       GridPane grid = new GridPane();
       grid.setHgap(20);
       grid.setVgap(12);
       grid.setAlignment(Pos.TOP_CENTER); // Centers the grid in the VBox
//       grid.setPadding(noPadding);
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
       grid.add(reg2, 0, 0);
       grid.add(label, 0,0);
       grid.add(teamName, 0, 1);
       grid.add(teamMembers, 0, 2);
       grid.add(teamMembers1, 0, 3);
       grid.add(teamMembers2, 0, 4);
       grid.add(teamMembers3, 0, 5);
       grid.add(teamMembers4, 0, 6);
       grid.add(teamMembers5, 0, 7);
       grid.add(teamMembers6, 0, 8);
       grid.add(teamMembers7, 0, 9);
       grid.add(teamMembers8, 0, 10);
       grid.add(teamMembers9, 0, 11);
       grid.add(teamMembers10, 0, 12);
       grid.add(teamMembers11, 0, 13);
       grid.add(teamMembers12, 0, 14);

       //grid.add(reg3, 0, 15);
       //grid.add(reg4, 0, 16);
       grid.add(thanksTitle, 0, 18);
       grid.setColumnSpan(thanksTitle,2);
       GridPane.setHalignment(thanksTitle, HPos.CENTER);
       grid.add(thanksText, 0, 19);
       grid.setColumnSpan(thanksText,2);
       GridPane.setHalignment(thanksText, HPos.CENTER);

       //grid.add(reg5, 0, 19);
       //grid.add(reg6, 0, 20);
       grid.add(copyState, 0, 21);
       grid.setColumnSpan(copyState,2);
       GridPane.setHalignment(copyState, HPos.CENTER);

       // Filling a grid pane that holds the username and password fields
       grid.add(label2, 1, 0);
       grid.add(apiName, 1, 1);
       grid.add(teamApiUsed, 1, 2);
       grid.add(apiExName, 1, 3);
       grid.add(apiUsed, 1, 4);
       grid.add(apiUsed1, 1, 5);
       grid.add(apiUsed2, 1, 6);
       grid.add(apiUsed3, 1, 7);
       grid.add(apiUsed4, 1, 8);
       grid.add(apiUsed5, 1, 9);
       grid.add(apiUsed6, 1, 10);
       grid.add(apiUsed7, 1, 11);
       grid.add(apiUsed8, 1, 12);
       grid.add(apiUsed9, 1, 13);
       grid.add(apiUsed10, 1, 14);
       grid.add(apiUsed11, 1, 15);
       grid.add(apiUsed12, 1, 16);
       grid.add(apiUsed13, 1, 17);

       // Filling a Vbox that holds the main message the grid and the submit button
       vb.getChildren().addAll(grid, aPane);

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

