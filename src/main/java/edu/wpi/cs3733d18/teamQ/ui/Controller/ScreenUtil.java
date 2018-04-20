package edu.wpi.cs3733d18.teamQ.ui.Controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import edu.wpi.cs3733d18.teamQ.pathfinding.Node;

import java.io.*;
import java.util.ArrayList;

import static edu.wpi.cs3733d18.teamQ.manageDB.DatabaseSystem.runningFromIntelliJ;


public class ScreenUtil {

    public ScreenUtil() {}
    public Scene prodAndBindScene(Parent pane, Stage stage){
       return new Scene(pane, stage.getScene().getWidth(),stage.getScene().getHeight());
    }


    /**
     * Generates ArrayList of ID and names for Autocomplete nodes
     * @param node
     * @return
     */
    public ArrayList<String> getNameIdNode(ArrayList<Node> node){
        int i;
        ArrayList<String > idName = new ArrayList<String>();
        Node n;

        for (i=0; i<node.size(); i++){
            n = node.get(i);

            //make sure user can only look up nodes on 2nd floor-------------------------------Delete After Iteration 1
//            if(n.getFloor() == 2){
//                idName.add(n.getNameLong() + "," + n.getNodeID());
//            }
            idName.add(n.getNameLong() + "," + n.getNodeID());
        }
        return idName;
    }


    public ArrayList<String> getNameIdNodeAll(ArrayList<Node> node){
        int i;
        ArrayList<String > idName = new ArrayList<String>();
        Node n;

        for (i=0; i<node.size(); i++){
            n = node.get(i);
            idName.add(n.getNameLong() + "," + n.getNodeID());

        }
        return idName;
    }
    }

