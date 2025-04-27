package client.commands;

import client.network.UDPClient;
import client.utility.Console;
import common.exceptions.WrongAmountOfElementsException;
import common.network.requests.HelpRequest;
import common.network.responses.HelpResponse;

import java.io.IOException;

/**
 * Команда 'help'. Выводит справку по доступным командам
 */
public class Help extends Command {
    private final Console console;
    private final UDPClient client;

    public Help(Console console, UDPClient client) {
        super("help");
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

            var response = (HelpResponse) client.sendAndReceiveCommand(new HelpRequest());

            console.print(response.helpMessage);

            return true;
        } catch(IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        } catch (WrongAmountOfElementsException e) {
            console.printError(e.getMessage());
        }
        return false;
    }
}
