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
    private boolean isOwnRegion; //ใช่พื้นที่เราไหม
    private boolean isOtherOwnRegion;
    private Region currentRegion;
    private Territory territory;


    public Player(String name){
        this.name = name;
        bindings = new HashMap<>();
        ownedRegion = new ArrayList<>();
    }

    public Executable eval(){
        Parser p = new Parser(new Tokenizer(plan));
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

    public boolean myRegion(Region region){ // เช็คว่าใช่ region เราไหม
        if(ownedRegion.contains(region)){
            return isOwnRegion = true;
        }
        else return isOwnRegion = false;
    }

    public boolean otherRegion(Region region){ // เช็คว่าใช้ region คนอื่นไหม
        if(region.getOwner() != this){
           return isOtherOwnRegion = true;
        }
        else return isOtherOwnRegion = false;
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

    public long opponent(){
            long distance = 1;
            boolean hasReturn = false;
            Region check = territory.getRegion(currentRegion.getRow(),currentRegion.getCol());
            while (true) { // up
                if(check == null){
                    continue;
                }
                check = territory.getRegion(currentRegion.getRow() - distance, currentRegion.getCol());
                if (check.getOwner() != null && check.getOwner() != this) {
                    String str_comb_dd = Long.toString(distance) + Long.toString(1);
                    return Long.parseLong(str_comb_dd);
                } // down
                check = territory.getRegion(currentRegion.getRow() + distance, currentRegion.getCol());
                if (check.getOwner() != null && check.getOwner() != this) {
                    String str_comb_dd = Long.toString(distance) + Long.toString(4);
                    return Long.parseLong(str_comb_dd);
                } // upleft
                    if(currentRegion.getCol() %2 == 0) {
                        check = territory.getRegion(currentRegion.getRow() - distance, currentRegion.getCol() - distance);
                    }else check = territory.getRegion(currentRegion.getRow() , currentRegion.getCol() - distance);
                if (check.getOwner() != null && check.getOwner() != this) {
                    String str_comb_dd = Long.toString(distance) + Long.toString(6);
                    return Long.parseLong(str_comb_dd);
                } // upright
                    if(currentRegion.getCol() %2 == 0) {
                        check = territory.getRegion(currentRegion.getRow() - distance, currentRegion.getCol() + distance);
                    }else check = territory.getRegion(currentRegion.getRow(), currentRegion.getCol() + distance);
                if (check.getOwner() != null && check.getOwner() != this) {
                    String str_comb_dd = Long.toString(distance) + Long.toString(2);
                    return Long.parseLong(str_comb_dd);
                } // downleft
                    if(currentRegion.getCol() %2 == 0) {
                        check = territory.getRegion(currentRegion.getRow(), currentRegion.getCol() - distance);
                    }else check = territory.getRegion(currentRegion.getRow() + distance, currentRegion.getCol() - distance);
                if (check.getOwner() != null && check.getOwner() != this) {
                    String str_comb_dd = Long.toString(distance) + Long.toString(5);
                    return Long.parseLong(str_comb_dd);
                } // downright
                    if(currentRegion.getCol() %2 == 0) {
                        check = territory.getRegion(currentRegion.getRow(), currentRegion.getCol() + distance);
                    }else check = territory.getRegion(currentRegion.getRow() + distance, currentRegion.getCol() + distance);
                    if (check.getOwner() != null && check.getOwner() != this) {
                    String str_comb_dd = Long.toString(distance) + Long.toString(3);
                    return Long.parseLong(str_comb_dd);
                }
                distance++;
            }
            return 0;
    }

    public long nearby(int instantdirection){ // แก้ direction ตาม move % 2
        switch (instantdirection){
            case 1 : // up
                for (long i = currentRegion.getRow()-1 ; i < 12 ; i++){
                    otherRegion(territory.getRegion(i,currentRegion.getCol()));
                    if(isOtherOwnRegion){
                        return  100*(currentRegion.getRow() - i) + (long)String.valueOf(territory.getRegion(i,currentRegion.getCol()).getDeposit()).length();
                    }
                    else return 0;
                }
            case 4 : // down
                for (long i = currentRegion.getRow()+1 ; i < 12 ; i++){
                    otherRegion(territory.getRegion(i,currentRegion.getCol()));
                    if(isOtherOwnRegion){
                        return  100*(i - currentRegion.getRow()) + (long)String.valueOf(territory.getRegion(i,currentRegion.getCol()).getDeposit()).length();
                    }
                    else return 0;
                }
            case 6 : // upleft
                    Region check = currentRegion;
                    long distance = 1;
                    while(check != null){
                        if(check.getCol() %2 == 0){
                            check = territory.getRegion(currentRegion.getRow()-distance , currentRegion.getCol()-distance);
                            otherRegion(check);
                            if(isOtherOwnRegion){
                                return 100*(distance)+(long)String.valueOf(check.getDeposit()).length();
                            }
                        }else {
                            check = territory.getRegion(currentRegion.getRow(), currentRegion.getCol() - distance);
                            otherRegion(check);
                            if (isOtherOwnRegion) {
                                return 100 * (distance) + (long) String.valueOf(check.getDeposit()).length();
                            }
                        }
                        distance++;
                    }
            case 2 : // upright
                for (long i = currentRegion.getRow()-1 ; i < 12 ; i++){
                    for(long j = currentRegion.getCol()+1 ; j < 12 ; j++){
                        otherRegion(territory.getRegion(i,j));
                        if(isOtherOwnRegion){
                            return  100*(currentRegion.getRow() - i) + (long)String.valueOf(territory.getRegion(i,j).getDeposit()).length();
                        }
                        else return 0;
                    }
                }
            case 5 : // downleft
                for (long i = currentRegion.getRow()+1 ; i < 12 ; i++){
                    for(long j = currentRegion.getCol()-1 ; j < 12 ; j++){
                        otherRegion(territory.getRegion(i,j));
                        if(isOtherOwnRegion){
                            return  100*(i - currentRegion.getRow()) + (long)String.valueOf(territory.getRegion(i,j).getDeposit()).length();
                        }
                        else return 0;
                    }
                }
            case 3 : // downright
                for (long i = currentRegion.getRow()+1 ; i < 12 ; i++){
                    for(long j = currentRegion.getCol()+1 ; j < 12 ; j++){
                        otherRegion(territory.getRegion(i,j));
                        if(isOtherOwnRegion){
                            return  100*(i - currentRegion.getRow()) + (long)String.valueOf(territory.getRegion(i,j).getDeposit()).length();
                        }
                        else return 0;
                    }
                }
        }
        return 0;
    }

    public void move(int instantdirection){
        switch (instantdirection){
            case 1 : // up
                this.currow -= 1;
                setCurrentRegion(currow, curcol);
                break;
            case 4 : // down
                this.currow += 1;
                setCurrentRegion(currow,curcol);
                break;
            case 6 : // upleft
                if(curcol %2 == 0) {
                    this.currow -= 1;
                    this.curcol -= 1;
                }
                else this.currow -= 1;
                setCurrentRegion(currow,curcol);
                break;
            case 2 : // upright
                if(curcol %2 == 0) {
                    this.currow -= 1;
                    this.curcol += 1;
                }
                else this.curcol += 1;
                setCurrentRegion(currow,curcol);
                break;
            case 5 : // downleft
                if(curcol %2 == 0) {
                    this.curcol -= 1;
                }
                else {
                    this.currow += 1;
                    this.curcol -= 1;
                }
                setCurrentRegion(currow,curcol);
                break;
            case 3 : // downright
                if(curcol %2 == 0) {
                    this.curcol += 1;
                }
                else {
                    this.curcol += 1;
                    this.currow += 1;
                }
                setCurrentRegion(currow,curcol);
                break;
        }
    }

    public void invest(long num){ // แก้แล้ว
        if(!myRegion(currentRegion) && !otherRegion(currentRegion) && isMyRegionAroundHere()); /* player nearby แล้วมีพื้นที่ตัวเองอยู่ */) { //ถ้าไม่ใช่ region เรา (ว่าง) ต้องแบ่งมั้ยว่า isRegion เรา isRegion คนอื่น
            if (this.budget > currentRegion.getDeposit() + 1){
                this.budget -= currentRegion.getDeposit() + 1;
                addRegion(currentRegion);
            }
            else {
                this.budget -= 1;
            }
        }
    }

    public void shoot(int instantdirection , long stake){ // แก้ direction ตาม move % 2
        switch (instantdirection){
            case 1: // up
                if(this.budget < stake+1){

                }
                else {
                    if(territory.getRegion(currentRegion.getRow()-1,currentRegion.getCol()).getDeposit() - stake < 0){
                        removeRegion(territory.getRegion(currentRegion.getRow()-1,currentRegion.getCol()));
                    }
                }
            case 4: // down
                if(this.budget < stake+1){

                }
                else {
                    if(territory.getRegion(currentRegion.getRow()+1,currentRegion.getCol()).getDeposit() - stake < 0){
                        removeRegion(territory.getRegion(currentRegion.getRow()+1,currentRegion.getCol()));
                    }
                }
            case 6: // upleft
                if(this.budget < stake+1){

                }
                else {
                    if(territory.getRegion(currentRegion.getRow()-1,currentRegion.getCol()-1).getDeposit() - stake < 0){
                        removeRegion(territory.getRegion(currentRegion.getRow()-1,currentRegion.getCol()-1));
                    }
                }
            case 2: //upright
                if(this.budget < stake+1){

                }
                else {
                    if(territory.getRegion(currentRegion.getRow()-1,currentRegion.getCol()+1).getDeposit() - stake < 0){
                        removeRegion(territory.getRegion(currentRegion.getRow()-1,currentRegion.getCol()+1));
                    }
                }
            case 5: //downleft
                if(this.budget < stake+1){

                }
                else {
                    if(territory.getRegion(currentRegion.getRow(),currentRegion.getCol()-1).getDeposit() - stake < 0){
                        removeRegion(territory.getRegion(currentRegion.getRow(),currentRegion.getCol()-1));
                    }
                }
            case 3: //downright
                if(this.budget < stake+1){

                }
                else {
                    if(territory.getRegion(currentRegion.getRow(),currentRegion.getCol()+1).getDeposit() - stake < 0){
                        removeRegion(territory.getRegion(currentRegion.getRow(),currentRegion.getCol()+1));
                    }
                }
        }

    }

    public Region setCurrentRegion(long currow, long curcol){
        return currentRegion = territory.getRegion(currow,curcol);
    }

    public long getCurrentRow() {
        return currentRegion.getRow();
    }

    public long getCurrentCol() {
        return currentRegion.getCol();
    }

    public boolean isMyRegionAroundHere(){ // แก้ direction ตาม move % 2
        int distance = 1;
        for(long row = currentRegion.getRow() - distance ; row <= currentRegion.getRow() + distance ; row++){
            for (long col = currentRegion.getCol() - distance ; col <= currentRegion.getCol() + distance ; col++){
                if(row == currentRegion.getRow() && col == currentRegion.getCol()){
                    continue;
                }
                Region region = territory.getRegion(row,col);
                if(myRegion(region)){
                    return true;
                }
            }
        }
        return false;
    }

    // time to edit plan

}
