package io.github.zeculesu.itmo.prog5.server.command;

import io.github.zeculesu.itmo.prog5.data.SpaceMarineCollection;
import io.github.zeculesu.itmo.prog5.error.OwnershipException;
import io.github.zeculesu.itmo.prog5.models.SpaceMarine;
import io.github.zeculesu.itmo.prog5.error.ElementNotFound;
import io.github.zeculesu.itmo.prog5.error.InputFormException;
import io.github.zeculesu.itmo.prog5.error.NamingEnumException;
import io.github.zeculesu.itmo.prog5.models.Response;
import io.github.zeculesu.itmo.prog5.client.ConsoleCommandEnvironment;
import io.github.zeculesu.itmo.prog5.client.ElementFormConsole;

/**
 * Обновление полей элемента по его id
 */
public class UpdateCommand extends AbstractCommand {

    public UpdateCommand(){
        super("update", "update id : обновить значение элемента коллекции, id которого равен заданному", true, true);
    }

    @Override
    public Response execute(SpaceMarineCollection collectionSpaceMarine, ConsoleCommandEnvironment env, String[] args, SpaceMarine... element) {
        Response response = new Response();
        if (args.length == 0) {
            response.setError("Не введен аргумент - id элемента");
            return response;
        }
        SpaceMarine elem = element[0];
        try {
            int id = ElementFormConsole.checkId(args[0]);
            collectionSpaceMarine.update(id, elem);
            response.setMessage("Элемент обновлен");
        } catch (NamingEnumException | InputFormException | ElementNotFound | OwnershipException e) {
            response.setError(e.getMessage());
        }
        return response;
    }
}
