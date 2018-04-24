package edu.wpi.cs3733d18.teamQ.ui.ProxyMaps;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

import static edu.wpi.cs3733d18.teamQ.manageDB.DatabaseSystem.runningFromIntelliJ;


public class FloorMaps implements IMap {
    private ArrayList<Image> maps3D = new ArrayList<Image>();
    private ArrayList<Image> maps2D = new ArrayList<Image>();
    private ImageView view;
    private int currFloor;

    private boolean is2D;

    /*
     ordered index- floormap
                0- L2
                1- L1
                2- 1
                3- 2
                4- 3
      */

    public FloorMaps(ImageView view, int currFloor, boolean is2D) {
        this.view = view;
        this.currFloor = currFloor;
        this.is2D = is2D;
    }

    public FloorMaps(ArrayList<Image> maps3D, ArrayList<Image> maps2D, ImageView view, int currFloor, boolean is2D) {
        this.maps3D = maps3D;
        this.maps2D = maps2D;
        this.view = view;
        this.currFloor = currFloor;
        this.is2D = is2D;
    }

    public int updateFloorMapNoArray(int floor){
            view.setImage(new Image(getMapFromFloor(floor)));
            return floor;
    }


    /**
     * Same function but uses attribute of is2D
     * @param floor
     */
    @Override
    public void updateFloorMap(int floor){
        if(!is2D)
            view.setImage(maps3D.get(dbToIndex(floor)));
        else
            view.setImage(maps2D.get(dbToIndex(floor)));

        currFloor = floor;
    }

    public int indexFloorToDB(int floor){
        switch (floor){
            case 0: return -2;
            case 1: return -1;
            case 2: return 1;
            case 3: return 2;
            case 4: return 3;
            default: return 1;
        }
    }

    public int dbToIndex(int floor){
        switch (floor){
            case -2: return 0;
            case -1: return 1;
            case 1: return 2;
            case 2: return 3;
            case 3: return 4;
            default: return -1;
        }
    }

    @Override
    public void goUpFloor(){
        currFloor ++;
        if(currFloor > 4)
            currFloor = 4;

        updateFloorMap(currFloor);
    }

    @Override
    public void goDownFloor(){
        currFloor --;
        if(currFloor < 0)
            currFloor = 0;
        updateFloorMap(currFloor);
    }


    public int goUpFloorNoArray(){
        currFloor ++;
        if(currFloor > 4) {
            currFloor = 4;
        }
        else
            updateFloorMapNoArray(currFloor);

        return currFloor;
    }

    public int goDownFloorNoArray(){
        currFloor --;
        if(currFloor < 0)
            currFloor = 0;
        else
            updateFloorMapNoArray(currFloor);
        return currFloor;
    }


    @Override
    public void setIs2D(boolean is2D) {
        this.is2D = is2D;
    }

    @Override
    public boolean getIs2D(){
        return this.is2D;
    }

    // Text for label based on index in array
    @Override
    public String currFloorString(){
        switch (currFloor){
            case -2: return "Floor L2";
            case -1: return "Floor L1";
            case 1: return "Floor 1";
            case 2: return "Floor 2";
            case 3: return "Floor 3";
            default: return "";
        }

    }

    // Get proper map from floor without having everything stored in an arrayList
    public String getMapFromFloor(int currFloor){
        if(runningFromIntelliJ()){
            if(!this.is2D){
                switch (currFloor) {
                    case -2: return "/Maps/L2-ICONS.png";
                    case -1: return "/Maps/L1-ICONS.png";
                    case 1: return "/Maps/1-ICONS.png";
                    case 2: return "/Maps/2-ICONS.png";
                    case 3: return "/Maps/3-ICONS.png";
                }
            }else{
                switch (currFloor) {
                    case -2: return "/Maps/00_thelowerlevel2.png";
                    case -1: return "/Maps/00_thelowerlevel1.png";
                    case 1: return "/Maps/01_thefirstfloor.png";
                    case 2: return "/Maps/02_thesecondfloor.png";
                    case 3: return "/Maps/03_thethirdfloor.png";
                }
            }
        }
        else{
            if(!this.is2D){
                switch (currFloor) {
                    case -2: return "Maps/L2-ICONS.png";
                    case -1: return "Maps/L1-ICONS.png";
                    case 1: return "Maps/1-ICONS.png";
                    case 2: return "Maps/2-ICONS.png";
                    case 3: return "Maps/3-ICONS.png";
                }
            }else{
                switch (currFloor) {
                    case -2: return "Maps/00_thelowerlevel2.png";
                    case -1: return "Maps/00_thelowerlevel1.png";
                    case 1: return "Maps/01_thefirstfloor.png";
                    case 2: return "Maps/02_thesecondfloor.png";
                    case 3: return "Maps/03_thethirdfloor.png";
                }
            }
        }
        return "";
    }

    @Override
    public int getCurrFloor() {
        return currFloor;
    }
}
