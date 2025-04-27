package client.commands;

import client.network.UDPClient;
import client.utility.Console;
import common.exceptions.*;
import common.network.requests.RemoveByIdRequest;
import common.network.responses.RemoveByIdResponse;

import java.io.IOException;

/**
 * Команда 'remove_by_id'. Удаляет элемент из коллекции.
 */
public class RemoveById extends Command {
    private final Console console;
    private final UDPClient client;

    public RemoveById(Console console, UDPClient client) {
        super("remove_by_id id");
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
            var id = Integer.parseInt(arguments[1]);

            var response = (RemoveByIdResponse) client.sendAndReceiveCommand(new RemoveByIdRequest(id));
            if (response.getError() != null && !response.getError().isEmpty()) {
                throw new APIException(response.getError());
            }

            console.println("Продукт успешно удален.");
            return true;
        } catch (NumberFormatException exception) {
            console.printError("ID должен быть представлен числом!");
        } catch(IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        } catch (WrongAmountOfElementsException | APIException e) {
            console.printError(e.getMessage());
        }
        return false;
    }
}
