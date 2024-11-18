package client;

import org.junit.jupiter.api.*;
import result.RegisterResult;
import server.Server;
import ui.ServerFacade;

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

}