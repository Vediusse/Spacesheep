package io.github.zeculesu.itmo.prog5.client;

public enum ColorConsole {
    ERROR("\u001B[31m"),
    DEFAULT("\u001B[0m"),

    SCRIPT("\u001B[32m");

    private final String ansiCode;
    ColorConsole(String s) {
        this.ansiCode = s;
    }

    public String getAnsiCode() {
        return ansiCode;
    }
}