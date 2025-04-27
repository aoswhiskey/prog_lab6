package common.network.requests;

import common.models.LabWork;
import common.utility.Commands;

public class AddRequest extends Request {
    public final LabWork labWork;

    public AddRequest(LabWork labWork) {
        super(Commands.ADD);
        this.labWork = labWork;
    }
}
