package client.commands;

import client.network.UDPClient;
import client.utility.Console;
import common.exceptions.*;
import common.network.requests.ClearRequest;
import common.network.responses.ClearResponse;

import java.io.IOException;

/**
 * Команда 'clear'. Очищает коллекцию.
 */
public class Clear extends Command {
    private final Console console;
    private final UDPClient client;

    public Clear(Console console, UDPClient client) {
        super("clear");
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

            var response = (ClearResponse) client.sendAndReceiveCommand(new ClearRequest());
            if (response.getError() != null && !response.getError().isEmpty()) {
                throw new APIException(response.getError());
            }

            console.println("Коллекция очищена!");
            return true;
        } catch (WrongAmountOfElementsException | APIException exception) {
            console.printError(exception.getMessage());
        } catch (IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        }
        return false;
    }
}
