package org.hanriel.toocore.playerData;

import org.hanriel.toocore.TOO_CORE;

import java.sql.*;

public class MySQL {

    private Connection connection = null;

    public MySQL() {
        connect();
    }

    private void connect() {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + TOO_CORE.getConf().mysql_host +
                        "/too?user=" + TOO_CORE.getConf().mysql_user +
                        "&password=" + TOO_CORE.getConf().mysql_password +
                        "&autoReconnect=true"
            );

            connection.isValid(30);
        } catch (SQLException e) {
            runException(e);
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
            return stmt.executeQuery(query);
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
