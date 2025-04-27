package common.network.responses;

import common.models.LabWork;
import common.models.Person;
import common.utility.Commands;

import java.util.List;

public class FilterLessThanAuthorResponse extends Response {
    public final List<LabWork> labWorks;

    public FilterLessThanAuthorResponse(List<LabWork> labWorks, String error) {
        super(Commands.FILTER_LESS_THAN_AUTHOR, error);
        this.labWorks = labWorks;
    }

}
