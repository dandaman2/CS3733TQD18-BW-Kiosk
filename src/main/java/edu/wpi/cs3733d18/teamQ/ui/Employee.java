package edu.wpi.cs3733d18.teamQ.ui;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.mindrot.jbcrypt.BCrypt;

public class Employee extends RecursiveTreeObject<Employee> {
    private StringProperty username;
    private StringProperty password;
    private SimpleStringProperty firstName;
    private SimpleStringProperty lastName;
    private SimpleStringProperty title;
    private StringProperty isAdmin;
    private String faceID;

    //constructor
    public Employee(String  username, String password, String firstName, String lastName, String title, boolean isAdmin, String faceID) {

        this.username = new SimpleStringProperty((String) username);
        this.password = new SimpleStringProperty(BCrypt.hashpw(password, BCrypt.gensalt()));
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.title = new SimpleStringProperty(title);
        if(isAdmin){
            this.isAdmin = new SimpleStringProperty("2");
        }
        else{
            this.isAdmin = new SimpleStringProperty("1");
        }
        this.faceID = faceID;
    }

    public Employee(String username, String firstName, String lastName, String title, boolean isAdmin, String faceID) {
        this.username = new SimpleStringProperty((String) username);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.title = new SimpleStringProperty(title);
        if(isAdmin){
            this.isAdmin = new SimpleStringProperty("2");
        }
        else{
            this.isAdmin = new SimpleStringProperty("1");
        }
        this.faceID = faceID;
    }


    //username
    public String getUsername() {
        return username.get();
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username = new SimpleStringProperty(username);
    }


    //password
    public String getPassword() {
        return password.get();
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password = new SimpleStringProperty(BCrypt.hashpw(password, BCrypt.gensalt()));
    }

    //first name
    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = new SimpleStringProperty(firstName);
    }

    //last name
    public String getLastName() {
        return lastName.get();
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = new SimpleStringProperty(lastName);
    }

    //title
    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title = new SimpleStringProperty(title);
    }


    //isadmin
    public String getIsAdmin() {
        return isAdmin.get();
    }

    public StringProperty getIsAdminProperty() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.username = new SimpleStringProperty(isAdmin);
    }



    //Helper Methods----------------------------------------------------------------

    /**
     * checks if given password is correct
     * @param passToCheck
     * @return
     */
    public boolean checkPassword(String passToCheck){
        return BCrypt.checkpw(passToCheck, password.get());
    }


    /**
     * sets the password to hashed passsword
     * @param hashedPassword
     */
    public void setHashedPassword(String hashedPassword){
        this.password = new SimpleStringProperty(hashedPassword);
    }


    /**
     * determines if employee is admin

     * @return
     */
    public int isAdminInt(){
        if(this.isAdmin.get().equals("2")){
            return 1;
        }
        else{
            return 0;
        }

    }

    public String getFaceID() {
        return faceID;
    }

    public void setFaceID(String faceID) {
        this.faceID = faceID;

    }
}