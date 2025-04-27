package common.exceptions;

/**
 * Исключения для неправильного количества элементов.
 */
public class WrongAmountOfElementsException extends Exception {
    private final String usage;

    public WrongAmountOfElementsException(String usage) {
        super("Неправильное количество аргументов при использовании команды " + usage);
        this.usage = usage;
    }

    public String getUsage() {
        return usage;
    }
}


