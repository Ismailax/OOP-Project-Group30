import Configuration.Config;
import Game.Gameplay;
import GameState.Player;
import GameState.Territory;
import PlanParser.Executable.Executable;
import PlanParser.Parser;
import PlanParser.Tokenizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Player p1 = new Player("A");
        Player p2 = new Player("B");

        Gameplay game = new Gameplay(p1,p2);

        game.setP1Plan("move up shoot upleft 100");
        game.setP1Statements();

        game.setP2Plan("move down shoot downright 200");
        game.setP2Statements();

        game.turnStart();
    }
}
