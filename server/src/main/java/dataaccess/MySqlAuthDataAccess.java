package dataaccess;

import model.AuthData;

import java.sql.SQLException;
import java.util.UUID;

import static dataaccess.DatabaseManager.createDatabase;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySqlAuthDataAccess implements AuthDataAccess {
    public MySqlAuthDataAccess() throws DataAccessException {
        createDatabase();
    }
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE auth";
        executeUpdate(statement);
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username FROM auth WHERE token=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new AuthData(authToken, rs.getString("username"));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
        return null;// throw new DataAccessException("Did not return authToken");
    }

    public String createAuth(String username) throws DataAccessException {
        String token = UUID.randomUUID().toString();
        executeUpdate("INSERT INTO auth (token, username) VALUES (?, ?)", token, username);
        return token;
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        if (authToken == null) {
            throw new DataAccessException("Cannot delete null authToken");
        }
        var statement = "DELETE FROM auth WHERE token=?";
        executeUpdate(statement, authToken);
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
