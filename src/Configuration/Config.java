package Configuration;

import PlanParser.Parser;
import PlanParser.Tokenizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Config {
    private long totalRows;
    private long totalCols;
    private long initPlanMin;
    private long initPlanSec;
    private long initBudget;
    private long initCCDep;
    private long planRevMin;
    private long planRevSec;
    private long revCost;
    private long maxDep;
    private long interestPct;

    public Config(){
        String text = readFile("src/Configuration/Configuration.txt");
        Map<String, Long> configBindings = parseConfigFile(text);
        this.totalRows = configBindings.get("totalRows");
        this.totalCols = configBindings.get("totalCols");
        this.initPlanMin = configBindings.get("initPlanMin");
        this.initPlanSec = configBindings.get("initPlanSec");
        this.initBudget = configBindings.get("initBudget");
        this.initCCDep = configBindings.get("initCCDep");
        this.planRevMin = configBindings.get("planRevMin");
        this.planRevSec = configBindings.get("planRevSec");
        this.revCost = configBindings.get("revCost");
        this.maxDep = configBindings.get("maxDep");
        this.interestPct = configBindings.get("interestPct");
    }

    private String readFile(String filename){
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

    private Map<String, Long> parseConfigFile(String text){
        Map<String, Long> configBindings = new HashMap<>();
        Parser p = new Parser(new Tokenizer(text));
        p.parse(configBindings).execute(configBindings);
        return configBindings;
    }


    public long getTotalRows(){
        return this.totalRows;
    }

    public long getTotalCols() {
        return totalCols;
    }

    public long getInitPlanMin() {
        return initPlanMin;
    }

    public long getInitPlanSec() {
        return initPlanSec;
    }

    public long getInitBudget() {
        return initBudget;
    }

    public long getInitCCDep() {
        return initCCDep;
    }

    public long getPlanRevMin() {
        return planRevMin;
    }

    public long getPlanRevSec() {
        return planRevSec;
    }

    public long getRevCost() {
        return revCost;
    }

    public long getMaxDep() {
        return maxDep;
    }

    public long getInterestPct() {
        return interestPct;
    }
}
