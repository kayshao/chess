package websocket;

public interface NotificationHandler {
    public void updateGame(String game);
    public void showNotification(String msg);
    public void showError(String msg);
}