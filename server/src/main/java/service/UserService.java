package service;
import dataaccess.*;
import exception.ServiceException;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import request.ClearRequest;
import request.LogoutRequest;
import request.LoginRequest;
import result.ClearResult;
import result.LogoutResult;
import result.RegisterResult;
import result.LoginResult;
import request.RegisterRequest;

public class UserService {
    private final AuthDataAccess authDAO;
    private final UserDataAccess userDAO;

    public UserService(AuthDataAccess authDAO, UserDataAccess userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }

    public RegisterResult register(RegisterRequest request) throws ServiceException, DataAccessException {
        if(request.username() == null | request.password() == null | request.email() == null) {
            throw new ServiceException("Error: bad request");
        }
        if(userDAO.getUser(request.username()) == null) {
            userDAO.createUser(request.username(), request.password(), request.email());
            String authToken = authDAO.createAuth(request.username());
            // System.out.println(authToken);
            return new RegisterResult(request.username(), authToken);
        }
        else {throw new ServiceException("Error: already taken");}
    }
    public LoginResult login(LoginRequest request) throws ServiceException, DataAccessException {
        UserData userData = userDAO.getUser(request.username());
        if (userData == null) {throw new ServiceException("Error: unauthorized");}
        else if(!BCrypt.checkpw(request.password(), userData.password())) {
            throw new ServiceException("Error: unauthorized");
        }
        else {
            String authToken = authDAO.createAuth(request.username());
            return new LoginResult(request.username(), authToken);
        }
    }
    public LogoutResult logout(LogoutRequest request) throws ServiceException, DataAccessException {
        AuthData authData = authDAO.getAuth(request.token());
        if (authData == null) {
            throw new ServiceException("Error: unauthorized");
        }
        else {
            String token = authData.authToken();
            authDAO.deleteAuth(token);
            return new LogoutResult("");
        }
    }
    public ClearResult clear(ClearRequest request) throws DataAccessException {
        authDAO.clear();
        userDAO.clear();
        return new ClearResult("");
    }
}
