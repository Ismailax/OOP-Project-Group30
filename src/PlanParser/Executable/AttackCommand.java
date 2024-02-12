package PlanParser.Executable;
import PlanParser.Evaluable.*;
import java.util.Map;

public class AttackCommand implements Executable{
    private String direction;
    private Evaluable expenditure;

    public AttackCommand(String direction, Evaluable expenditure) {
        this.direction = direction;
        this.expenditure = expenditure;
        long value = expenditure.eval(null);
        System.out.println("Shooting in direction: " + direction);
        System.out.println("Expenditure: " + value);
    }

    @Override
    public void execute(Map<String, Long> bindings) {
        // Implement logic to shoot in the specified direction with the calculated cost
    }
}
