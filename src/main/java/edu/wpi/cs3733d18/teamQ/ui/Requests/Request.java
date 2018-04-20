package edu.wpi.cs3733d18.teamQ.ui.Requests;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static edu.wpi.cs3733d18.teamQ.manageDB.DatabaseSystem.getNewRequestID;


public abstract class Request extends RecursiveTreeObject<Request> {
    private int room;
    private StringProperty firstName;//-------------
    private StringProperty lastName;//-----------
    private int fulfilled;
    private String nameWhoFulfilled;
    private String email;
    private String phoneNumber;
    private StringProperty requestID;//-----------
    private GregorianCalendar date;
    private StringProperty roomName;
    private String description;
    private StringProperty priority;//-----------
    private StringProperty type;//-------------



    // Constructor for complete request
    public Request(int room, int fulfilled, String whoFulfilled, String email, String phoneNumber, String requestIDStr, GregorianCalendar date) {
        this.room = room;
        this.fulfilled = fulfilled;
        this.nameWhoFulfilled = whoFulfilled;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.requestID = new SimpleStringProperty((String) requestIDStr);
        this.date = date;
    }

    public Request(String firstName, String lastName, String roomName, int fulfilled, String whoFulfilled, String email, String phoneNumber, String requestIDStr, GregorianCalendar date, String priority) {
        this.firstName = new SimpleStringProperty((String) firstName);
        this.lastName = new SimpleStringProperty((String) lastName);
        this.roomName = new SimpleStringProperty((String) roomName);
        this.fulfilled = fulfilled;
        this.nameWhoFulfilled = whoFulfilled;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.requestID = new SimpleStringProperty((String) requestIDStr);
        this.date = date;
        this.priority = new SimpleStringProperty((String) priority);
    }

    // Constructor for request that is not fulfilled
    public Request(int room, String email, String phoneNumber, String requestIDStr) {
        this.room = room;
        this.fulfilled = 0;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.requestID = new SimpleStringProperty((String) requestIDStr);
    }

    /*Constructor containing:
        firstName
        lastName
        email
        phone number
        room name
    */
    public Request(String firstName, String lastName, String email, String phoneNumber, String roomName) {
        this.requestID = new SimpleStringProperty(String.valueOf(getNewRequestID()));
        this.firstName = new SimpleStringProperty((String) firstName);
        this.setLastName(lastName);
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.setRoomName(roomName);
    }


    //----------------------------------------------------------------------------------------------------

    @Override
    public String toString(){
        String generalPrint = "ID: " + this.getRequestID() +
                "\t" + "Room: "  + getRoomName() +
                "\t" +  this.getSpecifics();


        if(isFulfilled()==0) {
            return generalPrint;        // to print if request is not fulfilled
        }
        else{
            //adds name and email for when fulfilled
            String fulFilledGeneral = this.getWhoFulfilled() +
                    "\t" + this.printDate() +
                    "\t" + generalPrint;

            return fulFilledGeneral;
        }
    }

    //prints the details of a request
    abstract public String getDetails();


    public String showDescription() {
        if (this != null) {
            return this.getDetails();
        }
        else{
            System.out.println("None");
            return null;
        }
    }

    // empty method for subclasses to fill out
    abstract public String getSpecifics();

    /**
     * Prints the date given
     * @return
     */
    public String printDate(){
        return this.getDate().get(Calendar.MONTH) + "/" + this.getDate().get(Calendar.DAY_OF_MONTH) + "/" + this.getDate().get(Calendar.YEAR);
    }


    //Getters and Setters

    //Room
    public int getRoom() {
        return room;
    }
    public void setRoom(int room) {
        this.room = room;
    }

    //Date
    public GregorianCalendar getDate() {
        return date;
    }
    public void setDate(GregorianCalendar date) {
        this.date = date;
    }

    //firstName
    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        //System.out.println(lastName);
        if(firstName == null){
            System.out.println("Null");
        }
        this.firstName = new SimpleStringProperty(firstName);
    }

    //lastName
    public String getLastName() {
        return lastName.get();
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        //System.out.println(lastName);
        if(lastName == null){
            System.out.println("Null");
        }
        this.lastName = new SimpleStringProperty(lastName);
    }

    //isFullfilled
    public int isFulfilled() {
        return fulfilled;
    }

    public void setFulfilled(int fulfilled) {
        this.fulfilled = fulfilled;
    }

    //whofulfilled
    public String getWhoFulfilled() {
        return nameWhoFulfilled;
    }

    public void setNameWhoFulfilled(String whoFulfilled) {
        this.nameWhoFulfilled = whoFulfilled;
    }

    public String getNameWhoFulfilled() {
        return nameWhoFulfilled;
    }

    //email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    //phoneNumber
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    //requestId
    public String getRequestID() {
        //System.out.println(requestID);
        return requestID.get();
    }

    public StringProperty requestIDProperty() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = new SimpleStringProperty(requestID);
    }

    //Room Name
    public String getRoomName() {
        return roomName.get();
    }

    public StringProperty roomNameProperty() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = new SimpleStringProperty(roomName);
    }

    //Type
    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        //System.out.println(type);
        this.type = new SimpleStringProperty(type);//.set(type);
    }

    //Description
    public String getDescription(){
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    //Priority
    public String getPriority() {
        //System.out.println(requestID);
        return priority.get();
    }

    public StringProperty priority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = new SimpleStringProperty(priority);
    }


    public abstract ArrayList<Request> addRequest(ArrayList<Request> arrayList);
}
