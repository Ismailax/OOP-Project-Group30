package PlanParser.Executable;

import GameState.Player;

import java.util.Map;

public interface Executable {
    void execute(Map<String, Long> bindings);
}
