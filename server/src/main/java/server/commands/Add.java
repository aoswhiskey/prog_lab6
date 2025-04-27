package server.commands;

import common.models.LabWork;
import common.network.requests.AddRequest;
import common.network.requests.Request;
import common.network.responses.AddResponse;
import common.network.responses.Response;
import server.managers.CollectionManager;

/**
 * Команда 'add'. Добавляет новый элемент в коллекцию.
 */
public class Add extends Command {
    private final CollectionManager collectionManager;
    public Add(CollectionManager collectionManager) {
        super("add {element}", "добавить новый элемент в коллекцию");
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public Response execute(Request request) {
        var req = (AddRequest) request;
        LabWork labWork = req.labWork;
        try {
            int newId = collectionManager.getFreeId();
            labWork.setId(newId);
            collectionManager.add(labWork);
            return new AddResponse(newId, null);
        } catch (Exception e) {
            return new AddResponse(-1, e.toString());
        }
    }
}
