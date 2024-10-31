package server;

import dataaccess.MemoryAuthDataAccess;
import dataaccess.MemoryGameDataAccess;
import dataaccess.MemoryUserDataAccess;
import handler.Handler;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        MemoryAuthDataAccess auth = new MemoryAuthDataAccess();
        MemoryUserDataAccess user = new MemoryUserDataAccess();
        MemoryGameDataAccess game = new MemoryGameDataAccess();
        UserService userService = new UserService(auth, user);
        GameService gameService = new GameService(auth, game);
        Handler handler = new Handler(userService, gameService);

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
