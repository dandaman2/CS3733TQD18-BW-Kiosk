package edu.wpi.cs3733d18.teamQ.IntegrationTests.Database;

import edu.wpi.cs3733d18.teamQ.manageDB.DatabaseSystem;
import org.junit.AfterClass;
import org.junit.Test;
import edu.wpi.cs3733d18.teamQ.pathfinding.Node;
import org.junit.BeforeClass;
import edu.wpi.cs3733d18.teamQ.ui.Employee;
import edu.wpi.cs3733d18.teamQ.ui.Requests.InterpreterRequest;
import edu.wpi.cs3733d18.teamQ.ui.Requests.Request;
import edu.wpi.cs3733d18.teamQ.ui.Requests.SanitationRequest;

import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EmployeeRequestAndDatabaseIntegrationTest {
    private static DatabaseSystem databaseSystem;

    private static final Node testAddNode = new Node("TEST", 0.0, 0.0, 0, "Test Building",
            "Test Node", "Test Node Long", "Test Node Short", "Team Q", 0.0, 0.0, 0.0, true);
    private final String testAddNodeAsString = "(\'TEST\', 0.0, 0.0, 0, \'Test Building\', \'Test Node\', \'Test Node Long\', \'Test Node Short\', \'Team Q\', 0.0, 0.0)";
    private final String testAddNodeID = "TEST";

    private final String staffName = "staff";
    private final String adminName = "admin";
    private Employee expectedEmployee = new Employee(staffName, staffName, null, null, null, false,"");
    private Employee expectedAdmin = new Employee(adminName, adminName, null, null, null, true, "");

    private final GregorianCalendar testDate = new GregorianCalendar(2018, 1, 1);
    private SanitationRequest testSanitationRequest;
    private InterpreterRequest testInterpreterRequest;

    /**
     * Initialize the databases and Request Interface Object
     */
    @BeforeClass
    public static void initialize(){
        databaseSystem = new DatabaseSystem();
        databaseSystem.initializeDb();
        databaseSystem.addNode(testAddNode);
    }

    /**
     *
     */
    @AfterClass
    public static void shutdown(){
        databaseSystem.removeNode(testAddNode);
        databaseSystem.closeDB();
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
        testInterpreterRequest = new InterpreterRequest(staffName, staffName, "TEST", 0, null, "testEmail@test.com","111111111",String.valueOf(requestNum), "Spanish",null, "Low");
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
        testSanitationRequest = new SanitationRequest(staffName, staffName, "TEST", 0, null, "testEmail@test.com","111111111",String.valueOf(requestNum),null, "very unclean spot", "Low");
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
        testInterpreterRequest = new InterpreterRequest(staffName, staffName, "TEST", 0, null, "testEmail@test.com","111111111",String.valueOf(requestNum), "Spanish",null, "Low");
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
        testSanitationRequest = new SanitationRequest(staffName, staffName, "TEST", 0, null, "testEmail@test.com","111111111",String.valueOf(requestNum),null, "very unclean spot", "Low");
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
