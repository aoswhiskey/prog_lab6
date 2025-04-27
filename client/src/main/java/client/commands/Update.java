package client.commands;

import client.ask.AskBreak;
import client.ask.AskLabWork;
import client.network.UDPClient;
import client.utility.Console;
import common.exceptions.*;
import common.models.LabWork;
import common.network.requests.UpdateRequest;
import common.network.responses.UpdateResponse;

import java.io.IOException;

/**
 * Команда 'update'. Обновляет элемент коллекции.
 * @author maxbarsukov
 */
public class Update extends Command {
    private final Console console;
    private final UDPClient client;

    public Update(Console console, UDPClient client) {
        super("update id {element}");
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

            console.println("* Введите данные обновленного продукта:");
            LabWork labWork = AskLabWork.askLabWork(console, id);

            var response = (UpdateResponse) client.sendAndReceiveCommand(new UpdateRequest(id, labWork));
            if (response.getError() != null && !response.getError().isEmpty()) {
                throw new APIException(response.getError());
            }

            console.println("Продукт успешно обновлен.");
            return true;
        } catch (NumberFormatException exception) {
            console.printError("id должен быть представлен числом!");
        } catch(IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        } catch (APIException | WrongAmountOfElementsException e) {
            console.printError(e.getMessage());
        } catch (AskBreak e) {
            console.printError("Прерывание ввода данных");
        }
        return false;
    }
}
