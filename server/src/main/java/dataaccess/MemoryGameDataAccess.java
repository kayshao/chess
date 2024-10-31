package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MemoryGameDataAccess implements GameDataAccess{
    private int gameID = 1;
    private final HashMap<Integer, GameData> games = new HashMap<>();

    public void clear() {
        games.clear();
    }
    public GameData getGame(int id) {
        return games.get(id);
    }

    public List<GameData> listGames() {
        if (games.isEmpty()) {return null;}
        else {
            return new ArrayList<>(games.values());
        }
    }

    public int createGame(String name) {
        GameData game = new GameData(gameID, null, null, name, new ChessGame());
        games.put(gameID, game);
        gameID++;
        return gameID-1;
    };

    public void setUsername(String color, AuthData authdata, int gameID) {
        GameData game = getGame(gameID);
        String username = authdata.username();
        games.put(gameID, game.setUser(color, username));
    }
}
