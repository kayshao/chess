package service;
import dataaccess.MemoryAuthDataAccess;
import dataaccess.MemoryUserDataAccess;
import request.ClearRequest;
import request.LogoutRequest;
import request.LoginRequest;
import result.ClearResult;
import result.LogoutResult;
import result.RegisterResult;
import result.LoginResult;
import request.RegisterRequest;

public class UserService {
    private final MemoryAuthDataAccess authDAO;
    private final MemoryUserDataAccess userDAO;

    public UserService(MemoryAuthDataAccess authDAO, MemoryUserDataAccess userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }

    public RegisterResult register(RegisterRequest request) {
        if(userDAO.getUser(request.username()) == null) {
            userDAO.createUser(request.username(), request.password(), request.email());
            String authToken = authDAO.createAuth(request.username());
            return new RegisterResult(request.username(), authToken);
        }
        else return new RegisterResult("chicken", "chicken");
    }
    public LoginResult login(LoginRequest request) {
        if(userDAO.getUser(request.username()).password().equals(request.password())) {
            String authToken = authDAO.createAuth(request.username());
            return new LoginResult(authToken);
        }
        else return null;
    }
    public LogoutResult logout(LogoutRequest request) {
        String token = authDAO.getAuth(request.token()).authToken();
        authDAO.deleteAuth(token);
        return new LogoutResult("");
    }
    public ClearResult clear(ClearRequest request) {
        authDAO.clear();
        userDAO.clear();
        return new ClearResult("");
    }
}
