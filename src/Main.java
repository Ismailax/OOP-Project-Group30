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

        game.setP1Plan(readFile("src/p1Plan.txt"));
        game.setP1Statements();

        game.setP2Plan(readFile("src/p2Plan.txt"));
        game.setP2Statements();

        do{
          game.turnStart();
        }while(!Gameplay.getWinner());

    }

    public static String readFile(String filename){
        StringBuilder s = new StringBuilder();
        try(Scanner reader = new Scanner(new File(filename))){
            if(!reader.hasNextLine()){
                System.out.println("File is empty");
            }
            while(reader.hasNextLine()) {
                String line = reader.nextLine();
                if (line.isEmpty()) continue;
                s.append(line);
            }
        }catch(FileNotFoundException e){
            System.out.println("File not found");
        }
        return s.toString();
    }
}


//        for (int i = 0; i < 100; i++){
//            game.turnStart();
//            if(game.checkWinner() != null) break;
//            System.out.println();
//        }