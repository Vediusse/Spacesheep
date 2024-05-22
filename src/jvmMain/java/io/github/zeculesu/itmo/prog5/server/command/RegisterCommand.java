package io.github.zeculesu.itmo.prog5.server.command;

import io.github.zeculesu.itmo.prog5.client.ConsoleCommandEnvironment;
import io.github.zeculesu.itmo.prog5.data.SpaceMarineCollection;
import io.github.zeculesu.itmo.prog5.models.Response;
import io.github.zeculesu.itmo.prog5.models.SpaceMarine;
import io.github.zeculesu.itmo.prog5.server.Auth;
import io.github.zeculesu.itmo.prog5.sql.ConnectingDB;
import io.github.zeculesu.itmo.prog5.sql.JDBCUsers;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterCommand extends AbstractCommand {
    public RegisterCommand() {
        super("register", "регистрация нового пользователя", false, true);
    }

    @Override
    public Response execute(SpaceMarineCollection collectionSpaceMarine, ConsoleCommandEnvironment env, String[] args, SpaceMarine... element) {
        Response response = new Response();
        String[] log_pas = args[0].split(" ");
        String login = log_pas[0];
        String password = log_pas[1];

        try{
            Connection connection = env.getConnection().connect();
            JDBCUsers.register(connection, login, password);
            response.setMessage("Регистрация успешно пройдена");
        }

        catch (SQLException e){
            response.setError("Не получилось подключиться к бд");
        }
        return response;
    }


}
