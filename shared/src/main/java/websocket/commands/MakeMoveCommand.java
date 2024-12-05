package websocket.commands;

public class MakeMoveCommand extends UserGameCommand{
    public MakeMoveCommand(CommandType commandType, String authToken, Integer gameID) {
        super(commandType.MAKE_MOVE, authToken, gameID);
    }
}
