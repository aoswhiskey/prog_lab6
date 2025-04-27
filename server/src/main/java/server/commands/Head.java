package server.commands;

import common.network.requests.Request;
import common.network.responses.HeadResponse;
import common.network.responses.Response;
import server.managers.CollectionManager;

/**
 * Команда 'head'. Выводит первый элемент коллекции.
 */
public class Head extends Command {
    private final CollectionManager collectionManager;

    public Head(CollectionManager collectionManager) {
        super("head", "вывести первый элемент коллекции");
        this.collectionManager = collectionManager;
    }
    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public Response execute(Request request) {
        try {
            return new HeadResponse(collectionManager.getFirst(), null);
        } catch (Exception e) {
            return new HeadResponse(null, e.toString());
        }
    }
}