import chess.*;


public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);

    Client client = new Client("http://localhost:8080");
    client.run();
    }
}

// error handling for game that is full
// error handling for reregister user