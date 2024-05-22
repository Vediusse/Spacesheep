package io.github.zeculesu.itmo.prog5.server.command;

import io.github.zeculesu.itmo.prog5.client.ConsoleCommandEnvironment;
import io.github.zeculesu.itmo.prog5.data.InMemorySpaceMarineCollection;
import io.github.zeculesu.itmo.prog5.data.SpaceMarineCollection;
import io.github.zeculesu.itmo.prog5.models.Response;
import io.github.zeculesu.itmo.prog5.models.SpaceMarine;
import io.github.zeculesu.itmo.prog5.server.parseFile.ReadFileXML;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;


public class DownloadCollectionCommand extends AbstractCommand {

    public  DownloadCollectionCommand(){
        super("download",  "download: загрузка коллекции из файла", false, false);
    }
    @Override
    public Response execute(SpaceMarineCollection collectionSpaceMarine, ConsoleCommandEnvironment env, String[] args, SpaceMarine... element) {
        Response response = new Response();
        if (env.getFileNameCollection() == null) {
            response.setError("Имя файла с коллекцией не указано, оно должно храниться в переменной окружения FILENAME");
        } else {
            response.addLineOutput("Файл с коллекцией: " + env.getFileNameCollection());
            try {
                ReadFileXML.parseFile(env.getFileNameCollection(), (InMemorySpaceMarineCollection) collectionSpaceMarine);
                ((InMemorySpaceMarineCollection) collectionSpaceMarine).setNewMaxId();
                response.addLineOutput("Элементы из коллекции загружены");
            } catch (FileNotFoundException | ParserConfigurationException | SAXException e) {
                response.setError(e.getMessage());
            }
        }
        return response;
    }
}
