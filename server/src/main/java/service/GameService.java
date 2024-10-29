package service;

import dataaccess.MemoryAuthDataAccess;
import dataaccess.MemoryGameDataAccess;
import model.AuthData;
import model.GameData;
import request.ClearRequest;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.ListGamesRequest;
import result.ClearResult;
import result.CreateGameResult;
import result.JoinGameResult;
import result.ListGamesResult;

import java.util.HashMap;

public class GameService {
    private final MemoryAuthDataAccess authDAO;
    private final MemoryGameDataAccess gameDAO;

    public GameService(MemoryAuthDataAccess authDAO, MemoryGameDataAccess gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public CreateGameResult createGame(CreateGameRequest request) {
        if (authDAO.getAuth(request.token()) != null) {
        int id = gameDAO.createGame(request.name());
        return new CreateGameResult(id);
        }
        else return null;
    }

    public JoinGameResult joinGame(JoinGameRequest request) {
        AuthData auth = authDAO.getAuth(request.token());
        if (auth.username() != null) {
            GameData game = gameDAO.getGame(request.gameID());
            gameDAO.setUsername(request.color(), auth, request.gameID());
        }
        return new JoinGameResult("");
    }

    public ListGamesResult listGames(ListGamesRequest request) {
        if (authDAO.getAuth(request.token()) != null) {
            HashMap<Integer, GameData> games = gameDAO.listGames();
            return new ListGamesResult(games);
        }
        return null;
    }

    public ClearResult clear(ClearRequest request) {
        gameDAO.clear();
        return new ClearResult("");
    }
}