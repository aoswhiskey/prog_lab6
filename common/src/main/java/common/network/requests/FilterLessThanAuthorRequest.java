package common.network.requests;

import common.models.Person;
import common.utility.Commands;

public class FilterLessThanAuthorRequest extends Request {
    public final Person author;
    public FilterLessThanAuthorRequest(Person author) {
        super(Commands.FILTER_LESS_THAN_AUTHOR);
        this.author = author;
    }
}
