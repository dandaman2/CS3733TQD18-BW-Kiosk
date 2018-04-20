package edu.wpi.cs3733d18.teamQ.ui.Requests;

import edu.wpi.cs3733d18.teamQ.ui.User;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class EmergencyRequest extends Request {

    //Constructors

    public EmergencyRequest(int room, int fulfilled, String whoFulfilled, String email, String phoneNumber, String requestIDStr, GregorianCalendar date) {
        super(room, fulfilled, whoFulfilled, email, phoneNumber, requestIDStr, date);
        this.setPriority("Critical");
        this.setType("EMERGENCY");
    }

    public EmergencyRequest(String firstName, String lastName, String roomName, int fulfilled, String whoFulfilled, String email, String phoneNumber, String requestIDStr, GregorianCalendar date, String priority) {
        super(firstName, lastName, roomName, fulfilled, whoFulfilled, email, phoneNumber, requestIDStr, date, priority);
        this.setPriority("Critical");
        this.setType("EMERGENCY");
    }

    public EmergencyRequest(int room, String email, String phoneNumber, String requestIDStr) {
        super(room, email, phoneNumber, requestIDStr);
        this.setPriority("Critical");
        this.setType("EMERGENCY");
    }

    public EmergencyRequest(String firstName, String lastName, String email, String phoneNumber, String roomName) {
        super(firstName, lastName, email, phoneNumber, roomName);
        this.setPriority("Critical");
        this.setType("EMERGENCY");
    }


    //Functions----------------------------------

    @Override
    public String getDetails() {
        String generalPrint = "ID: " + this.getRequestID() +
                "\n" + "Room: "  + User.getUser().getNodeLocation() +
                "\n" + "Type: " + this.getType() +
                "\n" +
                "\n" + "EMERGENCY - " + this.getSpecifics();


        if(isFulfilled()==0) {
            return generalPrint;        // to print if request is not fulfilled
        }
        else{
            //adds name and email for when fulfilled
            String fulFilledGeneral = "Who Fulfilled: " + this.getWhoFulfilled() +
                    "\n" + "Date Fulfilled: " + this.printDate() +
                    "\n" +
                    "\n" + generalPrint;

            return fulFilledGeneral;
        }
    }

    @Override
    public String getSpecifics() {
        return "Action Required Immediately!";
    }

    @Override
    public ArrayList<Request> addRequest(ArrayList<Request> arrayList) {
        arrayList.add(this);

        //Todo: fix method
        System.out.println("Sent!");

        return arrayList;
    }
}
