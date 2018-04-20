package edu.wpi.cs3733d18.teamQ.ui;

public class TransPoint {
    double tx;
    double ty;
    int floor;

    public TransPoint(double tx, double ty, int floor) {
        this.tx = tx;
        this.ty = ty;
        this.floor = floor;
    }

    public double getTx() {
        return tx;
    }

    public void setTx(double tx) {
        this.tx = tx;
    }

    public double getTy() {
        return ty;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public void setTy(double ty) {
        this.ty = ty;
    }
}
