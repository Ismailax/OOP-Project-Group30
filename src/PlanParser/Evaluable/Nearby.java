package PlanParser.Evaluable;

import Game.Gameplay;

import java.util.Map;

public class Nearby implements Evaluable {
    Gameplay game;
    private String direction;

    public Nearby(Gameplay game, String direction) {
        this.game = game;
        this.direction = direction;
    }

    @Override
    public long eval(Map<String, Long> bindings) {
        long dir;
        if(direction.equals("up")) dir = 1;
        else if (direction.equals("upright")) dir = 2;
        else if (direction.equals("downright")) dir = 3;
        else if (direction.equals("down")) dir = 4;
        else if ((direction.equals("downleft"))) dir = 5;
        else dir = 6;

        return game.nearby(dir);
    }
}
