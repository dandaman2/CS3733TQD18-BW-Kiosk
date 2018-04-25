package edu.wpi.cs3733d18.teamQ.IntegrationTests.UI;
import edu.wpi.cs3733d18.teamQ.ui.Controller.PathfindingCont;
import edu.wpi.cs3733d18.teamQ.ui.Controller.ScreenUtil;
import edu.wpi.cs3733d18.teamQ.ui.User;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static edu.wpi.cs3733d18.teamQ.manageDB.DatabaseSystem.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertEquals;

public class PathfinderScreenIntegrationTest extends ApplicationTest {
    private final String PATH_TO_PATHFINDERFXML = "/fxmlFiles/PathfindingScreen.fxml";
    private final String SAME_FLOOR_SAME_BUILDING_NODE_ID  = "GDEPT00702";
    private String kioskNodeID = "GELEV00N02";
    private String oneFloorFirstNodeID = "GDEPT02402";
    private String oneFloorSecondNodeID = "GHALL01702";
    private String oneFloorThirdNodeID = "GHALL01602";
    private String oneFloorFourthNodeID = "GHALL01502";
    private String oneFloorFifthNodeID = "GHALL01302";
    private String oneFloorSixthNodeID = "GHALL01102";
    private final String DIFFERENT_BUILDING_NODE_ID = "WHALL00702";
    private String multiBuildingFirstID = "GSTAI02602";
    private String multiBuildingSecondID = "GHALL02702";
    private String multiBuildingThirdID = "GHALL02302";
    private String multiBuildingFourthID = "GHALL02202";
    private String multiBuildingFifthID = "WHALL00802";

    private final String DIFFERENT_FLOOR_NODE_ID = "GDEPT01403";
    private String elevOneFloorUp = "GELEV00N03";

    private String firstBathroomID = "GDEPT02402";
    private String secondBathroomID = "GHALL03002";
    private final String nearestBathroomID = "GREST03102";

    private String firstExitID = "GELEV00N01";
    private String secondExitID = "GHALL02401";
    private String thirdExitID = "GSERV02301";
    private String fourthExitID = "GHALL02201";
    private String fifthExitID = "GHALL02101";
    private String sixthExitID = "GHALL00301";
    private String seventhExitID = "GHALL00201";
    private String exitID = "GEXIT00101";

    private PathfindingCont pathCont;
    private ScreenUtil sdUtil = new ScreenUtil();
    private static User user;

    /**
     * Initializes the stage, controller, and much of the user data
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception{
        user.setPrimaryStage(stage);
        user.initLoadMaps();
        user.setNodes(getNodes());
        user.setEdges(getEdges());

        FXMLLoader pathfinderLoader = new FXMLLoader(getClass().getResource(PATH_TO_PATHFINDERFXML));
        Parent pathfinderParent = pathfinderLoader.load();
        FxToolkit.setupStage(thisStage -> stage.setScene(new Scene(pathfinderParent)));

        //stage.setScene(new Scene(pathfinderParent));
        //Scene pathfindingScene = sdUtil.prodAndBindScene(pathfinderParent, stage);
        pathCont = pathfinderLoader.getController();
        //pathCont.setListen(stage);

        stage.setMinWidth(735);
        stage.setMinHeight(645);
        //stage.setScene(pathfindingScene);
        stage.show();
        stage.toFront();
    }

    /**
     * Needed to make sure that the test can handle an exception without exiting
     */
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    /**
     * Initializes the user and database
     */
    @BeforeClass
    public static void initialize(){
        user = User.getUser();
        initializeDb();
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");
    }

    @Override
    public void stop() throws Exception{
        FxToolkit.cleanupStages();
    }

    /**
     * Scripts the input for pathing to a node as a user would
     * @param nodeID the node to path to
     */
    public void getDirectionsToNode(String nodeID){
        //**Should** enter the search bar, write a node id, then search it
        clickOn("#topBar");
        moveBy(150, 0);
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        write(nodeID);
        try{
            Thread.sleep(200);
        }catch (Exception e){

        }
        push(KeyCode.DOWN);
        release(KeyCode.DOWN);
        push(KeyCode.ENTER);
        release(KeyCode.ENTER);
        //clickOn("#searchButt");
        //clickOn("#optionsHome");
    }

    /**
     * Tests that a single floor path can be generated as expected by a user
     */
    @Test
    public void testSingleFloorPath(){
        ArrayList<String> pathIDs = new ArrayList<String>();
        pathIDs.add(kioskNodeID);
        pathIDs.add(oneFloorFirstNodeID);
        pathIDs.add(oneFloorSecondNodeID);
        pathIDs.add(oneFloorThirdNodeID);
        pathIDs.add(oneFloorFourthNodeID);
        pathIDs.add(oneFloorFifthNodeID);
        pathIDs.add(oneFloorSixthNodeID);
        pathIDs.add(SAME_FLOOR_SAME_BUILDING_NODE_ID);

        getDirectionsToNode(SAME_FLOOR_SAME_BUILDING_NODE_ID);
        ArrayList<edu.wpi.cs3733d18.teamQ.pathfinding.Node> path = pathCont.queuedPath;
        for(int i = 0; i < path.size(); i++){
            assertEquals(path.get(i).getNodeID(), pathIDs.get(i));
        }
        //exception.expect(Exception.class);
    }

    /**
     * Tests that a multi floor path can be generated as expected by the user
     */
    @Test
    public void testMultiFloorPath(){
        ArrayList<String> pathIDs = new ArrayList<String>();
        pathIDs.add(kioskNodeID);
        pathIDs.add(elevOneFloorUp);
        pathIDs.add(DIFFERENT_FLOOR_NODE_ID);

        getDirectionsToNode(DIFFERENT_FLOOR_NODE_ID);
        ArrayList<edu.wpi.cs3733d18.teamQ.pathfinding.Node> path = pathCont.queuedPath;
        for(int i = 0; i < path.size(); i++){
            assertEquals(path.get(i).getNodeID(), pathIDs.get(i));
        }

    }

    /**
     * Tests that a multi building path can be generated as expected by the user
     */
    @Test
    public void testMultiBuildingPath(){
        ArrayList<String> pathIDs = new ArrayList<String>();
        pathIDs.add(kioskNodeID);
        pathIDs.add(multiBuildingFirstID);
        pathIDs.add(multiBuildingSecondID);
        pathIDs.add(multiBuildingThirdID);
        pathIDs.add(multiBuildingFourthID);
        pathIDs.add(multiBuildingFifthID);
        pathIDs.add(DIFFERENT_BUILDING_NODE_ID);

        getDirectionsToNode(DIFFERENT_BUILDING_NODE_ID);
        ArrayList<edu.wpi.cs3733d18.teamQ.pathfinding.Node> path = pathCont.queuedPath;
        for(int i = 0; i < path.size(); i++){
            assertEquals(path.get(i).getNodeID(), pathIDs.get(i));
        }
    }

    /*@Test
    public void changeFloorTests(){
        clickOn("#floorL21");
        assertEquals(-2, pathCont.getCurrFloor());
        clickOn("#floorL11");
        assertEquals(-1, pathCont.getCurrFloor());
        clickOn("#floor11");
        assertEquals(1, pathCont.getCurrFloor());
        clickOn("#floor21");
        assertEquals(2, pathCont.getCurrFloor());
        clickOn("#floor31");
        assertEquals(3, pathCont.getCurrFloor());
    }*/
}
