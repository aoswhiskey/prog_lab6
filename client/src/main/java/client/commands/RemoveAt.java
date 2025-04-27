package client.commands;

import client.network.UDPClient;
import common.exceptions.APIException;
import common.exceptions.WrongAmountOfElementsException;
import client.utility.Console;
import common.network.requests.RemoveAtRequest;
import common.network.responses.RemoveAtResponse;

import java.io.IOException;

/**
 * Команда 'remove_at'. Удаляет элемент, находящийся в заданной позиции коллекции.
 */
public class RemoveAt extends Command {
    private final Console console;
    private final UDPClient client;

    public RemoveAt(Console console, UDPClient client) {
        super("remove_at index");
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
            if (arguments[1].isEmpty()) throw new WrongAmountOfElementsException(getName());
            var ind = Integer.parseInt(arguments[1]);

            var response = (RemoveAtResponse) client.sendAndReceiveCommand(new RemoveAtRequest(ind));
            if (response.getError() != null && !response.getError().isEmpty()) {
                throw new APIException(response.getError());
            }

            console.println("Продукт успешно удален.");
            return true;
        } catch (WrongAmountOfElementsException | APIException e) {
            console.printError(e.getMessage());
        } catch (NumberFormatException exception) {
            console.printError("index должен быть представлен числом!");
        } catch(IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        }
        return false;
    }
}