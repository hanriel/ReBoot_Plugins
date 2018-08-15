package com.skyandforest.reboot_core.playerData;

import java.sql.*;

class MySQL {

    private Connection connection = null;

    MySQL() {

    }

    boolean connect(){
        try {
            connection = DriverManager.getConnection("jdbc:mysql://212.33.246.122/reboot?user=CMen&password=Aazz0909");
            return connection.isValid(30);
        } catch (SQLException e) {
            runException(e);
            return false;
        }
    }

    boolean disconnect(){
        try {
            connection.close();
            return !connection.isValid(30);
        } catch (SQLException e) {
            runException(e);
            return false;
        }
    }

    ResultSet query(String query){
        try {
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery(query);
            return result;
        } catch (SQLException e) {
            runException(e);
            return null;
        }
    }

    Connection getConnection(){
        return this.connection;
    }

    void runException(SQLException e){
        System.out.println("SQLException: " + e.getMessage());
        System.out.println("SQLState: " + e.getSQLState());
        System.out.println("VendorError: " + e.getErrorCode());
    }
}
