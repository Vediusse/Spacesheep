package io.github.zeculesu.itmo.prog5.server.command;

import io.github.zeculesu.itmo.prog5.server.command.CommandAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Список команд
 */
public interface CommandSet extends Iterable<CommandAction> {
    @Nullable
    CommandAction findCommand(@NotNull String comm);

    void addCommand(@NotNull CommandAction comm);

    void removeCommand(@NotNull CommandAction comm);
}