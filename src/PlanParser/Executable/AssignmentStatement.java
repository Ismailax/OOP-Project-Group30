package PlanParser.Executable;

import PlanParser.Evaluable.*;

import java.util.Map;

public class AssignmentStatement implements Executable{
    private String identifier;
    private Evaluable expression;
    Map<String, Long> bindings;

    public AssignmentStatement(String identifier, Evaluable expression ,Map<String, Long> bindings){
        this.identifier = identifier;
        this.expression = expression;
        this.bindings = bindings;
//        System.out.println(identifier + " = " + bindings.get(identifier));
    }
    @Override
    public void execute(Map<String, Long> bindings) {
        long value = expression.eval(bindings);
        bindings.put(identifier, value);
    }
}
