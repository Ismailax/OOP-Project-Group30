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

public class Main {
    public static void main(String[] args) {
        System.out.println("***************************************************************************");
        System.out.println("                                 UPBEAT");
        System.out.println("***************************************************************************");
        System.out.println("                      Press \"ENTER\" to continue...");

        Scanner n = new Scanner(System.in);
        n.nextLine();

        String[] playersName = setPlayersName();
        Player p1 = new Player(playersName[0]);
        Player p2 = new Player(playersName[1]);

        Config config = new Config();
        long[] configValues = setConfigValues();
        config.setTotalRows(configValues[0]);
        config.setTotalCols(configValues[1]);
        config.setMin(configValues[2]);
        config.setSec(configValues[3]);

        Gameplay game = new Gameplay(p1,p2,config);

        do{
            game.play();
        }while(!Gameplay.getWinner());

        n.close();

//        game.setP1Plan(readFile("src/p1Plan.txt"));
//        game.setP1Statements();
//
//        game.setP2Plan(readFile("src/p2Plan.txt"));
//        game.setP2Statements();

//        do{
//          game.gameTest();
//        }while(!Gameplay.getWinner());

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

    public static String[] setPlayersName(){
        System.out.println("***************************************************************************");
        String[] playersName = new String[2];
        Scanner n = new Scanner(System.in);
        do{
            System.out.print("Fill player 1's name: ");
            playersName[0] = n.nextLine();
            if (playersName[0].isEmpty()){
                System.out.println("Player 1's name cannot be empty.");
                System.out.println();
            }
        }while (playersName[0].isEmpty());
        do{
            System.out.print("Fill player 2's name: ");
            playersName[1] = n.nextLine();
            if (playersName[1].isEmpty()){
                System.out.println("Player 2's name cannot be empty.");
                System.out.println();
            }
        }while (playersName[1].isEmpty());
        return playersName;
    }

    public static long[] setConfigValues(){
        System.out.println("***************************************************************************");
        System.out.println("                          Game Configuration");
        long[] configValue = new long[4];
        Scanner n = new Scanner(System.in);
        do{
            System.out.print("Rows: ");
            configValue[0] = n.nextLong();
            if (configValue[0] < 5 || configValue[0] > 50){
                System.out.println("Rows must be 5-50.");
                System.out.println();
            }
        }while (configValue[0] < 5 || configValue[0] > 50);
        do{
            System.out.print("Columns: ");
            configValue[1] = n.nextLong();
            if (configValue[1] < 5 || configValue[1] > 50){
                System.out.println("Columns must be 5-50.");
                System.out.println();
            }
        }while (configValue[1] < 5 || configValue[1] > 50);
        do{
            System.out.print("Plan initialization & revision minutes: ");
            configValue[2] = n.nextLong();
            if (configValue[2] < 1 || configValue[2] > 10){
                System.out.println("Plan initialization & revision minutes must be 1-10.");
                System.out.println();
            }
        }while (configValue[2] < 1 || configValue[2] > 10);
        do{
            System.out.print("Plan initialization & revision seconds: ");
            configValue[3] = n.nextLong();
            if (configValue[3] < 0 || configValue[3] > 59){
                System.out.println("Plan initialization & revision seconds must be 0-59.");
                System.out.println();
            }
        }while (configValue[3] < 0 || configValue[3] > 59);
        System.out.println("***************************************************************************");
        return configValue;
    }
}