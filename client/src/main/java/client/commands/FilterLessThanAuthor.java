package client.commands;

import client.ask.AskAuthor;
import client.network.UDPClient;
import common.exceptions.APIException;
import common.exceptions.WrongAmountOfElementsException;
import common.models.Person;
import client.ask.AskBreak;
import client.utility.Console;
import common.network.requests.FilterLessThanAuthorRequest;
import common.network.responses.FilterLessThanAuthorResponse;

import java.io.IOException;

/**
 * Команда 'filter_less_than_author'. Выводит элементы, значение поля author которых меньше заданного.
 */
public class FilterLessThanAuthor extends Command {
    private final Console console;
    private final UDPClient client;

    public FilterLessThanAuthor(Console console, UDPClient client) {
        super("filter_less_than_author author");
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
            Person author = AskAuthor.askAuthor(console);
            var response = (FilterLessThanAuthorResponse) client.sendAndReceiveCommand(new FilterLessThanAuthorRequest(author));
            if (response.getError() != null && !response.getError().isEmpty()) {
                throw new APIException(response.getError());
            }
            if (response.labWorks.isEmpty()) {
                console.println("Коллекция пуста или нет подходящих элементов!");
                return true;
            }
            for (var product : response.labWorks) {
                console.println(product + "\n");
            }
            return true;

        } catch (AskBreak e) {
            throw new RuntimeException(e);
        } catch (RuntimeException ignored) {
            console.printError("Прерывание ввода данных");
        } catch (WrongAmountOfElementsException | APIException e) {
            console.printError(e.getMessage());
        } catch (IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        }
        return false;
    }
}
