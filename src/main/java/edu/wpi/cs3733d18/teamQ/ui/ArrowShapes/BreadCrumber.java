package edu.wpi.cs3733d18.teamQ.ui.ArrowShapes;

import edu.wpi.cs3733d18.teamQ.pathfinding.Node;
import edu.wpi.cs3733d18.teamQ.ui.Controller.PathfindingCont;
import edu.wpi.cs3733d18.teamQ.ui.ProxyMaps.IMap;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

import java.util.ArrayList;

public class BreadCrumber {
    private ArrayList<StackPane> stackList = new ArrayList<>();

    private ArrayList<Integer> floorsOfPath = new ArrayList<Integer>();
    private IMap floorMaps;
    private HBox hBox;
    private PathfindingCont pf;

    private final double height = 100;
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
        stackList = new ArrayList<>();
        floorsOfPath.clear();

        // generates list of floors to travel
        getListofFloorChanges(path);

        boolean isSelected;
        for(int i = 0; i < floorsOfPath.size(); i++){
            // creates arrow object based on if a start arrow is needed or not
            ArrowShape arrow;
            if(i == 0)
                arrow = new StartArrow(height, width, floorsOfPath.get(0), 0,0);
            else
                arrow =  new FatArrow(height, width, floorsOfPath.get(i), 0, 0);


            // creates text based on if it is on an end of the line or not
            Text text;
            if(i == 0)
                text = new Text(" Start\nFloor " + floorToString(floorsOfPath.get(0)));
            else if(i == floorsOfPath.size() - 1)
                text = new Text("  End\nFloor " + floorToString(floorsOfPath.get(i)));
            else
                text = new Text("   Floor " + floorToString(floorsOfPath.get(i)));

            if(i == currSelected)
                isSelected = true;
            else
                isSelected = false;

            StackPane stack;
            stack = createCrumb(arrow, text, isSelected, i);
            stackList.add(stack);
        }

        addArrows();
    }

    /**
     * creats a crumb with the proper shape, text and actions
     * @param arrow
     * @param text
     * @return
     */
    public StackPane createCrumb(ArrowShape arrow, Text text, boolean isSelected, int index){
        // if the arrow is to be selected, change its color
        if(isSelected)
            setArrowStyleSelected(arrow);
        else
            setArrowStyleDefault(arrow);

        // sets texts
        setTextDefault(text);
        text.setLayoutX(0);

        //stackpane to hold the arrowshape and text overlayed
        StackPane stack = new StackPane();
        stack.getChildren().addAll(arrow, text);

        // on click actions arrow
        stack.setOnMouseClicked((e) -> {
            this.currSelected = index;
            floorMaps.updateFloorMap(arrow.getFloor());
            pf.updateDrawings();
        });
        return stack;
    }


    // add crumbs to the screen
    public void addArrows(){
        hBox.getChildren().addAll(stackList);
    }

    public void removeArrows(){
        hBox.getChildren().removeAll(stackList);
    }

    /**
     *  Setting Styles
     */
    // set style for default arrow
    public Polygon setArrowStyleDefault(Polygon p){
        p.setOpacity(.8);
        p.setStyle("-fx-fill: #0067B1");
        return p;
    }

    // set style for selected arrow
    public Polygon setArrowStyleSelected(Polygon p){
        p.setOpacity(1);
        p.setStyle("-fx-fill: #013C78");
        return p;
    }

    public Text setTextDefault(Text t){
        t.setFill(Color.WHITE);
        t.setStyle("-fx-alignment: CENTER-RIGHT;" + "-fx-font-size: 26;" + "-fx-font-weight: 700;");
        t.setBoundsType(TextBoundsType.VISUAL);
        return t;
    }


    // helper function to get all floors needed
    public void getListofFloorChanges(ArrayList<Node> path){
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


    public ArrayList<StackPane> getStackList() {
        System.out.println("Stack List: " + stackList);
        return stackList;
    }

    public int getCurrSelected() {
        return currSelected;
    }

    public void setCurrSelected(int currSelected) {
        this.currSelected = currSelected;
    }
}
