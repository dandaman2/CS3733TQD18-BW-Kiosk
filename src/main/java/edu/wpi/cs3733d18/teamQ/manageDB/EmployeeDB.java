package edu.wpi.cs3733d18.teamQ.manageDB;

import edu.wpi.cs3733d18.teamQ.ui.Employee;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static edu.wpi.cs3733d18.teamQ.manageDB.database.executeStatement;
import static edu.wpi.cs3733d18.teamQ.manageDB.database.getTable;

 class EmployeeDB {

     static void populateEmployeeDB(){
         addEmployee(new Employee("staff", "staff", "Staff", "Staffer", "Dr", false, ""));
         addEmployee(new Employee("admin", "admin","Admin", "McAdminson", "Lord", true, "39433d228da4ba6ac1068dd16b8a1c10"));
     }

    /**
     * Adds an Employee to the db
     * @param employee The Employee to add
     */
     static void addEmployee(Employee employee) {
        try {
            String query = "INSERT INTO APP.EMPLOYEE VALUES " + employeeToString(employee);
            Statement queryStmnt = database.connection.createStatement();
            queryStmnt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns an ArrayList of all employees currently stored in the db
     * @return All employees in the db
     */
     static ArrayList<Employee> getEmployees(){
        ResultSet rs = getTable("APP.EMPLOYEE");
        ArrayList<Employee> employees = new ArrayList<Employee>();
        try{
            while (rs.next()){
                boolean isAdmin = 1==rs.getInt("ISADMIN");
                Employee newEmployee = new Employee(rs.getString("USERNAME"), rs.getString("FIRSTNAME"), rs.getString("LASTNAME"), rs.getString("TITLE"), isAdmin, rs.getString("FACEID"));
                newEmployee.setHashedPassword(rs.getString("PASSWORD"));
                employees.add(newEmployee);
            }
        } catch (SQLException e) {
            System.out.println("Failed to find request table");
            e.printStackTrace();
        }
        return employees;
    }

    /**
     * Returns the employee with the given username
     * @param username The username to search for
     * @return The employee with the matching username
     */
     static Employee getEmployee(String username){
        ResultSet results = null;
        Employee foundEmployee =null;
        try {
            String query = "SELECT * FROM APP.EMPLOYEE WHERE USERNAME=" + "'" + username +"'";
            System.out.println(query);
            Statement queryStmnt = database.connection.createStatement();
            results = queryStmnt.executeQuery(query);
            while (results.next()){
                foundEmployee = new Employee(results.getString("USERNAME"), results.getString("FIRSTNAME"), results.getString("LASTNAME"), results.getString("TITLE"), results.getInt("ISADMIN")==1,
                        results.getString("FACEID"));
                foundEmployee.setHashedPassword(results.getString("PASSWORD"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return foundEmployee;
    }

    static ArrayList<Integer> getEmployeeAbilities(Employee employee){
        ResultSet results = null;
        ArrayList<Integer> abilities = new ArrayList<Integer>();
        try {
            String query = "SELECT * FROM APP.ABILITY WHERE USERNAME='" + employee.getUsername() +"'";
            System.out.println(query);
            Statement queryStmnt = database.connection.createStatement();
            results = queryStmnt.executeQuery(query);
            while (results.next()) {
                abilities.add(results.getInt("TYPE"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return abilities;
    }

    /**
     * Removes the given Employee from the db
     * @param employee The Employee to remove
     */
     static void removeEmployee(Employee employee) {
        String databaseExecution = "DELETE FROM APP.EMPLOYEE WHERE USERNAME ='" + employee.getUsername() + "'";
        executeStatement(databaseExecution);
    }

    /**
     * Updates the password and admin status of the given employee
     * @param employee The employee to update
     */
     static void editEmployee(Employee employee){
        String dataBaseExecution = "UPDATE APP.EMPLOYEE SET PASSWORD='" + employee.getPassword() + "', FIRSTNAME='" +
                employee.getFirstName() + "', LASTNAME='"+ employee.getLastName() + "', TITLE='"+ employee.getTitle() +
                "', ISADMIN=" + employee.isAdminInt() +", FACEID='"+ employee.getFaceID() +"' WHERE USERNAME ='" +
                employee.getUsername() + "'";
        executeStatement(dataBaseExecution);
    }

    /**
     * Converts an Employee to a String that can be input to the db
     * @param input The Employee to convert
     * @return The given Employee in String form
     */
    private static String employeeToString(Employee input) {
        String employee = "('" + input.getUsername() + "',"
                + "'" + input.getPassword() + "',"
                + "'" + input.getFirstName() + "',"
                + "'" + input.getLastName() + "',"
                + "'" + input.getTitle() + "',"
                + input.isAdminInt() + ","
                + "'" + input.getFaceID() + "')";
        return employee;
    }
}