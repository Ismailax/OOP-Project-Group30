package Evaluable;

import Error.*;
import java.util.Map;

public class Identifier implements Evaluable{
    private String name;

    public Identifier(String name, Map<String, Integer> bindings) {
        this.name = name;
        this.eval(bindings);
    }

    @Override
    public int eval(Map<String, Integer> bindings) {
        return bindings.getOrDefault(name, 0);
    }
}
