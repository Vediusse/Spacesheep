package io.github.zeculesu.itmo.prog5.server.command;

import io.github.zeculesu.itmo.prog5.data.SpaceMarineCollection;
import io.github.zeculesu.itmo.prog5.models.SpaceMarine;
import io.github.zeculesu.itmo.prog5.error.FileCollectionException;
import io.github.zeculesu.itmo.prog5.models.Response;
import io.github.zeculesu.itmo.prog5.client.ConsoleCommandEnvironment;
import io.github.zeculesu.itmo.prog5.server.parseFile.WriteFileXML;

/**
 * Сохранение в файл
 */
public class SaveCommand extends AbstractCommand {
    public SaveCommand() {
        super("save", "save : сохранить коллекцию в файл", false, false);
    }

    @Override
    public Response execute(SpaceMarineCollection collectionSpaceMarine, ConsoleCommandEnvironment env, String[] args, SpaceMarine... element) {
        Response response = new Response();
        try {
            WriteFileXML.writeFile(env.getFileNameCollection(), collectionSpaceMarine);
            response.setMessage("Сохранение коллекции в файл прошло успешно");
        } catch (FileCollectionException e) {
            response.setError(e.getMessage());
        }
        return response;
    }
}
