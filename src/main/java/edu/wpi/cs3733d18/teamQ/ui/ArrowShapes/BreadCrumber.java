package edu.wpi.cs3733d18.teamQ.ui.ArrowShapes;

import com.jfoenix.controls.JFXNodesList;
import edu.wpi.cs3733d18.teamQ.pathfinding.Node;
import edu.wpi.cs3733d18.teamQ.ui.Controller.PathfindingCont;
import edu.wpi.cs3733d18.teamQ.ui.ProxyMaps.IMap;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

public class BreadCrumber {
    private ArrayList<ArrowButton> buttList = new ArrayList<>();
    private JFXNodesList nodeList = new JFXNodesList();

    private ArrayList<Integer> floorsOfPath = new ArrayList<Integer>();
    private IMap floorMaps;
    private HBox hBox;
    private PathfindingCont pf;

    private final double height = 80;
    private final double width = 130;
    private int currSelected = 0; // currently selected arrow is default start arrow

    public BreadCrumber(){}

    public BreadCrumber(IMap floorMaps, HBox hBox, PathfindingCont pf) {
        this.hBox = hBox;
        this.floorMaps = floorMaps;
        this.pf = pf;
    }

    public void drawCrumbs(ArrayList<Node> path){
        // clears current list
        removeArrows();
        buttList = new ArrayList<>();
        floorsOfPath = new ArrayList<>();

        // generates list of floors to travel
        getListofFloorChanges(path);

        boolean isSelected;
        for(int i = 0; i < floorsOfPath.size(); i++){
            final int index = i;
            // creates arrow object based on if a start arrow is needed or not
            ArrowShape arrow;
            if(i == 0)
                arrow = new StartArrow(height, width, 0,0);
            else
                arrow =  new FatArrow(height, width, 0, 0);

            // creates text based on if it is on an end of the line or not
            String text;
            if(i == 0)
                text = " Start\nFloor " + floorToString(floorsOfPath.get(0));
            else if(i == floorsOfPath.size() - 1)
                text = "  End\nFloor " + floorToString(floorsOfPath.get(i));
            else
                text = "   Floor " + floorToString(floorsOfPath.get(i));

            // creates jfx button and adds proper text and styles
            ArrowButton arrowButt = new ArrowButton(arrow, floorsOfPath.get(i), width, height);
            arrowButt.setText(text);

            if(floorsOfPath.get(i) == floorMaps.getCurrFloor())
                setArrowStyleSelected(arrowButt);
            else
                setArrowStyleDefault(arrowButt);

            // on click actions arrow
            arrowButt.setOnMouseClicked((e) -> {
                this.currSelected = index;
                floorMaps.updateFloorMap(arrowButt.getFloor());
                pf.updateDrawings();
            });

            buttList.add(arrowButt);
        }

        addArrows();
    }

    // add crumbs to the screen
    public void addArrows(){
        hBox.getChildren().addAll(buttList);
    }

    public void removeArrows(){
        hBox.getChildren().removeAll(buttList);
    }

    /**
     *  Setting Styles
     */
    // set style for default arrow
    public ArrowButton setArrowStyleDefault(ArrowButton p){
        p.setOpacity(.8);
        p.setStyle("-fx-background-color: #0067B1;" + "-jfx-button-type: RAISED;" +
                "-fx-font-size: 20;" + "-fx-text-fill: WHITE;" + "-fx-font-weight: 700;");
        return p;
    }

    // set style for selected arrow
    public ArrowButton setArrowStyleSelected(ArrowButton p){
        p.setOpacity(1);
        p.setStyle("-fx-background-color: #013C78;" + "-jfx-button-type: RAISED;" +
                "-fx-font-size: 20;" + "-fx-text-fill: WHITE;" + "-fx-font-weight: 700;");
        return p;
    }

    // helper function to get all floors needed
    public void getListofFloorChanges(ArrayList<Node> path){
        if(path==null){
            return;
        }
        floorsOfPath.add(path.get(0).getFloor());
        for(int i=0; i < path.size()-1; i++){
            if(path.get(i).getFloor() != path.get(i + 1).getFloor()){
                floorsOfPath.add(path.get(i + 1).getFloor());
            }
        }
    }

    public String floorToString(int f){
        switch (f){
            case -2: return "L2";
            case -1: return "L1";
            default: return String.valueOf(f);
        }
    }

    public int stringToFloor(String s){
        switch (s){
            case "L2": return -2;
            case "L1": return -1;
            default: return Integer.parseInt(s);
        }
    }


    public ArrayList<ArrowButton> getArrowList() {
        return buttList;
    }

    public int getCurrSelected() {
        return currSelected;
    }

    public void setCurrSelected(int currSelected) {
        this.currSelected = currSelected;
    }
}
