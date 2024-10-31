package service;
import dataaccess.MemoryAuthDataAccess;
import dataaccess.MemoryGameDataAccess;
import dataaccess.MemoryUserDataAccess;
import exception.ServiceException;
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
        try {
            MemoryAuthDataAccess authDAO = new MemoryAuthDataAccess();
            this.gameService = new GameService(authDAO, new MemoryGameDataAccess());
            UserService userService = new UserService(authDAO, new MemoryUserDataAccess());
            userService.register(new RegisterRequest("myUser", "myPass", "my@mail.com"));
            LoginResult loginResult = userService.login(new LoginRequest("myUser", "myPass"));
            this.authToken = loginResult.authToken();
            gameService.createGame(new CreateGameRequest(authToken, "myGame"));
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createGamePositive() {
        try {
            CreateGameRequest request = new CreateGameRequest(authToken, "nameOfGame");
            CreateGameResult result = gameService.createGame(request);
            assertEquals(result.gameID(), 2);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createGameNegative() {
        CreateGameRequest request = new CreateGameRequest("", "nameOfGame");
        assertThrows(ServiceException.class, () -> gameService.createGame(request));
    }

    @Test
    void joinGamePositive() {
        try {
            JoinGameRequest request = new JoinGameRequest(authToken, "black", 1);
            JoinGameResult result = gameService.joinGame(request);
            assertEquals("", result.result());
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void joinGameNegative() {
        JoinGameRequest request = new JoinGameRequest(authToken, "black", 2);
        assertThrows(ServiceException.class, () -> gameService.joinGame(request));
    }

    @Test
    void listGamesPositive() {
        try {
            ListGamesRequest request = new ListGamesRequest(authToken);
            ListGamesResult result = gameService.listGames(request);
            assertFalse(result.games().isEmpty());
            assertEquals(1, result.games().size());
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void listGamesNegative() {
        ListGamesRequest request = new ListGamesRequest("fakeAuth");
        assertThrows(ServiceException.class, () -> gameService.listGames(request));
    }

    @Test
    void clear() {
        try {
            ClearRequest request = new ClearRequest();
            ClearResult result = gameService.clear(request);
            assertEquals("", result.result());
            ListGamesResult listGames = gameService.listGames(new ListGamesRequest(authToken));
            assertEquals(0, listGames.games().size());
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }
}