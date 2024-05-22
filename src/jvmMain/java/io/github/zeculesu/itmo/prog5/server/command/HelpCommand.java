package io.github.zeculesu.itmo.prog5.server.command;

import io.github.zeculesu.itmo.prog5.data.SpaceMarineCollection;
import io.github.zeculesu.itmo.prog5.models.SpaceMarine;
import io.github.zeculesu.itmo.prog5.models.Response;
import io.github.zeculesu.itmo.prog5.client.ConsoleCommandEnvironment;

/**
 * Вывод всех возможных команд
 */
public class HelpCommand extends AbstractCommand {

    public HelpCommand() {
        super("help", "help : вывести справку по доступным командам", false, false);
    }

    @Override
    public Response execute(SpaceMarineCollection collectionSpaceMarine, ConsoleCommandEnvironment env, String[] args, SpaceMarine... element) {
        Response response = new Response();
        response.addLineOutput("Список доступных команд:");
        for (CommandAction command : env.getCommandSetMap()) {
            response.addLineOutput(command.getDescription());
        }
        return response;
    }
}
