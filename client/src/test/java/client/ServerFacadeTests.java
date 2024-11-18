package client;

import org.junit.jupiter.api.*;
import result.*;
import server.Server;
import ui.ServerFacade;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @BeforeEach
    void reset() {
        try {
            facade.clear();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void testRegisterPositive() {
        try {
            RegisterResult result = facade.register("user", "password", "email");
            assert(result.username().equals("user"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testRegisterNegative() {
        try {
            facade.register("newUser", "password", "email");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertThrows(Exception.class, () -> facade.register("newUser", "p", "e"));
    }

    @Test
    public void testLoginPositive() {
        try {
            facade.register("user", "password", "email");
            LoginResult result = facade.login("user", "password");
            assert(result.username().equals("user"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testLoginNegative() {
        assertThrows(Exception.class, () -> facade.login("fakeUser", "fakePass"));
    }

    @Test
    public void testLogoutPositive() {
        try {
            facade.register("try", "logging", "out");
            LoginResult result = facade.login("try", "logging");
            assertDoesNotThrow(() -> facade.logout(result.authToken()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testLogoutNegative() {
        assertThrows(Exception.class, () -> facade.logout("notARealAuth"));
    }

    @Test
    public void testCreateGamePositive() {
        try {
            facade.register("create", "game", "user");
            System.out.println("registered");
            LoginResult result = facade.login("create", "game");
            System.out.println(result.authToken());
            int game = facade.createGame("coolGame", result.authToken());
            System.out.println("created");
            assert (game == 1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCreateGameNegative() {
        assertThrows(Exception.class, () -> facade.createGame("badGame", "badAuth"));
    }

    @Test
    public void testListGamesPositive() {
        try {
            facade.register("create", "game", "user");
            System.out.println("registered");
            LoginResult result = facade.login("create", "game");
            System.out.println(result.authToken());
            facade.createGame("coolGame", result.authToken());
            List<Map<String, Object>> games = facade.listGames(result.authToken());
            System.out.println("created");
            assert ((games).size() == 1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testListGamesNegative() {
        assertThrows(Exception.class, () -> facade.listGames("htua"));
    }
    @Test
    public void testClear() {

    }
}
