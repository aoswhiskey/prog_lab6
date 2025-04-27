package common.network.responses;

import common.models.LabWork;
import common.utility.Commands;

public class HeadResponse extends Response {
    public final LabWork labWork;

    public HeadResponse(LabWork labWork, String error) {
        super(Commands.HEAD, error);
        this.labWork = labWork;
    }
}
