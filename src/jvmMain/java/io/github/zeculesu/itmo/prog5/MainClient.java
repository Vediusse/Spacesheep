package io.github.zeculesu.itmo.prog5;

import io.github.zeculesu.itmo.prog5.client.CLientConsole;
import static kotlin.io.ConsoleKt.readlnOrNull;

public class MainClient {
    public static void main(String[] args) {
        System.out.print("Введите хост (для локального localhost): ");
        String host = readlnOrNull();
        CLientConsole console = new CLientConsole(host, 45000);
        console.start();
    }
}
