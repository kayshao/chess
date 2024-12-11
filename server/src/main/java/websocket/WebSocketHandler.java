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
            case RESIGN -> resign(action.getAuthToken(), action.getGameID(), session);
            case MAKE_MOVE -> {MakeMoveCommand command = new Gson().fromJson(message, MakeMoveCommand.class);
                move(command.getAuthToken(), command.getGameID(), command.getMove(), session);
            }
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
        if (!validAuth(authToken, session) | observe(authToken, gameID, session)) {
            return;
        }
        ChessGame game;
        try {
            game = gameDAO.getGame(gameID).game();
            String username = authDAO.getAuth(authToken).username();
            if ((game.getTeamTurn().equals(ChessGame.TeamColor.BLACK)
                    && !gameDAO.getGame(gameID).blackUsername().equals(username))
                    | game.getTeamTurn().equals(ChessGame.TeamColor.WHITE)
                    && !gameDAO.getGame(gameID).whiteUsername().equals(username)) {
                sendError("error: not your turn", session);
                return;
            }
            if (game.validMoves(move.getStartPosition()).contains(move)) {
                game.makeMove(move);
                gameDAO.updateGame(gameID, game);
                var loadGame = new LoadGameMessage(game);
                connections.broadcast(username, loadGame);
                session.getRemote().sendString(new Gson().toJson(loadGame));
                var moveNotification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, move.toString());
                connections.broadcast(username, moveNotification);
                if (game.getTeamTurn().equals(ChessGame.TeamColor.WHITE)) {
                    game.setTeamTurn(ChessGame.TeamColor.BLACK);
                }
                else {
                    game.setTeamTurn(ChessGame.TeamColor.WHITE);
                }
            }
            else {
                sendError("error: invalid move", session);
                }

        } catch (Exception e) {
            sendError("error: unknown move error", session);
        }

    }
    private void resign(String authToken, Integer gameID, Session session) throws Exception {
        if (!validAuth(authToken, session)) {
            return;
        }
        if (observe(authToken, gameID, session)) {
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
            sendError("error: unknown resign error", session);
        }
    }
    private boolean validAuth(String authToken, Session session) throws Exception {
        System.out.println("validating auth " + authToken);
        try {
            var auth = authDAO.getAuth(authToken);
            if (auth == null) {
                System.out.println("no auth");
                sendError("error: invalid auth", session);
                return false;
            }
        } catch (DataAccessException e) {
            System.out.println("exception");
            sendError("error: invalid auth", session);
            return false;
        }
        return true;
    }
    private boolean observe(String authToken, Integer gameID, Session session) throws Exception {
        var game = gameDAO.getGame(gameID);
        String username = authDAO.getAuth(authToken).username();
        String bUser = game.blackUsername();
        String wUser = game.whiteUsername();
        if ((bUser == null | !bUser.equals(username)) && (wUser == null | !wUser.equals(username))) {
            sendError("error: invalid request for observer", session);
            return true;
        }
        return false;
    }
    private void sendError(String msg, Session session) throws Exception {
        var error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, msg);
        session.getRemote().sendString(new Gson().toJson(error));
    }
}
