package edu.wpi.cs3733d18.teamQ.IntegrationTests;

import edu.wpi.cs3733d18.teamQ.ui.Controller.EmployeeEditController;
import edu.wpi.cs3733d18.teamQ.ui.Employee;
import edu.wpi.cs3733d18.teamQ.ui.User;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.testfx.framework.junit.ApplicationTest;

import static edu.wpi.cs3733d18.teamQ.manageDB.DatabaseSystem.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EmployeeScreenIntegrationTest extends ApplicationTest {
    private final String PATH_TO_EMPLOYEEFXML = "/fxmlFiles/EmployeeEditController.fxml ";
    private static User user;
    private EmployeeEditController empEditCont;
    private static Stage primaryStage;
    /**
     * Needed to expect exceptions without exiting a test
     */
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    /**
     * Initializes the controller and window
     */
    @Override
    public void start(Stage stage) throws Exception{
        primaryStage = stage;
        user.setPrimaryStage(primaryStage);
        FXMLLoader employeeEditLoader = new FXMLLoader(getClass().getResource(PATH_TO_EMPLOYEEFXML));
        Parent employeeParent = employeeEditLoader.load();
        primaryStage.setScene(new Scene(employeeParent));
        // Scene pathfindingScene = sdUtil.prodAndBindScene(pathfinderParent, stage);
        empEditCont = employeeEditLoader.getController();
        //empEditCont.setListen(stage);

        primaryStage.setMinWidth(735);
        primaryStage.setMinHeight(645);
        //stage.setScene(pathfindingScene);
        primaryStage.show();
        primaryStage.toFront();
    }

    /**
     * Initializes the database once
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

    /**
     * Scripts adding an employee to the database
     */
    private void addEmployeeInputs(){
        //Select add
        clickOn("#actionCB");
        press(KeyCode.DOWN);
        release(KeyCode.DOWN);
        press(KeyCode.ENTER);

        //Fill in fields
        clickOn("#usernameTF");
        write("testUser");
        clickOn("#passwordTF");
        write("testPassword");
        clickOn("#adminCB");
        press(KeyCode.DOWN);
        release(KeyCode.DOWN);
        press(KeyCode.ENTER);
        clickOn("#confirmBtn");
    }

    /**
     * Scripts Removing an employee from the database
     */
    private void removeEmployeeInputs(){
        clickOn("#actionCB");
        press(KeyCode.DOWN);
        release(KeyCode.DOWN);
        press(KeyCode.DOWN);
        release(KeyCode.DOWN);
        press(KeyCode.DOWN);
        release(KeyCode.DOWN);
        press(KeyCode.ENTER);

        clickOn("#employeeTTV");
        moveBy(0, -75);
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
    }


    /**
     * Tests that an employee can be added as expected by an admin
     */
    @Test
    public void addStaffTest(){
        addEmployeeInputs();

        Employee addedEmployee = getEmployee("testUser");
        removeEmployeeInputs();
        clickOn("#confirmBtn");

        assertEquals(addedEmployee.getUsername(), "testUser");
        assertTrue(addedEmployee.checkPassword("testPassword"));
        assertEquals(addedEmployee.getIsAdmin(), "1");

    }

    /**
     * Tests that an employee can be edited as expected by a user
     */
    @Test
    public void editEmployeeTest(){
        //Select edit
        addEmployeeInputs();

        clickOn("#actionCB");
        press(KeyCode.DOWN);
        release(KeyCode.DOWN);
        //press(KeyCode.DOWN);
        //release(KeyCode.DOWN);
        press(KeyCode.ENTER);

        clickOn("#employeeTTV");
        moveBy(0, -75);
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);

        clickOn("#adminCB");
        press(KeyCode.DOWN);
        release(KeyCode.DOWN);
        press(KeyCode.ENTER);


        String userName = lookup("#usernameTF").queryAs(TextField.class).getText();
        clickOn("#confirmBtn");
        Employee changedEmployee = getEmployee(userName);

        removeEmployeeInputs();
        clickOn("#confirmBtn");



        assertEquals(changedEmployee.getIsAdmin(), "2");

        //exception.expect(Exception.class);
    }

    /**
     * Tests that an employee can be removed as expected by a user
     */
    @Test
    public void removeEmployeeTest(){
        addEmployeeInputs();
        removeEmployeeInputs();

        String userName = lookup("#usernameTF").queryAs(TextField.class).getText();

        clickOn("#confirmBtn");
        assertEquals(null, getEmployee(userName));

       // exception.expect(Exception.class);
    }
}
