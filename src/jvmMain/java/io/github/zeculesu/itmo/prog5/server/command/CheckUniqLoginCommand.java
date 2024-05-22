package io.github.zeculesu.itmo.prog5.server.command;

import io.github.zeculesu.itmo.prog5.client.ConsoleCommandEnvironment;
import io.github.zeculesu.itmo.prog5.data.SpaceMarineCollection;
import io.github.zeculesu.itmo.prog5.models.Response;
import io.github.zeculesu.itmo.prog5.models.SpaceMarine;
import io.github.zeculesu.itmo.prog5.sql.ConnectingDB;
import io.github.zeculesu.itmo.prog5.sql.JDBCUsers;

import java.sql.*;

public class CheckUniqLoginCommand extends AbstractCommand {


    public CheckUniqLoginCommand() {
        super("check_uniq_login", "Проверка на единственность логина", false, true);
    }

    @Override
    public Response execute(SpaceMarineCollection collectionSpaceMarine, ConsoleCommandEnvironment env, String[] args, SpaceMarine... element) {
        Response response = new Response();
        String login = args[0];
        try {
            Connection connection = env.getConnection().connect();

            if (JDBCUsers.checkUniqLogin(connection, login)) {
                response.setError("Такой логин уже существует, придумайте новый");
                return response;
            }
            response.setStatus(200);
        } catch (SQLException e) {
            response.setError("Не удалось получить доступ к бд");
        }
        return response;
    }
}
