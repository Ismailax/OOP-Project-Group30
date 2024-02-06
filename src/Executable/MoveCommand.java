package Executable;

import java.util.Map;

public class MoveCommand implements Executable {
    private String direction;

    public MoveCommand(String direction) {
        this.direction = direction;
    }

    @Override
    public void execute(Map<String, Integer> bindings) {
        // Implement move logic here based on the direction
        System.out.println("Moving in direction: " + direction);
    }
}

