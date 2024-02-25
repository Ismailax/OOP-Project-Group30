package PlanParser.Evaluable.SpecialVariables;

import Game.Gameplay;
import PlanParser.Evaluable.Evaluable;

import java.util.Map;

public class Budget implements Evaluable {
    Gameplay game;
    public Budget(Gameplay game){
        this.game = game;
    }
    @Override
    public long eval(Map<String, Long> bindings) {
        return game.getCurrentPlayer().getBudget();
    }
}
