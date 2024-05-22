package io.github.zeculesu.itmo.prog5.server.command;

import io.github.zeculesu.itmo.prog5.data.SpaceMarineCollection;
import io.github.zeculesu.itmo.prog5.models.SpaceMarine;
import io.github.zeculesu.itmo.prog5.models.Response;
import io.github.zeculesu.itmo.prog5.client.ConsoleCommandEnvironment;

/**
 * Достать элементы из коллекции, у которых имя начинается на заданную подстроку
 */
public class FilterNameCommand extends AbstractCommand {
    public FilterNameCommand() {
        super("filter_starts_with_name", "filter_starts_with_name name : вывести элементы, значение поля name которых начинается с заданной подстроки", false, true);
    }

    @Override
    public Response execute(SpaceMarineCollection collectionSpaceMarine, ConsoleCommandEnvironment env, String[] args, SpaceMarine... element) {
        Response response = new Response();
        if (args.length == 0) {
            response.setError("Не введен аргумент - подстрока, с которой должны начинаться имена элементов");
            return response;
        }
        String name = args[0];
        response.setOutputElement(collectionSpaceMarine.filterStartsWithName(name));
        if (!response.isOutputElement()) response.setError("Не нашлось ни одного элемента");
        return response;
    }
}
