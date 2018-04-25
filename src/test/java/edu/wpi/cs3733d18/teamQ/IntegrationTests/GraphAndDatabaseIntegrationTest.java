package edu.wpi.cs3733d18.teamQ.IntegrationTests;

import edu.wpi.cs3733d18.teamQ.manageDB.DatabaseSystem;
import edu.wpi.cs3733d18.teamQ.pathfinding.Astar;
import org.junit.AfterClass;
import edu.wpi.cs3733d18.teamQ.pathfinding.Graph;
import edu.wpi.cs3733d18.teamQ.pathfinding.Edge;
import org.junit.BeforeClass;
import org.junit.Test;
import edu.wpi.cs3733d18.teamQ.pathfinding.Node;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class GraphAndDatabaseIntegrationTest {
    private static DatabaseSystem databaseSystem;
    private static Graph testGraph;

    //TODO implement actual Node ID's from DB
    //TODO rename testNode names to be more clear
    //For one floor
    private String kioskNodeID = "GELEV00N02";
    private String oneFloorFirstNodeID = "GDEPT02402";
    private String oneFloorSecondNodeID = "GHALL01702";
    private String oneFloorThirdNodeID = "GHALL01602";
    private String oneFloorFourthNodeID = "GHALL01502";
    private String oneFloorFifthNodeID = "GHALL01302";
    private String oneFloorSixthNodeID = "GHALL01102";
    private String sameFloorDestinationID = "GDEPT00702";

    //For multi floor
    private String elevOneFloorUp = "GELEV00N03";
    private String thirdFloorDestination = "GDEPT01403";

    //For multi building
    private String multiBuildingFirstID = "GSTAI02602";
    private String multiBuildingSecondID = "GHALL02702";
    private String multiBuildingThirdID = "GHALL02302";
    private String multiBuildingFourthID = "GHALL02202";
    private String multiBuildingFifthID = "WHALL00802";
    private String nextBuildingDest = "WHALL00702";

    /**
     * Initialize the database and the graph. The graph is initialized with
     * our actual data.
     */
    @BeforeClass
    public static void initialize(){
        databaseSystem = new DatabaseSystem();
        databaseSystem.initializeDb();

        testGraph = new Astar();
    }

    @AfterClass
    public static void shutdown(){
        databaseSystem.closeDB();
    }

    /**
     * Tests the paths are correctly generated on one floor
     */
    @Test
    public void testOneFloorPath(){
        ArrayList<String> noType = new ArrayList<>();
        Node startNode = databaseSystem.getNode(kioskNodeID);
        Node endNode = databaseSystem.getNode(sameFloorDestinationID);
        ArrayList<Node> expectedPath = new ArrayList<>();
        expectedPath.add(startNode);
        expectedPath.add(databaseSystem.getNode(oneFloorFirstNodeID));
        expectedPath.add(databaseSystem.getNode(oneFloorSecondNodeID));
        expectedPath.add(databaseSystem.getNode(oneFloorThirdNodeID));
        expectedPath.add(databaseSystem.getNode(oneFloorFourthNodeID));
        expectedPath.add(databaseSystem.getNode(oneFloorFifthNodeID));
        expectedPath.add(databaseSystem.getNode(oneFloorSixthNodeID));
        expectedPath.add(endNode);
        testGraph.init2(databaseSystem.getNodes(), databaseSystem.getEdges());
        ArrayList<Node> foundPath = testGraph.findPath(startNode, endNode, noType);

        assertEquals(expectedPath.size(), foundPath.size());
        for(int i = 0; i < expectedPath.size(); i++){
            assertEquals(expectedPath.get(i).getNodeID(), foundPath.get(i).getNodeID());
        }
    }

    /**
     * Tests that a path can be generated from an existing node to a newly created one
     */
    @Test
    public void testAddedNodePath(){
        Node testAddNode = new Node("TEST", 0.0, 0.0, 0, "Test Building",
                "Test Node", "Test Node Long", "Test Node Short", "Team Q", 0.0, 0.0, 0.0);
        databaseSystem.addNode(testAddNode);
        Node startNode = databaseSystem.getNode(kioskNodeID);
        Node endNode = databaseSystem.getNode(sameFloorDestinationID);
        Edge testAddEdge = new Edge("TestEdge", endNode, testAddNode, 1.0);
        databaseSystem.addEdge(testAddEdge);

        ArrayList<String> noType = new ArrayList<>();
        ArrayList<Node> expectedPath = new ArrayList<>();
        expectedPath.add(startNode);
        expectedPath.add(databaseSystem.getNode(oneFloorFirstNodeID));
        expectedPath.add(databaseSystem.getNode(oneFloorSecondNodeID));
        expectedPath.add(databaseSystem.getNode(oneFloorThirdNodeID));
        expectedPath.add(databaseSystem.getNode(oneFloorFourthNodeID));
        expectedPath.add(databaseSystem.getNode(oneFloorFifthNodeID));
        expectedPath.add(databaseSystem.getNode(oneFloorSixthNodeID));
        expectedPath.add(endNode);
        expectedPath.add(testAddNode);

        testGraph.init2(databaseSystem.getNodes(), databaseSystem.getEdges());
        ArrayList<Node> foundPath = testGraph.findPath(startNode, testAddNode, noType);

        assertEquals(expectedPath.size(), foundPath.size());
        for(int i = 0; i < expectedPath.size(); i++){
            assertEquals(expectedPath.get(i).getNodeID(), foundPath.get(i).getNodeID());
        }

        databaseSystem.removeNode(testAddNode);
        databaseSystem.removeEdge(testAddEdge);
    }

    /**
     * Tests that a path will not be generated to a node after it has been removed.
     */
    @Test
    public void testRemoveNodePath(){
        Node testAddNode = new Node("TEST", 0.0, 0.0, 0, "Test Building",
                "Test Node", "Test Node Long", "Test Node Short", "Team Q", 0.0, 0.0, 0.0);
        databaseSystem.addNode(testAddNode);
        Node startNode = databaseSystem.getNode(kioskNodeID);
        Node endNode = databaseSystem.getNode(sameFloorDestinationID);
        Edge testAddEdge = new Edge("TestEdge", endNode, testAddNode, 1.0);
        databaseSystem.addEdge(testAddEdge);
        ArrayList<String> noType = new ArrayList<>();


        databaseSystem.removeNode(testAddNode);
        databaseSystem.removeEdge(testAddEdge);

        testGraph.init2(databaseSystem.getNodes(), databaseSystem.getEdges());
        ArrayList<Node> expectedPath = new ArrayList<>();
        ArrayList<Node> foundPath = testGraph.findPath(startNode, testAddNode, noType);

        assertEquals(expectedPath, foundPath);
    }

    /**
     * Tests that paths are correctly generated between floors
     */
    @Test
    public void testMultiFloorPath(){
        //TODO remove @Ignore when multifloor pathing is implemented / when we have the nodes and edges
        ArrayList<String> noType = new ArrayList<>();

        Node startNode = databaseSystem.getNode(kioskNodeID);
        Node middleNode = databaseSystem.getNode(elevOneFloorUp);
        Node endNode = databaseSystem.getNode(thirdFloorDestination);
        ArrayList<Node> expectedPath = new ArrayList<>();
        expectedPath.add(startNode);
        expectedPath.add(middleNode);
        expectedPath.add(endNode);
        testGraph.init2(databaseSystem.getNodes(), databaseSystem.getEdges());

        ArrayList<Node> foundPath = testGraph.findPath(startNode, endNode, noType);

        assertEquals(expectedPath.size(), foundPath.size());
        for(int i = 0; i < expectedPath.size(); i++){
            assertEquals(expectedPath.get(i).getNodeID(), foundPath.get(i).getNodeID());
        }
    }

    /**
     * Tests that paths are correctly generated between buildings
     */
    @Test
    public void testMultiBuildingPath(){
        //TODO remove @Ignore when multibuilding pathing is generated / when we have the nodes and edges
        ArrayList<String> noType = new ArrayList<>();
        Node startNode = databaseSystem.getNode(kioskNodeID);
        Node first = databaseSystem.getNode(multiBuildingFirstID);
        Node second = databaseSystem.getNode(multiBuildingSecondID);
        Node third = databaseSystem.getNode(multiBuildingThirdID);
        Node fourth = databaseSystem.getNode(multiBuildingFourthID);
        Node fifth = databaseSystem.getNode(multiBuildingFifthID);
        Node endNode = databaseSystem.getNode(nextBuildingDest);
        ArrayList<Node> expectedPath = new ArrayList<>();
        expectedPath.add(startNode);
        expectedPath.add(first);
        expectedPath.add(second);
        expectedPath.add(third);
        expectedPath.add(fourth);
        expectedPath.add(fifth);
        expectedPath.add(endNode);
        testGraph.init2(databaseSystem.getNodes(), databaseSystem.getEdges());
        ArrayList<Node> foundPath = testGraph.findPath(startNode, endNode, noType);

        assertEquals(expectedPath.size(), foundPath.size());
        for(int i = 0; i < expectedPath.size(); i++){
            assertEquals(expectedPath.get(i).getNodeID(), foundPath.get(i).getNodeID());
        }
    }
}