package io.github.zeculesu.itmo.prog5.client;

import io.github.zeculesu.itmo.prog5.models.SpaceMarine;
import io.github.zeculesu.itmo.prog5.data.InMemorySpaceMarineCollection;
import io.github.zeculesu.itmo.prog5.error.InputFormException;
import io.github.zeculesu.itmo.prog5.error.NamingEnumException;
import io.github.zeculesu.itmo.prog5.server.command.CommandAction;
import io.github.zeculesu.itmo.prog5.models.Response;
import io.github.zeculesu.itmo.prog5.server.command.DownloadCollectionCommand;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static io.github.zeculesu.itmo.prog5.client.ColorConsole.*;
import static kotlin.io.ConsoleKt.readlnOrNull;

/**
 * Реализует консоль, взаимодействие с пользователем
 */
public class Console implements CommunicatedClient {

    private final DefaultConsoleCommandEnvironmentImpl environment;
    private final InMemorySpaceMarineCollection collectionSpaceMarine;

    private final CommandIOConsole console;

    public Console(DefaultConsoleCommandEnvironmentImpl environment, InMemorySpaceMarineCollection collectionSpaceMarine) {
        this.environment = environment;
        this.collectionSpaceMarine = collectionSpaceMarine;
        this.console = new CommandIOConsole();
    }
    @Override
    public void start(){
        this.environment.setRun(true);

        //загрузка коллекции из файла
        outputResponse(new DownloadCollectionCommand().execute(this.collectionSpaceMarine, environment, new String[0]));
        run();
    }

    @Override
    public void run() {

        String command;

        while (environment.isRun()) {

            console.print("> ");

            command = readlnOrNullCommand();

            if (this.environment.getStateIO() == StateIO.SCRIPT_TO_CONSOLE) {
                console.println("Конец скрипта");
                this.environment.setStateIO(StateIO.CONSOLE);
                this.environment.clearScriptQueue();
            } else if (command == null) {
                console.println("Конец работы программы");
                return;
            } else if (command.isBlank()) {
                console.println("Команда не введена");
            } else {
                readCommand(command);
            }
        }
    }

    public void readCommand(@NotNull String command) {
        String[] token = command.split(" ");
        CommandAction com = this.environment.getCommandSetMap().findCommand(token[0]);
        if (com != null) {
            String[] args = token.length == 2 ? token[1].split(" ") : new String[0];
            SpaceMarine element = null;
            try {
                if (com.isAcceptsElement()) {
                    element = ElementFormConsole.getElemFromForm(new CommandIOConsole());
                }
                Response response = com.execute(this.collectionSpaceMarine, this.environment, args, element);
                outputResponse(response);
            } catch (InputFormException | NamingEnumException | IOException e) {
                console.println(e.getMessage(), ERROR);
            }

        } else console.println("Неизвестная команда. Введите 'help' для получения справки.");
        this.environment.addCommandToHistory(token[0]);
        if (this.environment.getStateIO() == StateIO.CONSOLE_TO_SCRIPT) {
            this.environment.setStateIO(StateIO.SCRIPT);
        }
    }

    public void outputResponse(Response response) {
        if (response.isOutputElement()) {
            for (SpaceMarine line : response.getOutputElement()) {
                console.println(line.toString());
            }
        }
        if (response.isOutput()) {
            for (String line : response.getOutput()) {
                console.println(line);
            }
        }
        if (response.isError()) console.println(response.getError(), ColorConsole.ERROR);
        if (response.isMessage()) console.println(response.getMessage());
    }

    public String readlnOrNullCommand() {
        if (this.environment.getStateIO() == StateIO.SCRIPT) {
            try {
                if (!this.environment.getBufferReaderScript().ready()) {
                    this.environment.setStateIO(StateIO.SCRIPT_TO_CONSOLE);
                    console.println("");
                    return null;
                }
                return console.readln();
            } catch (IOException e) {
                console.println("Проблемы с чтением файла скрипта", ERROR);
            }
        }
        return readlnOrNull();
    }

    class CommandIOConsole implements CommandIO {
        @Override
        public void print(String line, ColorConsole... color) {
            if (color.length != 0) {
                System.out.print(color[0].getAnsiCode() + conversionForScriptOutput(line) + DEFAULT.getAnsiCode());
            } else {
                System.out.print(conversionForScriptOutput(line));
            }
        }

        @Override
        public void println(String line, ColorConsole... color) {
            if (color.length != 0) {
                System.out.println(color[0].getAnsiCode() + conversionForScriptOutput(line) + DEFAULT.getAnsiCode());
            } else {
                System.out.println(conversionForScriptOutput(line));
            }
        }

        @Override
        public String readln() throws IOException {
            if (Console.this.environment.getStateIO() == StateIO.SCRIPT) {
                String input = Console.this.environment.getBufferReaderScript().readLine();
                System.out.println(SCRIPT.getAnsiCode() + input + DEFAULT.getAnsiCode());
                return input;
            }
            return readlnOrNull();
        }

        public String conversionForScriptOutput(String line) {
            if (Console.this.environment.getStateIO() == StateIO.SCRIPT) {
                return "\t" + line;
            }
            return line;
        }
    }
}
