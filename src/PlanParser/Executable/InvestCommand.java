package PlanParser.Executable;
import PlanParser.Evaluable.*;

import java.util.Map;

public class InvestCommand implements Executable{
    private Evaluable amount;
    public InvestCommand(Evaluable amount){
        this.amount = amount;
        long value = amount.eval(null);
        System.out.println("Parse invest: " + value);
    }
    @Override
    public void execute(Map<String, Long> bindings) {
        long value = amount.eval(null);
        System.out.println("Execute invest: " + value);
    }
}
