package io.github.zeculesu.itmo.prog5.server.command;

import io.github.zeculesu.itmo.prog5.data.SpaceMarineCollection;
import io.github.zeculesu.itmo.prog5.models.SpaceMarine;
import io.github.zeculesu.itmo.prog5.models.Response;
import io.github.zeculesu.itmo.prog5.client.ConsoleCommandEnvironment;

/**
 * Выполнение скрипта из файла
 */
public class ExecuteScriptCommand extends AbstractCommand {

    public ExecuteScriptCommand() {
        super("execute_script", "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.", false, true);
    }

    @Override
    public Response execute(SpaceMarineCollection collectionSpaceMarine, ConsoleCommandEnvironment env, String[] args, SpaceMarine... element) {
        Response response = new Response();
        if (args.length == 0) {
            response.setError("Имя файла не введено");
            return response;
        }
        try {
            //todo добавить обработку рекурсии
           // String fileName = args[0];
            //    if (env.checkRecursionScript(fileName)) {
            //          //todo переделать проверку на рекурсию
            //            response.setMessage("Вы создаете рекурсию из скриптов, ата-та");
            //              return response;
//            }
            //env.addScriptQueue(fileName);
//            FileReader fileReader = new FileReader(fileName);
//            BufferedReader bufferedReader = new BufferedReader(fileReader);
//            env.setBufferReaderScript(bufferedReader);
            response.addLineOutput("Начало выполнения скрипта: ");
//            env.setStateIO(StateIO.CONSOLE_TO_SCRIPT);
            response.setStatus(300);
        } catch (Exception e) {
            response.setError("Не удалось открыть файл");
        }
        return response;
    }
}
