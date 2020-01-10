package com.skyandforest.reboot_core.playerData;

import java.sql.*;

public class MySQL {

    private Connection connection = null;

    public MySQL(){
        connect();
    }

    private boolean connect(){
        try {
            connection = DriverManager.getConnection("jdbc:mysql://212.33.246.122/reboot?user=irondrag&password=admin2014&autoReconnect=true");
            return connection.isValid(30);
        } catch (SQLException e) {
            runException(e);
            return false;
        }
    }

    private void disconnect(){
        try {
            connection.close();
        } catch (SQLException e) {
            runException(e);
        }
    }

    public ResultSet query(String query){
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
