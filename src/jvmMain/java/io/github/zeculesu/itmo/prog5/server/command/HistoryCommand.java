package io.github.zeculesu.itmo.prog5.server.command;

import io.github.zeculesu.itmo.prog5.data.SpaceMarineCollection;
import io.github.zeculesu.itmo.prog5.models.SpaceMarine;
import io.github.zeculesu.itmo.prog5.models.Response;
import io.github.zeculesu.itmo.prog5.client.ConsoleCommandEnvironment;

import java.util.List;

/**
 * Вывод истории команд
 */
public class HistoryCommand extends AbstractCommand {

    public HistoryCommand() {
        super("history", "history : вывести последние 13 команд (без их аргументов)", false, false);
    }

    @Override
    public Response execute(SpaceMarineCollection collectionSpaceMarine, ConsoleCommandEnvironment env, String[] args, SpaceMarine... element) {
        Response response = new Response();
        // todo Изменить чтобы каждому пользователю своя история присылалась, а не общая
        List<String> history = env.getCommandHistory();
        int start = Math.max(history.size() - 13, 0);
        for (int i = 0; (i < 13 && i < history.size()); i++) {
            response.addLineOutput(history.get(start + i));
        }
        if (!response.isOutput()) response.setError("История команд пуста");
        return response;
    }
}
