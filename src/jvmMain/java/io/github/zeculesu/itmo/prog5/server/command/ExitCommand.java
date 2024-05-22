package io.github.zeculesu.itmo.prog5.server.command;

import io.github.zeculesu.itmo.prog5.data.SpaceMarineCollection;
import io.github.zeculesu.itmo.prog5.models.SpaceMarine;
import io.github.zeculesu.itmo.prog5.models.Response;
import io.github.zeculesu.itmo.prog5.client.ConsoleCommandEnvironment;

/**
 * Завершение работы
 */
public class ExitCommand extends AbstractCommand {

    public ExitCommand(){
        super("exit", "exit : завершить программу", false, false);
    }

    @Override
    public Response execute(SpaceMarineCollection collectionSpaceMarine, ConsoleCommandEnvironment env, String[] args, SpaceMarine... element) {
        Response response = new Response();
        response.setStatus(400);
        new SaveCommand().execute(collectionSpaceMarine, env, args, element);
        response.setMessage("Конец работы программы");
        return response;
    }
}
