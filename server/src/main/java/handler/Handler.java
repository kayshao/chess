package handler;
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

    public Object handleRegister(Request req, Response res) {
        RegisterRequest request = new Gson().fromJson(req.body(), RegisterRequest.class);
        RegisterResult result = userService.register(request);
        return new Gson().toJson(result);
    }
    public Object handleLogin(Request req, Response res) {
        LoginRequest request = new Gson().fromJson(req.body(), LoginRequest.class);
        LoginResult result = userService.login(request);
        return new Gson().toJson(result);
    }
    public Object handleLogout(Request req, Response res) {
        LogoutRequest request = new Gson().fromJson(req.body(), LogoutRequest.class);
        userService.logout(request);
        return "{}";
    }
    public Object handleCreateGame(Request req, Response res) {
        CreateGameRequest request = new Gson().fromJson(req.body(), CreateGameRequest.class);
        // String authToken = request.token();
        CreateGameResult result = gameService.createGame(request);
        return new Gson().toJson(result);
    }
    public Object handleListGames(Request req, Response res) {
        ListGamesRequest request = new Gson().fromJson(req.body(), ListGamesRequest.class);
        ListGamesResult result = gameService.listGames(request);
        return new Gson().toJson(result);
    }
    public Object handleJoinGame(Request req, Response res) {
        JoinGameRequest request = new Gson().fromJson(req.body(), JoinGameRequest.class);
        JoinGameResult result = gameService.joinGame(request);
        return new Gson().toJson(result);
    }
    public Object handleClear(Request req, Response res) {
        ClearRequest request = new Gson().fromJson(req.body(), ClearRequest.class);
        ClearResult result = gameService.clear(request);
        userService.clear(request);
        return new Gson().toJson(result);
    }
}
