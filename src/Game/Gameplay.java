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

    public boolean myRegion(Region region){ // set ค่า yourTurn ด้วย
        return currentPlayer.getOwnedRegion().contains(region);
    }

    public boolean otherRegion(Region region){
        return anotherPlayer.getOwnedRegion().contains(region);
    }

    public boolean isMyRegionAroundHere(){ // แก้ isMyRegionAroundHere
        return myRegion(territory.getUp(currentPlayer.getCurrow(), currentPlayer.getCurcol())) && myRegion(territory.getUpRight(currentPlayer.getCurrow(), currentPlayer.getCurcol())) && myRegion(territory.getDownRight(currentPlayer.getCurrow(), currentPlayer.getCurcol())) && myRegion(territory.getDownLeft(currentPlayer.getCurrow(), currentPlayer.getCurcol())) && myRegion(territory.getUpLeft(currentPlayer.getCurrow(), currentPlayer.getCurcol())) && myRegion(territory.getDown(currentPlayer.getCurrow(), currentPlayer.getCurcol()));
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
        if(!myRegion(cityCrew) && isMyRegionAroundHere() && !otherRegion(cityCrew)) {
            if (currentPlayer.getBudget() < amount + 1) {
                System.out.println("Not enough budget to invest.");
                if (currentPlayer.getBudget() < 1) return;
                currentPlayer.setBudget(currentPlayer.getBudget() - 1);
                return;
            }
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
        }
        if(territory.getRegion(cityCrew.getRow(),cityCrew.getCol()).getOwner() == currentPlayer) {
            if (currentPlayer.getBudget() < amount + 1) {
                System.out.println("Not enough budget to invest.");
                if (currentPlayer.getBudget() < 1) return;
                currentPlayer.setBudget(currentPlayer.getBudget() - 1);
                return;
            }
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

    public void opponent(){
        long distance = 0;
        String str_comb_dd;
        Region check = cityCrew;
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

    public void shoot(long instantdirection , long stake) { // แก้แล้ว final
        if(currentPlayer.getBudget() < stake+1) return;
        currentPlayer.setBudget(currentPlayer.getBudget() - stake+1);
        switch ((int) instantdirection) {
            case 1: // up
                if (currentPlayer.getBudget() < stake + 1) {
                    System.out.println("Sorry, your money not enough to SHOOT !!!");
                }
                else {
                    if(territory.getUp(cityCrew.getRow(), cityCrew.getCol()).getDeposit() - stake < territory.getUp(cityCrew.getRow(), cityCrew.getCol()).getDeposit()) {
                        territory.getUp(cityCrew.getRow(), cityCrew.getCol()).setDeposit(territory.getUp(cityCrew.getRow(), cityCrew.getCol()).getDeposit() - stake);
                        currentPlayer.setBudget(currentPlayer.getBudget() - stake - 1);
                        System.out.println("You SHOOT on up with " + stake + " damage");
                    }
                    if(stake >= territory.getUp(cityCrew.getRow(), cityCrew.getCol()).getDeposit()){
                        long haveLeft = config.getMaxDep() - territory.getUp(cityCrew.getRow(), cityCrew.getCol()).getDeposit(); // เลือดที่เหลือที่ควรจะตี
                        long stackLeft = stake - haveLeft; // stake จริงๆที่ควรจะตี
                        currentPlayer.setBudget(stake - stackLeft - 1);
                        anotherPlayer.removeRegion(territory.getUp(cityCrew.getRow(), cityCrew.getCol()));
                    }
                    if(territory.getUp(cityCrew.getRow(), cityCrew.getCol()).getDeposit() <= 0){
                        System.out.println("You have SHOOT on up and another player lose region");
                    }
                }
            case 4: // down
                if (currentPlayer.getBudget() < stake + 1) {
                    System.out.println("Sorry, your money not enough to SHOOT !!!");
                } else {
                    if(territory.getDown(cityCrew.getRow(), cityCrew.getCol()).getDeposit() - stake < territory.getDown(cityCrew.getRow(), cityCrew.getCol()).getDeposit()) {
                        territory.getDown(cityCrew.getRow(), cityCrew.getCol()).setDeposit(territory.getDown(cityCrew.getRow(), cityCrew.getCol()).getDeposit() - stake);
                        currentPlayer.setBudget(currentPlayer.getBudget() - stake - 1);
                        System.out.println("You SHOOT on down with " + stake + " damage and another player not lose region");
                    }
                    if(stake >= territory.getDown(cityCrew.getRow(), cityCrew.getCol()).getDeposit()){
                        long haveLeft = config.getMaxDep() - territory.getDown(cityCrew.getRow(), cityCrew.getCol()).getDeposit(); // เลือดที่เหลือที่ควรจะตี
                        long stackLeft = stake - haveLeft; // stake จริงๆที่ควรจะตี
                        currentPlayer.setBudget(stake - stackLeft - 1);
                        anotherPlayer.removeRegion(territory.getDown(cityCrew.getRow(), cityCrew.getCol()));
                    }
                    if(territory.getUp(cityCrew.getRow(), cityCrew.getCol()).getDeposit() <= 0){
                        System.out.println("You have SHOOT on down and another player lose region");
                    }
                }
            case 6: // upleft
                if (currentPlayer.getBudget() < stake + 1) {
                    System.out.println("Sorry, your money not enough to SHOOT !!!");
                } else {
                    if(territory.getUpLeft(cityCrew.getRow(), cityCrew.getCol()).getDeposit() - stake < territory.getUpLeft(cityCrew.getRow(), cityCrew.getCol()).getDeposit()) {
                        territory.getUpLeft(cityCrew.getRow(), cityCrew.getCol()).setDeposit(territory.getUpLeft(cityCrew.getRow(), cityCrew.getCol()).getDeposit() - stake);
                        currentPlayer.setBudget(currentPlayer.getBudget() - stake - 1);
                        System.out.println("You SHOOT on upleft with " + stake + " damage and another player not lose region");
                    }
                    if(stake >= territory.getUpLeft(cityCrew.getRow(), cityCrew.getCol()).getDeposit()){
                        long haveLeft = config.getMaxDep() - territory.getUpLeft(cityCrew.getRow(), cityCrew.getCol()).getDeposit(); // เลือดที่เหลือที่ควรจะตี
                        long stackLeft = stake - haveLeft; // stake จริงๆที่ควรจะตี
                        currentPlayer.setBudget(stake - stackLeft -1);
                        anotherPlayer.removeRegion(territory.getUpLeft(cityCrew.getRow(), cityCrew.getCol()));
                    }
                    if(territory.getUp(cityCrew.getRow(), cityCrew.getCol()).getDeposit() <= 0){
                        System.out.println("You have SHOOT on upleft and another player lose region");
                    }
                }
            case 2: //upright
                if (currentPlayer.getBudget() < stake + 1) {
                    System.out.println("Sorry, your money not enough to SHOOT !!!");
                } else {
                    if(territory.getUpRight(cityCrew.getRow(), cityCrew.getCol()).getDeposit() - stake < territory.getUpRight(cityCrew.getRow(), cityCrew.getCol()).getDeposit()) {
                        territory.getUpRight(cityCrew.getRow(), cityCrew.getCol()).setDeposit(territory.getUpRight(cityCrew.getRow(), cityCrew.getCol()).getDeposit() - stake);
                        currentPlayer.setBudget(currentPlayer.getBudget() - stake - 1);
                        System.out.println("You SHOOT on upright with " + stake + " damage and another player not lose region");
                    }
                    if(stake >= territory.getUpRight(cityCrew.getRow(), cityCrew.getCol()).getDeposit()){
                        long haveLeft = config.getMaxDep() - territory.getUpRight(cityCrew.getRow(), cityCrew.getCol()).getDeposit(); // เลือดที่เหลือที่ควรจะตี
                        long stackLeft = stake - haveLeft; // stake จริงๆที่ควรจะตี
                        currentPlayer.setBudget(stake - stackLeft -1);
                        anotherPlayer.removeRegion(territory.getUpRight(cityCrew.getRow(), cityCrew.getCol()));
                    }
                    if(territory.getUp(cityCrew.getRow(), cityCrew.getCol()).getDeposit() <= 0){
                        System.out.println("You have SHOOT on upright and another player lose region");
                    }
                }
            case 5: //downleft
                if (currentPlayer.getBudget() < stake + 1) {
                    System.out.println("Sorry, your money not enough to SHOOT !!!");
                } else {
                    if(territory.getDownLeft(cityCrew.getRow(), cityCrew.getCol()).getDeposit() - stake < territory.getDownLeft(cityCrew.getRow(), cityCrew.getCol()).getDeposit()) {
                        territory.getDownLeft(cityCrew.getRow(), cityCrew.getCol()).setDeposit(territory.getDownLeft(cityCrew.getRow(), cityCrew.getCol()).getDeposit() - stake);
                        currentPlayer.setBudget(currentPlayer.getBudget() - stake - 1);
                        System.out.println("You SHOOT on downleft with " + stake + " damage and another player not lose region");
                    }
                    if(stake >= territory.getDownLeft(cityCrew.getRow(), cityCrew.getCol()).getDeposit()){
                        long haveLeft = config.getMaxDep() - territory.getDownLeft(cityCrew.getRow(), cityCrew.getCol()).getDeposit(); // เลือดที่เหลือที่ควรจะตี
                        long stackLeft = stake - haveLeft; // stake จริงๆที่ควรจะตี
                        currentPlayer.setBudget(stake - stackLeft -1);
                        anotherPlayer.removeRegion(territory.getDownLeft(cityCrew.getRow(), cityCrew.getCol()));
                    }
                    if(territory.getUp(cityCrew.getRow(), cityCrew.getCol()).getDeposit() <= 0){
                        System.out.println("You have SHOOT on downleft and another player lose region");
                    }
                }
            case 3: //downright
                if (currentPlayer.getBudget() < stake + 1) {
                    System.out.println("Sorry, your money not enough to SHOOT !!!");
                } else {
                    if(territory.getDownRight(cityCrew.getRow(), cityCrew.getCol()).getDeposit() - stake < territory.getDownRight(cityCrew.getRow(), cityCrew.getCol()).getDeposit()) {
                        territory.getDownRight(cityCrew.getRow(), cityCrew.getCol()).setDeposit(territory.getDownRight(cityCrew.getRow(), cityCrew.getCol()).getDeposit() - stake);
                        currentPlayer.setBudget(currentPlayer.getBudget() - stake - 1);
                        System.out.println("You SHOOT on downright with " + stake + " damage and another player not lose region");
                    }
                    if(stake >= territory.getDownRight(cityCrew.getRow(), cityCrew.getCol()).getDeposit()){
                        long haveLeft = config.getMaxDep() - territory.getDownRight(cityCrew.getRow(), cityCrew.getCol()).getDeposit(); // เลือดที่เหลือที่ควรจะตี
                        long stackLeft = stake - haveLeft; // stake จริงๆที่ควรจะตี
                        currentPlayer.setBudget(stake - stackLeft -1);
                        anotherPlayer.removeRegion(territory.getDownRight(cityCrew.getRow(), cityCrew.getCol()));
                    }
                    if(territory.getUp(cityCrew.getRow(), cityCrew.getCol()).getDeposit() <= 0){
                        System.out.println("You have SHOOT on downright and another player lose region");
                    }
                }

        }
    }


}
