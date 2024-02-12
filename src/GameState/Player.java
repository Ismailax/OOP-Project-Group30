package GameState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import PlanParser.Parser;
import PlanParser.Tokenizer;

public class Player {
    private final String name;
    private Map<String, Long> bindings;
    private String plan;
    private List<Region> ownedRegion;
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

    public long getBindings(String variable) {
        if(bindings.get(variable) != null) return bindings.get(variable);
        return 0;
    }

    public void addRegion(Region region){
        ownedRegion.add(region);
    }

    public void removeRegion(Region region){
        ownedRegion.remove(region);
    }


}
