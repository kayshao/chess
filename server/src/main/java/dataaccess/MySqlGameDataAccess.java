package dataaccess;

import model.AuthData;
import model.GameData;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dataaccess.DatabaseManager.createDatabase;

public class MySqlGameDataAccess implements GameDataAccess {
    public MySqlGameDataAccess() throws DataAccessException {
        createDatabase();
    }
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE game";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public GameData getGame(int id) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT id, white_username, black_username, name FROM game WHERE id=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, id);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new GameData(rs.getInt("id"), rs.getString("white_username"),
                                rs.getString("black_username"), rs.getString("name"),
                                null);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
        throw new DataAccessException("Did not return anything");
    }

    public int createGame(String name) throws DataAccessException {
        var statement = "INSERT INTO game (name) VALUES (?)";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, name);
                ps.executeUpdate();
                try (var rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
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
        int rows;
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
}
