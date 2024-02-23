package PlanParser.Executable;
import GameState.Player;
import PlanParser.Evaluable.*;

import java.util.Map;

public class InvestCommand implements Executable{
    Player player;
    private Evaluable amount;
    public InvestCommand(Evaluable amount, Map<String, Long> bindings){
        this.amount = amount;
        long value = amount.eval(bindings);
        System.out.println("Parse invest: " + value);
    }
    @Override
    public void execute(Map<String, Long> bindings) {
        long value = amount.eval(bindings);
        // invest(value);
        System.out.println("Execute invest: " + value);
    }
}
