package result;

import model.GameData;

import java.util.HashMap;
import java.util.List;

public record ListGamesResult(HashMap<Integer, GameData> games) {
}
