package websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import model.AuthData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.*;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;


@WebSocket
public class WebSocketHandler {
    AuthDataAccess authDAO;
    UserDataAccess userDAO;
    GameDataAccess gameDAO;

    public WebSocketHandler(AuthDataAccess auth, UserDataAccess user, GameDataAccess game) {
        authDAO = auth;
        userDAO = user;
        gameDAO = game;
    }

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        System.out.println("receiving message");
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        switch (action.getCommandType()) {
            case CONNECT -> enter(action.getAuthToken(), session);
            case LEAVE -> exit(action.getAuthToken(), action.getGameID());
            case MAKE_MOVE -> {MakeMoveCommand command = new Gson().fromJson(message, MakeMoveCommand.class);
                move(command.getAuthToken(), command.getGameID(), command.getMove(), session);
            }
            case RESIGN -> resign(action.getAuthToken(), action.getGameID(), session);
        }
    }

    private void enter(String authToken, Session session) throws Exception {
        if (!validAuth(authToken, session)) {
            return;
        }
        String username = authDAO.getAuth(authToken).username();
        var loadGame = new LoadGameMessage(new ChessGame());
        connections.add(username, session);
        session.getRemote().sendString(new Gson().toJson(loadGame));
        var message = String.format("%s has joined the game", username);
        var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(username, notification);
    }

    private void exit(String authToken, Integer gameID) throws Exception {
        var authData = authDAO.getAuth(authToken);
        connections.remove(authData.username());
        var message = String.format("%s has left the game", authData.username());
        var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(authData.username(), notification);
        gameDAO.setUsername("BLACK", new AuthData(null, null), gameID);
    }

    private void move(String authToken, Integer gameID, ChessMove move, Session session) throws Exception {
        if (!validAuth(authToken, session)) {
            return;
        }
        ChessGame game;
        try {
            game = gameDAO.getGame(gameID).game();
            if (game.validMoves(move.getStartPosition()).contains(move)) {
                game.makeMove(move);
                // TODO: implement server update of game
            }
        } catch (Exception e) {
            throw new RuntimeException(e); // TODO: error handling
        }

    }
    private void resign(String authToken, Integer gameID, Session session) throws Exception {
        if (!validAuth(authToken, session)) {
            return;
        }
        ChessGame game;
        try {
            game = gameDAO.getGame(gameID).game();
            var authData = authDAO.getAuth(authToken);
            game.active = false;
            gameDAO.updateGame(gameID, game);
            var notifySelf = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, "You have resigned");
            session.getRemote().sendString(new Gson().toJson(notifySelf));
            var message = String.format("%s has resigned from the game", authData.username());
            var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            connections.broadcast(authData.username(), notification);
        } catch (Exception e) {
            var message = "error " + e;
            var error = new NotificationMessage(ServerMessage.ServerMessageType.ERROR, message);
            session.getRemote().sendString(new Gson().toJson(error));
        }
    }
    private boolean validAuth(String authToken, Session session) throws Exception {
        System.out.println("validating auth " + authToken);
        try {
            var auth = authDAO.getAuth(authToken);
            if (auth == null) {
                System.out.println("no auth");
                String message = "error: invalid auth";
                var error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, message);
                session.getRemote().sendString(new Gson().toJson(error));
                return false;
            }
        } catch (DataAccessException e) {
            System.out.println("exception");
            String message = "error: invalid auth";
            var error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, message);
            session.getRemote().sendString(new Gson().toJson(error));
            return false;
        }
        return true;
    }
}
