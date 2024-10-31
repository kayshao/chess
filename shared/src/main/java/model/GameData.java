package model;
import chess.ChessGame;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
    public GameData setUser(String color, String username) {
        if(color.equals("BLACK")) {
            return new GameData(this.gameID, this.whiteUsername, username, this.gameName, this.game);
        }
        else {
            return new GameData(this.gameID, username, this.blackUsername, this.gameName, this.game);
        }
    }
}
