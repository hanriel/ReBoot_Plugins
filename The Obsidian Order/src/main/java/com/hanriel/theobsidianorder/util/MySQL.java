package com.hanriel.theobsidianorder.util;

import com.hanriel.theobsidianorder.TheObsidianOrder;

import java.sql.*;

public class MySQL {

    private Connection connection = null;

    public MySQL() {
        connect();
    }

    private boolean connect() {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + TheObsidianOrder.getConf().mysql_host +
                        "/too?user=" + TheObsidianOrder.getConf().mysql_user +
                        "&password=" + TheObsidianOrder.getConf().mysql_password +
                        "&autoReconnect=true"
            );

            return connection.isValid(30);
        } catch (SQLException e) {
            runException(e);
            return false;
        }
    }

    private void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            runException(e);
        }
    }

    public ResultSet query(String query) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery(query);
            return result;
        } catch (SQLException e) {
            runException(e);
            return null;
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void runException(SQLException e) {
        System.out.println("SQLException: " + e.getMessage());
        System.out.println("SQLState: " + e.getSQLState());
        System.out.println("VendorError: " + e.getErrorCode());
    }
}
