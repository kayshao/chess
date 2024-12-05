package websocket;

import com.google.gson.Gson;


import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

    //need to extend Endpoint for websocket to work properly
    public class WebSocketFacade { //extends Endpoint {

        Session session;
        NotificationHandler notificationHandler;


        public WebSocketFacade(String url, NotificationHandler notificationHandler) {
    }}
