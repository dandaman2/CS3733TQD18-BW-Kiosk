package edu.wpi.cs3733d18.teamQ.ui;

import edu.wpi.cs3733d18.teamQ.pathfinding.Node;
import edu.wpi.cs3733d18.teamQ.ui.Controller.PathfindingCont;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;

public class ZoomPane {
    IZoomableCont controller;
    PathfindingCont pf;
    private boolean isLocked = false;

//    public ZoomPane(PathfindingController pf) {
//        this.pf = pf;
//    }
    public  ScrollPane scroller;
    public ZoomPane(IZoomableCont controller) {
        this.controller = controller;
    }

    public void setController(IZoomableCont z){
        controller = z;
    }

    public Parent createZoomPane(final ImageView group) {
        final double SCALE_DELTA = 1.1;
        final StackPane zoomPane = new StackPane();

        zoomPane.getChildren().add(group);

        scroller = new ScrollPane();
        final Group scrollContent = new Group(zoomPane);
        scroller.setContent(scrollContent);

        scroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroller.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);


        scroller.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> observable,
                                Bounds oldValue, Bounds newValue) {
                zoomPane.setMinSize(newValue.getWidth(), newValue.getHeight());
//                controller.updateDrawings();
            }
        });

        scroller.setPrefViewportWidth(256);
        scroller.setPrefViewportHeight(256);

        group.setScaleX(1.4);
        group.setScaleY(1.4);
        centerScroll();

        zoomPane.setOnScroll(new EventHandler<ScrollEvent>() {
            double minX;
            double minY;
            double minScale;
            double maxScale;
            double newScale;

            @Override
            public void handle(ScrollEvent event) {
                if (!isLocked) {

                    event.consume();

                    if (event.getDeltaY() == 0) {
                        return;
                    }

                    double scaleFactor = (event.getDeltaY() > 0) ? SCALE_DELTA
                            : 1 / SCALE_DELTA;

                    // amount of scrolling in each direction in scrollContent coordinate
                    // units
                    Point2D scrollOffset = figureScrollOffset(scrollContent, scroller);
                    newScale = group.getScaleX() * scaleFactor;

                    // looks to see which of x and y will fill the screen first and sets that as the min scale
                    // with default as Y
                    minY = 1387 / scroller.getHeight();
                    minX = 2500 / scroller.getWidth();

                    minScale = minY;
                    if (minY > minX)
                        minScale = minX;

                    maxScale = 3 * minScale;

                    if (newScale < minScale)
                        newScale = minScale;
                    else if (newScale > maxScale)
                        newScale = maxScale;

                    group.setScaleX(newScale);
                    group.setScaleY(newScale);
//                pf.manImage.setScaleY(newScale);
//                pf.manImage.setScaleX(newScale);


                    // move viewport so that old center remains in the center after the
                    // scaling
                    repositionScroller(scrollContent, scroller, scaleFactor, scrollOffset);

                    controller.updateDrawings();

                    System.out.println(" Max: " + maxScale + " Min: " + minScale + " Scale: " + group.getScaleX() + " width: " + scroller.getWidth() + " height: " + scroller.getHeight());
                }
            }

        });

        // Panning via drag....
        final ObjectProperty<Point2D> lastMouseCoordinates = new SimpleObjectProperty<Point2D>();
        scrollContent.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(!isLocked) {
                    lastMouseCoordinates.set(new Point2D(event.getX(), event.getY()));
                    controller.updateDrawings();
                }
            }
        });

        scrollContent.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(!isLocked) {
                    double deltaX = event.getX() - lastMouseCoordinates.get().getX();
                    double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
                    double deltaH = deltaX * (scroller.getHmax() - scroller.getHmin()) / extraWidth;
                    double desiredH = scroller.getHvalue() - deltaH;
                    scroller.setHvalue(Math.max(0, Math.min(scroller.getHmax(), desiredH)));

                    double deltaY = event.getY() - lastMouseCoordinates.get().getY();
                    double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
                    double deltaV = deltaY * (scroller.getHmax() - scroller.getHmin()) / extraHeight;
                    double desiredV = scroller.getVvalue() - deltaV;
                    scroller.setVvalue(Math.max(0, Math.min(scroller.getVmax(), desiredV)));
                    controller.updateDrawings();
                }
            }
        });
        return scroller;
    }

    public Point2D figureScrollOffset(javafx.scene.Node scrollContent, ScrollPane scroller) {
        double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
        double hScrollProportion = (scroller.getHvalue() - scroller.getHmin()) / (scroller.getHmax() - scroller.getHmin());
        double scrollXOffset = hScrollProportion * Math.max(0, extraWidth);
        double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
        double vScrollProportion = (scroller.getVvalue() - scroller.getVmin()) / (scroller.getVmax() - scroller.getVmin());
        double scrollYOffset = vScrollProportion * Math.max(0, extraHeight);
        return new Point2D(scrollXOffset, scrollYOffset);
    }

    public void repositionScroller(javafx.scene.Node scrollContent, ScrollPane scroller, double scaleFactor, Point2D scrollOffset) {
        double scrollXOffset = scrollOffset.getX();
        double scrollYOffset = scrollOffset.getY();
        double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
        if (extraWidth > 0) {
            double halfWidth = scroller.getViewportBounds().getWidth() / 2 ;
            double newScrollXOffset = (scaleFactor - 1) *  halfWidth + scaleFactor * scrollXOffset;
            scroller.setHvalue(scroller.getHmin() + newScrollXOffset * (scroller.getHmax() - scroller.getHmin()) / extraWidth);
        } else {
            scroller.setHvalue(scroller.getHmin());
        }
        double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
        if (extraHeight > 0) {
            double halfHeight = scroller.getViewportBounds().getHeight() / 2 ;
            double newScrollYOffset = (scaleFactor - 1) * halfHeight + scaleFactor * scrollYOffset;
            scroller.setVvalue(scroller.getVmin() + newScrollYOffset * (scroller.getVmax() - scroller.getVmin()) / extraHeight);
        } else {
            scroller.setHvalue(scroller.getHmin());
        }
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public void disableScroll(){
        scroller.setDisable(true);
    }
    public void enableScroll(){
        scroller.setDisable(false);
    }

    //centers the scrollers to the middle of the image
    public void centerScroll(){
       double vvmiddle = (scroller.getVmax() - scroller.getVmin())/2;
       double hhmiddle = (scroller.getHmax() - scroller.getHmin())/2;

       scroller.setVvalue(vvmiddle);
       scroller.setHvalue(hhmiddle);
    }

    //centers the scroller to the average path location
    public void centerScrollToPath(ArrayList<Node> path, int floor, boolean is2D){
        double avgXCoord=0;
        double avgYCoord=0;
        double numNodes=0;
        for(Node n : path){
            if(n.getFloor() == floor){
                numNodes++;
                if(is2D){
                    avgXCoord+=n.getxPos();
                    avgYCoord+=n.getyPos();
                }
                else{
                    avgXCoord+=n.getxPos3D();
                    avgYCoord+=n.getyPos3D();
                }
            }
        }
        if(avgXCoord+avgYCoord+numNodes>0){
            double imgWidth = 5000;
            double imgHeight;
            if(is2D){
                imgHeight=3400;
            }
            else{
                imgHeight = 2774;
            }
            scroller.setVvalue(avgYCoord/numNodes/imgHeight);
            scroller.setHvalue(avgXCoord/numNodes/imgWidth);
            System.out.println("x pos: " + avgXCoord);
            System.out.println("y pos: " + avgYCoord);
            System.out.println("H middle: " + (scroller.getHmax() - scroller.getHmin())/2);
            System.out.println("V middle: " + (scroller.getVmax() - scroller.getVmin())/2);
            System.out.println("Nodes: " + numNodes);
        }

    }

}
