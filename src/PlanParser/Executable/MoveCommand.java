package PlanParser.Executable;

import java.util.Map;

public class MoveCommand implements Executable {
    private String direction;

    public MoveCommand(String direction) {
        this.direction = direction;
        System.out.println("Parse move " + direction);
    }

    @Override
    public void execute(Map<String, Long> bindings) {
        long dir;
        if(direction.equals("up")) dir = 1;
        else if (direction.equals("upright")) dir = 2;
        else if (direction.equals("downright")) dir = 3;
        else if (direction.equals("down")) dir = 4;
        else if ((direction.equals("downleft"))) dir = 5;
        else dir = 6;
        // move(dir)
        System.out.println("Execute move " + direction);
    }
}

