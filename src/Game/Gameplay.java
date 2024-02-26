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
        anotherPlayer = this.player2;

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
        cityCrew = territory.getRegion(p1Row,p1Col);

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
        return p.parse(currentPlayer.getAllBindings());
    }

    public void setStatements(Executable statements){
        if(currentPlayer == player1 ){
            p1Statements = statements;
        }
        else{
            p2Statements = statements;
        }
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
        return this.getCurrentPlayer();
    }

    public Region getCityCrew(){
        return this.cityCrew;
    }

    public boolean isMyRegion(Region region){ // set ค่า yourTurn ด้วย

        return region.getOwner() == currentPlayer;
    }

    public boolean otherRegion(Region region){
        return region.getOwner() == anotherPlayer;
    }

    public boolean isMyRegionAroundHere(){ // แก้ isMyRegionAroundHere
        return isMyRegion(territory.getUp(currentPlayer.getCurrow(), currentPlayer.getCurcol())) && isMyRegion(territory.getUpRight(currentPlayer.getCurrow(), currentPlayer.getCurcol())) && isMyRegion(territory.getDownRight(currentPlayer.getCurrow(), currentPlayer.getCurcol())) && isMyRegion(territory.getDownLeft(currentPlayer.getCurrow(), currentPlayer.getCurcol())) && isMyRegion(territory.getUpLeft(currentPlayer.getCurrow(), currentPlayer.getCurcol())) && isMyRegion(territory.getDown(currentPlayer.getCurrow(), currentPlayer.getCurcol()));
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
        System.out.println();
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
            }else if(des.getOwner() == anotherPlayer){
                System.out.println("Cannot move to an another player's region.");
            }else{
                System.out.println(currentPlayer.getName() + " move up: (" + cityCrew.getRow() + "," + cityCrew.getCol() + ") -> ("+ des.getRow() + "," + des.getCol() + ")");
                cityCrew = des;
            }
        } else if (direction == 2) {
            des = territory.getUpRight(cityCrew.getRow(), cityCrew.getCol());
            if(des == null){
                System.out.println("Cannot move to an out-of-territory region.");
            }else if(des.getOwner() == anotherPlayer){
                System.out.println("Cannot move to an another player's region.");
            }else{
                System.out.println("move upright: (" + cityCrew.getRow() + "," + cityCrew.getCol() + ") -> ("+ des.getRow() + "," + des.getCol() + ")");
                cityCrew = des;
            }
        } else if (direction == 3){
            des = territory.getDownRight(cityCrew.getRow(), cityCrew.getCol());
            if(des == null){
                System.out.println("Cannot move to an out-of-territory region.");
            }else if(des.getOwner() == anotherPlayer){
                System.out.println("Cannot move to an another player's region.");
            }else{
                System.out.println("move downright: (" + cityCrew.getRow() + "," + cityCrew.getCol() + ") -> ("+ des.getRow() + "," + des.getCol() + ")");
                cityCrew = des;
            }
        } else if (direction == 4){
            des = territory.getDown(cityCrew.getRow(), cityCrew.getCol());
            if(des == null){
                System.out.println("Cannot move to an out-of-territory region.");
            }else if(des.getOwner() == anotherPlayer){
                System.out.println("Cannot move to an another player's region.");
            }else{
                System.out.println("move down: (" + cityCrew.getRow() + "," + cityCrew.getCol() + ") -> ("+ des.getRow() + "," + des.getCol() + ")");
                cityCrew = des;
            }
        } else if (direction == 5){
            des = territory.getDownLeft(cityCrew.getRow(), cityCrew.getCol());
            if(des == null){
                System.out.println("Cannot move to an out-of-territory region.");
            }else if(des.getOwner() == anotherPlayer){
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
            }else if(des.getOwner() == anotherPlayer){
                System.out.println("Cannot move to an another player's region.");
            }else{
                System.out.println("move upleft: (" + cityCrew.getRow() + "," + cityCrew.getCol() + ") -> ("+ des.getRow() + "," + des.getCol() + ")");
                cityCrew = des;
            }
        }
    }

    public void invest(long amount){
        if (currentPlayer.getBudget() < amount + 1) {
            System.out.println("Not enough budget to invest.");
            if (currentPlayer.getBudget() < 1) return;
            currentPlayer.setBudget(currentPlayer.getBudget() - 1);
            return;
        }
        if(!isMyRegion(cityCrew) && isMyRegionAroundHere() && !otherRegion(cityCrew)) {
            if (territory.getRegion(cityCrew.getRow(), cityCrew.getCol()).getDeposit() + amount > config.getMaxDep()) {
                currentPlayer.setBudget(currentPlayer.getBudget() - (config.getMaxDep() - territory.getRegion(cityCrew.getRow(), cityCrew.getCol()).getDeposit() + 1));
                territory.getRegion(cityCrew.getRow(), cityCrew.getCol()).setDeposit(config.getMaxDep());
                currentPlayer.addRegion(cityCrew);
                cityCrew.setOwner(currentPlayer);
                System.out.println("Invest at (" + cityCrew.getRow() + "," + cityCrew.getCol() + "): " + (config.getMaxDep() - territory.getRegion(cityCrew.getRow(), cityCrew.getCol()).getDeposit()));
            } else {
                currentPlayer.setBudget(currentPlayer.getBudget() - (amount + 1));
                territory.getRegion(cityCrew.getRow(), cityCrew.getCol()).setDeposit(territory.getRegion(cityCrew.getRow(), cityCrew.getCol()).getDeposit() + amount);
                currentPlayer.addRegion(cityCrew);
                cityCrew.setOwner(currentPlayer);
                System.out.println("Invest at (" + cityCrew.getRow() + "," + cityCrew.getCol() + "): " + amount);
            }
        } else if(isMyRegion(cityCrew)) {
            if (territory.getRegion(cityCrew.getRow(), cityCrew.getCol()).getDeposit() + amount > config.getMaxDep()) {
                currentPlayer.setBudget(currentPlayer.getBudget() - (config.getMaxDep() - territory.getRegion(cityCrew.getRow(), cityCrew.getCol()).getDeposit() + 1));
                territory.getRegion(cityCrew.getRow(), cityCrew.getCol()).setDeposit(config.getMaxDep());
                System.out.println("Invest at (" + cityCrew.getRow() + "," + cityCrew.getCol() + "): " + (config.getMaxDep() - territory.getRegion(cityCrew.getRow(), cityCrew.getCol()).getDeposit()));
            } else {
                currentPlayer.setBudget(currentPlayer.getBudget() - (amount + 1));
                territory.getRegion(cityCrew.getRow(), cityCrew.getCol()).setDeposit(territory.getRegion(cityCrew.getRow(), cityCrew.getCol()).getDeposit() + amount);
                System.out.println("Invest at (" + cityCrew.getRow() + "," + cityCrew.getCol() + "): " + amount);
            }
        }
    }

    public void collect(long amount){
        if (currentPlayer.getBudget() < 1){
            System.out.println("Not enough budget to invest.");
            return;
        }
        if(isMyRegion(cityCrew)){
            if(amount > territory.getRegion(cityCrew.getRow(), cityCrew.getCol()).getDeposit()){
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

    public void opponent(){
        long distance = 0;
        String str_comb_dd;
        Region check = territory.getRegion(getCityCrew().getRow(),cityCrew.getCol());
        boolean opponentFound;
        while (true) { // up
            if(check == null){
                continue;
            }
            check = territory.getUp(cityCrew.getRow()-distance,cityCrew.getCol());
            if (check.getOwner() != null && check.getOwner() == anotherPlayer) {
                str_comb_dd = distance + Long.toString(1);
                opponentFound = true;
                break;
            } // down
            check = territory.getRegion(cityCrew.getRow() + distance, cityCrew.getCol());
            if (check.getOwner() != null && check.getOwner() == anotherPlayer) {
                str_comb_dd = distance + Long.toString(4);
                opponentFound = true;
                break;
            } // upleft
            if(cityCrew.getCol() %2 == 0) {
                check = territory.getRegion(cityCrew.getRow() - distance, cityCrew.getCol() - distance);
            }else check = territory.getRegion(cityCrew.getRow() , cityCrew.getCol() - distance);
            if (check.getOwner() != null && check.getOwner() == anotherPlayer) {
                str_comb_dd = distance + Long.toString(6);
                opponentFound = true;
                break;
            } // upright
            if(cityCrew.getCol() %2 == 0) {
                check = territory.getRegion(cityCrew.getRow() - distance, cityCrew.getCol() + distance);
            }else check = territory.getRegion(cityCrew.getRow(), cityCrew.getCol() + distance);
            if (check.getOwner() != null && check.getOwner() == anotherPlayer) {
                str_comb_dd = distance + Long.toString(2);
                opponentFound = true;
                break;
            } // downleft
            if(cityCrew.getCol() %2 == 0) {
                check = territory.getRegion(cityCrew.getRow(), cityCrew.getCol() - distance);
            }else check = territory.getRegion(cityCrew.getRow() + distance, cityCrew.getCol() - distance);
            if (check.getOwner() != null && check.getOwner() == anotherPlayer) {
                str_comb_dd = distance + Long.toString(5);
                opponentFound = true;
                break;
            } // downright
            if(cityCrew.getCol() %2 == 0) {
                check = territory.getRegion(cityCrew.getRow(), cityCrew.getCol() + distance);
            }else check = territory.getRegion(cityCrew.getRow() + distance, cityCrew.getCol() + distance);
            if (check.getOwner() != null && check.getOwner() == anotherPlayer) {
                str_comb_dd = distance + Long.toString(3);
                opponentFound = true;
                break;
            }
            distance++;
        }

        if(opponentFound){
            System.out.println("That another cityCrew is" + Long.parseLong(str_comb_dd));
        }else{
            System.out.println("Sorry, I can't see another cityCrew");
        }
    }

    public void nearby(int instantdirection){ // แก้แล้ว
        Region check = cityCrew;
        long distance = 1;
        boolean found = false;
        switch (instantdirection){
            case 1 : // up
                while(check != null){
                    check = territory.getRegion(cityCrew.getRow() - distance,cityCrew.getCol());
                    if(otherRegion(check)){
                        found = true;
                        System.out.println("That another cityCrew is "+100*(distance) + (long)String.valueOf(check.getDeposit()).length());
                    }
                    else distance++;
                }
                if(!found) {
                    System.out.println("Sorry, I can't see another cityCrew");
                }
            case 4 : // down
                while(check != null){
                    check = territory.getRegion(cityCrew.getRow() + distance,cityCrew.getCol());
                    if(otherRegion(check)){
                        found = true;
                        System.out.println("That another cityCrew is" + 100*(distance) + (long)String.valueOf(check.getDeposit()).length());
                    }
                    else distance++;
                }
            case 6 : // upleft
                while(check != null){
                    if(check.getCol() %2 == 0){
                        check = territory.getRegion(cityCrew.getRow()-distance , cityCrew.getCol()-distance);
                        otherRegion(check);
                        if(otherRegion(check)){
                            found = true;
                            System.out.println("That another cityCrew is" + 100*(distance) + (long)String.valueOf(check.getDeposit()).length());
                        }
                    }else {
                        check = territory.getRegion(cityCrew.getRow(), cityCrew.getCol() - distance);
                        otherRegion(check);
                        if (otherRegion(check)) {
                            found = true;
                            System.out.println("That another cityCrew is" + 100*(distance) + (long)String.valueOf(check.getDeposit()).length());
                        }
                    }
                    distance++;
                }
            case 2 : // upright
                while(check != null){
                    if(check.getCol() %2 == 0){
                        check = territory.getRegion(cityCrew.getRow() - distance , cityCrew.getCol() + distance);
                        otherRegion(check);
                        if(otherRegion(check)){
                            found = true;
                            System.out.println("That another cityCrew is" + 100*(distance) + (long)String.valueOf(check.getDeposit()).length());

                        }
                    }else {
                        check = territory.getRegion(cityCrew.getRow(), cityCrew.getCol() + distance);
                        otherRegion(check);
                        if (otherRegion(check)) {
                            found = true;
                            System.out.println("That another cityCrew is" + 100*(distance) + (long)String.valueOf(check.getDeposit()).length());
                        }
                    }
                    distance++;
                }
            case 5 : // downleft
                while(check != null){
                    if(check.getCol() %2 == 0){
                        check = territory.getRegion(cityCrew.getRow(), cityCrew.getCol() - distance);
                        otherRegion(check);
                        if(otherRegion(check)){
                            found = true;
                            System.out.println("That another cityCrew is" + 100*(distance) + (long)String.valueOf(check.getDeposit()).length());

                        }
                    }else {
                        check = territory.getRegion(cityCrew.getRow() + distance, cityCrew.getCol() - distance);
                        otherRegion(check);
                        if (otherRegion(check)) {
                            found = true;
                            System.out.println("That another cityCrew is" + 100*(distance) + (long)String.valueOf(check.getDeposit()).length());
                        }
                    }
                    distance++;
                }
            case 3 : // downright
                while(check != null){
                    if(check.getCol() %2 == 0){
                        check = territory.getRegion(cityCrew.getRow() , cityCrew.getCol() + distance);
                        otherRegion(check);
                        if(otherRegion(check)){
                            found = true;
                            System.out.println("That another cityCrew is" + 100*(distance) + (long)String.valueOf(check.getDeposit()).length());
                        }
                    }else {
                        check = territory.getRegion(cityCrew.getRow() + distance, cityCrew.getCol() + distance);
                        otherRegion(check);
                        if (otherRegion(check)) {
                            found = true;
                            System.out.println("That another cityCrew is" + 100*(distance) + (long)String.valueOf(check.getDeposit()).length());
                        }
                    }
                    distance++;
                }
        }
        if(!found) {
            System.out.println("Sorry, I can't see another cityCrew");
        }
    }

    public long nearby2(long direction){
        Region nearby;
        long distance = 1;
        if(direction == 1){
            nearby = territory.getUp(cityCrew.getRow(), cityCrew.getCol());
            while(nearby != null){
                if(nearby.getOwner() == anotherPlayer){
                    long deposit = nearby.getDeposit();
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
                    return (100*distance) + (long) (Math.log10(deposit) + 1);
                }
                nearby = territory.getUpLeft(nearby.getRow(), nearby.getCol());
                distance++;
            }
        }
        System.out.println("No opponent's region in a given direction.");
        return 0;
    }

    public long opponent2(){
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
                    return Long.parseLong(value);
                }
                curUp = territory.getUp(curUp.getRow(),curUp.getCol());
            }
            if(curUpright != null){
                if(curUpright.getOwner() == anotherPlayer){
                    String value = distance + Long.toString(2);
                    return Long.parseLong(value);
                }
                curUpright = territory.getUp(curUpright.getRow(),curUpright.getCol());
            }
            if(curDownright != null){
                if(curDownright.getOwner() == anotherPlayer){
                    String value = distance + Long.toString(3);
                    return Long.parseLong(value);
                }
                curDownright = territory.getUp(curDownright.getRow(),curDownright.getCol());
            }
            if(curDown != null){
                if(curDown.getOwner() == anotherPlayer){
                    String value = distance + Long.toString(4);
                    return Long.parseLong(value);
                }
                curDown = territory.getUp(curDown.getRow(),curDown.getCol());
            }
            if(curDownleft != null){
                if(curDownleft.getOwner() == anotherPlayer){
                    String value = distance + Long.toString(5);
                    return Long.parseLong(value);
                }
                curDownleft = territory.getUp(curDownleft.getRow(),curDownleft.getCol());
            }
            if(curUpleft != null){
                if(curUpleft.getOwner() == anotherPlayer){
                    String value = distance + Long.toString(6);
                    return Long.parseLong(value);
                }
                curUpleft = territory.getUp(curUpleft.getRow(),curUpleft.getCol());
            }
            if(curUp == null && curUpright == null && curDownright == null && curDown == null && curDownleft == null && curUpleft == null){
                break;
            }
            distance++;
        }
        return 0;
    }

    public void shoot(long instantdirection , long stake) { // แก้แล้ว final
        if(currentPlayer.getBudget() < stake+1) return;
        Region des;
        switch ((int) instantdirection) {
            case 1: // up
                des = territory.getUp(cityCrew.getRow(), cityCrew.getCol());
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
                          System.out.println(anotherPlayer.getName() + "loses region ( " + des.getRow() + "," + des.getCol() + ").");
                          if(anotherPlayer.getCityCenter() == des){
                              System.out.println(currentPlayer.getName() + " Win!");
                              // แพ้
                          }
                      }else if(des.getOwner() == currentPlayer){
                          currentPlayer.removeRegion(des);
                          des.setOwner(null);
                          System.out.println(currentPlayer.getName() + "loses region ( " + des.getRow() + "," + des.getCol() + ").");
                          if(currentPlayer.getCityCenter() == des){
                              System.out.println(anotherPlayer.getName() + " Win!");
                              // แพ้
                          }
                      }
                    }
                }
            case 4: // down
                des = territory.getDown(cityCrew.getRow(), cityCrew.getCol());
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
                            System.out.println(anotherPlayer.getName() + "loses region ( " + des.getRow() + "," + des.getCol() + ").");
                            if(anotherPlayer.getCityCenter() == des){
                                System.out.println(currentPlayer.getName() + " Win!");
                                // แพ้
                            }
                        }else if(des.getOwner() == currentPlayer){
                            currentPlayer.removeRegion(des);
                            des.setOwner(null);
                            System.out.println(currentPlayer.getName() + "loses region ( " + des.getRow() + "," + des.getCol() + ").");
                            if(currentPlayer.getCityCenter() == des){
                                System.out.println(anotherPlayer.getName() + " Win!");
                                // แพ้
                            }
                        }
                    }
                }
            case 6: // upleft
                des = territory.getUpLeft(cityCrew.getRow(), cityCrew.getCol());
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
                            System.out.println(anotherPlayer.getName() + "loses region ( " + des.getRow() + "," + des.getCol() + ").");
                            if(anotherPlayer.getCityCenter() == des){
                                System.out.println(currentPlayer.getName() + " Win!");
                                // แพ้
                            }
                        }else if(des.getOwner() == currentPlayer){
                            currentPlayer.removeRegion(des);
                            des.setOwner(null);
                            System.out.println(currentPlayer.getName() + "loses region ( " + des.getRow() + "," + des.getCol() + ").");
                            if(currentPlayer.getCityCenter() == des){
                                System.out.println(anotherPlayer.getName() + " Win!");
                                // แพ้
                            }
                        }
                    }
                }
            case 2: //upright
                des = territory.getUpRight(cityCrew.getRow(), cityCrew.getCol());
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
                            System.out.println(anotherPlayer.getName() + "loses region ( " + des.getRow() + "," + des.getCol() + ").");
                            if(anotherPlayer.getCityCenter() == des){
                                System.out.println(currentPlayer.getName() + " Win!");
                                // แพ้
                            }
                        }else if(des.getOwner() == currentPlayer){
                            currentPlayer.removeRegion(des);
                            des.setOwner(null);
                            System.out.println(currentPlayer.getName() + "loses region ( " + des.getRow() + "," + des.getCol() + ").");
                            if(currentPlayer.getCityCenter() == des){
                                System.out.println(anotherPlayer.getName() + " Win!");
                                // แพ้
                            }
                        }
                    }
                }
            case 5: //downleft
                des = territory.getDownLeft(cityCrew.getRow(), cityCrew.getCol());
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
                            System.out.println(anotherPlayer.getName() + "loses region ( " + des.getRow() + "," + des.getCol() + ").");
                            if(anotherPlayer.getCityCenter() == des){
                                System.out.println(currentPlayer.getName() + " Win!");
                                // แพ้
                            }
                        }else if(des.getOwner() == currentPlayer){
                            currentPlayer.removeRegion(des);
                            des.setOwner(null);
                            System.out.println(currentPlayer.getName() + "loses region ( " + des.getRow() + "," + des.getCol() + ").");
                            if(currentPlayer.getCityCenter() == des){
                                System.out.println(anotherPlayer.getName() + " Win!");
                                // แพ้
                            }
                        }
                    }
                }
            case 3: //downright
                des = territory.getDownRight(cityCrew.getRow(), cityCrew.getCol());
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
                            System.out.println(anotherPlayer.getName() + "loses region ( " + des.getRow() + "," + des.getCol() + ").");
                            if(anotherPlayer.getCityCenter() == des){
                                System.out.println(currentPlayer.getName() + " Win!");
                                // แพ้
                            }
                        }else if(des.getOwner() == currentPlayer){
                            currentPlayer.removeRegion(des);
                            des.setOwner(null);
                            System.out.println(currentPlayer.getName() + "loses region ( " + des.getRow() + "," + des.getCol() + ").");
                            if(currentPlayer.getCityCenter() == des){
                                System.out.println(anotherPlayer.getName() + " Win!");
                                // แพ้
                            }
                        }
                    }
                }
        }
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

    public void switchPlayer(){
        if(currentPlayer == player1){
            currentPlayer = player2;
            anotherPlayer = player1;
        }else {
            currentPlayer = player1;
            anotherPlayer = player2;
        }
    }
}
