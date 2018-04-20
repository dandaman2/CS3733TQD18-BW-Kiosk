package edu.wpi.cs3733d18.teamQ.ui.Controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.cs3733d18.teamQ.ui.Employee;
import edu.wpi.cs3733d18.teamQ.ui.TimeoutData;
import edu.wpi.cs3733d18.teamQ.ui.User;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static edu.wpi.cs3733d18.teamQ.manageDB.DatabaseSystem.*;

public class EmployeeEditController implements Initializable {

    //buttons
    @FXML
    JFXButton clearBtn;
    @FXML
    JFXButton confirmBtn;
    @FXML
    JFXButton backBtn;

    //labels
    @FXML
    Label actionLabel;

    //textfields
    @FXML
    JFXTextField usernameTF;
    @FXML
    JFXTextField passwordTF;

    //dropDowns
    @FXML
    ComboBox<String> actionCB;
    @FXML
    ComboBox<String> adminCB;

    //JFXTableTreeView
    @FXML
    JFXTreeTableView<Employee> employeeTTV;

    @FXML
    private AnchorPane employeeEditPane;

    //Global Type -------------------------------------------
    private String actionType;

    //the user
    User user = User.getUser();


    public void initialize(URL url, ResourceBundle rb) {
        setUpDropBox();
        setUpButtons();
        setUpTreeTable();

        employeeTTV.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> fillFields(newValue.getValue()));

        confirmBtn.setBackground(new Background(new BackgroundFill(Paint.valueOf("#012D5A"), new CornerRadii(0), null)));
        confirmBtn.setStyle("-fx-text-fill: #FFFFFF;");
        confirmBtn.setRipplerFill(Paint.valueOf("#FFFFFF"));

        clearBtn.setBackground(new Background(new BackgroundFill(Paint.valueOf("#012D5A"), new CornerRadii(0), null)));
        clearBtn.setStyle("-fx-text-fill: #FFFFFF;");
        clearBtn.setRipplerFill(Paint.valueOf("#FFFFFF"));

        backBtn.setBackground(new Background(new BackgroundFill(Paint.valueOf("#ECECEC"), new CornerRadii(0), null)));
        backBtn.setStyle("-fx-text-fill: #FFFFFF;");
        backBtn.setRipplerFill(Paint.valueOf("#FFFFFF"));

        Image info;
        if(runningFromIntelliJ()) {
            info = new Image("../resources/ButtonImages/home.png");
        } else{
            info = new Image("ButtonImages/home.png");
        }
        ImageView infoView = new ImageView(info);
        infoView.setFitWidth(42);
        infoView.setFitHeight(40);
        backBtn.setGraphic(infoView);

        backBtn.setOnAction(e->goToAdminHome(e));

    }



    //initialize the drop down menus
    private void setUpDropBox() {
        actionCB.getItems().add("Add Employee");
        actionCB.getItems().add("Modify Employee");
        actionCB.getItems().add("Remove Employee");
        actionCB.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> displayActiontType(newValue));



        adminCB.getItems().add("1");
        adminCB.getItems().add("2");
    }

    //Initialize buttons of edit employee screen
    private void setUpButtons() {
        clearBtn.setOnAction(e -> handleAction(e));
        confirmBtn.setOnAction(e -> handleAction(e));
    }


    /**
     * initializes the pending tree table view
     */
    private void setUpTreeTable(){
        JFXTreeTableColumn<Employee, String> levelColumn = new JFXTreeTableColumn<>("Access Level");
        levelColumn.setPrefWidth(150);
        levelColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Employee, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Employee, String> param) {
                return param.getValue().getValue().getIsAdminProperty();
            }
        });
        JFXTreeTableColumn<Employee, String> usernameColumn = new JFXTreeTableColumn<>("Username");
        usernameColumn.setPrefWidth(150);
        usernameColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Employee, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Employee, String> param) {
                return param.getValue().getValue().usernameProperty();
            }
        });
        JFXTreeTableColumn<Employee, String> passwordColumn = new JFXTreeTableColumn<>("Password");
        passwordColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Employee, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Employee, String> param) {
                return param.getValue().getValue().passwordProperty();
            }
        });


        ObservableList<Employee> allEmployees = FXCollections.observableArrayList(getEmployees());
        final TreeItem<Employee> pendingRoot = new RecursiveTreeItem<Employee>(allEmployees, RecursiveTreeObject::getChildren);
        employeeTTV.getColumns().setAll(levelColumn, usernameColumn, passwordColumn);
        employeeTTV.setRoot(pendingRoot);
        employeeTTV.setShowRoot(false);
    }


    //Helper Methods -------------------------------------------------------------


    //ScreenUtil object for resizing of loading images
    private ScreenUtil sdUtil = new ScreenUtil();

    /**
     * Goes to the Admin Home Screen when called
     * @param actionEvent Takes an action event that says it has been called
     */
    public void goToAdminHome(javafx.event.ActionEvent actionEvent){
        try{
            //this line will execute immediately, not waiting for your task to complete
            FXMLLoader adminLoader;
            if(runningFromIntelliJ()) {
                adminLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/AdminHomeScreen.fxml"));
            } else{
                adminLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/AdminHomeScreen.fxml"));
            }
            Parent adminPane = adminLoader.load();
            Stage primaryStage = (Stage)((javafx.scene.Node)actionEvent.getSource()).getScene().getWindow();
            Scene adminScene = sdUtil.prodAndBindScene(adminPane, primaryStage);
            adminScene.getStylesheets().addAll("Stylesheet.css", "StyleMenus.css");
            primaryStage.setScene(adminScene);
        }
        catch(IOException io){
            System.out.println("errWelS");
            io.printStackTrace();
        }
    }


    //displays the text fields that are dependent of the type of request
    private void displayActiontType(String action){

        switch (action){
            //add employee
            case "Add Employee":
                actionType = "Add Employee";
                break;
            //modify employee
            case "Modify Employee":
                actionType = "Modify Employee";
                break;
            //remove employee
            case "Remove Employee":
                actionType = "Remove Employee";
                break;
            default:
        }
    }

    /**
     * resets the values of the
     */
    public void resetDefault(){
        usernameTF.setText("");
        passwordTF.setText("");
        adminCB.getSelectionModel().clearSelection();
    }


    /**
     * fills the fields of the action section of ui
     */
    public void fillFields(Employee employee){
        usernameTF.setText(employee.getUsername());
        passwordTF.setText(employee.getPassword());
        adminCB.setValue(String.valueOf(Integer.parseInt(employee.getIsAdmin())));
    }

    /**
     * performs button actions
     * @param event
     */
    public void handleAction(ActionEvent event){
        if(event.getSource() == clearBtn){
            resetDefault();
        }
        if(event.getSource() == confirmBtn){

            String uName = usernameTF.getText();
            String pass = passwordTF.getText();

            //determines if text fields are blank
            if(uName ==null || pass == null || uName.isEmpty() || pass.isEmpty() || adminCB == null) {
                System.out.println("Employee Error");
                return;
            }

            String level = adminCB.getValue();


            if(actionType == "Add Employee"){
                Employee employee;

                if(level == "1"){
                    employee = new Employee(uName,pass,false,null);
                }
                else{
                    employee = new Employee(uName,pass,true,null);
                }

                addEmployee(employee);

                ObservableList<Employee> allEmployees = FXCollections.observableArrayList(getEmployees());
                final TreeItem<Employee> pendingRoot = new RecursiveTreeItem<Employee>(allEmployees, RecursiveTreeObject::getChildren);
                employeeTTV.setRoot(pendingRoot);
                employeeTTV.setShowRoot(false);

                resetDefault();

            }
            if(actionType == "Modify Employee"){
                ArrayList<Employee> allEmployee = getEmployees();
                ArrayList<String> allUsername = new ArrayList<String>();

                for (Employee employee:allEmployee) {
                    allUsername.add(employee.getUsername());
                }

                if(allUsername.contains(usernameTF.getText())){
                    Employee employee = employeeTTV.getSelectionModel().getSelectedItem().getValue();

                    Employee employeeNew;

                    if(level == "1"){
                        employeeNew = new Employee(uName,pass,false,null);
                    }
                    else{
                        employeeNew = new Employee(uName,pass,true,null);
                    }

                    removeEmployee(employee);
                    addEmployee(employeeNew);

                    ObservableList<Employee> allEmployees = FXCollections.observableArrayList(getEmployees());
                    final TreeItem<Employee> pendingRoot = new RecursiveTreeItem<Employee>(allEmployees, RecursiveTreeObject::getChildren);
                    employeeTTV.setRoot(pendingRoot);
                    employeeTTV.setShowRoot(false);

                    resetDefault();
                }

                System.out.println("No Employee Exists");

            }
            if(actionType == "Remove Employee"){
                ArrayList<Employee> allEmployee = getEmployees();
                ArrayList<String> allUsername = new ArrayList<String>();

                for (Employee employee:allEmployee) {
                    allUsername.add(employee.getUsername());
                }

                if(allUsername.contains(usernameTF.getText())){
                    Employee employee = employeeTTV.getSelectionModel().getSelectedItem().getValue();
                    removeEmployee(employee);

                    ObservableList<Employee> allEmployees = FXCollections.observableArrayList(getEmployees());
                    final TreeItem<Employee> pendingRoot = new RecursiveTreeItem<Employee>(allEmployees, RecursiveTreeObject::getChildren);
                    employeeTTV.setRoot(pendingRoot);
                    employeeTTV.setShowRoot(false);

                    resetDefault();
                }

                System.out.println("No Employee Exists");

            }

        }
    }
}
