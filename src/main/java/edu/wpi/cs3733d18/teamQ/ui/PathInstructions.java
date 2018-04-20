package edu.wpi.cs3733d18.teamQ.ui;

import edu.wpi.cs3733d18.teamQ.ui.Controller.SnapData;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PathInstructions {

    public PathInstructions(){

    }

    public static SnapData captureAndSaveDisplay(AnchorPane backImagePane, double width, double height, int indexValue){
        WritableImage image = backImagePane.snapshot(new SnapshotParameters(), new WritableImage((int)width, (int)height ));
        return new SnapData(image, indexValue);

    }

    public void generatePathEmail(ArrayList<SnapData> allFiles,String textDirections, String email){
        Email attachment = new Email();
        attachment.sendDirections(allFiles, email, textDirections);
    }

    public String buildString(ArrayList<String> TxtInst){
        String str = "";
        for(int i=0;i<TxtInst.size();i++){
            str = str + TxtInst.get(i) + "\n";
        }
        System.out.println(str);
        return str;
    }
}
