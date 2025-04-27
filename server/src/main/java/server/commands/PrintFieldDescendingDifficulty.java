package server.commands;

import common.models.Difficulty;
import common.models.LabWork;
import common.network.requests.Request;
import common.network.responses.PrintFieldDescendingDifficultyResponse;
import common.network.responses.Response;
import server.managers.CollectionManager;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Команда 'print_field_descending_difficulty'. Выводит значения поля difficulty всех элементов в порядке убывания.
 */
public class PrintFieldDescendingDifficulty extends Command {
    private final CollectionManager collectionManager;
    public PrintFieldDescendingDifficulty(CollectionManager collectionManager) {
        super("print_field_descending_difficulty", "вывести значения поля difficulty всех элементов в порядке убывания");
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public Response execute(Request request) {
        try {
            return new PrintFieldDescendingDifficultyResponse(getDifficulties(), null);
        } catch (Exception e) {
            return new PrintFieldDescendingDifficultyResponse(null, e.toString());
        }
    }

    private List<Difficulty> getDifficulties() {
        List<Difficulty> difficulties = collectionManager.getCollection().stream()
                .map(LabWork::getDifficulty)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        return difficulties.isEmpty() ? null : difficulties;
    }
}
