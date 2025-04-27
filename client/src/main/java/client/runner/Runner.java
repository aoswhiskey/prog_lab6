package client.runner;

import client.App;
import client.ask.AskBreak;
import client.commands.*;
import client.network.UDPClient;
import client.utility.Console;
import common.exceptions.ScriptRecursionException;
import common.utility.Commands;

import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Класс, запускающий введенные пользователем команды.
 */
public class Runner {
    public enum ExitCode {
        OK,
        ERROR,
        EXIT,
    }

    private final Console console;
    private final UDPClient client;
    private final Map<String, Command> commands;
    private final Logger logger = App.logger;
    private final Set<String> scriptStack = new HashSet<>();

    public Runner(UDPClient client, Console console) {
        this.client = client;
        this.console = console;
        this.commands = new HashMap<>() {{
            put(Commands.ADD, new Add(console, client));
            put(Commands.CLEAR, new Clear(console, client));
            put(Commands.EXECUTE_SCRIPT, new ExecuteScript(console));
            put(Commands.EXIT, new Exit(console));
            put(Commands.FILTER_LESS_THAN_AUTHOR, new FilterLessThanAuthor(console, client));
            put(Commands.HEAD, new Head(console, client));
            put(Commands.HELP, new Help(console, client));
            put(Commands.INFO, new Info(console, client));
            put(Commands.MAX_BY_MINIMAL_POINT, new MaxByMinimalPoint(console, client));
            put(Commands.PRINT_FIELD_DESCENDING_DIFFICULTY, new PrintFieldDescendingDifficulty(console, client));
            put(Commands.REMOVE_AT, new RemoveAt(console, client));
            put(Commands.REMOVE_BY_ID, new RemoveById(console, client));
            put(Commands.REMOVE_FIRST, new RemoveFirst(console, client));
            put(Commands.SHOW, new Show(console, client));
            put(Commands.UPDATE, new Update(console, client));
        }};
    }
    /**
     * Интерактивный режим
     */
    public void interactiveMode() {
        try {
            ExitCode commandStatus;
            String[] userCommand;

            do {
                console.prompt();
                userCommand = (console.readln().trim() + " ").split(" ", 2);
                userCommand[1] = userCommand[1].trim();
                commandStatus = launchCommand(userCommand);
            } while (commandStatus != ExitCode.EXIT);

        } catch (IllegalStateException exception) {
            console.printError("Непредвиденная ошибка!");
        } catch (NoSuchElementException exception) {
            console.printError("Завершение программы...");
            System.exit(1);
        } catch (AskBreak e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Режим для запуска скрипта.
     * @param argument Аргумент скрипта
     * @return Код завершения.
     */
    public ExitCode scriptMode(String argument) {
        String[] userCommand;
        ExitCode commandStatus;
        scriptStack.add(argument);
        try (Scanner scriptScanner = new Scanner(new File(argument))) {
            if (!scriptScanner.hasNext()) throw new NoSuchElementException();
            console.selectFileScanner(scriptScanner);

            do {
                userCommand = (scriptScanner.nextLine().trim() + " ").split(" ", 2);
                userCommand[1] = userCommand[1].trim();
                while (console.isCanReadln() && userCommand[0].isEmpty()) {
                    userCommand = (scriptScanner.nextLine().trim() + " ").split(" ", 2);
                    userCommand[1] = userCommand[1].trim();
                }
                console.println(console.getPrompt() + String.join(" ", userCommand));
                if (userCommand[0].equals("execute_script")) {
                    if (scriptStack.contains(userCommand[1])) {
                        throw new ScriptRecursionException();
                    }
                }
                commandStatus = launchCommand(userCommand);
            } while (commandStatus == ExitCode.OK && console.isCanReadln());

            console.selectConsoleScanner();

            if (commandStatus == ExitCode.ERROR && (!userCommand[0].equals("execute_script") || userCommand[1].isEmpty())) {
                console.println("Скрипт содержит некорректные данные!");
            }

            return commandStatus;

        } catch (FileNotFoundException exception) {
            console.printError("Файл со скриптом не найден!");
        } catch (NoSuchElementException exception) {
            console.printError("Файл со скриптом пуст!");
        } catch (ScriptRecursionException e) {
            console.printError(e.getMessage());
        } catch (IllegalStateException exception) {
            console.printError("Непредвиденная ошибка!");
            System.exit(1);
        } catch (AskBreak e) {
            throw new RuntimeException(e);
        } finally {
            scriptStack.remove(argument);
        }
        return ExitCode.ERROR;
    }
    /**
     * Исполняет команду.
     * @param userCommand Команда для запуска.
     * @return Код завершения.
     */
    private ExitCode launchCommand(String[] userCommand) throws AskBreak {
        if (userCommand[0].equals("")) return ExitCode.OK;
        var command = commands.get(userCommand[0]);

        if (command == null) {
            console.printError("Команда '" + userCommand[0] + "' не найдена. Наберите 'help' для справки");
            return ExitCode.ERROR;
        }

        switch (userCommand[0]) {
            case "exit" -> {
                if (!command.execute(userCommand)) return ExitCode.ERROR;
                else return ExitCode.EXIT;
            }
            case "execute_script" -> {
                if (!command.execute(userCommand)) return ExitCode.ERROR;
                else return scriptMode(userCommand[1]);
            }
            default -> { if (!command.execute(userCommand)) return ExitCode.ERROR; }
        }

        return ExitCode.OK;
    }
}