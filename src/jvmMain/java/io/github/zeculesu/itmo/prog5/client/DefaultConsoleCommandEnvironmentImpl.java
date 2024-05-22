package io.github.zeculesu.itmo.prog5.client;

import io.github.zeculesu.itmo.prog5.server.command.CommandAction;
import io.github.zeculesu.itmo.prog5.server.command.CommandSet;
import io.github.zeculesu.itmo.prog5.sql.ConnectingDB;

import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Дефолтное окружение консоли для работы
 */
public class DefaultConsoleCommandEnvironmentImpl implements ConsoleCommandEnvironment {

    private boolean run;
    private StateIO stateIO;
    private final Set<String> scriptQueue = new HashSet<>();
    private BufferedReader bufferReaderScript;
    private final CommandSet commandSetMap;
    private final String fileNameCollection;
    private final List<String> commandHistory = new ArrayList<>();

    private ConnectingDB connection;

    public DefaultConsoleCommandEnvironmentImpl(CommandSet commandSetMap, String fileNameCollection) {
        this.commandSetMap = commandSetMap;
        this.fileNameCollection = fileNameCollection;
    }

    @Override
    public String getFileNameCollection() {
        return this.fileNameCollection;
    }

    @Override
    public CommandSet getCommandSetMap() {
        return this.commandSetMap;
    }

    @Override
    public void addCommandToHistory(String command) {
        this.commandHistory.add(command);
    }

    @Override
    public List<String> getCommandHistory() {
        return this.commandHistory;
    }

    public boolean isRun() {
        return run;
    }

    public void setRun(boolean run) {
        this.run = run;
    }

    public StateIO getStateIO() {
        return stateIO;
    }

    public void setStateIO(StateIO stateIO) {
        this.stateIO = stateIO;
    }

    @Override
    public BufferedReader getBufferReaderScript() {
        return bufferReaderScript;
    }

    @Override
    public void setBufferReaderScript(BufferedReader bufferReaderScript) {
        this.bufferReaderScript = bufferReaderScript;
    }

    public boolean checkRecursionScript(String fileName) {
        return this.scriptQueue.contains(fileName);
    }

    public void addScriptQueue(String scriptName) {
        this.scriptQueue.add(scriptName);
    }

    public void clearScriptQueue() {
        this.scriptQueue.clear();
    }

    @Override
    public void removeFromCommandSetMap(String comm) {
        CommandAction command = this.commandSetMap.findCommand(comm);
        this.commandSetMap.removeCommand(command);
    }

    @Override
    public ConnectingDB getConnection() {
        return this.connection;
    }

    @Override
    public void setConnection(String url, String username, String password) throws SQLException {
        this.connection = new ConnectingDB(url, username, password);
    }
}
