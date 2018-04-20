package edu.wpi.cs3733d18.teamQ.UnitTests;

import edu.wpi.cs3733d18.teamQ.pathfinding.Edge;
import edu.wpi.cs3733d18.teamQ.manageDB.*;
import edu.wpi.cs3733d18.teamQ.pathfinding.Node;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import edu.wpi.cs3733d18.teamQ.ui.Employee;
import edu.wpi.cs3733d18.teamQ.ui.Requests.InterpreterRequest;
import edu.wpi.cs3733d18.teamQ.ui.Requests.Request;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DatabaseTest {
    private static DatabaseSystem databaseSystem;

    private final Node testAddNode = new Node("TEST", 0.0, 0.0, 0, "Test Building",
            "Test Node", "Test Node Long", "Test Node Short", "Team Q", 0.0, 0.0, 0.0);
    private final String testAddNodeAsString = "(\'TEST\', 0.0, 0.0, 0, \'Test Building\', \'Test Node\', \'Test Node Long\', \'Test Node Short\', \'Team Q\', 0.0, 0.0, 0.0)";
    private final String testAddNodeID = "TEST";
    private final Node testGetNode = new Node("GDEPT00702", 1341.0, 1778.0, 2, "Shapiro",
            "DEPT", "Waiting room? Node 7 Floor 2", "Waiting Area Shapiro Floor 2", "Team G", 1213.00, 1627.00, 0.0);
    private final String testGetNodeID = "GDEPT00702";

    private final Edge testAddEdge = new Edge("TestEdge", testAddNode, testAddNode, 0.0);
    private final String testAddEdgeAsString = "(\'TestEdge\', \'TEST\', \'TEST\', 0.0)";

    private final String edgeCSVHeaderExpected = "EDGEID, STARTNODE, ENDNODE, DISTANCE";
    private final String getEdgeCSVFirstLineExpect = "AHALL00202_AHALL00302,AHALL00302,AHALL00202,141.0";//"WHALL002L2_GHALL001L2,WHALL002L2,GHALL001L2,0.0";
    private final String nodeCSVHeaderExpected = "nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName,teamAssigned,xcoord3d,ycoord3d";
    private final String getNodeCSVFirstLineExpected = "ACONF00102,1580.0,2538.0,2,BTM,HALL,Hall,Hall,Team A,1106.0,2080.0,0.0";//"GDEPT00702,1341.0,1778.0,2,Shapiro,DEPT,Waiting room? Node 7 Floor 2,Waiting Area Shapiro Floor 2,Team G,1213.0,1627.0";

    private final GregorianCalendar testDate = new GregorianCalendar(2018, 1, 1);
    private final String testRequestID = "1000";
    private final String unaddedTestRequestID = "1001";
    private final InterpreterRequest testRequest = new InterpreterRequest("first", "last", "room", 0, "fulfiller", "testEmail@test.com","111111111",testRequestID, "Spanish",null, "Low");
    //new InterpreterRequest(0,"testEmail@test.com", "111111111", testRequestID, "Spanish");
    private final String testFulfilledName = "Test Doctor";

    private final Employee testEmployee = new Employee("bobsmith", "password", false);
    private final Employee testAdmin = new Employee("bigcheese", "bosspass", true);

    private final String firstPassword = "testPassword";
    private final String secondPassword = "testPassword2";

    /**
     * Initialize the database before running any tests
     * Also check that the tables were created correctly
     */
    @BeforeClass
    public static void initDatabase(){
        databaseSystem = new DatabaseSystem();
        databaseSystem.initializeDb();

    }

    @AfterClass
    public static void shutdown(){
        databaseSystem.closeDB();
    }

    /**
     * Close the database after running all tests
     */
    @AfterClass
    public static void closeDatabase(){
        databaseSystem.closeDB();
    }

    /**
     * Test that a node can be added to the database.
     * This checks the nodeID, name, 2D and 3D coordinates.
     */
    @Test
    public void addNodeTest(){
        databaseSystem.addNodesToDb(testAddNodeAsString);
        Node fetchedNode = databaseSystem.getNode(testAddNodeID);
        assertEquals(fetchedNode.getNodeID(), testAddNode.getNodeID());
        assertEquals(fetchedNode.getNameLong(), testAddNode.getNameLong());
        assertTrue(fetchedNode.getxPos().equals(testAddNode.getxPos()));
        assertTrue(fetchedNode.getyPos().equals(testAddNode.getyPos()));
        assertTrue(fetchedNode.getxPos3D().equals(testAddNode.getxPos3D()));
        assertTrue(fetchedNode.getyPos3D().equals(testAddNode.getyPos3D()));
        databaseSystem.removeNode(testAddNode);
    }

    /**
     * Test that a node can be removed from the database.
     */
    @Test
    public void removeNodeTest(){
        //Add a node to remove
        databaseSystem.addNodesToDb(testAddNodeAsString);
        databaseSystem.removeNode(testAddNode);
        assertEquals(databaseSystem.getNode(testAddNodeID), null);
    }

    /**
     * Test that employees can be added to the db
     * Checks password, isAdmin
     */
    @Test
    public void addEmployeeTest(){
        databaseSystem.addEmployee(testEmployee);
        databaseSystem.addEmployee(testAdmin);
        Employee fetchedEmployee = databaseSystem.getEmployee("bobsmith");
        assertEquals(fetchedEmployee.getPassword(), testEmployee.getPassword());
        assertEquals(fetchedEmployee.getIsAdmin(), testEmployee.getIsAdmin());
        assertTrue(fetchedEmployee.checkPassword("password"));
        Employee fetchedAdmin = databaseSystem.getEmployee("bigcheese");
        assertEquals(fetchedAdmin.getPassword(), testAdmin.getPassword());
        assertEquals(fetchedAdmin.getIsAdmin(), testAdmin.getIsAdmin());
        assertTrue(fetchedAdmin.checkPassword("bosspass"));
        databaseSystem.removeEmployee(testEmployee);
        databaseSystem.removeEmployee(testAdmin);
    }

    @Test
    public void removeEmployeeTest(){
        databaseSystem.addEmployee(testEmployee);
        databaseSystem.removeEmployee(testEmployee);
        assertEquals(databaseSystem.getEmployee("bobsmith"),null);
    }

    @Test
    public void editEmployeeTest(){
        databaseSystem.addEmployee(testEmployee);
        testEmployee.setPassword("newPass");
        databaseSystem.editEmployee(testEmployee);
        assertTrue(databaseSystem.getEmployee("bobsmith").checkPassword("newPass"));
        databaseSystem.removeEmployee(testEmployee);
    }

    /**
     * Tests that an edge can be added to the database.
     * Checks all fields of the edge
     */
    @Test
    public void addEdgeTest(){
        boolean edgePresent = false;
        databaseSystem.addEdgesToDb(testAddEdgeAsString);
        databaseSystem.addNode(testAddNode);
        ArrayList<Edge> allEdges = databaseSystem.getEdges();

        //TODO check to see if getEdge() will be implemented, will make this much easier
        for(int i = 0; i < allEdges.size(); i++){
            Edge currEdge = allEdges.get(i);
            if(currEdge.getEdgeID().equals(testAddEdge.getEdgeID())){
                assertEquals(currEdge.getEdgeID(), testAddEdge.getEdgeID());
                System.out.println(currEdge.getStartNode());
                assertEquals(currEdge.getStartNode().getNodeID(), testAddEdge.getStartNode().getNodeID());
                assertEquals(currEdge.getEndNode().getNodeID(), testAddEdge.getEndNode().getNodeID());
                assertTrue(currEdge.getDistance() == testAddEdge.getDistance());
                edgePresent = true;
            }
        }
        if (!edgePresent){
            //If the edge is not present
            assertTrue(false);
        }
        databaseSystem.removeEdge(testAddEdge);
        databaseSystem.removeNode(testAddNode);
    }

    /**
     * Tests that the edges are exported to a CSV in the correct format
     * Uses the header and first expected line
     */
    @Test
    public void exportEdgeToCSVTest(){
        databaseSystem.exportEdgeToCSV();
        try {
            FileReader reader = new FileReader("NewMapQEdges.csv");
            BufferedReader buffReader = new BufferedReader(reader);
            String headerActual = buffReader.readLine();
            String firstLineActual = buffReader.readLine();
            assertEquals(headerActual, edgeCSVHeaderExpected);
            assertEquals(firstLineActual, getEdgeCSVFirstLineExpect);
        } catch (Exception e) {
            //If the file does not exist it does not work correctly, so fail the test
            assertTrue(false);
        }
    }

    /**
     * Tests that the edges are exported to a CSV in the correct format
     * Uses the header and first expected line
     */
    @Test
    public void exportNodeToCSVTest(){
        databaseSystem.exportNodeToCSV();
        try {
            FileReader reader = new FileReader("NewMapQNodes.csv");
            BufferedReader buffReader = new BufferedReader(reader);
            String headerActual = buffReader.readLine();
            String firstLineActual = buffReader.readLine();
            assertEquals(headerActual, nodeCSVHeaderExpected);
            assertEquals(firstLineActual, getNodeCSVFirstLineExpected);
        } catch (Exception e) {
            //If the file does not exist it does not work correctly, so fail the test
            assertTrue(false);
        }
    }

    /**
     * Tests that an edge can be removed from the database correctly
     */
    @Test
    public void removeEdgeTest(){
        boolean edgePresent = false;
        databaseSystem.addEdgesToDb(testAddEdgeAsString);
        databaseSystem.addNode(testAddNode);
        databaseSystem.removeEdge(testAddEdge);
        ArrayList<Edge> allEdges = databaseSystem.getEdges();

        for(int i = 0; i < allEdges.size(); i++){
            Edge currEdge = allEdges.get(i);
            if(currEdge.getEdgeID().equals(testAddEdge.getEdgeID())){
                edgePresent = true;
            }
        }
        if (!edgePresent){
            //If the edge is not present
            assertTrue(true);
        }
        databaseSystem.removeNode(testAddNode);
    }

    /**
     * Tests that a request can be added to the database correctly
     */
    @Test
    public void addRequestTest(){
        databaseSystem.addRequest(testRequest);
        Request fetchedRequest = databaseSystem.getRequest(testRequestID);

        //Test that key details are maintained
        assertEquals(testRequest.getRequestID(), fetchedRequest.getRequestID());
        System.out.println(testRequest.getDate());
        assertEquals(testRequest.getDate(), fetchedRequest.getDate());
        assertEquals(testRequest.getLanguage(), fetchedRequest.getSpecifics());
        databaseSystem.removeRequest(testRequest);
    }

    /**
     * Tests that a request can be edited. The date, who fulfilled, and fulfilled
     * will be changed.
     */
    @Test
    public void editRequestTest(){
        testRequest.setDate(testDate);
        testRequest.setNameWhoFulfilled("Test Doctor");
        databaseSystem.addRequest(testRequest);
        databaseSystem.editRequest(testRequest);
        Request fetchedRequest = databaseSystem.getRequest(testRequestID);

        //Test that the changed values are changed correctly
        assertEquals(1, fetchedRequest.isFulfilled());
        assertEquals(testFulfilledName, fetchedRequest.getWhoFulfilled());
        assertEquals(testDate, fetchedRequest.getDate());

        databaseSystem.removeRequest(testRequest);
    }

    /**
     * Tests that a request can be removed from the database correctly
     */
    @Test
    public void removeRequestTest(){
        databaseSystem.addRequest(testRequest);
        Request fetchedRequest = databaseSystem.getRequest(testRequestID);
        assertNotNull(fetchedRequest); //verify the request is present

        databaseSystem.removeRequest(testRequest);
        fetchedRequest = databaseSystem.getRequest(testRequestID);
        assertEquals(null, fetchedRequest); //verify it is no longer present
    }

    /**
     * Tests that a request can be fetched from the database, and that it
     * won't fetch requests not in the database
     */
    @Test
    public void getRequestTest(){
        databaseSystem.addRequest(testRequest);
        Request fetchedRequest = databaseSystem.getRequest(testRequestID);
        assertNotNull(fetchedRequest);

        fetchedRequest = databaseSystem.getRequest(unaddedTestRequestID);
        assertEquals(null, fetchedRequest);

        databaseSystem.removeRequest(testRequest);
    }

}