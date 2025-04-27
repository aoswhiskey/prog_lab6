package client.commands;

import client.network.UDPClient;
import client.utility.Console;
import common.exceptions.*;
import common.network.requests.*;
import common.network.responses.*;

import java.io.IOException;

/**
 * Команда 'show'. Выводит все элементы коллекции.
 * @author maxbarsukov
 */
public class Show extends Command {
    private final Console console;
    private final UDPClient client;

    public Show(Console console, UDPClient client) {
        super("show");
        this.console = console;
        this.client = client;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public boolean execute(String[] arguments) {
        try {
            if (!arguments[1].isEmpty()) throw new WrongAmountOfElementsException(getName());

            var response = (ShowResponse) client.sendAndReceiveCommand(new ShowRequest());
            if (response.getError() != null && !response.getError().isEmpty()) {
                throw new APIException(response.getError());
            }

            if (response.labWorks.isEmpty()) {
                console.println("Коллекция пуста!");
                return true;
            }

            for (var product : response.labWorks) {
                console.println(product + "\n");
            }
            return true;
        } catch(IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        } catch (APIException | WrongAmountOfElementsException e) {
            console.printError(e.getMessage());
        }
        return false;
    }
}
