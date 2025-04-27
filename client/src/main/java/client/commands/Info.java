package client.commands;

import client.network.UDPClient;
import client.utility.Console;
import common.exceptions.WrongAmountOfElementsException;
import common.network.requests.InfoRequest;
import common.network.responses.InfoResponse;

import java.io.IOException;

/**
 * Команда 'info'. Выводит информацию о коллекции.
 */
public class Info extends Command {
    private final Console console;
    private final UDPClient client;

    public Info(Console console, UDPClient client) {
        super("info");
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
            if (!arguments[1].isEmpty()) {
                throw new WrongAmountOfElementsException(getName());
            }

            var response = (InfoResponse) client.sendAndReceiveCommand(new InfoRequest());
            console.println(response.infoMessage);

            return true;
        } catch(IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        } catch (WrongAmountOfElementsException e) {
            console.printError(e.getMessage());
        }
        return false;
    }
}
