package io.github.zeculesu.itmo.prog5.models;

import io.github.zeculesu.itmo.prog5.models.Response;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SendedCommandResponse extends Response implements Serializable {

    private final Map<String, Boolean[]> commands;

    public SendedCommandResponse() {
        this.commands = new HashMap<>();
    }

    public void addData(String name, boolean acceptedElem, boolean acceptedArg) {
        Boolean[] values = {acceptedElem, acceptedArg};
        commands.put(name, values);
    }

    public boolean haveThisCommand(String name) {
        return commands.containsKey(name);
    }

    public boolean commandAcceptedElem(String name){return commands.get(name)[0];}

    public boolean commandAcceptedArg(String name){return commands.get(name)[1];}
}