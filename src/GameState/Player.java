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
    private long turns;
    private long base;

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

    public long collectInterest(){
        long interest = 0;
        for(Region r : ownedRegion){
            double rate =  base * Math.log10(r.getDeposit()) * Math.log(turns);
            interest += (long) (r.getDeposit() * rate / 100);
        }
        return interest;
    }

    public void addRegion(Region region){
        ownedRegion.add(region);
    }

    public void removeRegion(Region region){
        ownedRegion.remove(region);
    }


}
