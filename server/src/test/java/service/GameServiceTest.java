package service;

import dataaccess.MemoryAuthDataAccess;
import dataaccess.MemoryGameDataAccess;
import dataaccess.MemoryUserDataAccess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.*;
import result.*;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {
    private GameService gameService;
    private String authToken;


    @BeforeEach
    void init() {
        MemoryAuthDataAccess authDAO = new MemoryAuthDataAccess();
        this.gameService = new GameService(authDAO, new MemoryGameDataAccess());
        UserService userService = new UserService(authDAO, new MemoryUserDataAccess());
        userService.register(new RegisterRequest("myUser", "myPass", "my@mail.com"));
        LoginResult loginResult = userService.login(new LoginRequest("myUser", "myPass"));
        this.authToken = loginResult.authToken();
        CreateGameResult createResult = gameService.createGame(new CreateGameRequest(authToken, "myGame"));
    }

    @Test
    void createGame() {
        CreateGameRequest request = new CreateGameRequest(authToken, "nameOfGame");
        CreateGameResult result = gameService.createGame(request);

        assertEquals(result.gameID(), 1);
    }

    @Test
    void joinGame() {
        JoinGameRequest request = new JoinGameRequest(authToken, "black", 1);
        JoinGameResult result = gameService.joinGame(request);

        assertEquals("", result.result());
    }

    @Test
    void listGames() {
        ListGamesRequest request = new ListGamesRequest(authToken);
        ListGamesResult result = gameService.listGames(request);
        assertEquals("game", result.games());
    }

    @Test
    void clear() {
        ClearRequest request = new ClearRequest();
        ClearResult result = gameService.clear(request);
        assertEquals("", result.result());
        ListGamesResult listGames = gameService.listGames(new ListGamesRequest(authToken));
        assertEquals("{}", listGames.games());
    }
}