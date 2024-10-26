package dataaccess;
import model.AuthData;

public interface AuthDataAccess {
    void clear() throws DataAccessException;

    AuthData getAuth(String authToken) throws DataAccessException;

    void createAuth(String username) throws DataAccessException;

    void deleteAuth(String authToken) throws DataAccessException;
}