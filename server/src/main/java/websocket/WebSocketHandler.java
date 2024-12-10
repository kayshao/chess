package websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.*;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Timer;


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
            case CONNECT -> {
                System.out.println("message was connect command");
                ConnectCommand connectCommand = new Gson().fromJson(message, ConnectCommand.class);
                enter(connectCommand.getAuthToken(), session);
            }
            case LEAVE -> {
                LeaveCommand leaveCommand = (LeaveCommand) action;
                exit(leaveCommand.getUsername());
            }
            case MAKE_MOVE -> move();
            case RESIGN -> resign();
        }
    }

    private void enter(String authToken, Session session) throws Exception {
        System.out.println(authToken);
        String visitorName = authDAO.getAuth(authToken).username();
        var loadGame = new LoadGameMessage(new ChessGame());
        connections.add(visitorName, session);
        session.getRemote().sendString(new Gson().toJson(loadGame));
        var message = String.format("%s has joined the game", visitorName);
        var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(visitorName, notification);
    }

    private void exit(String visitorName) throws IOException {
    }
    private void move() {}
    private void resign() {}
}
