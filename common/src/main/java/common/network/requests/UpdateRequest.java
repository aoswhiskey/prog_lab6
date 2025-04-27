package common.network.requests;

import common.models.LabWork;
import common.utility.Commands;

public class UpdateRequest extends Request {
    public final int id;
    public final LabWork labWork;

    public UpdateRequest(int id, LabWork labWork) {
        super(Commands.UPDATE);
        this.id = id;
        this.labWork = labWork;
    }
}
