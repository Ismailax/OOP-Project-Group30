package GameState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import PlanParser.Executable.Executable;
import PlanParser.Parser;
import PlanParser.Tokenizer;

public class Player {
    private final String name;
    private Map<String, Long> bindings;
    private String plan;
    private Executable statements;
    private long currow;
    private long curcol;
    private List<Region> ownedRegion;
    private long turns;
    private long base;
    private long budget;
    private Region cityCenter;
    private Region currentRegion;
    private Territory territory;


    public Player(String name){
        this.name = name;
        bindings = new HashMap<>();
        ownedRegion = new ArrayList<>();
    }

    public Executable eval(){
        Parser p = new Parser(new Tokenizer(plan));
        p.setPlayer(this);
        return p.parse(bindings);
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public void setStatements(Executable statements){
        this.statements = statements;
    }

    public void executeStmt(){
        this.statements.execute(bindings);
    }

    public long getBindings(String variable) {
        if(bindings.get(variable) != null) return bindings.get(variable);
        return 0;
    }

    public Map<String, Long> getAllBindings(){
        return this.bindings;
    }

    public void setTerritory(Territory territory) {
        this.territory = territory;
    }

    public boolean myRegion(Region region){ // เช็คว่าใช่ region เราไหม
        if(ownedRegion.contains(region)){
            return true;
        }
        else return false;
    }

    public boolean otherRegion(Region region){ // เช็คว่าใช้ region คนอื่นไหม
        if(region.getOwner() != this){
           return true;
        }
        else return false;
    }

    public long collectInterest(){
        long interest = 0;
        for(Region r : ownedRegion){
            double rate =  base * Math.log10(r.getDeposit()) * Math.log(turns);
            interest += (long) (r.getDeposit() * rate / 100);
        }
        return interest;
    }

    public void addRegion(Region region){
        ownedRegion.add(region);
        region.setOwner(this); // set owner ของแต่ละ region เมื่อ addRegion
    }

    public void removeRegion(Region region){
        ownedRegion.remove(region);
        region.setOwner(null);
    }

    public long opponent(){ // คิดว่าแก้แล้ว น่าจะถูก
            long distance = 0;
            String str_comb_dd;
            Region check = currentRegion;
            boolean opponentFound = false;
            while (true) { // up
                if(check == null){
                    continue;
                }
                check = territory.getUp(currentRegion.getRow()-distance,currentRegion.getCol());
                if (check.getOwner() != null && check.getOwner() != this) {
                    str_comb_dd = Long.toString(distance) + Long.toString(1);
                    opponentFound = true;
                    break;
                } // down
                check = territory.getRegion(currentRegion.getRow() + distance, currentRegion.getCol());
                if (check.getOwner() != null && check.getOwner() != this) {
                    str_comb_dd = Long.toString(distance) + Long.toString(4);
                    opponentFound = true;
                    break;
                } // upleft
                    if(currentRegion.getCol() %2 == 0) {
                        check = territory.getRegion(currentRegion.getRow() - distance, currentRegion.getCol() - distance);
                    }else check = territory.getRegion(currentRegion.getRow() , currentRegion.getCol() - distance);
                if (check.getOwner() != null && check.getOwner() != this) {
                    str_comb_dd = Long.toString(distance) + Long.toString(6);
                    opponentFound = true;
                    break;
                } // upright
                    if(currentRegion.getCol() %2 == 0) {
                        check = territory.getRegion(currentRegion.getRow() - distance, currentRegion.getCol() + distance);
                    }else check = territory.getRegion(currentRegion.getRow(), currentRegion.getCol() + distance);
                if (check.getOwner() != null && check.getOwner() != this) {
                    str_comb_dd = Long.toString(distance) + Long.toString(2);
                    opponentFound = true;
                    break;
                } // downleft
                    if(currentRegion.getCol() %2 == 0) {
                        check = territory.getRegion(currentRegion.getRow(), currentRegion.getCol() - distance);
                    }else check = territory.getRegion(currentRegion.getRow() + distance, currentRegion.getCol() - distance);
                if (check.getOwner() != null && check.getOwner() != this) {
                    str_comb_dd = Long.toString(distance) + Long.toString(5);
                    opponentFound = true;
                    break;
                } // downright
                    if(currentRegion.getCol() %2 == 0) {
                        check = territory.getRegion(currentRegion.getRow(), currentRegion.getCol() + distance);
                    }else check = territory.getRegion(currentRegion.getRow() + distance, currentRegion.getCol() + distance);
                    if (check.getOwner() != null && check.getOwner() != this) {
                    str_comb_dd = Long.toString(distance) + Long.toString(3);
                    opponentFound = true;
                    break;
                }
                distance++;
            }

            if(opponentFound){
                return Long.parseLong(str_comb_dd);
            }else {
                return 0;
            }
    }

    public long nearby(int instantdirection){ // แก้แล้ว
        Region check = currentRegion;
        long distance = 1;
        switch (instantdirection){
            case 1 : // up
                    while(check != null){
                    check = territory.getRegion(currentRegion.getRow() - distance,currentRegion.getCol());
                    if(otherRegion(check)){
                        return  100*(distance) + (long)String.valueOf(check.getDeposit()).length();
                    }
                    else distance++;
                    }
                    return 0;
            case 4 : // down
                while(check != null){
                    check = territory.getRegion(currentRegion.getRow() + distance,currentRegion.getCol());
                    if(otherRegion(check)){
                        return  100*(distance) + (long)String.valueOf(check.getDeposit()).length();
                    }
                    else distance++;
                }
                return 0;
            case 6 : // upleft
                    while(check != null){
                        if(check.getCol() %2 == 0){
                            check = territory.getRegion(currentRegion.getRow()-distance , currentRegion.getCol()-distance);
                            otherRegion(check);
                            if(otherRegion(check)){
                                return 100*(distance)+(long)String.valueOf(check.getDeposit()).length();
                            }
                        }else {
                            check = territory.getRegion(currentRegion.getRow(), currentRegion.getCol() - distance);
                            otherRegion(check);
                            if (otherRegion(check)) {
                                return 100 * (distance) + (long) String.valueOf(check.getDeposit()).length();
                            }
                        }
                        distance++;
                    }
                    return 0;
            case 2 : // upright
                while(check != null){
                    if(check.getCol() %2 == 0){
                        check = territory.getRegion(currentRegion.getRow() - distance , currentRegion.getCol() + distance);
                        otherRegion(check);
                        if(otherRegion(check)){
                            return 100*(distance)+(long)String.valueOf(check.getDeposit()).length();
                        }
                    }else {
                        check = territory.getRegion(currentRegion.getRow(), currentRegion.getCol() + distance);
                        otherRegion(check);
                        if (otherRegion(check)) {
                            return 100 * (distance) + (long) String.valueOf(check.getDeposit()).length();
                        }
                    }
                    distance++;
                }
                return 0;
            case 5 : // downleft
                while(check != null){
                    if(check.getCol() %2 == 0){
                        check = territory.getRegion(currentRegion.getRow(), currentRegion.getCol() - distance);
                        otherRegion(check);
                        if(otherRegion(check)){
                            return 100*(distance)+(long)String.valueOf(check.getDeposit()).length();
                        }
                    }else {
                        check = territory.getRegion(currentRegion.getRow() + distance, currentRegion.getCol() - distance);
                        otherRegion(check);
                        if (otherRegion(check)) {
                            return 100 * (distance) + (long) String.valueOf(check.getDeposit()).length();
                        }
                    }
                    distance++;
                }
                return 0;
            case 3 : // downright
                while(check != null){
                    if(check.getCol() %2 == 0){
                        check = territory.getRegion(currentRegion.getRow() , currentRegion.getCol() + distance);
                        otherRegion(check);
                        if(otherRegion(check)){
                            return 100*(distance)+(long)String.valueOf(check.getDeposit()).length();
                        }
                    }else {
                        check = territory.getRegion(currentRegion.getRow() + distance, currentRegion.getCol() + distance);
                        otherRegion(check);
                        if (otherRegion(check)) {
                            return 100 * (distance) + (long) String.valueOf(check.getDeposit()).length();
                        }
                    }
                    distance++;
                }
                return 0;
        }
        return 0;
    }

    public void instmove(long instantdirection , long row , long col) { // แก้แล้ว
        switch ((int) instantdirection) {
            case 1:
                row -= 1;
                break;
            case 4:
                row += 1;
                break;
            case 6:
                if(col %2 == 0){
                    row -= 1;
                    col -= 1;
                    break;
                }else{
                    row -= 1;
                    break;
                }
            case 2:
                if(col %2 == 0){
                    row -= 1;
                    col += 1;
                    break;
                }else{
                    col += 1;
                    break;
                }
            case 5:
                if(col %2 == 0){
                    col -= 1;
                    break;
                }else{
                    row += 1;
                    col -= 1;
                    break;
                }
            case 3:
                if(col %2 == 0){
                    col += 1;
                    break;
                }else{
                    row += 1;
                    col += 1;
                    break;
                }
        }
    }

    public void move(long direction){ // ok
        Region des;
        if(direction == 1){
            des = territory.getUp(currow,curcol);
            if(des == null){
                System.out.println("Cannot move to an out-of-territory region.");
            }else{
                System.out.println("move up: (" + currow + "," + curcol + ") -> ("+ des.getRow() + "," + des.getCol() + ")");
                setCurrentRegion(des.getRow(), des.getCol());
            }
        } else if (direction == 2) {
            des = territory.getUpRight(currow,curcol);
            if(des == null){
                System.out.println("Cannot move to an out-of-territory region.");
            }else{
                System.out.println("move upright: (" + currow + "," + curcol + ") -> ("+ des.getRow() + "," + des.getCol() + ")");
                setCurrentRegion(des.getRow(), des.getCol());
            }
        } else if (direction == 3){
            des = territory.getDownRight(currow,curcol);
            if(des == null){
                System.out.println("Cannot move to an out-of-territory region.");
            }else{
                System.out.println("move downright: (" + currow + "," + curcol + ") -> ("+ des.getRow() + "," + des.getCol() + ")");
                setCurrentRegion(des.getRow(), des.getCol());
            }
        } else if (direction == 4){
            des = territory.getDown(currow,curcol);
            if(des == null){
                System.out.println("Cannot move to an out-of-territory region.");
            }else{
                System.out.println("move down: (" + currow + "," + curcol + ") -> ("+ des.getRow() + "," + des.getCol() + ")");
                setCurrentRegion(des.getRow(), des.getCol());
            }
        } else if (direction == 5){
            des = territory.getDownLeft(currow,curcol);
            if(des == null){
                System.out.println("Cannot move to an out-of-territory region.");
            }else{
                System.out.println("move downleft: (" + currow + "," + curcol + ") -> ("+ des.getRow() + "," + des.getCol() + ")");
                setCurrentRegion(des.getRow(), des.getCol());
            }
        }else {
            des = territory.getUpLeft(currow,curcol);
            if(des == null){
                System.out.println("Cannot move to an out-of-territory region.");
            }else{
                System.out.println("move upleft: (" + currow + "," + curcol + ") -> ("+ des.getRow() + "," + des.getCol() + ")");
                setCurrentRegion(des.getRow(), des.getCol());
            }
        }
    }

    public void invest(long num){ // แก้แล้ว
        if(!myRegion(currentRegion) && !otherRegion(currentRegion) && isMyRegionAroundHere()); /* player nearby แล้วมีพื้นที่ตัวเองอยู่ */ { //ถ้าไม่ใช่ region เรา (ว่าง) ต้องแบ่งมั้ยว่า isRegion เรา isRegion คนอื่น
            if (this.budget > currentRegion.getDeposit() + 1){
                this.budget -= currentRegion.getDeposit() + 1;
                addRegion(currentRegion);
            }
            else {
                this.budget -= 1;
            }
        }
    }

    public void shoot(long instantdirection , long stake) { // แก้แล้ว final
        if(budget < stake+1) return;
        budget -= stake+1;
//        switch ((int) instantdirection) {
//            case 1: // up
//                if (this.budget < stake + 1) {}
//                else {
//                    if (territory.getUp(currow, curcol).getDeposit() - stake < 0) {
//                        removeRegion(territory.getUp(currentRegion.getRow(), currentRegion.getCol()));
//                    }
//                }
//            case 4: // down
//                if (this.budget < stake + 1) {
//
//                } else {
//                    if (territory.getDown(currentRegion.getRow(), currentRegion.getCol()).getDeposit() - stake < 0) {
//                        removeRegion(territory.getDown(currentRegion.getRow(), currentRegion.getCol()));
//                    }
//                }
//            case 6: // upleft
//                if (this.budget < stake + 1) {
//
//                } else {
//                        if (territory.getUpLeft(currentRegion.getRow(), currentRegion.getCol()).getDeposit() - stake < 0) {
//                            removeRegion(territory.getUpLeft(currentRegion.getRow(), currentRegion.getCol()));
//                        }
//                }
//            case 2: //upright
//                if (this.budget < stake + 1) {
//
//                } else {
//                        if (territory.getUpRight(currentRegion.getRow(), currentRegion.getCol()).getDeposit() - stake < 0) {
//                            removeRegion(territory.getUpRight(currentRegion.getRow(), currentRegion.getCol()));
//                        }
//                }
//            case 5: //downleft
//                if (this.budget < stake + 1) {
//
//                } else {
//                        if (territory.getDownLeft(currentRegion.getRow(), currentRegion.getCol()).getDeposit() - stake < 0) {
//                            removeRegion(territory.getDownLeft(currentRegion.getRow(), currentRegion.getCol()));
//                        }
//                }
//            case 3: //downright
//                if (this.budget < stake + 1) {
//
//                } else {
//                        if (territory.getDownRight(currentRegion.getRow(), currentRegion.getCol()).getDeposit() - stake < 0) {
//                            removeRegion(territory.getDownRight(currentRegion.getRow(), currentRegion.getCol()));
//                        }
//                }
//
//        }
    }

    public long getCurrow(){
        return this.currow;
    }

    public long getCurcol(){
        return this.curcol;
    }

    public void setCurrentRegion(long currow, long curcol){
        this.currow = currow;
        this.curcol = curcol;
        currentRegion = territory.getRegion(currow, curcol);
    }

    public boolean isMyRegionAroundHere(){ // แก้ isMyRegionAroundHere
        if(myRegion(territory.getUp(currentRegion.getRow(),currentRegion.getCol())) && myRegion(territory.getDown(currentRegion.getRow(),currentRegion.getCol())) && myRegion(territory.getUpRight(currentRegion.getRow(),currentRegion.getCol())) && myRegion(territory.getUpLeft(currentRegion.getRow(),currentRegion.getCol())) && myRegion(territory.getDownLeft(currentRegion.getRow(),currentRegion.getCol())) && myRegion(territory.getDownLeft(currentRegion.getRow(),currentRegion.getCol()))){
            return true;
        } else return false;
    }

    public long getBudget() {
        return budget;
    }

    public void setBudget(long money){
        budget = money;
    }

    public Region getCityCenter(){
        return cityCenter;
    }

    public void setCityCenter(Region cc){
        cityCenter = cc;
    }

    public List<Region> getOwnedRegion() {
        return ownedRegion;
    }
}



