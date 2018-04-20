package edu.wpi.cs3733d18.teamQ.ui.ProxyMaps;

import javafx.scene.image.ImageView;

public class ProxyMaps implements IMap {
    private FloorMaps realMaps;

    private ImageView view;
    private String currFloorString;
    private int currFloor;
    private boolean is2D;

    public ProxyMaps(ImageView view, int currFloor, boolean is2D) {
        this.view = view;
        this.currFloor = currFloor;
        this.is2D = is2D;
    }

    @Override
    public void updateFloorMap(int floor){
        realMaps = new FloorMaps(view, currFloor, is2D);
        currFloor = realMaps.updateFloorMapNoArray(floor);
    }

    @Override
    public void goUpFloor(){
        realMaps = new FloorMaps(view, currFloor, is2D);

        currFloor = realMaps.goUpFloorNoArray();
    }

    @Override
    public void goDownFloor(){
        realMaps = new FloorMaps(view, currFloor, is2D);

        currFloor = realMaps.goDownFloorNoArray();
    }

    @Override
    public String currFloorString(){
        realMaps = new FloorMaps(view, currFloor, is2D);
        return realMaps.currFloorString();
    }

    @Override
    public void setIs2D(boolean is2D) {
        this.is2D = is2D;
    }

    @Override
    public int getCurrFloor() {
        return currFloor;
    }

    public boolean getIs2D() {
        return is2D;
    }

    public int indexFloorToDB(int floor){
        return realMaps.indexFloorToDB(floor);
    }
}
