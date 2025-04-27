package client.commands;

import client.network.UDPClient;
import client.utility.Console;
import common.exceptions.*;
import common.network.requests.HeadRequest;
import common.network.responses.HeadResponse;

import java.io.IOException;

/**
 * Команда 'head'. Выводит первый элемент коллекции.
 */
public class Head extends Command {
    private final Console console;
    private final UDPClient client;

    public Head(Console console, UDPClient client) {
        super("head");
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

            var response = (HeadResponse) client.sendAndReceiveCommand(new HeadRequest());

            if (response.getError() != null && !response.getError().isEmpty()) {
                throw new APIException(response.getError());
            }

            if (response.labWork == null) {
                console.println("Коллекция пуста!");
                return true;
            }

            console.println(response.labWork);
            return true;
        } catch (WrongAmountOfElementsException | APIException e) {
            console.printError(e.getMessage());
        } catch(IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        }
        return false;
    }
}
