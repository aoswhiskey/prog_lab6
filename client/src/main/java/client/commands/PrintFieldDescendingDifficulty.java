package client.commands;

import client.network.UDPClient;
import common.exceptions.APIException;
import common.exceptions.WrongAmountOfElementsException;
import client.utility.Console;
import common.network.requests.PrintFieldDescendingDifficultyRequest;
import common.network.responses.PrintFieldDescendingDifficultyResponse;

import java.io.IOException;
/**
 * Команда 'print_field_descending_difficulty'. Выводит значения поля difficulty всех элементов в порядке убывания.
 */
public class PrintFieldDescendingDifficulty extends Command{
    private final Console console;
    private final UDPClient client;

    public PrintFieldDescendingDifficulty(Console console, UDPClient client) {
        super("print_field_descending_difficulty");
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

            var response = (PrintFieldDescendingDifficultyResponse) client.sendAndReceiveCommand(new PrintFieldDescendingDifficultyRequest());

            if (response.getError() != null && !response.getError().isEmpty()) {
                throw new APIException(response.getError());
            }

            if (response.difficulties == null) {
                console.println("Коллекция пуста!");
                return true;
            }

            for (var difficulty : response.difficulties) {
                console.println(difficulty);
            }
            return true;
        } catch (WrongAmountOfElementsException | APIException e) {
            console.printError(e.getMessage());
        } catch(IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        }
        return false;
    }
}
