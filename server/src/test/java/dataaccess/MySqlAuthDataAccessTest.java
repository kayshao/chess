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
    }

    @Test
    void getAuth() {
    }

    @Test
    void createAuth() {
    }

    @Test
    void deleteAuth() {
    }
}