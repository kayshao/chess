package dataaccess;

import model.AuthData;

import static dataaccess.DatabaseManager.createDatabase;

public class MySqlAuthDataAccess implements AuthDataAccess {
    public MySqlAuthDataAccess() throws DataAccessException {
        createDatabase();
    }
    public void clear() throws DataAccessException {

    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    public String createAuth(String username) throws DataAccessException {
        return "";
    }

    public void deleteAuth(String authToken) throws DataAccessException {

    }
}
