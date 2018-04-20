package edu.wpi.cs3733d18.teamQ.ui.ProxyMaps;

public interface IMap {
    void updateFloorMap(int floor);
    void goUpFloor();
    void goDownFloor();
    String currFloorString();
    int indexFloorToDB(int f);


    boolean getIs2D();
    void setIs2D(boolean b);
    int getCurrFloor();
}
