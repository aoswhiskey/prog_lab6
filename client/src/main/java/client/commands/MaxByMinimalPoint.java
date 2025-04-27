package client.commands;

import client.network.UDPClient;
import common.exceptions.APIException;
import common.exceptions.WrongAmountOfElementsException;
import common.network.requests.MaxByMinimalPointRequest;
import client.utility.Console;
import common.network.responses.MaxByMinimalPointResponse;

import java.io.IOException;

/**
 * Команда 'max_by_minimal_point'. Выводит любой объект из коллекции, значение поля minimalPoint которого является максимальным.
 */
public class MaxByMinimalPoint extends Command {
    private final Console console;
    private final UDPClient client;

    public MaxByMinimalPoint(Console console, UDPClient client) {
        super("max_by_minimal_point");
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

            var response = (MaxByMinimalPointResponse) client.sendAndReceiveCommand(new MaxByMinimalPointRequest());

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