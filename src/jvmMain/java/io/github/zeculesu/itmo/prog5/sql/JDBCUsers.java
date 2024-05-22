package io.github.zeculesu.itmo.prog5.sql;

import io.github.zeculesu.itmo.prog5.server.Auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCUsers {
    public static boolean auth(Connection connection, String login, String password) throws SQLException {
        String query = "SELECT login FROM users WHERE login = ? AND password = ?";

        PreparedStatement ps = connection.prepareStatement(query);

        password = Auth.hash_password(password);

        ps.setString(1, login);
        ps.setString(2, password);

        ResultSet resultSet = ps.executeQuery();

        connection.close();

        return resultSet.next();
    }

    public static void register(Connection connection, String login, String password) throws SQLException {
        String query = "INSERT INTO users (login, password) VALUES (?, ?)";
        PreparedStatement ps = connection.prepareStatement(query);
        password = Auth.hash_password(password);
        ps.setString(1, login);
        ps.setString(2, password);
        ps.executeUpdate();
        connection.close();
    }

    public static boolean checkUniqLogin(Connection connection, String login) throws SQLException {
        String query = "SELECT login FROM users WHERE login = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, login);
        ResultSet resultSet = ps.executeQuery();
        connection.close();
        return resultSet.next();
    }
}
