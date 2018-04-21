package edu.wpi.cs3733d18.teamQ.IntegrationTests;

import com.jfoenix.controls.JFXTextField;
import com.sun.glass.events.MouseEvent;
import edu.wpi.cs3733d18.teamQ.ui.Controller.RequestController;
import edu.wpi.cs3733d18.teamQ.ui.Controller.ScreenUtil;
import edu.wpi.cs3733d18.teamQ.ui.Requests.InterpreterRequest;
import edu.wpi.cs3733d18.teamQ.ui.Requests.Request;
import edu.wpi.cs3733d18.teamQ.ui.Requests.SanitationRequest;
import edu.wpi.cs3733d18.teamQ.ui.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import org.junit.*;

import org.junit.rules.ExpectedException;
import org.testfx.framework.junit.ApplicationTest;

import static edu.wpi.cs3733d18.teamQ.manageDB.DatabaseSystem.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RequestScreenIntegrationTest extends ApplicationTest {
    private final String PATH_TO_REQUESTFXML = "/fxmlFiles/RequestScreen.fxml";
    private static User user;
    private RequestController reqController;
    private Stage thisStage;

    /**
     * Ensures that exceptions can be handled by tests without exiting
     */
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    /**
     * Initializes the user data and controller
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception{
        user.setPrimaryStage(stage);
        user.setNodes(getNodes());

        FXMLLoader reqLoader = new FXMLLoader(getClass().getResource(PATH_TO_REQUESTFXML));
        Parent reqParent = reqLoader.load();
        stage.setScene(new Scene(reqParent));
        //Scene pathfindingScene = sdUtil.prodAndBindScene(reqParent, stage);
        reqController = reqLoader.getController();
        //reqController.setListen(stage);

        stage.setMinWidth(735);
        stage.setMinHeight(645);
        //stage.setScene(pathfindingScene);
        stage.show();
        stage.toFront();
        thisStage = stage;
    }

    /**
     * Initializes the user and database
     */
    @BeforeClass
    public static void initialize(){
        user = User.getUser();
        initializeDb();
    }

    /**
     * Scripts the inputs that a user would do to add a request
     * @throws InterruptedException if the timeout fails
     */
    public void fillInRequestFieldsWithTestValues() throws InterruptedException{
        //Fill In Fields
        clickOn("#firstNameTF");
        write("Test First");
        clickOn("#lastNameTF");
        write("Test Last");
        clickOn("#roomLocationTF");
        write("GELEV00N02");
        Thread.sleep(200);
        press(KeyCode.ENTER);
        Thread.sleep(200);
        press(KeyCode.TAB);
        clickOn("#emailTF");
        write("test@test.com");
        clickOn("#phoneNumberTF");
        write("1111111111");
    }

    /**
     * Scripts selecting the first request in the list as the user would
     */
    public void selectRequest(){
        clickOn("#treeTableViewPending");
        moveBy(20,-80);
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
    }

    /**
     * Tests that an interpreter can be generated as expected by the test
     * @throws Exception if the timeout fails
     */
    @Test
    public void addInterpreterRequestTest() throws InterruptedException {
        clickOn("Pending Requests");

        int reqNum = getNewRequestID();
        InterpreterRequest expectedLoggedRequest = new InterpreterRequest("Test First", "Test Last", "test@test.com", "1111111111", "Elevator N Node 25 Floor 2,GELEV00N02", "English");
        //Select Interpreter
        clickOn("#dropDown");
        Thread.sleep(500);
        press(KeyCode.DOWN);
        press(KeyCode.ENTER);

        fillInRequestFieldsWithTestValues();

        /* TODO fix later
        For some reason testFX does not accept that the combobox is visible, even though it is in the debugger
        clickOn("#interpreterCB");
        Thread.sleep(500);
        press(KeyCode.DOWN);
        press(KeyCode.ENTER);*/
        clickOn("#submitRequest");

        Request fetchedRequest = getRequest(String.valueOf(reqNum));
        assertEquals(expectedLoggedRequest.getDetails(), fetchedRequest.getDetails());
    }

    /**
     * Tests that a sanitation request can be added as expected by a user
     * @throws InterruptedException if the sleep fails
     */
    @Test
    public void addSanitationRequestTest() throws InterruptedException {
        clickOn("Pending Requests");

        int reqNum = getNewRequestID();
        SanitationRequest expectedLoggedRequest = new SanitationRequest("Test First", "Test Last", "test@test.com", "1111111111", "Elevator N Node 25 Floor 2,GELEV00N02", "TEST");
        //Select Sanitation
        clickOn("#dropDown");
        Thread.sleep(500);
        press(KeyCode.DOWN);
        release(KeyCode.DOWN);
        press(KeyCode.DOWN);
        press(KeyCode.ENTER);

        fillInRequestFieldsWithTestValues();

       //TODO probably make this not so hard coded but testFX is picky with changing visibility
        moveBy(0, 50);
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        write("TEST");
        clickOn("#submitRequest");



        Request fetchedRequest = getRequest(String.valueOf(reqNum));
        assertEquals(expectedLoggedRequest.getDetails(), fetchedRequest.getDetails());
    }

    /**
     * Tests that an unfulfilled request can be removed as expected by a user
     * @throws NullPointerException
     */
    @Test
    public void removeRequestUnfulfilledTest() throws NullPointerException{
        clickOn("Pending Requests");

        selectRequest();
        String requestAllData = lookup("#scrollPane").queryAs(ScrollPane.class).getContent().toString();
        //System.out.println(requestAllData);
        String requestID = requestAllData.substring(15, requestAllData.indexOf("\n", 15));
        Request fetchedRequest = getRequest(requestID);
        assertNotNull(fetchedRequest); //make sure node exists
        clickOn("#removePending");
        fetchedRequest = getRequest(requestID);
        assertEquals( null, fetchedRequest);
        exception.expect(Exception.class);
    }

    /**
     * Tests that a request can be fulfilled as expected by a user
     * @throws InterruptedException
     */
    @Test
    public void fulfillRequestTest() throws InterruptedException{
        clickOn("Pending Requests");

        clickOn("#dropDown");
        Thread.sleep(500);
        press(KeyCode.DOWN);
        press(KeyCode.ENTER);
        fillInRequestFieldsWithTestValues();
        clickOn("#submitRequest");

        selectRequest();
        String requestAllData = lookup("#scrollPane").queryAs(ScrollPane.class).getContent().toString();
        String requestID = requestAllData.substring(15, requestAllData.indexOf("\n", 15));

        clickOn("#whoFulfilled");
        write("Test Person");
        clickOn("#calendar");
        write("4/16/2018");
        clickOn("#submit");

        Request fetchedRequest = getRequest(requestID);
        assertEquals(fetchedRequest.isFulfilled(), 1);
    }

    /**
     * Tests that a fulfilled request can be removed as expected by the user
     * @throws Exception
     */
    @Test
    public void removeRequestFulfilledTest() throws Exception {
        clickOn("Pending Requests");

        clickOn("#dropDown");
        Thread.sleep(500);
        press(KeyCode.DOWN);
        press(KeyCode.ENTER);
        fillInRequestFieldsWithTestValues();
        clickOn("#submitRequest");

        selectRequest();
        clickOn("#whoFulfilled");
        write("Test Person");
        clickOn("#calendar");
        write("4/16/2018");
        clickOn("#submit");

        clickOn("FulFilled Requests");
        clickOn("#treeTableViewFulfilled");
        moveBy(20,-80);
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);

        String requestAllData = lookup("#scrollPaneFulFill").queryAs(ScrollPane.class).getContent().toString();
        String requestID = requestAllData.substring(15, requestAllData.indexOf("\n", 15));
        Request fetchedRequest = getRequest(requestID);
        clickOn("#removeFulFilled");
        fetchedRequest = getRequest(requestID);
        assertEquals( null, fetchedRequest);
        clickOn("Pending Requests");
        selectRequest();
    }
}
