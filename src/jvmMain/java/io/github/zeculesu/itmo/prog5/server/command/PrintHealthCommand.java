package io.github.zeculesu.itmo.prog5.server.command;

import io.github.zeculesu.itmo.prog5.data.SpaceMarineCollection;
import io.github.zeculesu.itmo.prog5.models.SpaceMarine;
import io.github.zeculesu.itmo.prog5.error.EmptyCollectionException;
import io.github.zeculesu.itmo.prog5.models.Response;
import io.github.zeculesu.itmo.prog5.client.ConsoleCommandEnvironment;

/**
 * Получение всех полей здоровья из коллекции
 */
public class PrintHealthCommand extends AbstractCommand {

    public PrintHealthCommand() {
        super("print_field_descending_health", "print_field_descending_health : вывести значения поля health всех элементов в порядке убывания", false, false);
    }

    @Override
    public Response execute(SpaceMarineCollection collectionSpaceMarine, ConsoleCommandEnvironment env, String[] args, SpaceMarine... element) {
        Response response = new Response();
        try {
            response.addLineOutput(collectionSpaceMarine.printFieldDescendingHealth());
        } catch (EmptyCollectionException e) {
            response.setError(e.getMessage());
        }
        return response;
    }
}
