package websocket.commands;

public class LeaveCommand extends UserGameCommand{
    private final CommandType commandType;
    private final String username;
    private final String authToken;
    private final Integer gameID;

    public LeaveCommand(CommandType commandType, CommandType commandType1, String username, String authToken, Integer gameID) {
        super(CommandType.LEAVE, authToken, gameID);
        this.commandType = CommandType.LEAVE;
        this.username = username;
        this.authToken = authToken;
        this.gameID = gameID;
    }
    public CommandType getCommandType() {
        return commandType;
    }

    public String getAuthToken() {
        return authToken;
    }

    public Integer getGameID() {
        return gameID;
    }

    public String getUsername() {return username;}
}
