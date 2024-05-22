package io.github.zeculesu.itmo.prog5.server.command;

import io.github.zeculesu.itmo.prog5.client.ConsoleCommandEnvironment;
import io.github.zeculesu.itmo.prog5.data.SpaceMarineCollection;
import io.github.zeculesu.itmo.prog5.models.Response;
import io.github.zeculesu.itmo.prog5.models.SpaceMarine;
import io.github.zeculesu.itmo.prog5.server.Auth;
import io.github.zeculesu.itmo.prog5.sql.ConnectingDB;
import io.github.zeculesu.itmo.prog5.sql.JDBCUsers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthCommand extends AbstractCommand {
    public AuthCommand() {
        super("auth", "авторизация пользователя", false, true);
    }

    @Override
    public Response execute(SpaceMarineCollection collectionSpaceMarine, ConsoleCommandEnvironment env, String[] args, SpaceMarine... element) {
        Response response = new Response();
        String[] log_pas = args[0].split(" ");
        String login = log_pas[0];
        String password = log_pas[1];
        try {
            Connection connection = env.getConnection().connect();
            if (JDBCUsers.auth(connection, login, password)) {
                response.setStatus(200);
                response.setMessage("Авторизация успешно пройдена");
                return response;
            }
            response.setMessage("Неправильный пароль или логин");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            response.setError("Не удалось получить доступ к бд");
        }
        return response;
    }
}
