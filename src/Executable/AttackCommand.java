package Executable;
import Evaluable.*;
import java.util.Map;

public class AttackCommand implements Executable{
    private String direction;
    private Evaluable expenditure;

    public AttackCommand(String direction, Evaluable expenditure) {
        this.direction = direction;
        this.expenditure = expenditure;
    }

    @Override
    public void execute(Map<String, Integer> bindings) {
        // Implement logic to shoot in the specified direction with the calculated cost
    }
}
