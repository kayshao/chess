package handler;
import com.google.gson.JsonObject;
import dataaccess.DataAccessException;
import exception.ServiceException;
import request.*;
import result.*;
import service.GameService;
import service.UserService;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class Handler {
    private final UserService userService;
    private final GameService gameService;
    private static final Map<String, Integer> STATUS_CODES = new HashMap<>();


        static {
            STATUS_CODES.put("Error: unauthorized", 401);
            STATUS_CODES.put("Error: bad request", 400);
            STATUS_CODES.put("Error: already taken", 403);
            STATUS_CODES.put("Error: description", 500);
        }

    public Handler(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    public Object handleRegister(Request req, Response res) {
        try {
            RegisterRequest request = new Gson().fromJson(req.body(), RegisterRequest.class);
            RegisterResult result = userService.register(request);
            return new Gson().toJson(result);
        } catch (ServiceException | DataAccessException e) {
            res.status(getStatus(e.getMessage()));
            return new Gson().toJson(new ErrorResponse(e.getMessage()));
        }
    }
    public Object handleLogin(Request req, Response res) {
        try {
            LoginRequest request = new Gson().fromJson(req.body(), LoginRequest.class);
            LoginResult result = userService.login(request);
            return new Gson().toJson(result);
        } catch (ServiceException | DataAccessException e) {
            res.status(getStatus(e.getMessage()));
            return new Gson().toJson(new ErrorResponse(e.getMessage()));
        }
    }
    public Object handleLogout(Request req, Response res) {
        try {
            LogoutRequest request = new LogoutRequest(req.headers("Authorization"));
            System.out.println(req.headers());
            userService.logout(request);
            return new Gson().toJson(new Object());
        } catch (ServiceException | DataAccessException e) {
            res.status(getStatus(e.getMessage()));
            return new Gson().toJson(new ErrorResponse(e.getMessage()));
        }
    }
    public Object handleCreateGame(Request req, Response res) { //throws ResponseException {
        try {
            CreateGameRequest request = new CreateGameRequest(req.headers("Authorization"), req.body());
            CreateGameResult result = gameService.createGame(request);
            return new Gson().toJson(result);
        } catch (ServiceException | DataAccessException e) {
            res.status(getStatus(e.getMessage()));
            return new Gson().toJson(new ErrorResponse(e.getMessage()));
        }
    }
    public Object handleListGames(Request req, Response res) {
        try {
            ListGamesRequest request = new ListGamesRequest(req.headers("Authorization"));
            ListGamesResult result = gameService.listGames(request);
            return new Gson().toJson(result);
        } catch (ServiceException | DataAccessException e) {
            res.status(getStatus(e.getMessage()));
            return new Gson().toJson(new ErrorResponse(e.getMessage()));
        }
    }
    public Object handleJoinGame(Request req, Response res) {
        try {
            JsonObject jsonObject = new Gson().fromJson(req.body(), JsonObject.class);
            String color = null;
            Integer id = null;
            if (jsonObject.has("playerColor")) {
                color = jsonObject.get("playerColor").getAsString();
            }
            if (jsonObject.has("gameID")) {
                id = jsonObject.get("gameID").getAsInt();
            }
            JoinGameRequest request = new JoinGameRequest(req.headers("Authorization"), color, id);
            JoinGameResult result = gameService.joinGame(request);
            return new Gson().toJson(result);
        } catch (ServiceException | DataAccessException e) {
            res.status(getStatus(e.getMessage()));
            return new Gson().toJson(new ErrorResponse(e.getMessage()));
        }
    }
    public Object handleClear(Request req, Response res) throws DataAccessException {
        ClearRequest request = new Gson().fromJson(req.body(), ClearRequest.class);
        ClearResult result = gameService.clear(request);
        userService.clear(request);
        return new Gson().toJson(result);
    }
    private int getStatus(String message) {
        if (STATUS_CODES.get(message) == null) {
            return 500;
        } else {
            return STATUS_CODES.get(message);
        }
    }
}
