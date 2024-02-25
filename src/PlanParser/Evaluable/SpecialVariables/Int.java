package PlanParser.Evaluable.SpecialVariables;

import Game.Gameplay;
import PlanParser.Evaluable.Evaluable;

import java.util.Map;

public class Int implements Evaluable {
    Gameplay game;
    public Int(Gameplay game){
        this.game = game;
    }
    @Override
    public long eval(Map<String, Long> bindings) {
        return game.getConfig().getInterestPct();
    }
}
