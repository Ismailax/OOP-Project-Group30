import java.util.HashMap;
import java.util.Map;

public class Player {
    private final String name;
    Map<String, Integer> bindings;
    private String plan;
    public Player(String name){
        this.name = name;
        bindings = new HashMap<>();
    }
    public void eval(){
        Parser p = new Parser(new Tokenizer(plan));
        p.parse(bindings);
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public int getBindings(String variable) {
        return bindings.getOrDefault(variable, 0);
    }
}
