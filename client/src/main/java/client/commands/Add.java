package client.commands;

import client.ask.AskBreak;
import client.ask.AskLabWork;
import client.network.UDPClient;
import client.utility.Console;
import common.exceptions.*;
import common.models.LabWork;
import common.network.requests.AddRequest;
import common.network.responses.AddResponse;

import java.io.IOException;

/**
 * Команда 'add'. Добавляет новый элемент в коллекцию.
 */

public class Add extends Command {
    private final Console console;
    private final UDPClient client;

    public Add(Console console, UDPClient client) {
        super("add {element}");
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

            console.println("* Создание нового продукта:");
            LabWork labWork = AskLabWork.askLabWork(console, 1);
            var response = (AddResponse) client.sendAndReceiveCommand(new AddRequest(labWork));
            if (response.getError() != null && !response.getError().isEmpty()) {
                throw new APIException(response.getError());
            }

            console.println("Продукт успешно добавлен!");
            return true;

        } catch (WrongAmountOfElementsException | APIException e) {
            console.printError(e.getMessage());
        } catch (IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        } catch (AskBreak e) {
            throw new RuntimeException(e);
        } catch (RuntimeException exception) {
            console.printError("Прерывание ввода данных");
        }
        return false;
    }
}