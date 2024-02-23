package PlanParser.Executable;

import GameState.Player;

import java.util.Map;

public class MoveCommand implements Executable {
    Player player;
    private String direction;

    public MoveCommand(Player player,String direction) {
        this.player = player;
        this.direction = direction;
        System.out.println("Parse move " + direction);
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
        player.move(dir);
//        System.out.println("Execute move " + direction);
    }
}

