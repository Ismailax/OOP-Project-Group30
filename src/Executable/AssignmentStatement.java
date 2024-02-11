package Executable;

import Evaluable.Evaluable;

import java.util.Map;

public class AssignmentStatement implements Executable{
    private String identifier;
    private Evaluable expression;
    Map<String, Integer> bindings;

    public AssignmentStatement(String identifier, Evaluable expression ,Map<String, Integer> bindings){
        this.identifier = identifier;
        this.expression = expression;
        this.bindings = bindings;
//        System.out.println(identifier + " = " + bindings.get(identifier));
    }
    @Override
    public void execute(Map<String, Integer> bindings) {
        int value = expression.eval(bindings);
        bindings.put(identifier, value);
    }
}
