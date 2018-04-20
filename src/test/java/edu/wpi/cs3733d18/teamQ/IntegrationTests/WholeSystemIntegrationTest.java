package edu.wpi.cs3733d18.teamQ.IntegrationTests;

import edu.wpi.cs3733d18.teamQ.manageDB.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import edu.wpi.cs3733d18.teamQ.pathfinding.Node;
import edu.wpi.cs3733d18.teamQ.pathfinding.Edge;
import edu.wpi.cs3733d18.teamQ.pathfinding.Graph;
import edu.wpi.cs3733d18.teamQ.ui.Employee;
import edu.wpi.cs3733d18.teamQ.ui.Requests.InterpreterRequest;
import edu.wpi.cs3733d18.teamQ.ui.Requests.Request;
import edu.wpi.cs3733d18.teamQ.ui.Requests.SanitationRequest;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WholeSystemIntegrationTest {
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

    private final GregorianCalendar testDate = new GregorianCalendar(2018, 1, 1);
    private final String staffName = "staff";
    private final String adminName = "admin";
    private InterpreterRequest testInterpreterRequest;
    private SanitationRequest testSanitationRequest;

    @BeforeClass
    public static void initialize(){
        testGraph = new Graph();
        databaseSystem = new DatabaseSystem();
        databaseSystem.initializeDb();
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
        ArrayList<Node> foundPath = testGraph.findShortestPath(startNode, endNode, noType);

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
        ArrayList<Node> foundPath = testGraph.findShortestPath(startNode, testAddNode, noType);

        assertEquals(expectedPath.size(), foundPath.size());
        for(int i = 0; i < expectedPath.size(); i++){
            assertEquals(expectedPath.get(i).getNodeID(), foundPath.get(i).getNodeID());
        }

        databaseSystem.removeNode(testAddNode);
        databaseSystem.removeEdge(testAddEdge);
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

        ArrayList<Node> foundPath = testGraph.findShortestPath(startNode, endNode, noType);

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
        ArrayList<Node> foundPath = testGraph.findShortestPath(startNode, endNode, noType);

        assertEquals(expectedPath.size(), foundPath.size());
        for(int i = 0; i < expectedPath.size(); i++){
            assertEquals(expectedPath.get(i).getNodeID(), foundPath.get(i).getNodeID());
        }
    }

    /**
     * Tests that the initial employees are correctly loaded into the database
     */
    @Test
    public void testInitialEmployees(){
        Employee fetchedStaff = databaseSystem.getEmployee(staffName);
        assertEquals(fetchedStaff.getUsername(), staffName);
        assertTrue(fetchedStaff.checkPassword(staffName));
        assertEquals(fetchedStaff.isAdminInt()==1, false);

        Employee fetchedAdmin = databaseSystem.getEmployee(adminName);
        assertEquals(fetchedAdmin.getUsername(), adminName);
        assertTrue(fetchedAdmin.checkPassword(adminName));
        assertEquals(fetchedAdmin.isAdminInt()==1, true);
    }

    /**
     * Tests adding an interpreter request using actual database functions
     */
    @Test
    public void addInterpreterRequestTest(){
        int requestNum = databaseSystem.getNewRequestID();
        testInterpreterRequest = new InterpreterRequest(staffName, staffName, kioskNodeID, 0, null, "testEmail@test.com","111111111",String.valueOf(requestNum), "Spanish",null, "Low");
        databaseSystem.addRequest(testInterpreterRequest);

        Request fetchedRequest = databaseSystem.getRequest(String.valueOf(requestNum));
        assertEquals(testInterpreterRequest.getRoomName(), fetchedRequest.getRoomName());
        assertEquals(testInterpreterRequest.getFirstName(), fetchedRequest.getFirstName());
        assertEquals(testInterpreterRequest.getDetails(), fetchedRequest.getDetails());

        databaseSystem.removeRequest(testInterpreterRequest);
    }

    /**
     * Tests adding a sanitation request to the database using actual database
     * functions
     */
    @Test
    public void addSanitationRequestTest(){
        int requestNum = databaseSystem.getNewRequestID();
        testSanitationRequest = new SanitationRequest(staffName, staffName, kioskNodeID, 0, null, "testEmail@test.com","111111111",String.valueOf(requestNum),null, "very unclean spot", "Low");
        databaseSystem.addRequest(testSanitationRequest);

        Request fetchedRequest = databaseSystem.getRequest(String.valueOf(requestNum));
        assertEquals(testSanitationRequest.getRoomName(), fetchedRequest.getRoomName());
        assertEquals(testSanitationRequest.getFirstName(), fetchedRequest.getFirstName());
        assertEquals(testSanitationRequest.getDetails(), fetchedRequest.getDetails());

        databaseSystem.removeRequest(testSanitationRequest);
    }

    /**
     * Tests that an InterpreterRequest will be edited in the database
     */
    @Test
    public void editInterpreterRequestTest(){
        int requestNum = databaseSystem.getNewRequestID();
        testInterpreterRequest = new InterpreterRequest(staffName, staffName, kioskNodeID, 0, null, "testEmail@test.com","111111111",String.valueOf(requestNum), "Spanish",null, "Low");
        databaseSystem.addRequest(testInterpreterRequest);

        testInterpreterRequest.setDate(testDate);
        testInterpreterRequest.setFulfilled(1);
        testInterpreterRequest.setNameWhoFulfilled(adminName);
        databaseSystem.editRequest(testInterpreterRequest);

        Request fetchedRequest = databaseSystem.getRequest(String.valueOf(requestNum));
        assertEquals(testInterpreterRequest.getDate(), fetchedRequest.getDate());
        assertEquals(testInterpreterRequest.getWhoFulfilled(), fetchedRequest.getWhoFulfilled());
        assertEquals(testInterpreterRequest.isFulfilled(), fetchedRequest.isFulfilled());

        databaseSystem.removeRequest(testInterpreterRequest);
    }

    /**
     * Tests that an InterpreterRequest will be edited in the database
     */
    @Test
    public void editSanitationRequest(){
        int requestNum = databaseSystem.getNewRequestID();
        testSanitationRequest = new SanitationRequest(staffName, staffName, kioskNodeID, 0, null, "testEmail@test.com","111111111",String.valueOf(requestNum),null, "very unclean spot", "Low");
        databaseSystem.addRequest(testSanitationRequest);

        testSanitationRequest.setDate(testDate);
        testSanitationRequest.setFulfilled(1);
        testSanitationRequest.setNameWhoFulfilled(adminName);
        databaseSystem.editRequest(testSanitationRequest);

        Request fetchedRequest = databaseSystem.getRequest(String.valueOf(requestNum));
        assertEquals(testSanitationRequest.getDate(), fetchedRequest.getDate());
        assertEquals(testSanitationRequest.getWhoFulfilled(), fetchedRequest.getWhoFulfilled());
        assertEquals(testSanitationRequest.isFulfilled(), fetchedRequest.isFulfilled());

        databaseSystem.removeRequest(testSanitationRequest);
    }

}
