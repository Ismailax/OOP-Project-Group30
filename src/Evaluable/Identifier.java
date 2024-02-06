package Evaluable;
import java.util.Map;

public class Identifier implements Evaluable{
    String name;

    public Identifier(String name) {
        this.name = name;
    }

    @Override
    public int eval(Map<String, Integer> bindings) {
        return bindings.getOrDefault(name, 0);
    }
}
