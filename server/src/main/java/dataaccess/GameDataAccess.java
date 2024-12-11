package dataaccess;

import chess.ChessBoard;
import chess.ChessGame;
import model.GameData;
import model.AuthData;

import java.util.List;
import java.util.Map;

public interface GameDataAccess {

    void clear() throws DataAccessException;

    GameData getGame(int id) throws DataAccessException;

    int createGame(String name) throws DataAccessException;

    List<Map<String, String>> listGames() throws DataAccessException;

    void setUsername(String color, AuthData auth, int id) throws DataAccessException;

    void updateGame(int id, ChessGame game) throws DataAccessException;
}
