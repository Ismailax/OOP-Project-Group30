package PlanParser.Executable;

import Game.Gameplay;
import GameState.Player;

import java.util.Map;

public class MoveCommand implements Executable {
    Gameplay game;
    private String direction;

    public MoveCommand(Gameplay game,String direction) {
        this.game = game;
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
        game.move(dir);
    }
}

