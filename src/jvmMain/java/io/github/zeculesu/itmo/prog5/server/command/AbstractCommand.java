package io.github.zeculesu.itmo.prog5.server.command;

import org.jetbrains.annotations.NotNull;

public abstract class AbstractCommand implements CommandAction {

    String name;
    String description;
    boolean acceptsElement;
    boolean acceptsArg;

    public AbstractCommand(String name, String description, boolean acceptsElement, boolean acceptsArg){
        this.name = name;
        this.description = description;
        this.acceptsElement = acceptsElement;
        this.acceptsArg = acceptsArg;
    }

    @NotNull
    @Override
    public String getName() {
        return this.name;
    }

    @NotNull
    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public boolean isAcceptsElement() {
        return this.acceptsElement;
    }

    @Override
    public boolean isAcceptsArg() {
        return acceptsArg;
    }
}
