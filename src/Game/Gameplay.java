package Game;

import Configuration.Config;
import GameState.Player;
import GameState.Region;
import GameState.Territory;
import PlanParser.Executable.Executable;
import PlanParser.Parser;
import PlanParser.Tokenizer;

import java.util.Random;

public class Gameplay {
    private Config config;
    private Territory territory;
    private Player player1, player2, currentPlayer, anotherPlayer ,winner;
    private long round;
    private String p1Plan, p2Plan;
    private Executable p1Statements, p2Statements;
    private Region cityCrew;

    public Gameplay(Player player1, Player player2){
        config = new Config(); // read the configuration file
        territory = new Territory(config.getTotalRows(),config.getTotalCols());
        this.player1 = player1;
        this.player2 = player2;
        round = 1;
        currentPlayer = this.player1;
        anotherPlayer = this .player2;

        // set initial budget
        this.player1.setBudget(config.getInitBudget());
        this.player2.setBudget(config.getInitBudget());

        // random starting region , city center &  set initial deposit
        Random rand = new Random();
        long p1Row = rand.nextLong(1,config.getTotalRows());
        long p1Col = rand.nextLong(1,config.getTotalCols());
        territory.getRegion(p1Row,p1Col).setOwner(this.player1);
        this.player1.addRegion(territory.getRegion(p1Row,p1Col));
        this.player1.setCityCenter(territory.getRegion(p1Row,p1Col));
        territory.getRegion(p1Row,p1Col).setDeposit(config.getInitCCDep());

        long p2Row;
        long p2Col;
        do{
            p2Row = rand.nextLong(1,config.getTotalRows());
            p2Col = rand.nextLong(1,config.getTotalCols());
        }while(p2Row == p1Row && p2Col == p1Col);
        territory.getRegion(p1Row,p1Col).setOwner(this.player2);
        this.player2.addRegion(territory.getRegion(p1Row,p1Col));
        this.player2.setCityCenter(territory.getRegion(p2Row,p2Col));
        territory.getRegion(p1Row,p1Col).setDeposit(config.getInitCCDep());

//        long initPlanMin = config.getInitPlanMin();
//        long initPlanSec = config.getInitPlanSec();
    }

    public Executable eval(){
        String plan;
        if(currentPlayer.equals(player1)){
            plan = p1Plan;
        }
        else{
            plan = p2Plan;
        }
        Parser p = new Parser(new Tokenizer(plan));
        return p.parse(currentPlayer.getAllBindings());
    }

    public void setStatements(Executable statements){
        if(currentPlayer.equals(player1)){
            p1Statements = statements;
        }
        else{
            p2Statements = statements;
        }
    }

    public void execStatement(){
        if(currentPlayer.equals(player1)){
            p1Statements.execute(player1.getAllBindings());
        }
        else{
            p2Statements.execute(player2.getAllBindings());
        }
    }

    public void collectInterest(){
        long interest = 0;
        for(Region r : currentPlayer.getOwnedRegion()){
            double rate =  config.getInterestPct() * Math.log10(r.getDeposit()) * Math.log(round);
            interest += (long) (r.getDeposit() * rate / 100);
        }
        currentPlayer.setBudget(currentPlayer.getBudget()+interest);
        System.out.println("total interest: " + interest);
    }

    public void move(long direction){
        if(currentPlayer.getBudget() < 1){
            System.out.println("Not enough budget to move.");
            return;
        }
        currentPlayer.setBudget(currentPlayer.getBudget()-1);
        Region des;
        if(direction == 1){
            des = territory.getUp(cityCrew.getRow(), cityCrew.getCol());
            if(des == null){
                System.out.println("Cannot move to an out-of-territory region.");
            }else if(des.getOwner().equals(anotherPlayer)){
                System.out.println("Cannot move to an another player's region.");
            }else{
                System.out.println("move up: (" + cityCrew.getRow() + "," + cityCrew.getCol() + ") -> ("+ des.getRow() + "," + des.getCol() + ")");
                cityCrew = des;
            }
        } else if (direction == 2) {
            des = territory.getUpRight(cityCrew.getRow(), cityCrew.getCol());
            if(des == null){
                System.out.println("Cannot move to an out-of-territory region.");
            }else if(des.getOwner().equals(anotherPlayer)){
                System.out.println("Cannot move to an another player's region.");
            }else{
                System.out.println("move upright: (" + cityCrew.getRow() + "," + cityCrew.getCol() + ") -> ("+ des.getRow() + "," + des.getCol() + ")");
                cityCrew = des;
            }
        } else if (direction == 3){
            des = territory.getDownRight(cityCrew.getRow(), cityCrew.getCol());
            if(des == null){
                System.out.println("Cannot move to an out-of-territory region.");
            }else if(des.getOwner().equals(anotherPlayer)){
                System.out.println("Cannot move to an another player's region.");
            }else{
                System.out.println("move downright: (" + cityCrew.getRow() + "," + cityCrew.getCol() + ") -> ("+ des.getRow() + "," + des.getCol() + ")");
                cityCrew = des;
            }
        } else if (direction == 4){
            des = territory.getDown(cityCrew.getRow(), cityCrew.getCol());
            if(des == null){
                System.out.println("Cannot move to an out-of-territory region.");
            }else if(des.getOwner().equals(anotherPlayer)){
                System.out.println("Cannot move to an another player's region.");
            }else{
                System.out.println("move down: (" + cityCrew.getRow() + "," + cityCrew.getCol() + ") -> ("+ des.getRow() + "," + des.getCol() + ")");
                cityCrew = des;
            }
        } else if (direction == 5){
            des = territory.getDownLeft(cityCrew.getRow(), cityCrew.getCol());
            if(des == null){
                System.out.println("Cannot move to an out-of-territory region.");
            }else if(des.getOwner().equals(anotherPlayer)){
                System.out.println("Cannot move to an another player's region.");
            }else{
                System.out.println("move downleft: (" + cityCrew.getRow() + "," + cityCrew.getCol() + ") -> ("+ des.getRow() + "," + des.getCol() + ")");
                cityCrew = des;
            }
        }else {
            des = territory.getUpLeft(cityCrew.getRow(), cityCrew.getCol()); if(des == null){
                System.out.println("Cannot move to an out-of-territory region.");
            }
            if(des == null){
                System.out.println("Cannot move to an out-of-territory region.");
            }else if(des.getOwner().equals(anotherPlayer)){
                System.out.println("Cannot move to an another player's region.");
            }else{
                System.out.println("move upleft: (" + cityCrew.getRow() + "," + cityCrew.getCol() + ") -> ("+ des.getRow() + "," + des.getCol() + ")");
                cityCrew = des;
            }
        }
    }

    public void invest(long amount){
        if (currentPlayer.getBudget() < amount + 1){
            System.out.println("Not enough budget to invest.");
            if(currentPlayer.getBudget() < 1) return;
            currentPlayer.setBudget(currentPlayer.getBudget()-1);
            return;
        }
        if (territory.getRegion(cityCrew.getRow(), cityCrew.getCol()).getDeposit() + amount > config.getMaxDep()){
            currentPlayer.setBudget(currentPlayer.getBudget()- (config.getMaxDep()-territory.getRegion(cityCrew.getRow(), cityCrew.getCol()).getDeposit()+1));
            territory.getRegion(cityCrew.getRow(), cityCrew.getCol()).setDeposit(config.getMaxDep());
            System.out.println("Invest at (" + cityCrew.getRow() + "," + cityCrew.getCol() + "): " + (config.getMaxDep() - territory.getRegion(cityCrew.getRow(), cityCrew.getCol()).getDeposit()));
        }else{
            currentPlayer.setBudget(currentPlayer.getBudget()-(amount + 1));
            territory.getRegion(cityCrew.getRow(), cityCrew.getCol()).setDeposit(territory.getRegion(cityCrew.getRow(), cityCrew.getCol()).getDeposit()+amount);
            System.out.println("Invest at (" + cityCrew.getRow() + "," + cityCrew.getCol() + "): " + amount);
        }
    }

    public void collect(long amount){
        if (currentPlayer.getBudget() < 1){
            System.out.println("Not enough budget to invest.");
        }else if(amount > territory.getRegion(cityCrew.getRow(), cityCrew.getCol()).getDeposit()){
            currentPlayer.setBudget(currentPlayer.getBudget()-1);
            System.out.println("The collection amount exceeds the deposit in the current region.");
        }else {
            territory.getRegion(cityCrew.getRow(), cityCrew.getCol()).setDeposit(territory.getRegion(cityCrew.getRow(), cityCrew.getCol()).getDeposit()-amount);
            if(territory.getRegion(cityCrew.getRow(),cityCrew.getCol()).getDeposit() == 0){
                territory.getRegion(cityCrew.getRow(),cityCrew.getCol()).setOwner(null);
            }
            currentPlayer.setBudget(currentPlayer.getBudget()+amount-1);
        }
    }
}
