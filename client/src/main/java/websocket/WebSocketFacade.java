package websocket;
import chess.*;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.glassfish.tyrus.client.ClientProperties;
import server.Server;
import websocket.commands.*;
import websocket.messages.*;


import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static ui.EscapeSequences.SET_TEXT_COLOR_MAGENTA;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {
    private Session session;
    private NotificationHandler notificationHandler;

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws Exception {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            System.out.println(container.toString()); // TODO: remove after debugging
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String msg) {
                    ServerMessage serverMessage = new Gson().fromJson(msg, ServerMessage.class);
                    handleServerMessage(serverMessage, msg);
                    System.out.print(SET_TEXT_COLOR_MAGENTA + "\nIn Chess Gameplay>>> ");
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            ex.printStackTrace();
        throw new Exception(ex.getMessage());
        }
    }
    private void handleServerMessage(ServerMessage message, String msg) {
        System.out.println("handling server message"); // TODO: delete
        ServerMessage.ServerMessageType type = message.getServerMessageType();
        if (type == ServerMessage.ServerMessageType.LOAD_GAME) {
            System.out.println("server message is load game"); // TODO: delete
            LoadGameMessage loadGameMessage = new Gson().fromJson(msg, LoadGameMessage.class);
            ChessGame game = loadGameMessage.getGame();
            notificationHandler.updateGame(game);
        }
        if (type == ServerMessage.ServerMessageType.ERROR) {
            ErrorMessage error = new Gson().fromJson(msg, ErrorMessage.class);
            notificationHandler.showError(error.getMessage());
        }
        if (type == ServerMessage.ServerMessageType.NOTIFICATION) {
            NotificationMessage notification = new Gson().fromJson(msg, NotificationMessage.class);
            notificationHandler.showNotification(notification.getMessage());
        }
            // case NOTIFICATION -> notificationHandler.showNotification(message.getMessage());
            // case ERROR -> notificationHandler.showError(message.getErrorMessage());
    }

    public void sendCommand(UserGameCommand command) {
        System.out.println("Sending command");
        try {
            String jsonCommand = new Gson().toJson(command);
            session.getBasicRemote().sendText(jsonCommand);
        } catch (IOException e) {
            notificationHandler.showError("Failed to send command: " + e.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig config) {}

    public void connect(String authToken, Integer gameID) throws Exception {
        try {
            var action = new ConnectCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
            // this.session.getBasicRemote().sendText(new Gson().toJson(action));
            sendCommand(action);
            System.out.println("Connected"); //TODO: delete after debugging
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    public void move(String authToken, Integer gameID, ChessMove move) {
        var action = new MakeMoveCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameID, move);
        sendCommand(action);
    }

    public void disconnect(String authToken, Integer gameID) throws Exception {
        try {
            var action = new LeaveCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
            sendCommand(action);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    public void resign(String authToken, Integer gameID) {
        var action = new ResignCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
        sendCommand(action);
    }
    public boolean validateMove(ChessGame game, ChessMove move) {
        return game.validMoves(move.getStartPosition()).contains(move);
    }
}
