package io.github.zeculesu.itmo.prog5.client;

import io.github.zeculesu.itmo.prog5.client.ColorConsole;

import java.io.IOException;

/**
 * Ввод/Вывод команд
 */
public interface CommandIO {
    void print(String line, ColorConsole... color);

    void println(String line, ColorConsole... color);

    String readln() throws IOException;
}
