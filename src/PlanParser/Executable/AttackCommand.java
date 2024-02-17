package PlanParser.Executable;
import PlanParser.Evaluable.*;
import java.util.Map;

public class AttackCommand implements Executable{
    private String direction;
    private Evaluable expenditure;

    public AttackCommand(String direction, Evaluable expenditure, Map<String, Long> bindings) {
        this.direction = direction;
        this.expenditure = expenditure;
        long value = expenditure.eval(bindings);
        System.out.println("Parse shoot " + direction + " " + value);
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
        // shoot(dir,expenditure.eval(bindings))
        System.out.println("Execute shoot " + direction + " " + expenditure.eval(bindings));
    }
}
