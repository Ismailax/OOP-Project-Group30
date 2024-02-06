package Executable;

import Evaluable.Evaluable;

import java.util.Map;

public class AssignmentStatement implements Executable{
    private String identifier;
    private Evaluable expression;
    public AssignmentStatement(String identifier, Evaluable expression){
        this.identifier = identifier;
        this.expression = expression;
    }
    @Override
    public void execute(Map<String, Integer> bindings) {

    }
}
