package edu.wpi.cs3733d18.teamQ.ui.Requests;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class SanitationRequest extends Request {

    // no specifics added yet
    private String description;

    /**
     * Fulfilled request constructor
     * @param room
     * @param fulfilled
     * @param whoFulfilled
     * @param email
     * @param phoneNumber
     * @param requestID
     * @param date
     * @param description
     */
    public SanitationRequest(int room, int fulfilled, String whoFulfilled, String email, String phoneNumber, String requestID, GregorianCalendar date, String description, String priority) {
        super(room, fulfilled, whoFulfilled, email, phoneNumber, requestID, date);
        this.description = description;
    }

    /**
     * Fulfilled request constructor with requester name
     * @param firstName
     * @param lastName
     * @param roomName
     * @param fulfilled
     * @param whoFulfilled
     * @param email
     * @param phoneNumber
     * @param requestID
     * @param date
     * @param description
     */
    public SanitationRequest(String firstName, String lastName, String roomName, int fulfilled, String whoFulfilled, String email, String phoneNumber, String requestID, GregorianCalendar date, String description, String priority) {
        super(firstName, lastName, roomName, fulfilled, whoFulfilled, email, phoneNumber, requestID, date, priority);
        this.setType("Sanitation");
        this.description = description;
    }

    /**
     * Unfulfilled request constructor
     * @param room
     * @param email
     * @param phoneNumber
     * @param requestID
     * @param description
     */
    public SanitationRequest(int room, String email, String phoneNumber, String requestID, String description) {
        super(room, email, phoneNumber, requestID);
        this.setType("Sanitation");
        this.description = description;
    }

    /**
     * Unfulfilled request constructor
     * @param firstName
     * @param lastName
     * @param email
     * @param phoneNumber
     * @param roomName
     * @param description
     */
    public SanitationRequest(String firstName, String lastName, String email, String phoneNumber, String roomName, String description) {
        super(firstName, lastName, email, phoneNumber, roomName);
        this.setType("Sanitation");
        this.description = description;
    }

    //extracts request specific data
    @Override
    public String getSpecifics(){
        return this.getDescription();
    }

    /**
     * Adds the request to the given array List
     * used for storing requests to be displaying on request screen
     * @param arrayList
     * @return arrayList
     */
    @Override
    public ArrayList<Request> addRequest(ArrayList<Request> arrayList) {
        arrayList.add(this);

        //Todo: fix method
        // do we still need to fix this??? -Maggie
        System.out.println("Sent!");

        return arrayList;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
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
                "\n" + "Sanitation Details - " + this.getSpecifics();


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
