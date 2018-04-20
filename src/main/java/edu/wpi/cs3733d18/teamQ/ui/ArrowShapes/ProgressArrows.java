package edu.wpi.cs3733d18.teamQ.ui.ArrowShapes;

import edu.wpi.cs3733d18.teamQ.pathfinding.Node;
import edu.wpi.cs3733d18.teamQ.ui.Controller.PathfindingCont;
import edu.wpi.cs3733d18.teamQ.ui.ProxyMaps.IMap;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

import java.util.ArrayList;

public class ProgressArrows {
    private StartArrow startArrow;
    private ArrayList<FatArrow> fatArrowList = new ArrayList<>();
    private ArrayList<StackPane> stackList = new ArrayList<>();

    private ArrayList<Integer> floorsOfPath = new ArrayList<Integer>();
    private ArrayList<Node> path;
    private IMap floorMaps;
    private HBox hBox;
    private PathfindingCont pf;

    private final double height = 100;
    private final double width = 130;
    private double runningX = 0;
    private int currSelected = 0; // currently selected arrow

    public ProgressArrows(){}

    public ProgressArrows(ArrayList<Node> path, IMap floorMaps, HBox hBox, PathfindingCont pf, int currSelected) {
        this.hBox = hBox;
        this.path = path;
        this.floorMaps = floorMaps;
        this.pf = pf;
        this.currSelected = currSelected;

        stackList = new ArrayList<StackPane>();
        StackPane stack = new StackPane();

        getListofFloorChanges(path);

        startArrow = new StartArrow(height, width, floorsOfPath.get(0), 0,0);

        if(currSelected == 0)
            setArrowSelected(startArrow);
        else
            setArrowDefault(startArrow);

        Text text = new Text(" Start\nFloor " + floorToString(floorsOfPath.get(0)));
        setTextDefault(text);
        stack.getChildren().addAll(startArrow, text);
        text.setLayoutX(0);
        stackList.add(stack);

        startArrow.setOnMouseClicked((e) -> {
            System.out.println("start arrow clicked");
            this.currSelected = 0;
            floorMaps.updateFloorMap(startArrow.getFloor());
            pf.updateDrawings();
        });

        text.setOnMouseClicked((e) -> {
            this.currSelected = 0;
            floorMaps.updateFloorMap(startArrow.getFloor());
            pf.updateDrawings();
        });


//        runningX = 0;
        for(int i = 1; i < floorsOfPath.size(); i++){
            final int selectedIndex = i;
//            runningX += width;
            FatArrow fat = new FatArrow(height, width, floorsOfPath.get(i), runningX, 0);
            fatArrowList.add(fat);

            if(currSelected == i)
                setArrowSelected(fat);
            else
                setArrowDefault(fat);

            Text text1;
            if(i == floorsOfPath.size() - 1)
                text1 = new Text("  End\nFloor " + floorToString(floorsOfPath.get(i)));
            else
                text1 = new Text("   Floor " + floorToString(floorsOfPath.get(i)));

            setTextDefault(text1);
            StackPane stack1 = new StackPane();
            stack1.getChildren().addAll(fat, text1);

            text1.setOnMouseClicked((event -> {
                this.currSelected = selectedIndex;
                floorMaps.updateFloorMap(fat.getFloor());
                pf.updateDrawings();
            }));

            fat.setOnMouseClicked((e) -> {
                this.currSelected = selectedIndex;
                floorMaps.updateFloorMap(fat.getFloor());
                pf.updateDrawings();
            });

            stackList.add(stack1);
        }
    }

    // set style for default arrow
    public Polygon setArrowDefault(Polygon p){
        p.setOpacity(.8);
        p.setStyle("-fx-fill: #0067B1");
        return p;
    }

    // set style for selected arrow
    public Polygon setArrowSelected(Polygon p){
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

    public void addArrows(){
        hBox.getChildren().addAll(stackList);
    }

    public void getListofFloorChanges(ArrayList<Node> path){
        floorsOfPath.add(path.get(0).getFloor());
        for(int i=0; i < path.size()-1; i++){
            if(path.get(i).getFloor() != path.get(i + 1).getFloor()){
                floorsOfPath.add(path.get(i + 1).getFloor());
            }
        }
//        System.out.println(" floors of path" + floorsOfPath);
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

    public StartArrow getStartArrow() {
        return startArrow;
    }

    public ArrayList<FatArrow> getFatArrowList() {
        return fatArrowList;
    }

    public ArrayList<StackPane> getStackList() {
        return stackList;
    }

    public int getCurrSelected() {
        return currSelected;
    }

    public void setCurrSelected(int currSelected) {
        this.currSelected = currSelected;
    }
}
