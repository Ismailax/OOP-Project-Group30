package Evaluable;

import java.util.Map;

public class Opponent implements Evaluable {
    @Override
    public int eval(Map<String, Integer> bindings) {
        // Logic to evaluate the Opponent expression
        // This can involve accessing game state to determine the presence of opponents
        System.out.println("Evaluating Opponent expression");
        return 0; // Placeholder value, actual implementation depends on game state
    }
}
