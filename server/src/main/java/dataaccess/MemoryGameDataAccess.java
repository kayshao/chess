package dataaccess;

import chess.ChessBoard;
import chess.ChessGame;
import model.AuthData;
import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryGameDataAccess implements GameDataAccess{
    private int gameID = 100; //TODO change this
    private final HashMap<Integer, GameData> games = new HashMap<>();

    public void clear() {
        games.clear();
    }
    public GameData getGame(int id) {
        return games.get(id);
    }

    public List<Map<String, String>> listGames() {
        List<Map<String, String>> gameList = new ArrayList<>();
        for (GameData game : games.values()) {
            Map<String, String> gameInfo = new HashMap<>();
            gameInfo.put("gameID", String.valueOf(game.gameID()));
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

    public void updateGame(int gameID, ChessGame game) {
        GameData oldGame = getGame(gameID);
        oldGame.game().setBoard(game.getBoard());
        games.put(gameID, oldGame);
    }

}
