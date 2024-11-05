package dataaccess;
import dataaccess.MySqlUserDataAccessTest;
import model.GameData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static dataaccess.DatabaseManager.getConnection;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MySqlGameDataAccessTest {
    MySqlGameDataAccess gameDAO;
    Connection conn;

    @BeforeEach
    void setUp() {
        try {
            this.gameDAO = new MySqlGameDataAccess();
            this.conn = getConnection();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void tearDown() {
        try {
            gameDAO.clear();
        } catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    void clear() {
        try {
            gameDAO.createGame("myGame");
            gameDAO.createGame("newOne");
            gameDAO.createGame("emag");
            assertEquals(3, getRowCount());
            gameDAO.clear();
            assertEquals(0, getRowCount());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getGamePositive() {
        try {
            gameDAO.createGame("myGame");
            GameData gotGame = gameDAO.getGame(1);
            assertEquals(new GameData(1, null, null, "myGame", null), gotGame);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getGameNegative() {
        assertThrows(DataAccessException.class, () -> gameDAO.getGame(200));
    }

    @Test
    void createGamePositive() {
        try {
            gameDAO.createGame("myGame");
            int rowCount = getRowCount();
            assertEquals(1, rowCount, "Table should contain exactly 1 row");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createGameNegative() {
        assertThrows(DataAccessException.class, () -> gameDAO.createGame(null));
    }

    @Test
    void listGames() {
    }

    @Test
    void setUsername() {
    }

    private int getRowCount() throws DataAccessException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM game")) {

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}