public class Client {
    private ServerFacade server;
    private String authToken;

    public Client(String url) {
        server = new ServerFacade(url);
    }

    public void run() {}
}
