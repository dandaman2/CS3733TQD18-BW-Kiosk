package edu.wpi.cs3733d18.teamQ.ui.Controller;
import com.github.fedy2.weather.YahooWeatherService;
import com.github.fedy2.weather.data.Channel;
import com.github.fedy2.weather.data.unit.DegreeUnit;
import edu.wpi.cs3733d18.teamQ.pathfinding.Edge;
import edu.wpi.cs3733d18.teamQ.pathfinding.Node;
import edu.wpi.cs3733d18.teamQ.ui.Admin_Login.FaceRecognition;
import edu.wpi.cs3733d18.teamQ.ui.Employee;
import edu.wpi.cs3733d18.teamQ.ui.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;

import static edu.wpi.cs3733d18.teamQ.manageDB.DatabaseSystem.*;

public class AlertBox {

    /**
     * Creates a formatted popup window that can be formatted differently based on what the title is
     * @param title The name of the window being created
     * @param message The main message that will be displayed on the window
     */
    public static void display(String title, String message) {
        Stage window = new Stage();

        // Keeps users from interacting with other windows before resolving this one
        window.initModality(Modality.APPLICATION_MODAL);

        //Set window title and window size
        window.setTitle(title);
        window.setMinWidth(500);
        window.setMinHeight(300);

        Label label = new Label();
        label.setText(message); // Label that displays the inputted message
        label.setStyle("-fx-font-size: 15pt;");

        // Formatting the window
        BorderPane layout = new BorderPane(); // Implements a Border Pane (splits screen into 5 sections)
        AnchorPane aPane = addAnchorPane();

        // Only runs for the Admin Login
        if (title.equals("Admin Login")) {
            HBox hb = addHBox();
            hb.setStyle("-fx-background-color: #012D5A;"); // Makes the top blue
            layout.setTop(hb);
            aPane.setStyle("-fx-background-color: #012D5A;"); // Makes the top blue
            layout.setBottom(aPane); // Sets the top of the window to an anchor pane
            VBox midVbox = new VBox(10);
            layout.setCenter(midVbox); // Sets the center of the window to a Vbox
            midVbox.setAlignment(Pos.CENTER);

            Label lbluName = new Label("Username:");
            lbluName.setStyle("-fx-font-size: 15pt;");
            TextField tfuName = new TextField();

            Label lblPwd = new Label("Password:");
            lblPwd.setStyle("-fx-font-size: 15pt;");
            PasswordField pfPwd = new PasswordField();

            Label errorlbl = new Label();
            errorlbl.setText("Incorrect Username/Password");
            errorlbl.setTextFill(Paint.valueOf("FF0000"));
            errorlbl.setVisible(false);

            Region spacing = new Region();
            spacing.setPrefHeight(2);
            Button btnFace = new Button("Alternative Login");
            btnFace.setStyle("-fx-font-size: 15pt;");
            btnFace.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    User user = User.getUser();
                    Employee userToLogIn = FaceRecognition.getInstance().compareFaces();
                    if (userToLogIn!=null){
                        user.setCurrentUser(userToLogIn);
                        if(userToLogIn.getIsAdmin().equals("2")){
                            user.setLevelAccess(2);
                        }else {
                            user.setLevelAccess(1);
                        }
                    }else {
                        user.setCurrentUser(null);
                        errorlbl.setText("Face not recognized in the system");
                        errorlbl.setVisible(true);
                    }
                    Stage primaryStage = user.getPrimaryStage();
                    FXMLLoader requestLoader;
                    if (runningFromIntelliJ()) {
                        requestLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/AdminHomeScreen.fxml"));
                    } else {
                        requestLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/AdminHomeScreen.fxml"));
                    }
                    Parent requestPane = null;
                    try {
                        requestPane = requestLoader.load();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    if (user.getLevelAccess() >= 1) {
                        ScreenUtil sdUtil = new ScreenUtil();
                        Scene requestScene = sdUtil.prodAndBindScene(requestPane, primaryStage);
                        requestScene.getStylesheets().addAll("Stylesheet.css", "StyleMenus.css");
                        primaryStage.setScene(requestScene);
                        window.close();
                    }
                }
            });
            // Buttons and text fields specific to the admin sign in
            Button btnSubmit = new Button("Log In");
            btnSubmit.setStyle("-fx-font-size: 15pt;");
            btnSubmit.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    String userName = tfuName.getText();
                    String password = pfPwd.getText();
                    FXMLLoader editScreen;
                    User user = User.getUser();
                    if (isAdmin(userName, password)) {
                        user.setLevelAccess(2);
                    } else if (isEmployee(userName, password)) {
                        user.setLevelAccess(1);
                    } else {
                        user.setCurrentUser(null);
                        errorlbl.setText("Incorrect Username/Password");
                        errorlbl.setVisible(true);
                    }
                    Stage primaryStage = user.getPrimaryStage();
                    FXMLLoader requestLoader;
                    if (runningFromIntelliJ()) {
                        requestLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/AdminHomeScreen.fxml"));
                    } else {
                        requestLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/AdminHomeScreen.fxml"));
                    }
                    Parent requestPane = null;
                    try {
                        requestPane = requestLoader.load();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    if (user.getLevelAccess() >= 1) {
                        ScreenUtil sdUtil = new ScreenUtil();
                        Scene requestScene = sdUtil.prodAndBindScene(requestPane, primaryStage);
                        requestScene.getStylesheets().addAll("Stylesheet.css", "StyleMenus.css");
                        primaryStage.setScene(requestScene);
                        window.close();
                    }
                }

                private boolean isAdmin(String userName, String password) {
                    Employee user = getEmployee(userName);
                    User.getUser().setCurrentUser(user);
                    return user != null && user.checkPassword(password) && user.getIsAdmin().equals("2");
                }

                private boolean isEmployee(String userName, String password) {
                    Employee user = getEmployee(userName);
                    User.getUser().setCurrentUser(user);
                    return user != null && user.checkPassword(password) && (user.getIsAdmin().equals("1"));
                }
            });

            // Formatting for the admin login
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(12);
            grid.setAlignment(Pos.CENTER); // Centers the grid in the Vbox
            btnSubmit.setAlignment(Pos.CENTER); // Centers the submit button in the Vbox

            // Filling a grid pane that holds the username and password fields
            grid.add(lbluName, 0, 1);
            grid.add(tfuName, 1, 1);
            grid.add(lblPwd, 0, 2);
            grid.add(pfPwd, 1, 2);

            // Filling a Vbox that holds the main message the grid and the submit button
            midVbox.getChildren().addAll(label, grid, errorlbl, btnSubmit, btnFace, spacing);
        }

        // Runs for Add a Custom Edge AlertBox
        if (title.equals("Add a Custom Edge")) {
            HBox hb = addHBox();
            hb.setStyle("-fx-background-color: #012D5A;"); // Makes the top blue
            layout.setTop(hb);
            aPane.setStyle("-fx-background-color: #012D5A;"); // Makes the top blue
            layout.setBottom(aPane); // Sets the top of the window to an anchor pane
            VBox midVbox = new VBox(10);
            layout.setCenter(midVbox); // Sets the center of the window to a Vbox
            midVbox.setAlignment(Pos.CENTER);

            window.setMinWidth(700);
            window.setMinHeight(125);
            // Buttons and text fields specific edge additions
            Button btnSubmit = new Button("Add Edge");
            btnSubmit.setStyle("-fx-font-size: 15pt;");
            Label lblnode1 = new Label("Node 1:");
            lblnode1.setStyle("-fx-font-size: 15pt;");
            TextField node1txt = new TextField();

            Label lblnode2 = new Label("Node 2:");
            lblnode2.setStyle("-fx-font-size: 15pt;");
            TextField node2txt = new TextField();

            Region spacing = new Region();
            spacing.setPrefHeight(2);

            node1txt.setPrefWidth(500);
            node2txt.setPrefWidth(500);

            // Formatting for adding edges
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(12);
            grid.setAlignment(Pos.CENTER); // Centers the grid in the Vbox
            btnSubmit.setAlignment(Pos.CENTER); // Centers the submit button in the Vbox
            grid.prefWidthProperty().bind(midVbox.widthProperty());

            // Filling a grid pane that holds the node fields
            grid.add(lblnode1, 0, 1);
            grid.add(node1txt, 1, 1);
            grid.add(lblnode2, 0, 2);
            grid.add(node2txt, 1, 2);

            // Filling a Vbox that holds the main message the grid and the submit button

            ArrayList<String> nodeIdentification = new ScreenUtil().getNameIdNodeAll(getNodes());

            TextFields.bindAutoCompletion(node1txt, nodeIdentification);
            TextFields.bindAutoCompletion(node2txt, nodeIdentification);

            btnSubmit.setOnAction(e -> {
                Node t1 = null;
                Node t2 = null;
                Boolean set1 = false;
                Boolean set2 = false;
                String[] values;

                values = node1txt.getText().split(",");
                String node1_id = values[0];

                values = node2txt.getText().split(",");
                String node2_id = values[0];

                for (Node nod : getNodes()) {
                    if (nod.getNameLong().equals(node1_id)) {
                        t1 = nod;
                        set1 = true;
                        System.out.println("match1");
                    }
                    if (nod.getNameLong().equals(node2_id)) {
                        t2 = nod;
                        set2 = true;
                        System.out.println("match2");
                    }
                    if ((set1 && set2) && (!t1.getNodeID().equals(t2.getNodeID()))) {
                        System.out.println("Match 3");
                        String id = t1.getNodeID() + "_" + t2.getNodeID();
                        Edge edge = new Edge(id, t1, t2, t1.calcDistance(t2),true);
                        addEdge(edge);
                        System.out.println("Edge added with id: " + id);
                        window.close();
                    }
                }
            });
            midVbox.getChildren().addAll(label, grid, btnSubmit, spacing);
        }

        // Runs for Emergency AlertBox
        if (title.equals("Emergency")) {
            //HBox hb = addHBox();
            //hb.setStyle("-fx-background-color: #012D5A;"); // Makes the top blue
            //layout.setTop(hb);
            aPane.setStyle("-fx-background-color: #012D5A;"); // Makes the top blue
            layout.setBottom(aPane); // Sets the top of the window to an anchor pane
            VBox midVbox = new VBox(10);
            layout.setCenter(midVbox); // Sets the center of the window to a Vbox
            midVbox.setAlignment(Pos.CENTER);

//            HBox bottom = addHBox();
//            bottom.setStyle("-fx-background-color: #FF0000;");
//            layout.setBottom(bottom);
//            HBox top = addHBox();
//            top.setPrefHeight(30);
//            top.setStyle("-fx-background-color: #FF0000;");
//            layout.setTop(top);

            StackPane pane = new StackPane();
            layout.setCenter(pane);
            pane.setStyle("-fx-background-color: #FF0000;");
            Image emergencyIcon;
            if(runningFromIntelliJ()) {
                emergencyIcon = new Image("/ButtonImages/Emergency_Icon5.png");
            } else{
                emergencyIcon = new Image("/ButtonImages/Emergency_Icon5.png");
            }
            ImageView emergencyView = new ImageView(emergencyIcon);
            emergencyView.setFitWidth(1400);
            emergencyView.setFitHeight(500);

            pane.getChildren().add(emergencyView);
            StackPane.setAlignment(emergencyView, Pos.CENTER);

//            anchorPane.getChildren().add(label);
//            label.setAlignment(Pos.CENTER);
//            label.setText(message);
//            label.setTextFill(Color.WHITE);
            label.setStyle("-fx-font-size: 70pt;");


            label.setPadding(new Insets(20, 10, 10, 10));
            label.setMaxWidth(Double.MAX_VALUE);
            AnchorPane.setLeftAnchor(label, 0.0);
            AnchorPane.setRightAnchor(label, 0.0);
            label.setAlignment(Pos.CENTER);

            midVbox.getChildren().addAll();

            window.setMinWidth(1500);
            window.setMinHeight(300);
            window.setMaxHeight(600);



        }

        if(title.equals("Weather")){
            HBox hb = addHBox();
            hb.setStyle("-fx-background-color: #012D5A;"); // Makes the top blue
            layout.setTop(hb);
            aPane.setStyle("-fx-background-color: #012D5A;"); // Makes the top blue
            layout.setBottom(aPane); // Sets the top of the window to an anchor pane
            VBox midVbox = new VBox(10);
            layout.setCenter(midVbox); // Sets the center of the window to a Vbox
            midVbox.setAlignment(Pos.CENTER);

            HBox bottom = addHBox();
            bottom.setStyle("-fx-background-color: #012D5A;");
            layout.setBottom(bottom);
            HBox top = addHBox();
            top.setStyle("-fx-background-color: #012D5A;");
            layout.setTop(top);

            AnchorPane anchorPane = new AnchorPane();
            layout.setCenter(anchorPane);
            anchorPane.setStyle("-fx-background-color: #dddddd;");
            anchorPane.getChildren().add(label);
            label.setAlignment(Pos.CENTER);
            label.setText(getWeather());
            label.setStyle("-fx-font-color: #FF0000;");
            label.setStyle("-fx-font-size: 36pt;");

            label.setPadding(new Insets(10, 10, 10, 10));
            label.setMaxWidth(Double.MAX_VALUE);
            AnchorPane.setLeftAnchor(label, 0.0);
            AnchorPane.setRightAnchor(label, 0.0);
            label.setAlignment(Pos.CENTER);

            midVbox.getChildren().addAll();

            window.setMinWidth(200);
            window.setMinHeight(100);
        }
        // Creates the scene and sets the scene to the window
        Scene scene = new Scene(layout);
        window.setScene(scene);

        // Means the window has to be closed before the user can interact with other windows again
        window.showAndWait();
    }

    private static String getWeather() {
        String conditions="";
        String temp="";
        try {
            YahooWeatherService service = new YahooWeatherService();
            Channel channel = service.getForecast("2367105", DegreeUnit.FAHRENHEIT);
            System.out.println(channel.getDescription());
            conditions = channel.getItem().getCondition().getText();
            temp = String.valueOf(channel.getItem().getCondition().getTemp());
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }
        return "Conditions: "+conditions+"\n"+"Temperature: "+temp+" F";
    }

    /**
     * Creates and formats an HBox
     *
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
