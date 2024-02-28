package GameState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player {
    private final String name;
    private Map<String, Long> bindings;
    private List<Region> ownedRegion;
    private long budget;
    private Region cityCenter;


    public Player(String name){
        this.name = name;
        bindings = new HashMap<>();
        ownedRegion = new ArrayList<>();
    }

    public String getName(){
        return name;
    }

    public long getBindings(String variable) {
        if(bindings.get(variable) != null) return bindings.get(variable);
        return 0;
    }

    public Map<String, Long> getAllBindings(){
        return this.bindings;
    }

    public void addRegion(Region region){
        ownedRegion.add(region);
        region.setOwner(this);
    }

    public void removeRegion(Region region){
        ownedRegion.remove(region);
        region.setOwner(null);
    }

    public long getBudget() {
        return budget;
    }

    public void setBudget(long money){
        budget = money;
    }

    public Region getCityCenter(){
        return cityCenter;
    }

    public void setCityCenter(Region cc){
        cityCenter = cc;
    }

    public List<Region> getOwnedRegion() {
        return ownedRegion;
    }
}



