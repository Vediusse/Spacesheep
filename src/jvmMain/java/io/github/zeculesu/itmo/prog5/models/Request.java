package io.github.zeculesu.itmo.prog5.models;

import java.io.Serializable;

public class Request implements Serializable {
    private String command;
    private String arg;
    private SpaceMarine elem;

    private String login;
    private String password;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getArg() {
        return arg;
    }

    public void setArg(String arg) {
        this.arg = arg;
    }

    public SpaceMarine getElem() {
        return elem;
    }

    public void setElem(SpaceMarine elem) {
        this.elem = elem;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
