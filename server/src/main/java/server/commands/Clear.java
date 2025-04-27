package server.commands;

import common.network.requests.Request;
import common.network.responses.ClearResponse;
import common.network.responses.Response;
import server.managers.CollectionManager;

/**
 * Команда 'clear'. Очищает коллекцию.
 */
public class Clear extends Command {
    private final CollectionManager collectionManager;
    public Clear(CollectionManager collectionManager) {
        super("clear", "очистить коллекцию");
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public Response execute(Request request) {
        try {
            collectionManager.clearCollection();
            return new ClearResponse(null);
        } catch (Exception e) {
            return new ClearResponse(e.toString());
        }
    }
}
