package service;

import dataaccess.*;
import exception.ServiceException;
import model.AuthData;
import request.ClearRequest;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.ListGamesRequest;
import result.ClearResult;
import result.CreateGameResult;
import result.JoinGameResult;
import result.ListGamesResult;
import java.util.List;
import java.util.Map;

public class GameService {
    private final AuthDataAccess authDAO;
    private final GameDataAccess gameDAO;

    public GameService(AuthDataAccess authDAO, GameDataAccess gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public CreateGameResult createGame(CreateGameRequest request) throws ServiceException, DataAccessException {
        if (authDAO.getAuth(request.authToken()) == null) {
            throw new ServiceException("Error: unauthorized");
        }
        else {
            int id = gameDAO.createGame(request.gameName());
            return new CreateGameResult(id);
        }
    }

    public JoinGameResult joinGame(JoinGameRequest request) throws ServiceException, DataAccessException {
        if (authDAO.getAuth(request.token()) == null) {
            throw new ServiceException("Error: unauthorized");
        } else if(request.playerColor() == null | request.gameID() == null) {
            throw new ServiceException("Error: bad request");
        } else {
            AuthData auth = authDAO.getAuth(request.token());
            if (auth.username() != null) {
                if (gameDAO.getGame(request.gameID()) == null) {
                    throw new ServiceException("Error: bad request");
                } else if ((request.playerColor().equals("BLACK")
                        && gameDAO.getGame(request.gameID()).blackUsername() != null)
                        | (request.playerColor().equals("WHITE")
                        && gameDAO.getGame(request.gameID()).whiteUsername() != null)) {
                    throw new ServiceException("Error: already taken");
                } else {
                    gameDAO.setUsername(request.playerColor(), auth, request.gameID());
                }
            }
            return new JoinGameResult("");
        }
    }

    public ListGamesResult listGames(ListGamesRequest request) throws ServiceException, DataAccessException {
        if (authDAO.getAuth(request.token()) == null) {
            throw new ServiceException("Error: unauthorized");
        } else {
            List<Map<String, Object>> games = gameDAO.listGames();
            return new ListGamesResult(games);
        }
    }

    public ClearResult clear(ClearRequest request) throws DataAccessException {
        gameDAO.clear();
        return new ClearResult("");
    }
}
