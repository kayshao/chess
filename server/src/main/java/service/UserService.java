package service;
import dataaccess.MemoryAuthDataAccess;
import dataaccess.MemoryUserDataAccess;
import exception.ServiceException;
import model.AuthData;
import model.UserData;
import request.ClearRequest;
import request.LogoutRequest;
import request.LoginRequest;
import result.ClearResult;
import result.LogoutResult;
import result.RegisterResult;
import result.LoginResult;
import request.RegisterRequest;
import exception.ServiceException;

public class UserService {
    private final MemoryAuthDataAccess authDAO;
    private final MemoryUserDataAccess userDAO;

    public UserService(MemoryAuthDataAccess authDAO, MemoryUserDataAccess userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }

    public RegisterResult register(RegisterRequest request) throws ServiceException {
        if(userDAO.getUser(request.username()) == null) {
            userDAO.createUser(request.username(), request.password(), request.email());
            String authToken = authDAO.createAuth(request.username());
            return new RegisterResult(request.username(), authToken);
        }
        else {throw new ServiceException("Error: already taken");}
    }
    public LoginResult login(LoginRequest request) throws ServiceException {
        UserData userData = userDAO.getUser(request.username());
        if (userData == null) {throw new ServiceException("Error: Unauthorized");}
        else if(!(userData.password().equals(request.password()))) {
            throw new ServiceException("Error: Unauthorized");
        }
        else {
            String authToken = authDAO.createAuth(request.username());
            return new LoginResult(authToken);
        }
    }
    public LogoutResult logout(LogoutRequest request) throws ServiceException {
        AuthData authData = authDAO.getAuth(request.token());
        if (authData == null) {
            throw new ServiceException("Error: Unauthorized");
        }
        else {
            String token = authData.authToken();
            authDAO.deleteAuth(token);
            return new LogoutResult("");
        }
    }
    public ClearResult clear(ClearRequest request) {
        authDAO.clear();
        userDAO.clear();
        return new ClearResult("");
    }
}
