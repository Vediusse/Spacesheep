package io.github.zeculesu.itmo.prog5.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectingDB {

    final String jdbcUrl;
    final String username;
    final String password;

    public ConnectingDB(String jdbcUrl, String username, String password) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(this.jdbcUrl, this.username, this.password);
    }
}
