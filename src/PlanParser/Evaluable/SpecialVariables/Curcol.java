package PlanParser.Evaluable.SpecialVariables;

import Game.Gameplay;
import PlanParser.Evaluable.Evaluable;

import java.util.Map;

public class Curcol implements Evaluable {
    Gameplay game;
    public Curcol(Gameplay game){
        this.game = game;
    }
    @Override
    public long eval(Map<String, Long> bindings) {
        return game.getCityCrew().getCol();
    }
}
