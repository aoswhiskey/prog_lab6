package server.commands;

import common.network.requests.Request;
import common.network.requests.UpdateRequest;
import common.network.responses.Response;
import common.network.responses.UpdateResponse;
import server.managers.CollectionManager;

/**
 * Команда 'update'. Обновляет элемент коллекции.
 */
public class Update extends Command {
  private final CollectionManager collectionManager;

  public Update(CollectionManager collectionManager) {
    super("update id {element}", "обновить значение элемента коллекции по ID");
    this.collectionManager = collectionManager;
  }

  /**
   * Выполняет команду
   * @return Успешность выполнения команды.
   */
  @Override
  public Response execute(Request request) {
    var req = (UpdateRequest) request;
    try {
      if (!collectionManager.checkExist(req.id)) {
        return new UpdateResponse("LabWork с таким ID в коллекции нет!");
      }

      collectionManager.update(req.labWork);
      return new UpdateResponse(null);
    } catch (Exception e) {
      return new UpdateResponse(e.toString());
    }
  }
}
