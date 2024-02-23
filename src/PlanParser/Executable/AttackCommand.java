package PlanParser.Executable;
import GameState.Player;
import PlanParser.Evaluable.*;
import java.util.Map;

public class AttackCommand implements Executable{
    Player player;
    private String direction;
    private Evaluable expenditure;

    public AttackCommand(String direction, Evaluable expenditure, Map<String, Long> bindings, Player player) {
        this.direction = direction;
        this.expenditure = expenditure;
        this.player = player;
        long value = expenditure.eval(bindings);
        System.out.println("Parse shoot " + direction + " " + value);
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
        player.shoot(dir,expenditure.eval(bindings));
    }
}
