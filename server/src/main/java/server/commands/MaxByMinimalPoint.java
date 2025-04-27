package server.commands;

import common.models.LabWork;
import common.network.requests.Request;
import common.network.responses.MaxByMinimalPointResponse;
import common.network.responses.Response;
import server.managers.CollectionManager;

import java.util.Comparator;

/**
 * Команда 'max_by_minimal_point'. Выводит любой объект из коллекции, значение поля minimalPoint которого является максимальным.
 */
public class MaxByMinimalPoint extends Command {
    private final CollectionManager collectionManager;
    public MaxByMinimalPoint(CollectionManager collectionManager) {
        super("max_by_minimal_point", "вывести любой объект из коллекции, значение поля minimalPoint которого является максимальным");
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public Response execute(Request request) {
        try {
            return new MaxByMinimalPointResponse(getMax(), null);
        } catch (Exception e) {
            return new MaxByMinimalPointResponse(null, e.toString());
        }
    }

    private LabWork getMax() {
        return collectionManager.getCollection().stream()
                .max(Comparator.comparing(LabWork::getMinimalPoint))
                .orElse(null);
    }
}
