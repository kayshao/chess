package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT id, white_username, black_username, name FROM game WHERE id=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, id);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new GameData(rs.getInt("id"), rs.getString("white_username"), rs.getString("black_username"), rs.getString("name"), null);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
        throw new DataAccessException("Did not return anything");
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
        List<Map<String, Object>> gameList = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT id, white_username, black_username, name FROM game";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Map<String, Object> gameInfo = new HashMap<>();
                        gameInfo.put("gameID", rs.getInt("id"));
                        gameInfo.put("whiteUsername", rs.getString("white_username"));
                        gameInfo.put("blackUsername", rs.getString("black_username"));
                        gameInfo.put("gameName", rs.getString("name"));
                        gameList.add(gameInfo);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
        return gameList;
    }

    public void setUsername(String color, AuthData auth, int id) throws DataAccessException {
        int rows = 0;
        try (var conn = DatabaseManager.getConnection()) {
            if (color.equals("BLACK")) {
                var statement = "UPDATE game SET black_username = ? WHERE id = ?";
                try (var ps = conn.prepareStatement(statement)) {
                    ps.setString(1, auth.username());
                    ps.setInt(2, id);
                    rows = ps.executeUpdate();
                }
            }
            else {
                var statement = "UPDATE game SET white_username = ? WHERE id = ?";
                try (var ps = conn.prepareStatement(statement)) {
                    ps.setString(1, auth.username());
                    ps.setInt(2, id);
                    rows = ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        if (rows != 1) {
            throw new DataAccessException("Did not update expected number of rows");
        }
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
