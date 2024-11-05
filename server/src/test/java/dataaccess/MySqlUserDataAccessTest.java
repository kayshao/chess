package dataaccess;

import exception.ServiceException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static dataaccess.DatabaseManager.getConnection;
import static org.junit.jupiter.api.Assertions.*;

class MySqlUserDataAccessTest {
    MySqlUserDataAccess userDAO;
    Connection conn;

    @BeforeEach
    void setUp() {
        try {
            this.userDAO = new MySqlUserDataAccess();
            this.conn = getConnection();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void clear() {
    }

    @Test
    void GetUserPositive() {
    }

    @Test
    void getUserNegative() {

    }

    @Test
    void CreateUserPositive() {
        try {
            userDAO.createUser("myUser", "myPass", "my@email.com");
            int rowCount = getRowCount(userDAO);
            assertEquals(1, rowCount, "Table should contain exactly one row");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }



    @Test
    void createUserNegative() {
        assertThrows(DataAccessException.class, () -> userDAO.createUser(null, null, null));

    }

    private int getRowCount(MySqlUserDataAccess userDAO) throws DataAccessException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM user")) {

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @AfterEach
    void takeDown() {
        clear();
    }

}