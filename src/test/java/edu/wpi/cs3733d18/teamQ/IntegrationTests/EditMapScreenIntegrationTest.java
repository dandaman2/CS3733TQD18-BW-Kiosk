package edu.wpi.cs3733d18.teamQ.IntegrationTests;

import edu.wpi.cs3733d18.teamQ.pathfinding.Edge;
import edu.wpi.cs3733d18.teamQ.pathfinding.Node;
import edu.wpi.cs3733d18.teamQ.ui.Controller.EditMapController;
import edu.wpi.cs3733d18.teamQ.ui.Controller.ScreenUtil;
import edu.wpi.cs3733d18.teamQ.ui.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.testfx.framework.junit.ApplicationTest;

import java.util.ArrayList
        ;

import static edu.wpi.cs3733d18.teamQ.manageDB.DatabaseSystem.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class EditMapScreenIntegrationTest extends ApplicationTest {
    //TODO put in the right path
    private final String PATH_TO_EDITMAPFXML = "/fxmlFiles/EditMap.fxml";
    private static User user;
    private EditMapController editCont;
    ScreenUtil sdUtil = new ScreenUtil();

    /**
     * Creates the stages and loads initial user data
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception{
      /*  user.setPrimaryStage(stage);
        user.initLoadMaps();
        user.setNodes(getNodes());
        user.setEdges(getEdges());

        FXMLLoader editMapLoader = new FXMLLoader(getClass().getResource(PATH_TO_EDITMAPFXML));
        Parent editMapParent = editMapLoader.load();
        stage.setScene(new Scene(editMapParent));
        //Scene pathfindingScene = sdUtil.prodAndBindScene(editMapParent, stage);
        editCont = editMapLoader.getController();
        //empEditCont.setListen(stage);

        stage.setMinWidth(735);
        stage.setMinHeight(645);
        //stage.setScene(pathfindingScene);
        stage.show();
        stage.toFront();*/
    }

    /*
    *This rule is because some exceptions need to be expected but the rest of the code needs to work
     */
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @BeforeClass @Ignore
    public static void initialize(){
        user = User.getUser();
        initializeDb();
    }

    /**
     * Script for adding a node. Automates inputing all of the data
     */
    public void addNodeInputs(){
        clickOn("#mapFrame2D");
        moveBy(-375, -310);
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
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
    }

    /**
     * Tests that a node can be edited as expected by a user
     */
    @Test @Ignore
    public void editNodeTest(){
        switchToNode();
        addNodeInputs();

        clickOn("#mapFrame2D");
        moveBy(-375, -310);
        press(MouseButton.PRIMARY);
        moveBy(0.0, 99.0);
        release(MouseButton.PRIMARY);
        doubleClickOn("#y2dNode_fromEdgeField");
        //clickOn("#addButton");
        Node fetched = user.getNode("QREST05902");
        System.out.println(fetched.getyPos());
        assertTrue(fetched.getyPos().equals(122.0));
        clickOn("#mapFrame2D");
        moveBy(-375, -310);
        moveBy(0.0, 99.0);
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        clickOn("#removeButton");
        clickOn("#clearButton");
    }

    /**
     * Tests that a node can be added as expected by a user
     */
    @Test @Ignore
    public void addNodeTest(){
        switchToNode();
        addNodeInputs();

        Node fetchedNode = user.getNode("QREST05902");
        assertTrue(fetchedNode.getyPos().equals(23.0));
        assertTrue(fetchedNode.getxPos().equals(9.0));
        assertTrue(fetchedNode.getyPos3D().equals(1.0));
        assertTrue(fetchedNode.getxPos3D().equals(1.0));
        assertEquals(fetchedNode.getType(), "REST");
        clickOn("#mapFrame2D");

        moveBy(-375, -310);
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        clickOn("#removeButton");
        clickOn("#clearButton");
    }

    /**
     * Tests that a node can be deleted as expected by a user
     */
    @Test @Ignore
    public void deleteNodeTest(){
        switchToNode();
        addNodeInputs();
        clickOn("#mapFrame2D");
        moveBy(-375, -310);
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        clickOn("#removeButton");
        assertEquals(user.getNode("QREST05902"), null);
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
    @Test @Ignore
    public void addEdgeTest(){
        switchToNode();
        //ADD THE NODES
        addNodeInputs();
        clickOn("#clearButton");
        addNodeInputs();
        clickOn("#mapFrame2D");
        moveBy(-375, -310);
        press(MouseButton.PRIMARY);
        moveBy(0.0, 99.0);
        release(MouseButton.PRIMARY);
        switchToEdge();
        //SELECT THE NODES
        clickOn("#mapFrame2D");
        moveBy(-375, -310);
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
            if(edges.get(i).getStartNode().getNodeID().equals("QREST05902") || edges.get(i).getEndNode().getNodeID().equals("QREST05902")){
                if (edges.get(i).getStartNode().getNodeID().equals("QREST06002") || edges.get(i).getEndNode().getNodeID().equals("QREST06002")){
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
        moveBy(-375, -310);
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        clickOn("#removeButton");
        clickOn("#mapFrame2D");
        moveBy(-375, -310);
        moveBy(0.0, 99.0);
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        clickOn("#removeButton");
    }

    /**
     * Tests that an edge can be removed as expected by a user
     */
    @Test @Ignore
    public void removeEdgeTest(){
        switchToNode();
        //ADD THE NODES
        addNodeInputs();
        clickOn("#clearButton");
        addNodeInputs();
        clickOn("#mapFrame2D");
        moveBy(-375, -310);
        press(MouseButton.PRIMARY);
        moveBy(0.0, 99.0);
        release(MouseButton.PRIMARY);

        switchToEdge();
        //SELECT THE NODES
        clickOn("#mapFrame2D");
        moveBy(-375, -310);
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        moveBy(0.0, 99.0);
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        //ADD THE EDGE
        clickOn("#addButton");
        clickOn("#mapFrame2D");
        moveBy(-375, -310);
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        moveBy(0.0, 99.0);
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        clickOn("#removeButton");

        boolean edgeFound = false;
        ArrayList<Edge> edges = user.getEdges();
        for(int i = 0; i < edges.size(); i++){
            if(edges.get(i).getStartNode().getNodeID().equals("QREST05902") || edges.get(i).getEndNode().getNodeID().equals("QREST05902")){
                if (edges.get(i).getStartNode().getNodeID().equals("QREST06002") || edges.get(i).getEndNode().getNodeID().equals("QREST06002")){
                    edgeFound = true;
                }
            }
        }
        assertTrue(!edgeFound);

        //Teardown -- remove the nodes
        switchToNode();
        clickOn("#mapFrame2D");
        moveBy(-375, -310);
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        clickOn("#removeButton");
        clickOn("#mapFrame2D");
        moveBy(-375, -310);
        moveBy(0.0, 99.0);
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        clickOn("#removeButton");
    }
}
