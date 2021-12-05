package com.hanriel.reboot_core.playerData;

import com.hanriel.reboot_core.Core;

import java.sql.*;

public class MySQL {

    private Connection connection = null;

    public MySQL() {
        connect();
    }

    private boolean connect() {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + Core.getConf().mysql_host +
                        "/too?user=" + Core.getConf().mysql_user +
                        "&password=" + Core.getConf().mysql_password +
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

    Connection getConnection() {
        return this.connection;
    }

    void runException(SQLException e) {
        System.out.println("SQLException: " + e.getMessage());
        System.out.println("SQLState: " + e.getSQLState());
        System.out.println("VendorError: " + e.getErrorCode());
    }
}
