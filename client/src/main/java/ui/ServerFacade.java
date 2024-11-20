package ui;
import com.google.gson.Gson;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.reflect.TypeToken;
import request.*;
import result.*;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(String url) {
        this.serverUrl = url;
    }

    public RegisterResult register(String username, String password, String email) throws Exception {
        var path = "/user";
        return this.makeRequest("POST", path, new RegisterRequest(username, password, email), RegisterResult.class, null);
    }

    public LoginResult login(String username, String password) throws Exception {
        var path = "/session";
        return this.makeRequest("POST", path, new LoginRequest(username, password), LoginResult.class, null);
    }

    public void logout(String authToken) throws Exception {
        var path = "/session";
        this.makeRequest("DELETE", path, null, null, authToken);
    }

    public int createGame(String name, String authToken) throws Exception {
        var path = "/game";
        return this.makeRequest("POST", path, new CreateGameRequest("hidden", name), CreateGameResult.class, authToken).gameID();
    }

    public ListGamesResult listGames(String authToken) throws Exception {
        var path = "/game";
        return this.makeRequest("GET", path, null, ListGamesResult.class, authToken);
    }

    public void joinGame(Integer id, String color, String authToken) throws Exception {
        var path = "/game";
        this.makeRequest("PUT", path, new JoinGameRequest("hidden", color, id), null, authToken);
    }

    public void clear() throws Exception {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authToken) throws Exception {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            if (authToken != null) {
                http.setRequestProperty("Authorization", authToken);
                System.out.println("added authtoken");}
            writeBody(request, http);
            http.connect();
            return readBody(http, responseClass);
        } catch (Exception e) {
                throw new Exception(e.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                    System.out.println(response);
                }
            }
        }
        return response;
    }
}
