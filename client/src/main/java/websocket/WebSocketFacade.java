package websocket;
import chess.ChessGame;
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
        System.out.println("handling server message");
        ServerMessage.ServerMessageType type = message.getServerMessageType();
        if (type == ServerMessage.ServerMessageType.LOAD_GAME) {
            System.out.println("server message is load game");
            LoadGameMessage loadGameMessage = new Gson().fromJson(msg, LoadGameMessage.class);
            ChessGame game = loadGameMessage.getGame();
            notificationHandler.updateGame(game);
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
            var action = new ConnectCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID); //TODO: add actual username functionality
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
            System.out.println("Connected"); //TODO: delete after debugging
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
    }
}
