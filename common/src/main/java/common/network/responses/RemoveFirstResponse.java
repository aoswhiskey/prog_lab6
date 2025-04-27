package common.network.responses;

import common.utility.Commands;

public class RemoveFirstResponse extends Response {
    public RemoveFirstResponse(String error) {
        super(Commands.REMOVE_FIRST, error);
    }
}
