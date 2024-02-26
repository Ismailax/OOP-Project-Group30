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


//        Territory t = new Territory(10,10);
//
//        String constructionPlan_A = "x = 2 while(x) { relocate x = x -1 }";
//        Player p1 = new Player("A");
//        p1.setTerritory(t);
//        p1.setCurrentRegion(3,4);
//        p1.setPlan(constructionPlan_A);
//        p1.setStatements(p1.eval());
//        p1.executeStmt();
//        System.out.println(p1.getBindings("x"));

//        System.out.println(p1.getCurrow() + " " + p1.getCurcol());

//        Random rand = new Random();
//        System.out.println(rand.nextLong(1,10));

//        Player p1 = new Player("A");
//        Player p2 = new Player("B");
//        Gameplay game = new Gameplay(p1,p2);
//        game.move(1);
//        long number = 1000000;
//        long length = (long) (Math.log10(number) + 1);
//        System.out.println(length);
        long dis = 5;
        String s = dis + Long.toString(6);
        long r = Long.parseLong(s);
        System.out.println(r);

//        System.out.println(p1.getBindings("x"));


    }
}

//        System.out.println(p1.getBindings("x"));

//        for(int i = 0; i < 5; i++){
//            p1.executeStmt();
//        }

//        Tokenizer tkz = new Tokenizer(constructionPlan_A);
//        while (tkz.hasNextToken()) {
//            String token = tkz.consume();
//            System.out.println("Token: " + token);
//        }

//        Config config1 = new Config();
//        System.out.println(config1.getTotalRows());
//        System.out.println(config1.getTotalCols());
//        System.out.println(config1.getInitPlanMin());
//        System.out.println(config1.getInitPlanSec());
//        System.out.println(config1.getInitBudget());
//        System.out.println(config1.getInitCCDep());
//        System.out.println(config1.getPlanRevMin());
//        System.out.println(config1.getPlanRevSec());
//        System.out.println(config1.getRevCost());
//        System.out.println(config1.getMaxDep());
//        System.out.println(config1.getInterestPct());