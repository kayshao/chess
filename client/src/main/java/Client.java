import ui.PreLoginUI;
import ui.ServerFacade;

public class Client {
    private ServerFacade server;
    private String authToken;

    public Client(String url) {
        server = new ServerFacade(url);
    }

    public void run() {
        System.out.println("Welcome to Chess!");
        runPreloginUI();
        runPostloginUI();
    }
    private void runPreloginUI() {
        PreLoginUI preLoginUI = new PreLoginUI(server);
        preLoginUI.run();
    };

    private void runPostloginUI() {};
}
