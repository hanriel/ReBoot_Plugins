package com.hanriel.toohub;

import java.sql.*;

public class MySQL {

    private Connection connection = null;

    public MySQL() {
        connect();
    }

    private boolean connect() {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + Main.getInstance().config.getString("data_base.host") +
                            "/" + Main.getInstance().config.getString("data_base.base") +
                            "?user=" + Main.getInstance().config.getString("data_base.user") +
                            "&password=" + Main.getInstance().config.getString("data_base.password") +
                            "&autoReconnect=" + Main.getInstance().config.getString("data_base.autoReconnect")
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
