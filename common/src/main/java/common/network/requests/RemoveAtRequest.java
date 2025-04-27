package common.network.requests;

import common.utility.Commands;

public class RemoveAtRequest extends Request {
    public final int ind;
    public RemoveAtRequest(int ind) {
        super(Commands.REMOVE_AT);
        this.ind = ind;
    }
}
