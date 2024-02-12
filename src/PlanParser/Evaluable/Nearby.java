package PlanParser.Evaluable;

import java.util.Map;

public class Nearby implements Evaluable {
    private String direction;

    public Nearby(String direction) {
        this.direction = direction;
    }

    @Override
    public long eval(Map<String, Long> bindings) {
        // Logic to evaluate the Nearby expression
        // This can involve accessing game state to determine nearby opponent regions
        System.out.println("Evaluating Nearby expression in direction: " + direction);
        return 0; // Placeholder value, actual implementation depends on game state
    }
}
