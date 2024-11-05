package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDataAccess;
import dataaccess.MemoryUserDataAccess;
import exception.ServiceException;
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
        // this.userService = new UserService(new MemoryAuthDataAccess(), new MemoryUserDataAccess());
        try {
            userService.register(new RegisterRequest("myUsername", "myPassword", "my@email.com"));
        } catch (ServiceException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void registerPositive() {
        RegisterRequest request = new RegisterRequest("aNewUsername", "myPassword", "mail@email.com");
        try {
            RegisterResult result = userService.register(request);
            assertNotNull(result.authToken());
            assertEquals("aNewUsername", result.username());
        } catch (ServiceException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void registerNegative() {
        RegisterRequest request = new RegisterRequest("myUsername", "myPassword", "my@email.com");
        assertThrows(ServiceException.class, () -> userService.register(request));
        }

    @Test
    void loginPositive() {
        LoginRequest request = new LoginRequest("myUsername", "myPassword");
        try {
            LoginResult result = userService.login(request);
            assertNotNull(result.authToken());
        } catch (ServiceException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void loginNegative() {
        LoginRequest request = new LoginRequest("chicken", "chicken");
        assertThrows(ServiceException.class, () -> userService.login(request));
    }

    @Test
    void logoutPositive() {
        LoginRequest request = new LoginRequest("myUsername", "myPassword");
        try {
            LoginResult result = userService.login(request);
            LogoutRequest logoutRequest = new LogoutRequest(result.authToken());
            LogoutResult logoutResult = userService.logout(logoutRequest);
            assertNotNull(logoutResult);
            assertEquals("", logoutResult.result());
        } catch (ServiceException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void logoutNegative() {
        LogoutRequest logoutRequest = new LogoutRequest("fakeToken");
        assertThrows(ServiceException.class, () -> userService.logout(logoutRequest));
    }

    @Test
    void clear() throws DataAccessException {
        ClearRequest request = new ClearRequest();
        ClearResult result = userService.clear(request);

        assertNotNull(result);
        assertEquals("", result.result());
    }
}