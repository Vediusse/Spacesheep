package io.github.zeculesu.itmo.prog5.server.command;

import io.github.zeculesu.itmo.prog5.data.*;
import io.github.zeculesu.itmo.prog5.error.InputFormException;
import io.github.zeculesu.itmo.prog5.error.NamingEnumException;
import io.github.zeculesu.itmo.prog5.models.Response;
import io.github.zeculesu.itmo.prog5.client.ConsoleCommandEnvironment;
import io.github.zeculesu.itmo.prog5.models.SpaceMarine;

/**
 * Добавление элемента SpaceMarine в коллекцию
 */
public class AddCommand extends AbstractCommand {

    public AddCommand(){
        super("add", "add {element} : добавить новый элемент в коллекцию", true, false);
    }

    @Override
    public Response execute(SpaceMarineCollection collectionSpaceMarine, ConsoleCommandEnvironment env, String[] args, SpaceMarine... element) {
        Response response = new Response();
        SpaceMarine elem = element[0];
        try {
            collectionSpaceMarine.add(elem);
            response.setMessage("Новый элемент добавлен");
            return response;
        } catch (NamingEnumException | InputFormException e) {
            response.setError(e.getMessage());
            return response;
        }
    }
}
