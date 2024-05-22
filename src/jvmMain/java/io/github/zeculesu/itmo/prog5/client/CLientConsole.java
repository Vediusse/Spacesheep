package io.github.zeculesu.itmo.prog5.client;

import io.github.zeculesu.itmo.prog5.models.Request;
import io.github.zeculesu.itmo.prog5.models.SendedCommandResponse;
import io.github.zeculesu.itmo.prog5.models.SpaceMarine;
import io.github.zeculesu.itmo.prog5.error.InputFormException;
import io.github.zeculesu.itmo.prog5.error.NamingEnumException;
import io.github.zeculesu.itmo.prog5.models.Response;
import io.github.zeculesu.itmo.prog5.server.Auth;
import org.jetbrains.annotations.NotNull;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

import static io.github.zeculesu.itmo.prog5.client.ColorConsole.*;
import static kotlin.io.ConsoleKt.readlnOrNull;

/**
 * Реализует консоль, взаимодействие с пользователем
 */
public class CLientConsole implements CommunicatedClient {
    private String login;
    private String password;
    private StateIO stateIO;
    private final Set<String> scriptQueue = new HashSet<>();
    private BufferedReader bufferReaderScript;
    private SendedCommandResponse commandsSet;

    private final CommandIOConsole console;
    boolean run;
    UDPClient udpClient;

    public CLientConsole(String host, int port) {
        this.console = new CommandIOConsole();
        this.udpClient = new UDPClient(host, port);
    }

    @Override
    public void start() {
        console.println("Добро пожаловать");
        try {
            if (!auth()){
                console.println("Сессия провалена", ERROR);
                return;
            };
            this.commandsSet = udpClient.sendMeCommand();
            this.run = true;
            run();
        } catch (IOException | ClassNotFoundException e) {
            console.println("Сервер временно недоступен", ERROR);
            String answer = "";
            while (!answer.equals("y")) {
                console.println("Повторить подключение? (y/n)");
                console.print("> ");
                answer = readlnOrNullCommand();
                if (answer == null || answer.equals("n")) {
                    console.println("Конец работы программы");
                    return;
                } else if (answer.equals("y")) {
                    start();
                }
            }
        }
    }

    public boolean auth() throws IOException, ClassNotFoundException {
        String answer = "f";
        while (answer != null) {
            console.println("Для начала работы надо пройти авторизацию");
            console.print("Выберите авторизоваться (1) или зарегистрироваться (2): ");
            answer = readlnOrNullCommand();
            if (answer == null){
                console.println("Конец работы программы");
                System.exit(0);
            }
            if (answer.equals("1")) {
                console.print("Введите логин: ");
                String login = readlnOrNullCommand();
                if (login == null){
                    console.println("Конец работы программы");
                    System.exit(0);
                }
                console.print("Введите пароль: ");
                String password = readlnOrNullCommand();
                if (password == null){
                    console.println("Конец работы программы");
                    System.exit(0);
                }
                Response response = Auth.sendAuth(login, password, this.udpClient);
                outputResponse(response);
                if (response.getStatus() == 200){
                    this.login = login;
                    this.password = password;
                    return true;
                }
            } else if (answer.equals("2")) {
                console.print("Введите логин: ");
                String login = readlnOrNullCommand();
                if (login == null){
                    console.println("Конец работы программы");
                    System.exit(0);
                }
                Response response = Auth.checkUniqLogin(login, udpClient);
                outputResponse(response);
                if (response.getStatus() == 200){
                    console.print("Придумайте пароль: ");
                    String password = readlnOrNullCommand();
                    if (password == null){
                        console.println("Конец работы программы");
                        System.exit(0);
                    }
                    Response response2 = Auth.sendReg(login, password, udpClient);
                    outputResponse(response2);
                }
            }
        }
        return false;
    }

    @Override
    public void run() {
        String command;

        while (run) {
            try {

                console.print("> ");

                command = readlnOrNullCommand();

                if (this.stateIO == StateIO.SCRIPT_TO_CONSOLE) {
                    console.println("Конец скрипта");
                    this.stateIO = StateIO.CONSOLE;
                    this.scriptQueue.clear();
                } else if (command == null) {
                    console.println("Конец работы программы");
                    return;
                } else if (command.isBlank()) {
                    console.println("Команда не введена");
                } else {
                    Request request = readCommand(command);
                    if (request != null) {
                        Response response = this.udpClient.sendRequest(request);

                        readStatus(response.getStatus(), request.getArg());
                        outputResponse(response);
                    }
                }
            } catch (Exception e) {
                console.println(e.getMessage());
            }
        }
    }

    public Request readCommand(@NotNull String command) {
        String[] token = command.split(" ");
        String nameCommand = token[0];
        boolean com = this.commandsSet.haveThisCommand(nameCommand);

        if (com) {
            Request request = new Request();
            request.setCommand(nameCommand);
            if (this.commandsSet.commandAcceptedArg(nameCommand)) {
                if (token.length == 1) {
                    console.println("Не введен аргумент для команды, для справки воспользуйтесь командой 'help'", ERROR);
                    return null;
                } else if (token.length > 2) {
                    console.println("Слишком много аргументов, для справки воспользуйтесь командой 'help'");
                    return null;
                } else {
                    request.setArg(token[1]);
                }
            }
            SpaceMarine element = null;
            try {
                if (this.commandsSet.commandAcceptedElem(nameCommand)) {
                    element = ElementFormConsole.getElemFromForm(new CommandIOConsole());
                }
            } catch (InputFormException | NamingEnumException | IOException e) {
                console.println(e.getMessage(), ERROR);
                return null;
            }
            request.setElem(element);
            request.setLogin(this.login);
            request.setPassword(this.password);
            return request;
        } else console.println("Неизвестная команда. Введите 'help' для получения справки.");
        if (this.stateIO == StateIO.CONSOLE_TO_SCRIPT) {
            this.stateIO = StateIO.SCRIPT;
        }
        return null;
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
        if (response.isError()) console.println(response.getError(), ERROR);
        if (response.isMessage()) console.println(response.getMessage());
    }

    public void readStatus(int status, String arg) {
        if (status == 400) {
            this.run = false;
        }
        if (status == 300) {
            try {
                scriptQueue.add(arg);
                FileReader fileReader = new FileReader(arg);
                this.bufferReaderScript = new BufferedReader(fileReader);
                this.stateIO = StateIO.SCRIPT;
            } catch (FileNotFoundException e) {
                console.println("Не удалось открыть файл", ERROR);
            }
        }
    }

    public String readlnOrNullCommand() {
        if (this.stateIO == StateIO.SCRIPT) {
            try {
                if (!this.bufferReaderScript.ready()) {
                    this.stateIO = StateIO.SCRIPT_TO_CONSOLE;
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
            if (CLientConsole.this.stateIO == StateIO.SCRIPT) {
                String input = CLientConsole.this.bufferReaderScript.readLine();
                System.out.println(SCRIPT.getAnsiCode() + input + DEFAULT.getAnsiCode());
                return input;
            }
            return readlnOrNull();
        }

        public String conversionForScriptOutput(String line) {
            if (CLientConsole.this.stateIO == StateIO.SCRIPT) {
                return "\t" + line;
            }
            return line;
        }
    }
}
