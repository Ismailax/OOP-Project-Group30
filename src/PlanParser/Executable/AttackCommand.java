package PlanParser.Executable;
import Game.Gameplay;
import GameState.Player;
import PlanParser.Evaluable.*;
import java.util.Map;

public class AttackCommand implements Executable{
    Gameplay game;
    private String direction;
    private Evaluable expenditure;

    public AttackCommand(String direction, Evaluable expenditure, Map<String, Long> bindings, Gameplay game) {
        this.direction = direction;
        this.expenditure = expenditure;
        this.game = game;
        long value = expenditure.eval(bindings);
    }

    @Override
    public void execute(Map<String, Long> bindings) {
        long dir = switch (direction) {
            case "up" -> 1;
            case "upright" -> 2;
            case "downright" -> 3;
            case "down" -> 4;
            case "downleft" -> 5;
            default -> 6;
        };
        game.shoot(dir,expenditure.eval(bindings));
    }
}
