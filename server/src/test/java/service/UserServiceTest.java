package service;

import dataaccess.MemoryAuthDataAccess;
import dataaccess.MemoryUserDataAccess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.ClearRequest;
import request.LoginRequest;
import request.LogoutRequest;
import request.RegisterRequest;
import result.ClearResult;
import result.LoginResult;
import result.LogoutResult;
import result.RegisterResult;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private UserService userService;

    @BeforeEach
    void init() {
        this.userService = new UserService(new MemoryAuthDataAccess(), new MemoryUserDataAccess());
        userService.register(new RegisterRequest("myUsername", "myPassword", "my@email.com"));
    }

    @Test
    void register_positive() {
        RegisterRequest request = new RegisterRequest("aNewUsername", "myPassword", "mail@email.com");
        RegisterResult result = userService.register(request);

        assertNotNull(result.token());
        assertEquals("aNewUsername", result.username());
    }

    @Test
    void register_negative() {
        RegisterRequest request = new RegisterRequest("myUsername", "myPassword", "my@email.com");
        RegisterResult result = userService.register(request);
    }

    @Test
    void login_positive() {
        LoginRequest request = new LoginRequest("myUsername", "myPassword");
        LoginResult result = userService.login(request);

        assertNotNull(result.authToken());
    }

    @Test
    void logout() {
        LoginRequest request = new LoginRequest("myUsername", "myPassword");
        LoginResult result = userService.login(request);
        LogoutRequest logoutRequest = new LogoutRequest(result.authToken());
        LogoutResult logoutResult = userService.logout(logoutRequest);

        assertNotNull(logoutResult);
        assertEquals("", logoutResult.result());
    }

    @Test
    void clear() {
        ClearRequest request = new ClearRequest();
        ClearResult result = userService.clear(request);

        assertNotNull(result);
        assertEquals("", result.result());
    }
}