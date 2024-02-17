package PlanParser.Executable;

import PlanParser.Evaluable.*;

import java.util.Map;

public class CollectCommand implements Executable{
    private Evaluable amount;

    public CollectCommand(Evaluable amount, Map<String, Long> bindings) {
        this.amount = amount;
        long value = amount.eval(bindings);
        System.out.println("Parse collect: " + value);
    }

    @Override
    public void execute(Map<String, Long> bindings) {
        long value = amount.eval(bindings);
        System.out.println("Execute collect: " + value);
    }
}