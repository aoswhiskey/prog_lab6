package common.network.responses;

import common.models.Difficulty;
import common.utility.Commands;

import java.util.List;

public class PrintFieldDescendingDifficultyResponse extends Response {
    public final List<Difficulty> difficulties;
    public PrintFieldDescendingDifficultyResponse(List<Difficulty> difficulties, String error) {
        super(Commands.PRINT_FIELD_DESCENDING_DIFFICULTY, error);
        this.difficulties = difficulties;
    }
}
