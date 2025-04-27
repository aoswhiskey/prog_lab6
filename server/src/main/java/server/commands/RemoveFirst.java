package server.commands;

import common.network.requests.Request;
import common.network.responses.RemoveFirstResponse;
import common.network.responses.Response;
import server.managers.CollectionManager;

/**
 * Команда 'remove_first'. Удаляет первый элемент из коллекции.
 */
public class RemoveFirst extends Command {
    private final CollectionManager collectionManager;
    public RemoveFirst(CollectionManager collectionManager) {
        super("remove_first", "удалить первый элемент из коллекции");
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public Response execute(Request request) {
        try {
            if (collectionManager.getCollection().isEmpty()) {
                return new RemoveFirstResponse("Коллекция пуста!");
            } else {
                collectionManager.remove(collectionManager.getFirst().getId());
                return new RemoveFirstResponse(null);
            }
        } catch (Exception e) {
            return new RemoveFirstResponse(e.toString());
        }
    }
}
