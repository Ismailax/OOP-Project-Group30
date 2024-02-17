import Configuration.Config;
import GameState.Player;
import PlanParser.Executable.Executable;
import PlanParser.Parser;
import PlanParser.Tokenizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) {
        String constructionPlan_A = "invest 2";
        Player p1 = new Player("A");
        p1.setPlan(constructionPlan_A);
        p1.setStatements(p1.eval());

        p1.executeStmt();
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