package request;

public record JoinGameRequest(String token, String playerColor, Integer gameID) {
}
