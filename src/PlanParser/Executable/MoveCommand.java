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
        System.out.println("Execute move " + direction);

    }
}

