package dataaccess;

import model.AuthData;
import model.GameData;

import java.util.List;
import java.util.Map;

import static dataaccess.DatabaseManager.createDatabase;

public class MySqlGameDataAccess implements GameDataAccess {
    public MySqlGameDataAccess() throws DataAccessException {
        createDatabase();
    }
    public void clear() throws DataAccessException {

    }

    public GameData getGame(int id) throws DataAccessException {
        return null;
    }

    public int createGame(String name) throws DataAccessException {
        return 0;
    }

    public List<Map<String, Object>> listGames() throws DataAccessException {
        return List.of();
    }

    public void setUsername(String color, AuthData auth, int id) throws DataAccessException {

    }
}
