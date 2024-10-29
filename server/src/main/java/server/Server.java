package server;

import dataaccess.MemoryAuthDataAccess;
import dataaccess.MemoryGameDataAccess;
import dataaccess.MemoryUserDataAccess;
import handler.Handler;
import service.GameService;
import service.UserService;
import spark.*;
import exception.ResponseException;

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
        // Spark.exception(ResponseException.class, handler::handleException);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        // Spark.init();
        //Spark.exception(ResponseException.class, this::exceptionHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }
    public int port() {
        return Spark.port();
    }

    /* private void exceptionHandler(ResponseException ex, Request req, Response res) {
        res.status(ex.StatusCode());
    }*/


    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    /*private Object register(Request req, Response res) {
        return null;
    }

    private Object clear(Request req, Response res) {
        return "clearing";
    }

    private Object login(Request req, Response res) {
        return "logging in";
    }

    private Object logout(Request req, Response res) {return "logging out";}

    private Object listGames(Request req, Response res) {
        return "listing games";
    }

    private Object createGame(Request req, Response res) {
        return "creating game";
    }

     */
}
