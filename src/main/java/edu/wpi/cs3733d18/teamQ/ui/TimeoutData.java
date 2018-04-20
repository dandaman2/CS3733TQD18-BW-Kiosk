package edu.wpi.cs3733d18.teamQ.ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.IOException;

import static edu.wpi.cs3733d18.teamQ.manageDB.DatabaseSystem.runningFromIntelliJ;

public class TimeoutData {

    //The idle time duration
    Timeline timeline;
    final double IDLEDURATION = 60000; //60 seconds

    User user = User.getUser();


    public void initTimer(AnchorPane pane) {
        try {
            timeline.stop();
        } catch (NullPointerException ex) {
            System.out.println("timeline not stoppin");
        }
        timeline = new Timeline(new KeyFrame(
                Duration.millis(IDLEDURATION),
                ae -> user.goToWelcome()));

        pane.addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                timeline.playFromStart();
            }
        });
        timeline.playFromStart();


    }



}
