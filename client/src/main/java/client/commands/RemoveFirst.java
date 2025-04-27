package client.commands;

import client.network.UDPClient;
import client.utility.Console;
import common.exceptions.APIException;
import common.exceptions.WrongAmountOfElementsException;
import common.network.requests.RemoveFirstRequest;
import common.network.responses.RemoveFirstResponse;

import java.io.IOException;

/**
 * Команда 'remove_first'. Удаляет первый элемент из коллекции.
 */
public class RemoveFirst extends Command {
    private final Console console;
    private final UDPClient client;
    public RemoveFirst(Console console, UDPClient client) {
        super("remove_first");
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
            if (!arguments[1].isEmpty()) { throw new WrongAmountOfElementsException(getName()); }

            var response = (RemoveFirstResponse) client.sendAndReceiveCommand(new RemoveFirstRequest());
            if (response.getError() != null && !response.getError().isEmpty()) {
                throw new APIException(response.getError());
            }

            console.println("Продукт успешно удален.");
            return true;
        } catch (WrongAmountOfElementsException | APIException e) {
            console.printError(e.getMessage());
        } catch (IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        }
        return false;
    }
}
