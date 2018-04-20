package edu.wpi.cs3733d18.teamQ.manageDB;

import edu.wpi.cs3733d18.teamQ.ui.Employee;

import java.sql.SQLException;
import java.sql.Statement;

import static edu.wpi.cs3733d18.teamQ.manageDB.database.executeStatement;

 class AbilityDB {
    /**
     * Adds an employee ability to the db
     * 1=Interpreter, 2=Sanitation
     * @param employee The Employee to add an ability for
     * @param serviceType The number of the service type (request type) to add
     */
     static void addAbility(Employee employee, int serviceType) {
        try {
            String query = "INSERT INTO APP.ABILITY VALUES ('" + employee.getUsername() +"', " + serviceType +")";
            System.out.println(query);
            Statement queryStmnt = database.connection.createStatement();
            queryStmnt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes the given employees ability entry for the given type
     * 1=Interpreter, 2=Sanitation
     * @param employee The Employee to remove an ability for
     * @param serviceType The number of the service type (request type) to remove
     */
     static void removeAbility(Employee employee, int serviceType) {
        String databaseExecution = "DELETE FROM APP.ABILITY WHERE USERNAME ='" + employee.getUsername() + "' AND TYPE ="+serviceType;
        executeStatement(databaseExecution);
    }
}
