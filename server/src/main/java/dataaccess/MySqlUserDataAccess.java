package dataaccess;

import model.UserData;

import java.sql.SQLException;

import static dataaccess.DatabaseManager.createDatabase;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySqlUserDataAccess implements UserDataAccess {
    public MySqlUserDataAccess() throws DataAccessException {
        createDatabase();
    }
    public void clear() throws DataAccessException {

    }

    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    public void createUser(String username, String password, String email) throws DataAccessException {
        var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        executeUpdate(statement, username, password, email);


    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var db = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) db.setString(i + 1, p);
                    else if (param instanceof Integer p) db.setInt(i + 1, p);
                    else if (param == null) db.setNull(i + 1, NULL);
                }
                db.executeUpdate();

                var rs = db.getGeneratedKeys();
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
