package server.commands;

import common.network.requests.RemoveAtRequest;
import common.network.requests.Request;
import common.network.responses.RemoveAtResponse;
import common.network.responses.Response;
import server.managers.CollectionManager;

/**
 * Команда 'remove_at'. Удаляет элемент, находящийся в заданной позиции коллекции.
 */
public class RemoveAt extends Command {
    private final CollectionManager collectionManager;
    public RemoveAt(CollectionManager collectionManager) {
        super("remove_at index", "удалить элемент, находящийся в заданной позиции коллекции (index)");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        var req = (RemoveAtRequest) request;
        int ind = req.ind;
        try {
            int id = collectionManager
                    .getElementByIndex(ind)
                    .getId();
            collectionManager.remove(id);
            return new RemoveAtResponse(null);
        } catch (IndexOutOfBoundsException e) {
            return new RemoveAtResponse("index за границами допустимых значений");
        } catch (Exception e) {
            return new RemoveAtResponse(e.toString());
        }
    }
}
