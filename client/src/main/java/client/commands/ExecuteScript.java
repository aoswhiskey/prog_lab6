package client.commands;

import client.utility.Console;
import common.exceptions.WrongAmountOfElementsException;

/**
 * Команда 'execute_script'. Выполняет скрипт из файла.
 */
public class ExecuteScript extends Command {
    private final Console console;

    public ExecuteScript(Console console) {
        super("execute_script file_name");
        this.console = console;
    }
    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public boolean execute(String[] arguments) {
        try {
            if (arguments[1].isEmpty()) {
                throw new WrongAmountOfElementsException(getName());
            }
            console.println("Выполнение скрипта '" + arguments[1] + "'...");
            return true;
        } catch (WrongAmountOfElementsException e) {
            console.printError(e.getMessage());
        }
        return false;
    }
}