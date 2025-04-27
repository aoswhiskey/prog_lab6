package common.network.requests;

import common.utility.Commands;

public class PrintFieldDescendingDifficultyRequest extends Request {
    public PrintFieldDescendingDifficultyRequest() {
        super(Commands.PRINT_FIELD_DESCENDING_DIFFICULTY);
    }
}
