package edu.wpi.cs3733d18.teamQ.IntegrationTests;

import edu.wpi.cs3733d18.teamQ.pathfinding.Edge;
import edu.wpi.cs3733d18.teamQ.pathfinding.Node;
import edu.wpi.cs3733d18.teamQ.ui.Controller.EditMapController;
import edu.wpi.cs3733d18.teamQ.ui.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import java.util.ArrayList
        ;

import static edu.wpi.cs3733d18.teamQ.manageDB.DatabaseSystem.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class EditMapScreenIntegrationTest extends ApplicationTest {
    private final double EXPECTED_X_COORD = 1740.0;
    private final double EXPECTED_Y_COORD = 1416.0;
    private final int MOVE_BY = -270;
    private final String PATH_TO_EDITMAPFXML = "/fxmlFiles/EditMap.fxml";
    private static User user;
    private EditMapController editCont;
    /**
     * Creates the stages and loads initial user data
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception{
        user.setPrimaryStage(stage);
        user.initLoadMaps();
        user.setNodes(getNodes());
        user.setEdges(getEdges());

        FXMLLoader editMapLoader = new FXMLLoader(getClass().getResource(PATH_TO_EDITMAPFXML));
        Parent editMapParent = editMapLoader.load();
        FxToolkit.setupStage(thisStage -> stage.setScene(new Scene(editMapParent)));
        //stage.setScene(new Scene(editMapParent));
        //Scene pathfindingScene = sdUtil.prodAndBindScene(editMapParent, stage);
        editCont = editMapLoader.getController();
        //empEditCont.setListen(stage);

        stage.setMinWidth(735);
        stage.setMinHeight(645);
        //stage.setScene(pathfindingScene);
        stage.show();
        stage.toFront();
    }


    /*
    *This rule is because some exceptions need to be expected but the rest of the code needs to work
     */
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @BeforeClass
    public static void initialize(){
        user = User.getUser();
        initializeDb();
        /*System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");*/
    }

    @Override
    public void stop() throws Exception{
        FxToolkit.cleanupStages();
    }

    /**
     * Moves the cursor to the top right of the map
     */
    private void moveToNodeLocation(){
        clickOn("#mapFrame2D");
        moveBy(MOVE_BY, MOVE_BY);
    }


    /**
     * Script for adding a node. Automates inputing all of the data
     */
    public String addNodeInputs(){
        String id = editCont.generateID("REST");
        doubleClickOn("#nameField");
        write("testNode");
        //clickOn("#x2dNode_toEdgeField");
        //write("1.0");
        //clickOn("#y2dNode_fromEdgeField");
        //write("1.0");
        doubleClickOn("#x3dField");
        write("1.0");
        doubleClickOn("#y3dField");
        write("1.0");
        doubleClickOn("#typeField");
        write("REST");
        clickOn("#addButton");
        return id;
    }

    /**
     * Tests that a node can be edited as expected by a user
     */
    @Test
    public void editNodeTest(){
        switchToNode();
        moveToNodeLocation();
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        String id = addNodeInputs();

        clickOn("#mapFrame2D");
        moveBy(MOVE_BY, MOVE_BY);
        press(MouseButton.PRIMARY);
        moveBy(0.0, 99.0);
        release(MouseButton.PRIMARY);
        doubleClickOn("#y2dNode_fromEdgeField");
        //clickOn("#addButton");
        Node fetched = user.getNode(id);
        System.out.println(fetched.getyPos());
        assertTrue(fetched.getyPos().equals(1614.0));
        clickOn("#mapFrame2D");
        moveBy(MOVE_BY, MOVE_BY);
        moveBy(0.0, 99.0);
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        clickOn("#removeButton");
        clickOn("#clearButton");
    }

    /**
     * Tests that a node can be added as expected by a user
     */
    @Test
    public void addNodeTest(){
        //Add node
        switchToNode();
        moveToNodeLocation();
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        String id = addNodeInputs();

        //Select and assert the node is correct
        Node fetchedNode = user.getNode(id);
        System.out.println(fetchedNode.getyPos());
        assertTrue(fetchedNode.getyPos().equals(EXPECTED_Y_COORD));
        assertTrue(fetchedNode.getxPos().equals(EXPECTED_X_COORD));
        assertTrue(fetchedNode.getyPos3D().equals(1.0));
        assertTrue(fetchedNode.getxPos3D().equals(1.0));
        assertEquals(fetchedNode.getType(), "REST");
        clickOn("#mapFrame2D");

        //Remove node
        moveBy(MOVE_BY, MOVE_BY);
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        clickOn("#removeButton");
        clickOn("#clearButton");
    }

    /**
     * Tests that a node can be deleted as expected by a user
     */
    @Test
    public void deleteNodeTest(){
        switchToNode();
        moveToNodeLocation();
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        String id = addNodeInputs();
        clickOn("#mapFrame2D");
        moveBy(MOVE_BY, MOVE_BY);
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        clickOn("#removeButton");
        assertEquals(user.getNode(id), null);
    }

    /**
     * Scripts switching to the node edit mode as the user would
     */
    public void switchToNode(){
        clickOn("#selector");
        press(KeyCode.UP);
        release(KeyCode.UP);
        press(KeyCode.UP);
        release(KeyCode.UP);
        press(KeyCode.DOWN);
        release(KeyCode.DOWN);
        press(KeyCode.UP);
        release(KeyCode.UP);
        press(KeyCode.ENTER);
        release(KeyCode.ENTER);

    }

    /**
     * Scripts switching to edge edit mode as the user would
     */
    public void switchToEdge(){
        clickOn("#selector");
        press(KeyCode.UP);
        release(KeyCode.UP);
        press(KeyCode.UP);
        release(KeyCode.UP);
        press(KeyCode.DOWN);
        release(KeyCode.DOWN);
        press(KeyCode.DOWN);
        release(KeyCode.DOWN);
        press(KeyCode.ENTER);
        release(KeyCode.ENTER);
    }

    /**
     * Tests that an edge can be added as expected by a user
     */
    @Test
    public void addEdgeTest(){
        switchToNode();
        //ADD THE NODES
        moveToNodeLocation();
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        String id1 = addNodeInputs();
        clickOn("#clearButton");

        moveToNodeLocation();
        moveBy(0.0, 99.0);
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        String id2 = addNodeInputs();


        switchToEdge();
        //SELECT THE NODES
        clickOn("#mapFrame2D");
        moveBy(MOVE_BY, MOVE_BY);
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        moveBy(0.0, 99.0);
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        //ADD THE EDGE
        clickOn("#addButton");
        boolean edgeFound = false;
        ArrayList<Edge> edges = user.getEdges();
        for(int i = 0; i < edges.size(); i++){
            if(edges.get(i).getStartNode().getNodeID().equals(id1) || edges.get(i).getEndNode().getNodeID().equals(id1)){
                if (edges.get(i).getStartNode().getNodeID().equals(id2) || edges.get(i).getEndNode().getNodeID().equals(id2)){
                    edgeFound = true;
                }
            }
        }
        assertTrue(edgeFound);
        //TEARDOWN -- REMOVE EDGE
        clickOn("#removeButton");
        switchToNode();
        //TEARDOWN -- REMOVE NODES
        clickOn("#mapFrame2D");
        moveBy(MOVE_BY, MOVE_BY);
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        clickOn("#removeButton");
        clickOn("#mapFrame2D");
        moveBy(MOVE_BY, MOVE_BY);
        moveBy(0.0, 99.0);
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        clickOn("#removeButton");
    }

    /**
     * Tests that an edge can be removed as expected by a user
     */
    @Test
    public void removeEdgeTest(){
        switchToNode();
        //ADD THE NODES
        moveToNodeLocation();
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        String id1 = addNodeInputs();
        clickOn("#clearButton");

        moveToNodeLocation();
        moveBy(0.0, 99.0);
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        String id2 = addNodeInputs();

        switchToEdge();
        //SELECT THE NODES
        clickOn("#mapFrame2D");
        moveBy(MOVE_BY, MOVE_BY);
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        moveBy(0.0, 99.0);
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        //ADD THE EDGE
        clickOn("#addButton");
        clickOn("#mapFrame2D");
        moveBy(MOVE_BY, MOVE_BY);
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        moveBy(0.0, 99.0);
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        clickOn("#removeButton");

        boolean edgeFound = false;
        ArrayList<Edge> edges = user.getEdges();
        for(int i = 0; i < edges.size(); i++){
            if(edges.get(i).getStartNode().getNodeID().equals(id1) || edges.get(i).getEndNode().getNodeID().equals(id1)){
                if (edges.get(i).getStartNode().getNodeID().equals(id2) || edges.get(i).getEndNode().getNodeID().equals(id2)){
                    edgeFound = true;
                }
            }
        }
        assertTrue(!edgeFound);

        //Teardown -- remove the nodes
        switchToNode();
        clickOn("#mapFrame2D");
        moveBy(MOVE_BY, MOVE_BY);
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        clickOn("#removeButton");
        clickOn("#mapFrame2D");
        moveBy(MOVE_BY, MOVE_BY);
        moveBy(0.0, 99.0);
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        clickOn("#removeButton");
    }
}
