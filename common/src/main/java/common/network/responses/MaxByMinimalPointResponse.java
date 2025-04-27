package common.network.responses;

import common.models.LabWork;
import common.utility.Commands;

public class MaxByMinimalPointResponse extends Response {
    public final LabWork labWork;

    public MaxByMinimalPointResponse(LabWork labWork, String error) {
        super(Commands.MAX_BY_MINIMAL_POINT, error);
        this.labWork = labWork;
    }
}
