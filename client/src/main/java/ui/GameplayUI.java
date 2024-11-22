package ui;

public class GameplayUI {
    public GameplayUI() {}
    public void run() {
        System.out.println("In gameplayUI");
        DrawBoard b = new DrawBoard();
        b.draw();

    }
}
