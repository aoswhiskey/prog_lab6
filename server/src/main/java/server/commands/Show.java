package server.commands;

import common.network.requests.Request;
import common.network.responses.Response;
import common.network.responses.ShowResponse;
import server.managers.CollectionManager;

/**
 * Команда 'show'. Выводит все элементы коллекции.
 */
public class Show extends Command {
    private final CollectionManager collectionManager;
    public Show(CollectionManager collectionManager) {
        super("show", "вывести все элементы коллекции");
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public Response execute(Request request) {
        try {
            collectionManager.sort();
            return new ShowResponse(collectionManager.getSorted(), null);
        } catch (Exception e) {
            return new ShowResponse(null, e.toString());
        }
    }
}
