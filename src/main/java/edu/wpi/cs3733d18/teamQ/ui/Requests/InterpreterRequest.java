package edu.wpi.cs3733d18.teamQ.ui.Requests;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class InterpreterRequest extends Request{
    private String language;

    public InterpreterRequest(int room, int fulfilled, String whoFulfilled, String email, String phoneNumber, String requestID, String language, GregorianCalendar date) {
        super(room, fulfilled, whoFulfilled, email, phoneNumber, requestID, date);
        this.setType("Interpreter");
        this.language = language;
    }

    public InterpreterRequest(String firstName, String lastName, String roomName, int fulfilled, String whoFulfilled, String email, String phoneNumber, String requestID, String language, GregorianCalendar date, String priority) {
        super(firstName, lastName, roomName, fulfilled, whoFulfilled, email, phoneNumber, requestID, date, priority);
        this.setType("Interpreter");
        this.language = language;
    }

    public InterpreterRequest(int room, String email, String phoneNumber, String requestID, String language) {
        super(room, email, phoneNumber, requestID);
        this.setType("Interpreter");
        this.language = language;
    }

    /*Constructor containing:
        firstName
        lastName
        email
        phone number
        room name
        language
    */
    public InterpreterRequest(String firstName, String lastName, String email, String phoneNumber, String roomName, String language) {
        super(firstName, lastName, email, phoneNumber, roomName);
        this.setType("Interpreter");
        this.language = language;
    }

    //extracts request specific data
    @Override
    public String getSpecifics(){
        return this.getLanguage();
    }

    //Getters and Setters
    public String getLanguage() {        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }

    //function that sends request to some database for storage
    @Override
    public ArrayList<Request> addRequest(ArrayList<Request> arrayList){
        arrayList.add(this);

        //Todo: fix method
        System.out.println("Sent!");

        return arrayList;
    }



    //prints the details of a request
    @Override
    public String getDetails(){
        String generalPrint = "ID: " + this.getRequestID() +
                "\n" + "Room: "  + this.getRoomName() +
                "\n" + "Type: " + this.getType() +
                "\n" +
                "\n" + "Requested By: " + this.getFirstName() + " " + this.getLastName() +
                "\n" + "Email: " + this.getEmail() +
                "\n" + "Phone Number: " + this.getPhoneNumber() +
                "\n" +
                "\n" + "Interpreter - " + this.getLanguage();


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
}
