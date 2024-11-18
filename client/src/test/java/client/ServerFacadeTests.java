package client;

import org.junit.jupiter.api.*;
import result.*;
import server.Server;
import ui.ServerFacade;

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
    public void testClear() {

    }
}
