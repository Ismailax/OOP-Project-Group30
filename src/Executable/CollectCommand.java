package Executable;

import Evaluable.*;

import java.util.Map;

public class CollectCommand implements Executable{
    private Evaluable amount;

    public CollectCommand(Evaluable amount) {
        this.amount = amount;
    }

    @Override
    public void execute(Map<String, Integer> bindings) {

    }
}
