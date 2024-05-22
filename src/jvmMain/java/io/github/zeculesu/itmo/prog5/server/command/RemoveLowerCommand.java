package io.github.zeculesu.itmo.prog5.server.command;

import io.github.zeculesu.itmo.prog5.data.SpaceMarineCollection;
import io.github.zeculesu.itmo.prog5.models.SpaceMarine;
import io.github.zeculesu.itmo.prog5.error.InputFormException;
import io.github.zeculesu.itmo.prog5.error.NamingEnumException;
import io.github.zeculesu.itmo.prog5.models.Response;
import io.github.zeculesu.itmo.prog5.client.ConsoleCommandEnvironment;

/**
 * Удаление элементов меньше заданного
 */
public class RemoveLowerCommand extends AbstractCommand {

    public RemoveLowerCommand() {
        super("remove_lower", "remove_lower {element} : удалить из коллекции все элементы, меньшие, чем заданный", true, false);
    }

    @Override
    public Response execute(SpaceMarineCollection collectionSpaceMarine, ConsoleCommandEnvironment env, String[] args, SpaceMarine... element) {
        Response response = new Response();
        SpaceMarine elem = element[0];
        try {

            int startSize = collectionSpaceMarine.size();

            collectionSpaceMarine.removeLower(elem);

            int endSize = collectionSpaceMarine.size();
            if (startSize == endSize) {
                response.setError("Элементов с меньше данного в коллекции не найдено");
            }
            response.setMessage("Удаление произошло успешно");
        } catch (NamingEnumException | InputFormException e) {
            response.setError(e.getMessage());
        }
        return response;
    }
}
