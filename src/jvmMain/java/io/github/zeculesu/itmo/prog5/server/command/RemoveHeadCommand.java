package io.github.zeculesu.itmo.prog5.server.command;

import io.github.zeculesu.itmo.prog5.data.SpaceMarineCollection;
import io.github.zeculesu.itmo.prog5.error.OwnershipException;
import io.github.zeculesu.itmo.prog5.models.SpaceMarine;
import io.github.zeculesu.itmo.prog5.error.EmptyCollectionException;
import io.github.zeculesu.itmo.prog5.models.Response;
import io.github.zeculesu.itmo.prog5.client.ConsoleCommandEnvironment;

/**
 * Удаление первого элемента из коллекции
 */
public class RemoveHeadCommand extends AbstractCommand {

    public RemoveHeadCommand() {
        super("remove_head", "remove_head : вывести первый элемент коллекции и удалить его", false, false);
    }

    @Override
    public Response execute(SpaceMarineCollection collectionSpaceMarine, ConsoleCommandEnvironment env, String[] args, SpaceMarine... element) {
        Response response = new Response();
        try {
            response.addElement(collectionSpaceMarine.removeHead());
            response.setMessage("Элемент успешно удален");
        } catch (EmptyCollectionException e) {
            response.setError(e.getMessage());
        } catch (OwnershipException e){
            response.setError("Вы не можете модифицировать не свои объекты");
        }
        return response;
    }
}
