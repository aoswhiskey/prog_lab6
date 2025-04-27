package common.network.responses;

import common.models.LabWork;
import common.utility.Commands;

import java.util.List;

public class ShowResponse extends Response {
    public final List<LabWork> labWorks;

    public ShowResponse(List<LabWork> labWorks, String error) {
        super(Commands.SHOW, error);
        this.labWorks = labWorks;
    }
}
