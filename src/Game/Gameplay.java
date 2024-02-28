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
    private final Config config;
    private final Territory territory;
    private final Player player1, player2;
    private Player currentPlayer;
    private Player anotherPlayer;
    private static Player winner;
    private long round;
    private String p1Plan, p2Plan;
    private Executable p1Statements, p2Statements;
    private Region cityCrew;

    public Gameplay(Player player1, Player player2){
        config = new Config(); // read the configuration file

        territory = new Territory(config.getTotalRows(),config.getTotalCols());
        this.player1 = player1;
        this.player2 = player2;
        currentPlayer = this.player1;
        anotherPlayer = this.player2;

        // set initial budget
        this.player1.setBudget(config.getInitBudget());
        this.player2.setBudget(config.getInitBudget());

        // random starting region , city center &  set initial deposit
        Random rand = new Random();
        long p1Row = rand.nextLong(1,config.getTotalRows());
        long p1Col = rand.nextLong(1,config.getTotalCols());
        Region p1CC = territory.getRegion(p1Row,p1Col);
        p1CC.setOwner(this.player1);
        this.player1.addRegion(p1CC);
        this.player1.setCityCenter(p1CC);
        p1CC.setDeposit(config.getInitCCDep());
        cityCrew =  territory.getRegion(p1Row,p1Col);

        long p2Row;
        long p2Col;
        do{
            p2Row = rand.nextLong(1,config.getTotalRows());
            p2Col = rand.nextLong(1,config.getTotalCols());
        }while(p2Row == p1Row && p2Col == p1Col);
        Region p2CC = territory.getRegion(p2Row,p2Col);
        p2CC.setOwner(this.player2);
        this.player2.addRegion(p2CC);
        this.player2.setCityCenter(p2CC);
        p2CC.setDeposit(config.getInitCCDep());

        long initPlanMin = config.getInitPlanMin();
        long initPlanSec = config.getInitPlanSec();
    }

    public Config getConfig(){
        return this.config;
    }

    public Executable eval(){
        String plan;
        if(currentPlayer == player1){
            plan = p1Plan;
        }
        else{
            plan = p2Plan;
        }
        Parser p = new Parser(new Tokenizer(plan));
        p.setGame(this);
        return p.parse(currentPlayer.getAllBindings());
    }

    public void setP1Plan(String plan){
        p1Plan = plan;
    }

    public void setP2Plan(String plan){
        p2Plan = plan;
    }

    public void setP1Statements(){
        Parser p = new Parser(new Tokenizer(p1Plan));
        p.setGame(this);
        p1Statements = p.parse(player1.getAllBindings());
    }

    public void setP2Statements(){
        Parser p = new Parser(new Tokenizer(p2Plan));
        p.setGame(this);
        p2Statements = p.parse(player2.getAllBindings());
    }

    public void execStatement(){
        if(currentPlayer == player1){
            p1Statements.execute(player1.getAllBindings());
        }
        else{
            p2Statements.execute(player2.getAllBindings());
        }
    }

    public Player getCurrentPlayer(){
        return this.currentPlayer;
    }

    public Region getCityCrew(){
        return this.cityCrew;
    }
    public boolean isMyRegion(Region region){ // set ค่า yourTurn ด้วย
        if(region == null) return false;
        return region.getOwner() == currentPlayer;
    }

    public boolean otherRegion(Region region){
        return region.getOwner() == anotherPlayer;
    }

    public boolean isMyRegionAroundHere(){
        return isMyRegion(territory.getUp(cityCrew.getRow(), cityCrew.getCol())) || isMyRegion(territory.getUpRight(cityCrew.getRow(), cityCrew.getCol())) || isMyRegion(territory.getDownRight(cityCrew.getRow(), cityCrew.getCol())) || isMyRegion(territory.getDownLeft(cityCrew.getRow(), cityCrew.getCol())) || isMyRegion(territory.getUpLeft(cityCrew.getRow(), cityCrew.getCol())) || isMyRegion(territory.getDown(cityCrew.getRow(), cityCrew.getCol()));
    }


    public void collectInterest(){
        long interest = 0;
        for(Region r : currentPlayer.getOwnedRegion()){
            double rate =  config.getInterestPct() * Math.log10(r.getDeposit()) * Math.log(round);
            interest += (long) (r.getDeposit() * rate / 100);
        }
        currentPlayer.setBudget(currentPlayer.getBudget()+interest);
        System.out.println(currentPlayer.getName() + " receives total interest: $" + interest + ".");
    }

    public void move(long direction){
        if(currentPlayer.getBudget() < 1){
            System.out.println(currentPlayer.getName() + " doesn't have enough budget to move.");
            checkWinner();
            return;
        }
        currentPlayer.setBudget(currentPlayer.getBudget()-1);
        Region des;
        if(direction == 1){
            des = territory.getUp(cityCrew.getRow(), cityCrew.getCol());
            if(des == null){
                System.out.println(currentPlayer.getName() + "'s city crew cannot move to an out-of-territory region.");
            }else if(des.getOwner() == anotherPlayer){
                System.out.println(currentPlayer.getName() + "'s city crew cannot move to an opponent's region.");
            }else{
                System.out.println(currentPlayer.getName() + "'s city crew move up: (" + cityCrew.getRow() + "," + cityCrew.getCol() + ") -> ("+ des.getRow() + "," + des.getCol() + ")");
                cityCrew = des;
            }
        } else if (direction == 2) {
            des = territory.getUpRight(cityCrew.getRow(), cityCrew.getCol());
            if(des == null){
                System.out.println(currentPlayer.getName() + "'s city crew cannot move to an out-of-territory region.");
            }else if(des.getOwner() == anotherPlayer){
                System.out.println(currentPlayer.getName() + "'s city crew cannot move to an opponent's region.");
            }else{
                System.out.println(currentPlayer.getName() + "'s city crew move upright: (" + cityCrew.getRow() + "," + cityCrew.getCol() + ") -> ("+ des.getRow() + "," + des.getCol() + ")");
                cityCrew = des;
            }
        } else if (direction == 3){
            des = territory.getDownRight(cityCrew.getRow(), cityCrew.getCol());
            if(des == null){
                System.out.println(currentPlayer.getName() + "'s city crew cannot move to an out-of-territory region.");
            }else if(des.getOwner() == anotherPlayer){
                System.out.println(currentPlayer.getName() + "'s city crew cannot move to an opponent's region.");
            }else{
                System.out.println(currentPlayer.getName() + "'s city crew move downright: (" + cityCrew.getRow() + "," + cityCrew.getCol() + ") -> ("+ des.getRow() + "," + des.getCol() + ")");
                cityCrew = des;
            }
        } else if (direction == 4){
            des = territory.getDown(cityCrew.getRow(), cityCrew.getCol());
            if(des == null){
                System.out.println(currentPlayer.getName() + "'s city crew cannot move to an out-of-territory region.");
            }else if(des.getOwner() == anotherPlayer){
                System.out.println(currentPlayer.getName() + "'s city crew cannot move to an opponent's region.");
            }else{
                System.out.println(currentPlayer.getName() + "'s city crew move down: (" + cityCrew.getRow() + "," + cityCrew.getCol() + ") -> ("+ des.getRow() + "," + des.getCol() + ")");
                cityCrew = des;
            }
        } else if (direction == 5){
            des = territory.getDownLeft(cityCrew.getRow(), cityCrew.getCol());
            if(des == null){
                System.out.println(currentPlayer.getName() + "'s city crew cannot move to an out-of-territory region.");
            }else if(des.getOwner() == anotherPlayer){
                System.out.println(currentPlayer.getName() + "'s city crew cannot move to an opponent's region.");
            }else{
                System.out.println(currentPlayer.getName() + "'s city crew move downleft: (" + cityCrew.getRow() + "," + cityCrew.getCol() + ") -> ("+ des.getRow() + "," + des.getCol() + ")");
                cityCrew = des;
            }
        }else {
            des = territory.getUpLeft(cityCrew.getRow(), cityCrew.getCol());
            if(des == null){
                System.out.println(currentPlayer.getName() + "'s city crew cannot move to an out-of-territory region.");
            }else if(des.getOwner() == anotherPlayer){
                System.out.println(currentPlayer.getName() + "'s city crew cannot move to an opponent's region.");
            }else{
                System.out.println(currentPlayer.getName() + "'s city crew move upleft: (" + cityCrew.getRow() + "," + cityCrew.getCol() + ") -> ("+ des.getRow() + "," + des.getCol() + ")");
                cityCrew = des;
            }
        }
    }

    public void invest(long amount){
        if (currentPlayer.getBudget() < amount + 1) {
            System.out.println(currentPlayer.getName() + " doesn't have enough budget to invest.");
            if (currentPlayer.getBudget() < 1){
                checkWinner();
                return;
            }
            currentPlayer.setBudget(currentPlayer.getBudget() - 1);
            return;
        }
        if(!isMyRegion(cityCrew) && isMyRegionAroundHere() && !otherRegion(cityCrew)) {
            if (territory.getRegion(cityCrew.getRow(), cityCrew.getCol()).getDeposit() + amount > config.getMaxDep()) {
                currentPlayer.setBudget(currentPlayer.getBudget() - config.getMaxDep() - territory.getRegion(cityCrew.getRow(), cityCrew.getCol()).getDeposit() - 1);
                territory.getRegion(cityCrew.getRow(), cityCrew.getCol()).setDeposit(config.getMaxDep());
                System.out.println(currentPlayer.getName() + " invests at (" + cityCrew.getRow() + "," + cityCrew.getCol() + "): " + (config.getMaxDep() - territory.getRegion(cityCrew.getRow(), cityCrew.getCol()).getDeposit()));
            } else {
                currentPlayer.setBudget(currentPlayer.getBudget() - amount - 1);
                territory.getRegion(cityCrew.getRow(), cityCrew.getCol()).setDeposit(territory.getRegion(cityCrew.getRow(), cityCrew.getCol()).getDeposit() + amount);
                System.out.println(currentPlayer.getName() + " invests at (" + cityCrew.getRow() + "," + cityCrew.getCol() + "): " + amount);
            }
            currentPlayer.addRegion(cityCrew);
            cityCrew.setOwner(currentPlayer);
        } else if(isMyRegion(cityCrew)) {
            if (territory.getRegion(cityCrew.getRow(), cityCrew.getCol()).getDeposit() + amount > config.getMaxDep()) {
                currentPlayer.setBudget(currentPlayer.getBudget() - config.getMaxDep() - territory.getRegion(cityCrew.getRow(), cityCrew.getCol()).getDeposit() - 1);
                territory.getRegion(cityCrew.getRow(), cityCrew.getCol()).setDeposit(config.getMaxDep());
                System.out.println(currentPlayer.getName() + " invests at (" + cityCrew.getRow() + "," + cityCrew.getCol() + "): " + (config.getMaxDep() - territory.getRegion(cityCrew.getRow(), cityCrew.getCol()).getDeposit()));
            } else {
                currentPlayer.setBudget(currentPlayer.getBudget() - amount - 1);
                territory.getRegion(cityCrew.getRow(), cityCrew.getCol()).setDeposit(territory.getRegion(cityCrew.getRow(), cityCrew.getCol()).getDeposit() + amount);
                System.out.println(currentPlayer.getName() + " invests at (" + cityCrew.getRow() + "," + cityCrew.getCol() + "): " + amount);
            }
        }
    }

    public void collect(long amount){
        if (currentPlayer.getBudget() < 1){
            System.out.println(currentPlayer.getName() + " doesn't have enough budget to collect.");
            checkWinner();
            return;
        }
        if(isMyRegion(cityCrew)){
            currentPlayer.setBudget(currentPlayer.getBudget()-1);
            if(amount > territory.getRegion(cityCrew.getRow(), cityCrew.getCol()).getDeposit()){
                System.out.println("The collection amount exceeds the deposit in the current region.");
            }else {
                territory.getRegion(cityCrew.getRow(), cityCrew.getCol()).setDeposit(territory.getRegion(cityCrew.getRow(), cityCrew.getCol()).getDeposit()-amount);
                if(territory.getRegion(cityCrew.getRow(),cityCrew.getCol()).getDeposit() == 0){
                    territory.getRegion(cityCrew.getRow(),cityCrew.getCol()).setOwner(null);
                    currentPlayer.removeRegion(territory.getRegion(cityCrew.getRow(),cityCrew.getCol()));
                    if(territory.getRegion(cityCrew.getRow(), cityCrew.getCol()) == currentPlayer.getCityCenter()){
                        currentPlayer.setCityCenter(null);
                        checkWinner();
                    }
                }
                currentPlayer.setBudget(currentPlayer.getBudget()+amount);
            }
        }
    }

    public long nearby(long direction){
        Region nearby;
        long distance = 1;
        if(direction == 1){
            nearby = territory.getUp(cityCrew.getRow(), cityCrew.getCol());
            while(nearby != null){
                if(nearby.getOwner() == anotherPlayer){
                    long deposit = nearby.getDeposit();
                    System.out.println(currentPlayer.getName() + "'s city crew nearby = " + ((100*distance) + (long) (Math.log10(deposit) + 1)));
                    return (100*distance) + (long) (Math.log10(deposit) + 1);
                }
                nearby = territory.getUp(nearby.getRow(), nearby.getCol());
                distance++;
            }
        }else if(direction == 2){
            nearby = territory.getUpRight(cityCrew.getRow(), cityCrew.getCol());
            while(nearby != null){
                if(nearby.getOwner() == anotherPlayer){
                    long deposit = nearby.getDeposit();
                    System.out.println(currentPlayer.getName() + "'s city crew nearby = " + ((100*distance) + (long) (Math.log10(deposit) + 1)));
                    return (100*distance) + (long) (Math.log10(deposit) + 1);
                }
                nearby = territory.getUpRight(nearby.getRow(), nearby.getCol());
                distance++;
            }
        }else if(direction == 3){
            nearby = territory.getDownRight(cityCrew.getRow(), cityCrew.getCol());
            while(nearby != null){
                if(nearby.getOwner() == anotherPlayer){
                    long deposit = nearby.getDeposit();
                    System.out.println(currentPlayer.getName() + "'s city crew nearby = " + ((100*distance) + (long) (Math.log10(deposit) + 1)));
                    return (100*distance) + (long) (Math.log10(deposit) + 1);
                }
                nearby = territory.getDownRight(nearby.getRow(), nearby.getCol());
                distance++;
            }
        }else if(direction == 4){
            nearby = territory.getDown(cityCrew.getRow(), cityCrew.getCol());
            while(nearby != null){
                if(nearby.getOwner() == anotherPlayer){
                    long deposit = nearby.getDeposit();
                    System.out.println(currentPlayer.getName() + "'s city crew nearby = " + ((100*distance) + (long) (Math.log10(deposit) + 1)));
                    return (100*distance) + (long) (Math.log10(deposit) + 1);
                }
                nearby = territory.getDown(nearby.getRow(), nearby.getCol());
                distance++;
            }
        }else if(direction == 5){
            nearby = territory.getDownLeft(cityCrew.getRow(), cityCrew.getCol());
            while(nearby != null){
                if(nearby.getOwner() == anotherPlayer){
                    long deposit = nearby.getDeposit();
                    System.out.println(currentPlayer.getName() + "'s city crew nearby = " + ((100*distance) + (long) (Math.log10(deposit) + 1)));
                    return (100*distance) + (long) (Math.log10(deposit) + 1);
                }
                nearby = territory.getDownLeft(nearby.getRow(), nearby.getCol());
                distance++;
            }
        }else{
            nearby = territory.getUpLeft(cityCrew.getRow(), cityCrew.getCol());
            while(nearby != null){
                if(nearby.getOwner() == anotherPlayer){
                    long deposit = nearby.getDeposit();
                    System.out.println(currentPlayer.getName() + "'s city crew nearby = " + ((100*distance) + (long) (Math.log10(deposit) + 1)));
                    return (100*distance) + (long) (Math.log10(deposit) + 1);
                }
                nearby = territory.getUpLeft(nearby.getRow(), nearby.getCol());
                distance++;
            }
        }
        System.out.println(currentPlayer.getName() + "'s city crew nearby = 0");
        return 0;
    }

    public long opponent(){
        long distance = 1;
        Region curUp = territory.getUp(cityCrew.getRow(), cityCrew.getCol());
        Region curUpright = territory.getUpRight(cityCrew.getRow(), cityCrew.getCol());
        Region curDownright = territory.getDownRight(cityCrew.getRow(), cityCrew.getCol());
        Region curDown = territory.getDown(cityCrew.getRow(), cityCrew.getCol());
        Region curDownleft = territory.getDownLeft(cityCrew.getRow(), cityCrew.getCol());
        Region curUpleft = territory.getUpLeft(cityCrew.getRow(), cityCrew.getCol());
        while(true){
            if(curUp != null){
                if(curUp.getOwner() == anotherPlayer){
                    String value = distance + Long.toString(1);
                    System.out.println(currentPlayer.getName() + "'s city crew opponent = " + value);
                    return Long.parseLong(value);
                }
                curUp = territory.getUp(curUp.getRow(),curUp.getCol());
            }
            if(curUpright != null){
                if(curUpright.getOwner() == anotherPlayer){
                    String value = distance + Long.toString(2);
                    System.out.println(currentPlayer.getName() + "'s city crew opponent = " + value);
                    return Long.parseLong(value);
                }
                curUpright = territory.getUp(curUpright.getRow(),curUpright.getCol());
            }
            if(curDownright != null){
                if(curDownright.getOwner() == anotherPlayer){
                    String value = distance + Long.toString(3);
                    System.out.println(currentPlayer.getName() + "'s city crew opponent = " + value);
                    return Long.parseLong(value);
                }
                curDownright = territory.getUp(curDownright.getRow(),curDownright.getCol());
            }
            if(curDown != null){
                if(curDown.getOwner() == anotherPlayer){
                    String value = distance + Long.toString(4);
                    System.out.println(currentPlayer.getName() + "'s city crew opponent = " + value);
                    return Long.parseLong(value);
                }
                curDown = territory.getUp(curDown.getRow(),curDown.getCol());
            }
            if(curDownleft != null){
                if(curDownleft.getOwner() == anotherPlayer){
                    String value = distance + Long.toString(5);
                    System.out.println(currentPlayer.getName() + "'s city crew opponent = " + value);
                    return Long.parseLong(value);
                }
                curDownleft = territory.getUp(curDownleft.getRow(),curDownleft.getCol());
            }
            if(curUpleft != null){
                if(curUpleft.getOwner() == anotherPlayer){
                    String value = distance + Long.toString(6);
                    System.out.println(currentPlayer.getName() + "'s city crew opponent = " + value);
                    return Long.parseLong(value);
                }
                curUpleft = territory.getUp(curUpleft.getRow(),curUpleft.getCol());
            }
            if(curUp == null && curUpright == null && curDownright == null && curDown == null && curDownleft == null && curUpleft == null){
                break;
            }
            distance++;
        }
        System.out.println(currentPlayer.getName() + "'s city crew opponent = 0");
        return 0;
    }

    public void shoot(long direction , long stake) {
        if(currentPlayer.getBudget() < stake+1){
            System.out.println("Not enough budget to attack.");
            if (currentPlayer.getBudget() < 1){
                checkWinner();
                return;
            }
            return;
        }
        Region des;
        if(direction == 1){
            des = territory.getUp(cityCrew.getRow(), cityCrew.getCol());
            if(des == null){
                System.out.println(currentPlayer.getName() + "'s city crew cannot attack an out-of-territory region.");
                return;
            }
            if(des.getOwner() == null){
                currentPlayer.setBudget(currentPlayer.getBudget() - stake - 1);
                System.out.println(currentPlayer.getName() + " attacked an unowned region.");
            }else{
                long diff = des.getDeposit() - stake;
                currentPlayer.setBudget(currentPlayer.getBudget() - stake - 1);
                des.setDeposit(Math.max(0,diff));
                System.out.println(currentPlayer.getName() + " attacked region (" + des.getRow() + "," + des.getCol() + ").");
                if(des.getDeposit() == 0){
                    if(des.getOwner() == anotherPlayer){
                        anotherPlayer.removeRegion(des);
                        des.setOwner(null);
                        System.out.println(anotherPlayer.getName() + " loses region (" + des.getRow() + "," + des.getCol() + ").");
                        if(anotherPlayer.getCityCenter() == des){
                            anotherPlayer.setCityCenter(null);
                        }
                    }else if(des.getOwner() == currentPlayer){
                        currentPlayer.removeRegion(des);
                        des.setOwner(null);
                        System.out.println(currentPlayer.getName() + " loses region (" + des.getRow() + "," + des.getCol() + ").");
                        if(currentPlayer.getCityCenter() == des){
                            currentPlayer.setCityCenter(null);
                        }
                    }
                }
            }
        }else if(direction == 2){
            des = territory.getUpRight(cityCrew.getRow(), cityCrew.getCol());
            if(des == null){
                System.out.println(currentPlayer.getName() + "'s city crew cannot attack an out-of-territory region.");
                return;
            }
            if(des.getOwner() == null){
                currentPlayer.setBudget(currentPlayer.getBudget() - stake - 1);
                System.out.println(currentPlayer.getName() + " attacked an unowned region.");
            }else{
                long diff = des.getDeposit() - stake;
                currentPlayer.setBudget(currentPlayer.getBudget() - stake - 1);
                des.setDeposit(Math.max(0,diff));
                System.out.println(currentPlayer.getName() + " attacked region (" + des.getRow() + "," + des.getCol() + ").");
                if(des.getDeposit() == 0){
                    if(des.getOwner() == anotherPlayer){
                        anotherPlayer.removeRegion(des);
                        des.setOwner(null);
                        System.out.println(anotherPlayer.getName() + " loses region (" + des.getRow() + "," + des.getCol() + ").");
                        if(anotherPlayer.getCityCenter() == des){
                            anotherPlayer.setCityCenter(null);
                        }
                    }else if(des.getOwner() == currentPlayer){
                        currentPlayer.removeRegion(des);
                        des.setOwner(null);
                        System.out.println(currentPlayer.getName() + " loses region (" + des.getRow() + "," + des.getCol() + ").");
                        if(currentPlayer.getCityCenter() == des){
                            currentPlayer.setCityCenter(null);
                        }
                    }
                }
            }
        }else if(direction == 3){
            des = territory.getDownRight(cityCrew.getRow(), cityCrew.getCol());
            if(des == null){
                System.out.println(currentPlayer.getName() + "'s city crew cannot attack an out-of-territory region.");
                return;
            }
            if(des.getOwner() == null){
                currentPlayer.setBudget(currentPlayer.getBudget() - stake - 1);
                System.out.println(currentPlayer.getName() + " attacked an unowned region.");
            }else{
                long diff = des.getDeposit() - stake;
                currentPlayer.setBudget(currentPlayer.getBudget() - stake - 1);
                des.setDeposit(Math.max(0,diff));
                System.out.println(currentPlayer.getName() + " attacked region (" + des.getRow() + "," + des.getCol() + ").");
                if(des.getDeposit() == 0){
                    if(des.getOwner() == anotherPlayer){
                        anotherPlayer.removeRegion(des);
                        des.setOwner(null);
                        System.out.println(anotherPlayer.getName() + " loses region (" + des.getRow() + "," + des.getCol() + ").");
                        if(anotherPlayer.getCityCenter() == des){
                            anotherPlayer.setCityCenter(null);
                        }
                    }else if(des.getOwner() == currentPlayer){
                        currentPlayer.removeRegion(des);
                        des.setOwner(null);
                        System.out.println(currentPlayer.getName() + " loses region (" + des.getRow() + "," + des.getCol() + ").");
                        if(currentPlayer.getCityCenter() == des){
                            currentPlayer.setCityCenter(null);
                        }
                    }
                }
            }
        }else if(direction == 4){
            des = territory.getDown(cityCrew.getRow(), cityCrew.getCol());
            if(des == null){
                System.out.println(currentPlayer.getName() + "'s city crew cannot attack an out-of-territory region.");
                return;
            }
            if(des.getOwner() == null){
                currentPlayer.setBudget(currentPlayer.getBudget() - stake - 1);
                System.out.println(currentPlayer.getName() + " attacked an unowned region.");
            }else{
                long diff = des.getDeposit() - stake;
                currentPlayer.setBudget(currentPlayer.getBudget() - stake - 1);
                des.setDeposit(Math.max(0,diff));
                System.out.println(currentPlayer.getName() + " attacked region (" + des.getRow() + "," + des.getCol() + ").");
                if(des.getDeposit() == 0){
                    if(des.getOwner() == anotherPlayer){
                        anotherPlayer.removeRegion(des);
                        des.setOwner(null);
                        System.out.println(anotherPlayer.getName() + " loses region (" + des.getRow() + "," + des.getCol() + ").");
                        if(anotherPlayer.getCityCenter() == des){
                            anotherPlayer.setCityCenter(null);
                        }
                    }else if(des.getOwner() == currentPlayer){
                        currentPlayer.removeRegion(des);
                        des.setOwner(null);
                        System.out.println(currentPlayer.getName() + " loses region (" + des.getRow() + "," + des.getCol() + ").");
                        if(currentPlayer.getCityCenter() == des){
                            currentPlayer.setCityCenter(null);
                        }
                    }
                }
            }
        }
        else if(direction == 5){
            des = territory.getDownLeft(cityCrew.getRow(), cityCrew.getCol());
            if(des == null){
                System.out.println(currentPlayer.getName() + "'s city crew cannot attack an out-of-territory region.");
                return;
            }
            if(des.getOwner() == null){
                currentPlayer.setBudget(currentPlayer.getBudget() - stake - 1);
                System.out.println(currentPlayer.getName() + " attacked an unowned region.");
            }else{
                long diff = des.getDeposit() - stake;
                currentPlayer.setBudget(currentPlayer.getBudget() - stake - 1);
                des.setDeposit(Math.max(0,diff));
                System.out.println(currentPlayer.getName() + " attacked region (" + des.getRow() + "," + des.getCol() + ").");
                if(des.getDeposit() == 0){
                    if(des.getOwner() == anotherPlayer){
                        anotherPlayer.removeRegion(des);
                        des.setOwner(null);
                        System.out.println(anotherPlayer.getName() + " loses region (" + des.getRow() + "," + des.getCol() + ").");
                        if(anotherPlayer.getCityCenter() == des){
                            anotherPlayer.setCityCenter(null);
                        }
                    }else if(des.getOwner() == currentPlayer){
                        currentPlayer.removeRegion(des);
                        des.setOwner(null);
                        System.out.println(currentPlayer.getName() + " loses region (" + des.getRow() + "," + des.getCol() + ").");
                        if(currentPlayer.getCityCenter() == des){
                            currentPlayer.setCityCenter(null);
                        }
                    }
                }
            }
        }else{
            des = territory.getUpLeft(cityCrew.getRow(), cityCrew.getCol());
            if(des == null){
                System.out.println(currentPlayer.getName() + "'s city crew cannot attack an out-of-territory region.");
                return;
            }
            if(des.getOwner() == null){
                currentPlayer.setBudget(currentPlayer.getBudget() - stake - 1);
                System.out.println(currentPlayer.getName() + " attacked an unowned region.");
            }else{
                long diff = des.getDeposit() - stake;
                currentPlayer.setBudget(currentPlayer.getBudget() - stake - 1);
                des.setDeposit(Math.max(0,diff));
                System.out.println(currentPlayer.getName() + " attacked region (" + des.getRow() + "," + des.getCol() + ").");
                if(des.getDeposit() == 0){
                    if(des.getOwner() == anotherPlayer){
                        anotherPlayer.removeRegion(des);
                        des.setOwner(null);
                        System.out.println(anotherPlayer.getName() + " loses region (" + des.getRow() + "," + des.getCol() + ").");
                        if(anotherPlayer.getCityCenter() == des){
                            anotherPlayer.setCityCenter(null);
                        }
                    }else if(des.getOwner() == currentPlayer){
                        currentPlayer.removeRegion(des);
                        des.setOwner(null);
                        System.out.println(currentPlayer.getName() + " loses region (" + des.getRow() + "," + des.getCol() + ").");
                        if(currentPlayer.getCityCenter() == des){
                            currentPlayer.setCityCenter(null);
                        }
                    }
                }
            }
        }
        checkWinner();
    }

    public void relocate(){
        if(cityCrew.getOwner() != currentPlayer){
            System.out.println(currentPlayer.getName() + " cannot relocate the city center to an unowned region.");
            return;
        }
        long CCrow = currentPlayer.getCityCenter().getRow();
        long CCcol = currentPlayer.getCityCenter().getCol();
        long x = (long) Math.floor(Math.sqrt(Math.pow(cityCrew.getRow()-CCrow, 2) + Math.pow(cityCrew.getCol()-CCcol, 2)));
        long cost = 5*x+10;
        if(currentPlayer.getBudget() < cost){
            System.out.println(currentPlayer.getName() + " does not have enough budget to relocate the city center.");
            return;
        }
        currentPlayer.setBudget(currentPlayer.getBudget()-cost);
        currentPlayer.setCityCenter(cityCrew);
    }

    public static Boolean getWinner(){
        return winner != null;
    }

    public Player checkWinner(){
        if(player1.getBudget() <= 0 || player1.getCityCenter() == null){
            winner = player2;
            return player2;
        }else if(player2.getBudget() <= 0 || player2.getCityCenter() == null){
            winner = player1;
            return player1;
        }
        return null;
    }

    public void turnStart(){
        round++;
        System.out.println("------------------------------------------------------------------");
        System.out.println("                           Round " + round);
        System.out.println("------------------------------------------------------------------");
        currentPlayer = player1;
        anotherPlayer = player2;
        cityCrew = currentPlayer.getCityCenter();

        collectInterest();
        execStatement();
        System.out.println("******************************************************************");
        checkWinner();
        if(winner != null){
            System.out.println();
            System.out.println(winner.getName() + " Win!");
            return;
        }

        currentPlayer = player2;
        anotherPlayer = player1;
        cityCrew = currentPlayer.getCityCenter();


        collectInterest();
        execStatement();
        System.out.println("******************************************************************");
        if(winner != null){
            System.out.println();
            System.out.println(winner.getName() + " Win!");
            return;
        }
    }
}