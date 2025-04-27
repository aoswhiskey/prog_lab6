package server.commands;

import common.network.requests.RemoveByIdRequest;
import common.network.requests.Request;
import common.network.responses.RemoveByIdResponse;
import common.network.responses.Response;
import server.managers.CollectionManager;

/**
 * Команда 'remove_by_id'. Удаляет элемент из коллекции по его id.
 */
public class RemoveById extends Command {
    private final CollectionManager collectionManager;
    public RemoveById(CollectionManager collectionManager) {
        super("remove_by_id id", "удалить элемент из коллекции по ID");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        var req = (RemoveByIdRequest) request;
        int id = req.id;
        try {
            if (!collectionManager.checkExist(id)) {
                return new RemoveByIdResponse("LabWork с таким ID в коллекции нет!");
            }
            collectionManager.remove(id);
            return new RemoveByIdResponse(null);
        } catch (Exception e) {
            return new RemoveByIdResponse(e.toString());
        }
    }
}
