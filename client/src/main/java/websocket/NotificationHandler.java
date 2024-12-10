package websocket;

import chess.ChessGame;

public interface NotificationHandler {
    public void updateGame(ChessGame game);
    public void showNotification(String msg);
    public void showError(String msg);
}