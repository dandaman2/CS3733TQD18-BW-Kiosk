package edu.wpi.cs3733d18.teamQ.ui.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class CreditController {
    public static void displayCreditPage() {
        Stage window = new Stage();

        //JFXButton creditsButton = new JFXButton();

        //creditsButton.setOnAction(e -> AboutController.displayCreditPage());

        // Format the window
        window.setTitle("Credits Page");
        Label label = new Label("Credit:");
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

        Label apiName = new Label("APIs Used:");
        apiName.setStyle("-fx-font-size: 15pt;");
        Label apiUsed = new Label("Team O's Gift service request API\n" +
                "Face recognition API\n" );
        apiUsed.setStyle("-fx-font-size: 12pt;");
        apiUsed.setTextAlignment(TextAlignment.CENTER);

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
        grid.add(apiName, 0, 1);
        grid.add(apiUsed, 0, 2);

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
