public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(String url) {
        this.serverUrl = url;
    }

    public void register(String username, String password, String email) {}

    public void login(String username, String password) {}

    public void logout() {}

    public void createGame(String name) {}

    public void listGames() {}

    public void joinGame() {}

    public void clear() {}
}
