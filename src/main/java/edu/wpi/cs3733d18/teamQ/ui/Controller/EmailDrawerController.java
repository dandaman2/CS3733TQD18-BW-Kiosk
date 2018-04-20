package edu.wpi.cs3733d18.teamQ.ui.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class EmailDrawerController implements Initializable{

    private PathfindingCont cont;

    @FXML
    private Button sendEmailButton;

    @FXML
    private TextField emailTextField;

    @FXML
    private Button xButton;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        xButton.setStyle("-fx-background-color: RED");
    }

    public PathfindingCont getCont() {return cont;}

    public void setCont(PathfindingCont cont) {this.cont = cont;}

    public void sendEmailByPath(){
        cont.openEmailDrawer();
        cont.sendEmailByPath(emailTextField.getText());


    }

    //opens/closes the email controller
    public void openEmailDrawer(){
        cont.openEmailDrawer();
    }
}
