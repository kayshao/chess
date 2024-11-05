package dataaccess;

import exception.ServiceException;
import model.UserData;
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
        try {
            userDAO.createUser("anotherUser", "myPass", "my@mail.com");
            userDAO.createUser("chicken", "chicken", "chicken@mail.com");
            userDAO.createUser("emanresu", "drowssap", "moc@liame");
            assertEquals(3, getRowCount());
            userDAO.clear();
            assertEquals(0, getRowCount());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void GetUserPositive() {
        try {
            userDAO.createUser("myUser", "myPass", "my@email.com");
            UserData gotUser = userDAO.getUser("myUser");
            assertEquals(new UserData("myUser", "myPass", "my@email.com"), gotUser);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getUserNegative() {
        assertThrows(DataAccessException.class, () -> userDAO.getUser("chicken"));
    }

    @Test
    void CreateUserPositive() {
        try {
            userDAO.createUser("myUser", "myPass", "my@email.com");
            int rowCount = getRowCount();
            assertEquals(1, rowCount, "Table should contain exactly one row");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createUserNegative() {
        assertThrows(DataAccessException.class, () -> userDAO.createUser(null, null, null));

    }

    private int getRowCount() throws DataAccessException {
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
        try {
            userDAO.clear();
        } catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}