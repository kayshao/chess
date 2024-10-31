package request;

public record CreateGameRequest(String authToken, String name) {
}
