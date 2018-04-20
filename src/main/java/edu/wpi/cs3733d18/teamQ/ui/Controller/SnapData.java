package edu.wpi.cs3733d18.teamQ.ui.Controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class SnapData {
    private WritableImage image;
    private int indexNum;

    public SnapData(WritableImage image, int indexNum) {
        this.image = image;
        this.indexNum = indexNum;
    }

    public String imageToString(){
        String pathname = ("../../image" + indexNum + ".png");
        File file = new File(pathname);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    public WritableImage getImage() {
        return image;
    }

    public void setImage(WritableImage image) {
        this.image = image;
    }

    public int getIndexNum() {
        return indexNum;
    }

    public void setIndexNum(int indexNum) {
        this.indexNum = indexNum;
    }

}
