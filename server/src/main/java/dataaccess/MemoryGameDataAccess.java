package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryGameDataAccess implements GameDataAccess{
    private int gameID = 1;
    private final HashMap<Integer, GameData> games = new HashMap<>();

    public void clear() {
        games.clear();
    }
    public GameData getGame(int id) {
        return games.get(id);
    }

    public List<Map<String, Object>> listGames() {
        List<Map<String, Object>> gameList = new ArrayList<>();
        for (GameData game : games.values()) {
            Map<String, Object> gameInfo = new HashMap<>();
            gameInfo.put("gameID", game.gameID());
            gameInfo.put("whiteUsername", game.whiteUsername());
            gameInfo.put("blackUsername", game.blackUsername());
            gameInfo.put("gameName", game.gameName());
            gameList.add(gameInfo);
        }
        return gameList;
    }


    public int createGame(String name) {
        GameData game = new GameData(gameID, null, null, name, new ChessGame());
        games.put(gameID, game);
        gameID++;
        return gameID-1;
    }

    public void setUsername(String color, AuthData authdata, int gameID) {
        GameData game = getGame(gameID);
        String username = authdata.username();
        games.put(gameID, game.setUser(color, username));
    }
}
