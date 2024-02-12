package PlanParser.Executable;

import PlanParser.Evaluable.*;

import java.util.Map;

public class CollectCommand implements Executable{
    private Evaluable amount;

    public CollectCommand(Evaluable amount) {
        this.amount = amount;
        long value = amount.eval(null);
        System.out.println("collect: " + value);
    }

    @Override
    public void execute(Map<String, Long> bindings) {

    }
}
