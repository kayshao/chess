package websocket.messages;
import chess.ChessGame;

public class LoadGameMessage extends ServerMessage {
    private ChessGame game;

    public LoadGameMessage(ChessGame game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public void setGame(ChessGame game) {
        this.game = game;
    }
    public ChessGame getGame() {
        return game;
    }
}
