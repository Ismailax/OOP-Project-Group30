package PlanParser.Evaluable;

import java.util.Map;

public class Nearby implements Evaluable {
    private String direction;

    public Nearby(String direction) {
        this.direction = direction;
    }

    @Override
    public long eval(Map<String, Long> bindings) {
        long dir;
        if(direction.equals("up")) dir = 1;
        else if (direction.equals("upright")) dir = 2;
        else if (direction.equals("downright")) dir = 3;
        else if (direction.equals("down")) dir = 4;
        else if ((direction.equals("downleft"))) dir = 5;
        else dir = 6;
        // return nearby(dir)
        return 0; // Placeholder value, actual implementation depends on game state
    }
}
