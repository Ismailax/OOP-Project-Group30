package PlanParser.Evaluable;

import java.util.Map;

public class Identifier implements Evaluable{
    private String name;

    public Identifier(String name, Map<String, Long> bindings) {
        this.name = name;
        this.eval(bindings);
    }

    @Override
    public long eval(Map<String, Long> bindings) {
        if(bindings.get(name) != null) return bindings.get(name);
        return 0;
    }
}
