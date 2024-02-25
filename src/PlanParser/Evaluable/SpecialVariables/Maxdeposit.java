package PlanParser.Evaluable.SpecialVariables;

import Game.Gameplay;
import PlanParser.Evaluable.Evaluable;

import java.util.Map;

public class Maxdeposit implements Evaluable {
    Gameplay game;
    public Maxdeposit(Gameplay game){
        this.game = game;
    }
    @Override
    public long eval(Map<String, Long> bindings) {
        return game.getConfig().getMaxDep();
    }
}
