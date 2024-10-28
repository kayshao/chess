package dataaccess;
import model.AuthData;
import model.UserData;
import java.util.UUID;
import java.util.HashMap;

public class MemoryAuthDataAccess implements AuthDataAccess {
    private final HashMap<String, AuthData> authToken_authData = new HashMap<>();
    public void clear(){
        authToken_authData.clear();
    }

    public AuthData getAuth(String authToken) {
        return authToken_authData.get(authToken);
    }

    public String createAuth(String username) {
        String token = UUID.randomUUID().toString();
        AuthData auth = new AuthData(username, token);
        authToken_authData.put(token, auth);
        return token;
    }

    public void deleteAuth(String token) {
        authToken_authData.remove(token);
    }
}
