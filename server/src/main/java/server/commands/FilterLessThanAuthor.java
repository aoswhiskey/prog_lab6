package server.commands;

import common.models.LabWork;
import common.models.Person;
import common.network.requests.FilterLessThanAuthorRequest;
import common.network.requests.Request;
import common.network.responses.FilterLessThanAuthorResponse;
import common.network.responses.Response;
import server.managers.CollectionManager;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Команда 'filter_less_than_author'. Выводит элементы, значение поля author которых меньше заданного.
 */
public class FilterLessThanAuthor extends Command {
    private final CollectionManager collectionManager;
    public FilterLessThanAuthor(CollectionManager collectionManager) {
        super("filter_less_than_author author", "вывести элементы, значение поля author которых меньше заданного");
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public Response execute(Request request) {
        var req = (FilterLessThanAuthorRequest) request;
        Person author = req.author;
        try {
            return new FilterLessThanAuthorResponse(filter(author), null);
        } catch (Exception e) {
            return new FilterLessThanAuthorResponse(null, e.toString());
        }
    }

    private List<LabWork> filter(Person author) {
        return collectionManager.getCollection().stream()
                .filter(e -> e.getAuthor().compareTo(author) < 0)
                .sorted()
                .collect(Collectors.toList());
    }
}
