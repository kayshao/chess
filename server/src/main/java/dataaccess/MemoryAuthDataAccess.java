package dataaccess;
import model.AuthData;
import java.util.UUID;
import java.util.HashMap;

public class MemoryAuthDataAccess implements AuthDataAccess {
    private final HashMap<String, AuthData> authDataHashMap = new HashMap<>();
    public void clear(){
        authDataHashMap.clear();
    }

    public AuthData getAuth(String authToken) {
        return authDataHashMap.get(authToken);
    }

    public String createAuth(String username) {
        String token = UUID.randomUUID().toString();
        AuthData auth = new AuthData(token, username);
        authDataHashMap.put(token, auth);
        return token;
    }

    public void deleteAuth(String token) {
        authDataHashMap.remove(token);
    }
}
