package dataaccess;

import model.UserData;

import static dataaccess.DatabaseManager.createDatabase;

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

    }
}
