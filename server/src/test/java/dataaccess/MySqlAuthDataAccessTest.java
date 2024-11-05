package dataaccess;

import model.AuthData;
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
        assertThrows(DataAccessException.class, () -> authDAO.getAuth("fakeAuth"));
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
        // assertThrows(DataAccessException.class, () -> authDAO.createAuth("not in db"));
    }

    @Test
    void deleteAuthPositive() {
        assertDoesNotThrow(() -> authDAO.deleteAuth(authDAO.createAuth("myTestUser")));
    }

    @Test
    void deleteAuthNegative() {
        assertThrows(DataAccessException.class, () -> authDAO.deleteAuth("fakeAuth"));
    }
}