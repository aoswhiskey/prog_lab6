package common.network.responses;

import common.utility.Commands;

public class RemoveAtResponse extends Response {
    public RemoveAtResponse(String error) {
        super(Commands.REMOVE_AT, error);
    }
}
