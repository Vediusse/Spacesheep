package io.github.zeculesu.itmo.prog5.server.net;

import io.github.zeculesu.itmo.prog5.client.ConsoleCommandEnvironment;
import io.github.zeculesu.itmo.prog5.data.AuthCheckSpaceMarineCollection;
import io.github.zeculesu.itmo.prog5.models.Request;
import io.github.zeculesu.itmo.prog5.models.Response;
import io.github.zeculesu.itmo.prog5.models.SpaceMarine;
import io.github.zeculesu.itmo.prog5.server.command.*;

import java.net.DatagramPacket;
import java.util.Map;

public class RequestExecute {
    public static Response requestExecute(ConsoleCommandEnvironment env, Map clientCollections, Request request) {
        String login = request.getLogin();

        if (!clientCollections.containsKey(login)) {
            // Создание новой коллекции для клиента
            clientCollections.put(login, new AuthCheckSpaceMarineCollection(Server.cachedSpaceMarineCollection, login));
        }

        AuthCheckSpaceMarineCollection collection = (AuthCheckSpaceMarineCollection) clientCollections.get(login);

        // Выводим полученное сообщение от клиента на консоль
        System.out.println("Команда с клиента: " + request.getCommand());
        if (request.getCommand().equals("send_command")) {
            CommandAction comm = new SendCommandSet();
            return comm.execute(collection, env, new String[0], null);
        }

        if (request.getCommand().equals("check_uniq_login")) {
            CommandAction comm = new CheckUniqLoginCommand();
            return comm.execute(collection, env, new String[]{request.getArg()}, null);
        }

        if (request.getCommand().equals("register")) {
            CommandAction comm = new RegisterCommand();
            return comm.execute(collection, env, new String[]{request.getArg()}, null);
        }

        if (request.getCommand().equals("auth")) {
            CommandAction comm = new AuthCommand();
            return comm.execute(collection, env, new String[]{request.getArg()}, null);

        }

        // проверка на регистрацию
        CommandAction comm = new AuthCommand();
        Response response = comm.execute(collection, env, new String[]{request.getLogin() + " " + request.getPassword()});
        if (response.getStatus() != 200) {
            Response r = new Response();
            r.setError("Такого пользователя нет в бд");
            return r;
        }

        env.addCommandToHistory(request.getCommand());
        CommandAction com = env.getCommandSetMap().findCommand(request.getCommand());
        if (com != null) {
            SpaceMarine elem = null;
            if (com.isAcceptsElement()) {
                elem = request.getElem();
            }
            String[] args = new String[1];
            if (com.isAcceptsArg()) {
                args[0] = request.getArg();
            }
            return com.execute(collection, env, args, elem);
        }
        return new Response();
    }
}