package dataaccess;

import model.GameData;
import model.AuthData;

import java.util.List;

public interface GameDataAccess {

    void clear() throws DataAccessException;

    GameData getGame(int id) throws DataAccessException;

    public int createGame(String name) throws DataAccessException;

    List<GameData> listGames() throws DataAccessException;

    void setUsername(String color, AuthData auth, int id) throws DataAccessException;
}
