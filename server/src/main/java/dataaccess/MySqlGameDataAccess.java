package dataaccess;

import model.AuthData;
import model.GameData;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static dataaccess.DatabaseManager.createDatabase;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySqlGameDataAccess implements GameDataAccess {
    public MySqlGameDataAccess() throws DataAccessException {
        createDatabase();
    }
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE game";
        executeUpdate(statement);
    }

    public GameData getGame(int id) throws DataAccessException {
        return null;
    }

    public int createGame(String name) throws DataAccessException {
        executeUpdate("INSERT INTO game (name) VALUES (?)", name);
        var statement = "SELECT id FROM game WHERE name=?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, name);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("id");
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
        throw new DataAccessException("Did not return anything");
    }


    public List<Map<String, Object>> listGames() throws DataAccessException {
        return List.of();
    }

    public void setUsername(String color, AuthData auth, int id) throws DataAccessException {

    }
    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }
}
