package PlanParser.Evaluable;

import Game.Gameplay;

import java.util.Map;

public class Opponent implements Evaluable {
    Gameplay game;
    public Opponent(Gameplay game){
        this.game = game;
    }
    @Override
    public long eval(Map<String, Long> bindings) {
        return game.opponent();
    }
}
