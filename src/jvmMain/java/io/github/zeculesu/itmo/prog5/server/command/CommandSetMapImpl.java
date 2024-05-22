package io.github.zeculesu.itmo.prog5.server.command;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Множество команд, методы для работы с ними
 */
public class CommandSetMapImpl implements CommandSet {

    HashMap<String, CommandAction> commandSet = new HashMap<>();

    public CommandSetMapImpl(CommandAction... commands){
        for (CommandAction command : commands){
            addCommand(command);
        }
    }

    @Nullable
    @Override
    public CommandAction findCommand(@NotNull String comm) {
        return this.commandSet.get(comm);
    }

    @Override
    public void addCommand(@NotNull CommandAction comm) {
        this.commandSet.put(comm.getName(), comm);
    }

    @NotNull
    @Override
    public Iterator<CommandAction> iterator() {
        return this.commandSet.values().iterator();
    }

    @Override
    public void removeCommand(@NotNull CommandAction comm) {
        this.commandSet.remove(comm);
    }
}
