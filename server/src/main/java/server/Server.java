package server;

import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::register);
        Spark.delete("/db", this::clear);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);

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

    private Object register(Request req, Response res) {
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
}
