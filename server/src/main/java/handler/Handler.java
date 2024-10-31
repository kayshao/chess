package handler;
import com.google.gson.JsonObject;
import exception.ResponseException;
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
    // UserHandler.java
    private final UserService userService;
    private final GameService gameService;
    private static final Map<String, Integer> statusCodes = new HashMap<>();


        static {
            statusCodes.put("Error: unauthorized", 401);
            statusCodes.put("Error: bad request", 400);
            statusCodes.put("Error: already taken", 403);
            statusCodes.put("Error: description", 500);
        }

    public Handler(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }
    /* private void exceptionHandler(ResponseException ex, Request req, Response res) {
        res.status(ex.StatusCode());
    } */

    public Object handleRegister(Request req, Response res) throws ResponseException {
        try {
            RegisterRequest request = new Gson().fromJson(req.body(), RegisterRequest.class);
            RegisterResult result = userService.register(request);
            return new Gson().toJson(result);
        } catch (ServiceException e) {
            res.status(getStatus(e.getMessage()));
            return new Gson().toJson(new ErrorResponse(e.getMessage()));
        }
    }
    public Object handleLogin(Request req, Response res) throws ResponseException {
        try {
            LoginRequest request = new Gson().fromJson(req.body(), LoginRequest.class);
            LoginResult result = userService.login(request);
            return new Gson().toJson(result);
        } catch (ServiceException e) {
            res.status(getStatus(e.getMessage()));
            return new Gson().toJson(new ErrorResponse(e.getMessage()));
        }
    }
    public Object handleLogout(Request req, Response res) { //throws ResponseException {
        try {
            LogoutRequest request = new LogoutRequest(req.headers("Authorization"));
            System.out.println(req.headers());
            userService.logout(request);
            return new Gson().toJson(new Object());
        } catch (ServiceException e) {
            res.status(getStatus(e.getMessage()));
            return new Gson().toJson(new ErrorResponse(e.getMessage()));
        }
    }
    public Object handleCreateGame(Request req, Response res) { //throws ResponseException {
        try {
            CreateGameRequest request = new CreateGameRequest(req.headers("Authorization"), req.body());
            CreateGameResult result = gameService.createGame(request);
            return new Gson().toJson(result);
        } catch (ServiceException e) {
            res.status(getStatus(e.getMessage()));
            return new Gson().toJson(new ErrorResponse(e.getMessage()));
        }
    }
    public Object handleListGames(Request req, Response res) { //throws ResponseException {
        try {
            ListGamesRequest request = new ListGamesRequest(req.headers("Authorization"));
            ListGamesResult result = gameService.listGames(request);
            return new Gson().toJson(result);
        } catch (ServiceException e) {
            res.status(getStatus(e.getMessage()));
            return new Gson().toJson(new ErrorResponse(e.getMessage()));
        }
    }
    public Object handleJoinGame(Request req, Response res) { //throws ResponseException {
        try {
            JsonObject jsonObject = new Gson().fromJson(req.body(), JsonObject.class);
            JoinGameRequest request = new JoinGameRequest(req.headers("Authorization"), jsonObject.get("playerColor").getAsString(), jsonObject.get("gameID").getAsInt());
            JoinGameResult result = gameService.joinGame(request);
            return new Gson().toJson(result);
        } catch (ServiceException e) {
            res.status(getStatus(e.getMessage()));
            return new Gson().toJson(new ErrorResponse(e.getMessage()));
        }
    }
    public Object handleClear(Request req, Response res) { //throws ResponseException {
        ClearRequest request = new Gson().fromJson(req.body(), ClearRequest.class);
        ClearResult result = gameService.clear(request);
        userService.clear(request);
        return new Gson().toJson(result);
    }
    private int getStatus(String message) {
        if (statusCodes.get(message) == null) {
            return 500;
        } else {
            return statusCodes.get(message);
        }
    }
}
