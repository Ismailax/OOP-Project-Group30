package PlanParser.Executable;

import Game.Gameplay;
import GameState.Player;

import java.util.Map;

public class Relocate implements Executable{
    Gameplay game;
    public Relocate(Gameplay game){
        this.game = game;
        System.out.println("Parse relocate");
    }
    @Override
    public void execute(Map<String, Long> bindings) {
        System.out.println("Execute relocate");
//        game.relocate();
    }
}
