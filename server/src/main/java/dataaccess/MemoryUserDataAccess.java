package dataaccess;
import model.GameData;
import model.UserData;

import java.util.HashMap;

public class MemoryUserDataAccess {
    private final HashMap<String, UserData> users = new HashMap<>();

    public void clear() {
        users.clear();
    }

    UserData getUser(String username) {
        return users.get(username);
    }

    public void createUser(String username, String password, String email) {
        UserData user = new UserData(username, password, email);
        users.put(username, user);
    }
}
