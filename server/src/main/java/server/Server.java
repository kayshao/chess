package server;

import dataaccess.*;
import handler.Handler;
import service.GameService;
import service.UserService;
import spark.*;
import websocket.WebSocketHandler;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        AuthDataAccess auth;
        UserDataAccess user;
        GameDataAccess game;
        try {
            auth = new MySqlAuthDataAccess();
            user = new MySqlUserDataAccess();
            game = new MySqlGameDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }

        UserService userService = new UserService(auth, user);
        GameService gameService = new GameService(auth, game);
        Handler handler = new Handler(userService, gameService);
        WebSocketHandler webSocketHandler = new WebSocketHandler(auth, user, game);
        Spark.webSocket("/ws", webSocketHandler);

        Spark.post("/user", handler::handleRegister);
        Spark.delete("/db", handler::handleClear);
        Spark.post("/session", handler::handleLogin);
        Spark.delete("/session", handler::handleLogout);
        Spark.get("/game", handler::handleListGames);
        Spark.post("/game", handler::handleCreateGame);
        Spark.put("/game", handler::handleJoinGame);


        Spark.awaitInitialization();
        return Spark.port();
    }
    public int port() {
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
