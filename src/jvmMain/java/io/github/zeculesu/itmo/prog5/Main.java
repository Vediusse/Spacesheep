package io.github.zeculesu.itmo.prog5;

import io.github.zeculesu.itmo.prog5.data.InMemorySpaceMarineCollection;
import io.github.zeculesu.itmo.prog5.server.command.CommandSet;
import io.github.zeculesu.itmo.prog5.server.command.CommandSetMapImpl;
import io.github.zeculesu.itmo.prog5.server.command.*;
import io.github.zeculesu.itmo.prog5.client.CommunicatedClient;
import io.github.zeculesu.itmo.prog5.client.Console;
import io.github.zeculesu.itmo.prog5.client.DefaultConsoleCommandEnvironmentImpl;

/**
 * Главный класс, который запускает всё
 */
// new life in new branch
public class Main {
    public static void main(String[] args) {
        CommandSet commandSetMap = new CommandSetMapImpl(new AddCommand(), new ClearCommand(), new FilterNameCommand(),
                new HelpCommand(), new PrintHealthCommand(), new RemoveByIdCommand(), new RemoveByWeaponCommand(), new RemoveHeadCommand(), new RemoveLowerCommand(),
                new SaveCommand(), new ShowCommand(), new UpdateCommand(), new InfoCommand(), new ExitCommand(), new HistoryCommand(), new ExecuteScriptCommand());
        String fileName = System.getenv("FILENAME");
        DefaultConsoleCommandEnvironmentImpl env = new DefaultConsoleCommandEnvironmentImpl(commandSetMap, fileName);
        InMemorySpaceMarineCollection collectionSpaceMarine = new InMemorySpaceMarineCollection();
        CommunicatedClient console = new Console(env, collectionSpaceMarine);
        console.start();
    }
}
