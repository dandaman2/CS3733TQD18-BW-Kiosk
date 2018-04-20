package edu.wpi.cs3733d18.teamQ.ui.Controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HoursController {

    /**
     * Creates a formatted popup window that can be formatted differently based on what the title is
     */
    public static void display(){
        Stage window = new Stage();

        // Format the window
        window.setTitle("Hospital Hours");
        Label label = new Label("Hospital Hours");
        label.setStyle("-fx-font-size: 27pt;");
        BorderPane layout = new BorderPane(); // Implements a Border Pane (splits screen into 5 sections)
        HBox hb = addHBox();
        hb.setStyle("-fx-background-color: #012D5A;");
        layout.setTop(hb);
        VBox vb = addVBox();
        layout.setCenter(vb);
        vb.setAlignment(Pos.CENTER);
        AnchorPane aPane = addAnchorPane();
        Label disclaim = new Label("\"Please note, any patient food that is not prepared by our food services department must be cleared with the patient’s nurse.\"");
        disclaim.setStyle("-fx-font-size: 15pt;");
        aPane.getChildren().add(disclaim);
        disclaim.setAlignment(Pos.CENTER);
        HBox hb2 = addHBox();
        hb2.setStyle("-fx-background-color: #012D5A;");
        layout.setBottom(hb2);

        Label caf = new Label("Cafeteria");
        caf.setStyle("-fx-font-size: 15pt;");
        Label cafHrs = new Label("~Monday through Friday~ \n" +
                "Breakfast/Morning: 6:15 a.m. - 10:30 a.m. \n" +
                "Lunch: 11:15 a.m. - 2:30 p.m. \n" +
                "Snack/Dinner: 2:30 p.m. - 8 p.m. \n" +
                "Late Night: 8 p.m. - 11:30 p.m. \n" +
                "\n" +
                "~Saturday and Sunday~ \n" +
                "Breakfast/Morning: 6:15 a.m. - 10:30 a.m. \n" +
                "Lunch: 11:15 a.m. - 2:30 p.m. \n" +
                "Snack/Dinner: 3 p.m. - 8 p.m. \n" +
                "Late Night: 8 p.m. - 11:30 p.m. \n" +
                "\n" +
                "~Café X-Press Hours (Weekdays Only)~\n" +
                "Gourmet My Way: 11 a.m. - 3 p.m. \n" +
                "Pizza and Grinders: 11 a.m. – 7:30 p.m.");
        cafHrs.setStyle("-fx-font-size: 10pt;");

        Label cof = new Label("Coffee Connections at 45");
        cof.setStyle("-fx-font-size: 15pt;");
        Label cofHrs = new Label("Monday through Friday: 6:30 a.m. - 6 p.m.");
        cofHrs.setStyle("-fx-font-size: 10pt;");

        Label vis = new Label("Visiting Hours");
        vis.setStyle("-fx-font-size: 15pt;");
        Label visHrs = new Label("\"In respecting the care and comfort needs of \n" +
                "our patients, visitors are generally welcome \n" +
                "between 1 p.m. and 9 p.m. Under certain \n" +
                "circumstances a designated support person \n" +
                "may have overnight visitation on some nursing \n" +
                "units if it does not interfere with the medical \n" +
                "care and treatment of our patients. A support \n" +
                "person may be a spouse, adult child, parent,\n" +
                " close relative, friend, domestic partner, and\n" +
                " both different sex and same sex significant others.\" ");
        visHrs.setStyle("-fx-font-size: 10pt;");

        Label auBo = new Label("Au Bon Pain");
        auBo.setStyle("-fx-font-size: 15pt;");
        Label auBoHrs = new Label("It is open 24 hours a day. \n" +
                "The staff can be reached at (617) 739-6860.");
        auBoHrs.setStyle("-fx-font-size: 10pt;");

        Label oNat = new Label("O'Naturals");
        oNat.setStyle("-fx-font-size: 15pt;");
        Label oNatHrs = new Label("Monday through Friday: 6:30 a.m. - 7 p.m. \n" +
                "Saturday and Sunday: 9 a.m. to 3:30 p.m.\n" +
                "Staff can be reached at (617) 232-6200.");
        oNatHrs.setStyle("-fx-font-size: 10pt;");

        Label pats = new Label("Pat's Place");
        pats.setStyle("-fx-font-size: 15pt;");
        Label patsHrs = new Label("Monday through Friday: 6 a.m. - 5 p.m. \n" +
                "Saturdays, Sundays, and holidays: closed");
        patsHrs.setStyle("-fx-font-size: 10pt;");

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
        grid.add(vis, 0, 1);
        grid.add(visHrs, 0, 2);
        grid.add(caf, 1, 1);
        grid.add(cafHrs, 1, 2);
        grid.add(reg3, 0, 3);
        grid.add(reg4, 1, 3);
        grid.add(cof, 0, 4);
        grid.add(cofHrs, 0, 5);
        grid.add(auBo, 1, 4);
        grid.add(auBoHrs, 1, 5);
        grid.add(reg5, 0, 6);
        grid.add(reg6, 1, 6);
        grid.add(oNat, 0, 7);
        grid.add(oNatHrs, 0, 8);
        grid.add(pats, 1, 7);
        grid.add(patsHrs, 1, 8);
        grid.add(reg7, 0, 9);
        grid.add(reg8, 1, 9);

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
