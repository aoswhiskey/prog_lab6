package client.commands;

import client.utility.Console;
import common.exceptions.WrongAmountOfElementsException;

/**
 * Команда 'exit'. Завершает программу.
 */
public class Exit extends Command {
    private final Console console;

    public Exit(Console console) {
        super("exit");
        this.console = console;
    }
    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public boolean execute(String[] arguments) {
        try {
            if (!arguments[1].isEmpty()) {
                throw new WrongAmountOfElementsException(getName());
            }
            console.println("Завершение выполнения...");
            return true;
        } catch (WrongAmountOfElementsException e) {
            console.printError(e.getMessage());
        }
        return false;
    }
}