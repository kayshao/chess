package dataaccess;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static dataaccess.DatabaseManager.getConnection;
import static org.junit.jupiter.api.Assertions.*;

class MySqlAuthDataAccessTest {
    MySqlAuthDataAccess authDAO;
    Connection conn;

    @BeforeEach
    void setUp() {
        try {
            this.authDAO = new MySqlAuthDataAccess();
            this.conn = getConnection();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void tearDown() {
        try {
            authDAO.clear();
        } catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    void clear() {
        try {
            authDAO.createAuth("myUser");
            assertDoesNotThrow(() -> authDAO.clear());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getAuthPositive() {
        try {
            assertEquals("m", authDAO.getAuth(authDAO.createAuth("m")).username());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getAuthNegative() {
        try {
            assertNull(authDAO.getAuth("fakeAuth"));
        } catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    void createAuthPositive() {
        try {
            assertNotNull(authDAO.createAuth("me"));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createAuthNegative() {
        assertThrows(DataAccessException.class, () -> authDAO.createAuth(null));
    }

    @Test
    void deleteAuthPositive() {
        assertDoesNotThrow(() -> authDAO.deleteAuth(authDAO.createAuth("myTestUser")));
    }

    @Test
    void deleteAuthNegative() {
        assertThrows(DataAccessException.class, () -> authDAO.deleteAuth(null));
    }
}