package handler;
import exception.ResponseException;
import exception.ServiceException;
import request.*;
import result.*;
import service.GameService;
import service.UserService;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

public class Handler {
    // UserHandler.java
    private final UserService userService;
    private final GameService gameService;

    public Handler(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }
    private void exceptionHandler(ResponseException ex, Request req, Response res) {
        res.status(ex.StatusCode());
    }

    public Object handleRegister(Request req, Response res) throws ResponseException {
        try {
            RegisterRequest request = new Gson().fromJson(req.body(), RegisterRequest.class);
            RegisterResult result = userService.register(request);
            return new Gson().toJson(result);
        } catch (ServiceException e) {
            res.status(403);
            return new Gson().toJson(new ErrorResponse(e.getMessage()));
        }
    }
    public Object handleLogin(Request req, Response res) throws ResponseException {
        try {
            LoginRequest request = new Gson().fromJson(req.body(), LoginRequest.class);
            LoginResult result = userService.login(request);
            return new Gson().toJson(result);
        } catch (ServiceException e) {
            res.status(401);
            return new Gson().toJson(new ErrorResponse(e.getMessage()));
        }
    }
    public Object handleLogout(Request req, Response res) { //throws ResponseException {
        try {
            LogoutRequest request = new Gson().fromJson(req.body(), LogoutRequest.class);
            userService.logout(request);
            return "{}";
        } catch (ServiceException e) {
            res.status(401);
            return new Gson().toJson(new ErrorResponse(e.getMessage()));
        }
    }
    public Object handleCreateGame(Request req, Response res) { //throws ResponseException {
        CreateGameRequest request = new Gson().fromJson(req.body(), CreateGameRequest.class);
        // String authToken = request.token();
        CreateGameResult result = gameService.createGame(request);
        return new Gson().toJson(result);
    }
    public Object handleListGames(Request req, Response res) { //throws ResponseException {
        ListGamesRequest request = new Gson().fromJson(req.body(), ListGamesRequest.class);
        ListGamesResult result = gameService.listGames(request);
        return new Gson().toJson(result);
    }
    public Object handleJoinGame(Request req, Response res) { //throws ResponseException {
        JoinGameRequest request = new Gson().fromJson(req.body(), JoinGameRequest.class);
        JoinGameResult result = gameService.joinGame(request);
        return new Gson().toJson(result);
    }
    public Object handleClear(Request req, Response res) { //throws ResponseException {
        ClearRequest request = new Gson().fromJson(req.body(), ClearRequest.class);
        ClearResult result = gameService.clear(request);
        userService.clear(request);
        return new Gson().toJson(result);
    }
}
