package edu.wpi.cs3733d18.teamQ.ui.Requests;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class GiftRequest extends Request{
    public GiftRequest(int room, int fulfilled, String whoFulfilled, String email, String phoneNumber, String requestIDStr, GregorianCalendar date) {
        super(room, fulfilled, whoFulfilled, email, phoneNumber, requestIDStr, date);
        this.setPriority("Medium");
        this.setType("Gift");
    }

    public GiftRequest(String firstName, String lastName, String roomName, int fulfilled, String whoFulfilled, String email, String phoneNumber, String requestIDStr, GregorianCalendar date, String priority) {
        super(firstName, lastName, roomName, fulfilled, whoFulfilled, email, phoneNumber, requestIDStr, date, priority);
        this.setPriority("Medium");
        this.setType("Gift");
    }

    public GiftRequest(int room, String email, String phoneNumber, String requestIDStr) {
        super(room, email, phoneNumber, requestIDStr);
        this.setPriority("Medium");
        this.setType("Gift");
    }

    public GiftRequest(String firstName, String lastName, String email, String phoneNumber, String roomName) {
        super(firstName, lastName, email, phoneNumber, roomName);
        this.setPriority("Medium");
        this.setType("Gift");
    }

    @Override
    public String getDetails() {
        return null;
    }

    @Override
    public String getSpecifics() {
        return "Gift Request";
    }

    @Override
    public ArrayList<Request> addRequest(ArrayList<Request> arrayList) {
        arrayList.add(this);

        System.out.println("Sent!");

        return arrayList;
    }
}
