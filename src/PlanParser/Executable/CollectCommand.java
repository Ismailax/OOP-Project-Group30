package PlanParser.Executable;

import Game.Gameplay;
import GameState.Player;
import PlanParser.Evaluable.*;

import java.util.Map;

public class CollectCommand implements Executable{
    Gameplay game;
    private Evaluable amount;

    public CollectCommand(Evaluable amount, Map<String, Long> bindings, Gameplay game) {
        this.game = game;
        this.amount = amount;
        long value = amount.eval(bindings);
        System.out.println("Parse collect: " + value);
    }

    @Override
    public void execute(Map<String, Long> bindings) {
        long value = amount.eval(bindings);
        game.collect(value);
    }
}
