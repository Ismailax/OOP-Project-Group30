package Executable;

import Evaluable.*;

import java.util.Map;

public class CollectCommand implements Executable{
    private Evaluable amount;

    public CollectCommand(Evaluable amount) {
        this.amount = amount;
        int value = amount.eval(null);
        System.out.println("collect: " + value);
    }

    @Override
    public void execute(Map<String, Integer> bindings) {

    }
}
