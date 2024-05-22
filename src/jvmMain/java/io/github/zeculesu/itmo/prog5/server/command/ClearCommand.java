package io.github.zeculesu.itmo.prog5.server.command;

import io.github.zeculesu.itmo.prog5.data.SpaceMarineCollection;
import io.github.zeculesu.itmo.prog5.models.SpaceMarine;
import io.github.zeculesu.itmo.prog5.models.Response;
import io.github.zeculesu.itmo.prog5.client.ConsoleCommandEnvironment;

/**
 * Очищение коллекции
 */
public class ClearCommand extends AbstractCommand {

    public ClearCommand() {
        super("clear", "clear : очистить коллекцию", false, false);
    }


    @Override
    public Response execute(SpaceMarineCollection collectionSpaceMarine, ConsoleCommandEnvironment env, String[] args, SpaceMarine... element) {
        Response response = new Response();
        collectionSpaceMarine.clear();
        response.setMessage("Коллекция очищена");
        return response;
    }
}
