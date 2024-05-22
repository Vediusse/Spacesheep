package io.github.zeculesu.itmo.prog5.server.command;

import io.github.zeculesu.itmo.prog5.data.SpaceMarineCollection;
import io.github.zeculesu.itmo.prog5.models.SpaceMarine;
import io.github.zeculesu.itmo.prog5.models.Response;
import io.github.zeculesu.itmo.prog5.client.ConsoleCommandEnvironment;

/**
 * Получение информации о коллекции
 */
public class InfoCommand extends AbstractCommand {

    public InfoCommand() {
        super("info", "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов)", false, false);
    }

    @Override
    public Response execute(SpaceMarineCollection collectionSpaceMarine, ConsoleCommandEnvironment env, String[] args, SpaceMarine... element) {
        Response response = new Response();
        response.setOutput(collectionSpaceMarine.info());
        return response;
    }
}

