package PlanParser.Executable;
import Game.Gameplay;
import GameState.Player;
import PlanParser.Evaluable.*;

import java.util.Map;

public class InvestCommand implements Executable{
    Gameplay game;
    private Evaluable amount;
    public InvestCommand(Evaluable amount, Map<String, Long> bindings, Gameplay game){
        this.game = game;
        this.amount = amount;
        long value = amount.eval(bindings);
        System.out.println("Parse invest: " + value);
    }
    @Override
    public void execute(Map<String, Long> bindings) {
        long value = amount.eval(bindings);
        game.invest(value);
    }
}
