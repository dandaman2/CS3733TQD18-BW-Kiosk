package edu.wpi.cs3733d18.teamQ.ui;

import edu.wpi.cs3733d18.teamQ.ui.Controller.SnapData;
import edu.wpi.cs3733d18.teamQ.ui.Requests.Request;
import javafx.embed.swing.SwingFXUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class Email {
    private String username = "cs3733teamQ@gmail.com";
    private String password = "cowwhale";
    private Properties properties;
    private Session session;
    private String signature = "\n\nThank you,\nThe Quenched Quinotaurs";
    private String greeting;
   // private Request request;

    // set up properties of email
    // -- if want to send from an outlook address, is outlook.office365.com
    // -- if want to send from a gmail address, is smtp.gmail.com
    public Email() {
        this.properties = new Properties();
        this.properties.put("mail.smtp.auth", "true");
        this.properties.put("mail.smtp.starttls.enable", "true");
        this.properties.put("mail.smtp.host", "smtp.gmail.com");
        this.properties.put("mail.smtp.port", "587");
       // this.request = request;
    }

    /**
     * checks if address entered by requester is valid (dont change this method, its magic)
     * @param email
     * @return true if valid address, false otherwise
     */
    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * logs into sender's email using static username and password fields of Email
     */
    private void login() {
        this.session = Session.getInstance(this.properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
    }

    /**
     * Sets greeting of email given requesters first and last name
     * @param firstname
     * @param lastname
     */
    public void setGreeting(String firstname, String lastname) {
        this.greeting = "\nHello, " + firstname + " " + lastname + ";\n";
    }

    // TODO not sure how Alek is giving me the email I need to send this to, so assume string

    /**
     * Send screenshot of path to given user
     * @param allFiles
     * @param sendTo
     */
    public void sendDirections(ArrayList<SnapData> allFiles, String sendTo, String directions) {
        this.login();

        //2) compose message
        try {

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(this.username));
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(sendTo));
            message.setSubject("Kiosk Directions");

            //3) create MimeBodyPart object and set your message text
            BodyPart messageBodyPart1 = new MimeBodyPart();
            messageBodyPart1.setText("Test message body \n" + directions);

            //4) create new MimeBodyPart object and set DataHandler object to this object
            //^^done in addAttachment;

            //5) create Multipart object and add MimeBodyPart objects to this object
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart1);
            for(SnapData file: allFiles) {
                String sFile = getSnapData(file);
                addAttachment(multipart, sFile);
            }

        //    multipart.addBodyPart(messageBodyPart2);

            //6) set the multiplart object to the message object
            message.setContent(multipart);

            //7) send message
            if (this.isValidEmailAddress(sendTo)) {
                Transport.send(message);
                System.out.println("Email sent");
            } else {
                System.out.println("Invalid email address.");
            }

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * adds an attatchment to the given multipart
     * @param multipart
     * @param filename
     */
    private static void addAttachment(Multipart multipart, String filename)
    {
        try {
            DataSource source = new FileDataSource(filename);
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);
        }
        catch (MessagingException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * logs into email, then sends an email to the requester
     * -- works for both unfulfilled and fulfilled requests
     */
    public void sendRequestEmail(Request request) {
        this.login();
        try {

            // Fill in From, To, Subject, and body of email
            Message message = new MimeMessage(this.session);
            message.setFrom(new InternetAddress(this.username));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(request.getEmail()));
            message.setSubject("Service Request Notification");
            this.setGreeting(request.getFirstName(), request.getLastName());

            if (request.isFulfilled() == 0) { // is not fulfilled
                message.setText(this.greeting + "Your " + request.getType() + " request to room " + request.getRoomName() + " has been submitted."
                        + "\nDetails: " + request.getSpecifics()
                        + this.signature);
            } else { // is fulfilled
                message.setText(this.greeting + "Your " + request.getType() + "  request to room " + request.getRoomName() + " has been fulfilled by " + request.getNameWhoFulfilled() + " on " + request.printDate() + "."
                        + "\nDetails: " + request.getSpecifics()
                        + this.signature);
            }

            if (this.isValidEmailAddress(request.getEmail())) {
                Transport.send(message);
                System.out.println("Email sent");
            } else {
                System.out.println("Invalid email address.");
            }

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends a text version of the message via ##########@txt.att.net
     * in future, can ask user for their carrier and set the text address that way
     * or, I will just look into a library for sending SMS messages
     */
    public void sendRequestText(Request request) {
        this.login();
        try {

            Message message = new MimeMessage(this.session);
            message.setFrom(new InternetAddress(this.username));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(request.getPhoneNumber() + "@txt.att.net"));
            //message.setSubject("Service Request Notification");
            //this.setGreeting(this.request.getFirstName(), this.request.getLastName());

            if (request.isFulfilled() == 0) { // is not fulfilled
                message.setText(this.greeting + "Your " + request.getType() + " request to room " + request.getRoomName() + " has been submitted."
                        + "\nDetails: " + request.getSpecifics()
                        + this.signature);
            } else { // is fulfilled
                message.setText(this.greeting + "Your " + request.getType() + "  request to room " + request.getRoomName() + " has been fulfilled by " + request.getNameWhoFulfilled() + " on " + request.printDate() + "."
                        + "\nDetails: " + request.getSpecifics()
                        + this.signature);
            }

            if (this.isValidEmailAddress(request.getPhoneNumber() + "@txt.att.net")) {
                Transport.send(message);
                System.out.println("Text sent");
            } else {
                System.out.println("Invalid text address.");
            }

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * gets the absolutepath from a snapdata
     */
    public String getSnapData(SnapData data){
        String pathname = ("../../image" + data.getIndexNum() + ".png");
        File file = new File(pathname);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(data.getImage(), null), "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

}