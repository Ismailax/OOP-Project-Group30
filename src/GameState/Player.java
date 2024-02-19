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


//            for(sightcol = 1+currentRegion.getCol() ; sightcol < 100 ; sightcol++){
            long distance = 1;
            while(!territory.getRegion()){
                if(territory.getRegion(currentRegion.getCol(),currentRegion.getRow()-distance))  {
                    if (isOtherOwnRegion) { // Region คนอื่น ??
                        String str_comb_dd = Long.toString(distance) + Long.toString(1);
                        return Long.parseLong(str_comb_dd);
                    }
                }else if(territory.getRegion(currentRegion.getCol(),currentRegion.getRow()-distance)

//            for(sightrow = 1+currentRegion.getRow() ; sightrow <= 6 ; sightrow++){ // distance ไม่รู้ว่าจะ set loop ไปจบไหนเพราะรอเช็คว่าตก null หรือป่าว
//                if(territory.getRegion(sightcol,sightrow) == null){ // ตก map แล้ว
//                    break;
//                }
//                if(isOtherOwnRegion){ // Region คนอื่น ??
//                    String str_comb_dd = Long.toString(sightcol-currentRegion.getCol()) + Long.toString(sightrow-currentRegion.getRow());
//                    return Long.parseLong(str_comb_dd);
//                }
//            }
//        }
//        return 0; // กรณีไม่เจอเมืองใครเลย ไว้ตรงนี้มั้ยวะ
    }
    }

    public long nearby(int instantdirection){
        switch (instantdirection){
            case 1 : // up
                for (long i = currentRegion.getRow()-1 ; i < 100 ; i++){
                    otherRegion(territory.getRegion(currentRegion.getCol(),i));
                    if(isOtherOwnRegion){
                        return  100*(currentRegion.getRow() - i) + (long)String.valueOf(territory.getRegion(currentRegion.getCol(),i).getDeposit()).length();
                    }
                    else return 0;
                }
            case 4 : // down
                for (long i = currentRegion.getRow()+1 ; i < 100 ; i++){
                    otherRegion(territory.getRegion(currentRegion.getCol(),i));
                    if(isOtherOwnRegion){
                        return  100*(i - currentRegion.getRow()) + (long)String.valueOf(territory.getRegion(currentRegion.getCol(),i).getDeposit()).length();
                    }
                    else return 0;
                }
            case 6 : // upleft
                for (long i = currentRegion.getRow()-1 ; i < 100 ; i++){
                    for(long j = currentRegion.getCol()-1 ; j < 100 ; j++){
                        otherRegion(territory.getRegion(j,i));
                        if(isOtherOwnRegion){
                            return  100*(currentRegion.getRow() - i) + (long)String.valueOf(territory.getRegion(j,i).getDeposit()).length();
                        }
                        else return 0;
                    }
                }
            case 2 : // upright
                for (long i = currentRegion.getRow()-1 ; i < 100 ; i++){
                    for(long j = currentRegion.getCol()+1 ; j < 100 ; j++){
                        otherRegion(territory.getRegion(j,i));
                        if(isOtherOwnRegion){
                            return  100*(currentRegion.getRow() - i) + (long)String.valueOf(territory.getRegion(j,i).getDeposit()).length();
                        }
                        else return 0;
                    }
                }
            case 5 : // downleft
                for (long i = currentRegion.getRow()+1 ; i < 100 ; i++){
                    for(long j = currentRegion.getCol()-1 ; j < 100 ; j++){
                        otherRegion(territory.getRegion(j,i));
                        if(isOtherOwnRegion){
                            return  100*(i - currentRegion.getRow()) + (long)String.valueOf(territory.getRegion(j,i).getDeposit()).length();
                        }
                        else return 0;
                    }
                }
            case 3 : // downright
                for (long i = currentRegion.getRow()+1 ; i < 100 ; i++){
                    for(long j = currentRegion.getCol()+1 ; j < 100 ; j++){
                        otherRegion(territory.getRegion(j,i));
                        if(isOtherOwnRegion){
                            return  100*(i - currentRegion.getRow()) + (long)String.valueOf(territory.getRegion(j,i).getDeposit()).length();
                        }
                        else return 0;
                    }
                }
        }
    }

    public void move(int instantdirection){
        switch (instantdirection){
            case 1 : // up
                this.currow -= 1;
                break;
            case 4 : // down
                this.currow += 1;
                break;
            case 6 : // upleft
                this.currow -= 1;
                this.curcol -= 1;
                break;
            case 2 : // upright
                this.currow -= 1;
                this.curcol += 1;
                break;
            case 5 : // downleft
                this.currow += 1;
                this.curcol -= 1;
                break;
            case 3 : // downright
                this.currow += 1;
                this.curcol += 1;
                break;
        }
    }

    public void invest(long num){
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

    public void shoot(int instantdirection , long stake){
        switch (instantdirection){
            case 1: // up
                if(this.budget < stake+1){

                }
                else {
                    if(territory.getRegion(currentRegion.getCol(),currentRegion.getRow()-1).getDeposit() - stake < 0){
                        removeRegion(territory.getRegion(currentRegion.getCol(),currentRegion.getRow()-1));
                    }
                }
            case 4: // down
                if(this.budget < stake+1){

                }
                else {
                    if(territory.getRegion(currentRegion.getCol(),currentRegion.getRow()+1).getDeposit() - stake < 0){
                        removeRegion(territory.getRegion(currentRegion.getCol(),currentRegion.getRow()+1));
                    }
                }
            case 6: // upleft
                if(this.budget < stake+1){

                }
                else {
                    if(territory.getRegion(currentRegion.getCol()-1,currentRegion.getRow()-1).getDeposit() - stake < 0){
                        removeRegion(territory.getRegion(currentRegion.getCol()-1,currentRegion.getRow()-1));
                    }
                }
            case 2: //upright
                if(this.budget < stake+1){

                }
                else {
                    if(territory.getRegion(currentRegion.getCol()+1,currentRegion.getRow()-1).getDeposit() - stake < 0){
                        removeRegion(territory.getRegion(currentRegion.getCol()+1,currentRegion.getRow()-1));
                    }
                }
            case 5: //downleft
                if(this.budget < stake+1){

                }
                else {
                    if(territory.getRegion(currentRegion.getCol()-1,currentRegion.getRow()+1).getDeposit() - stake < 0){
                        removeRegion(territory.getRegion(currentRegion.getCol()-1,currentRegion.getRow()+1));
                    }
                }
            case 3: //downright
                if(this.budget < stake+1){

                }
                else {
                    if(territory.getRegion(currentRegion.getCol()+1,currentRegion.getRow()+1).getDeposit() - stake < 0){
                        removeRegion(territory.getRegion(currentRegion.getCol()+1,currentRegion.getRow()+1));
                    }
                }
        }

    }

    public Region setCurrentRegion(long curcol, long currow){
        return currentRegion = territory.getRegion(curcol,currow);
    }

    public long getCurrentRow() {
        return currentRegion.getRow();
    }

    public long getCurrentCol() {
        return currentRegion.getCol();
    }

    public boolean isMyRegionAroundHere(){
        int distance = 1;
        for(long row = currentRegion.getRow() - distance ; row <= currentRegion.getRow() + distance ; row++){
            for (long col = currentRegion.getCol() - distance ; col <= currentRegion.getCol() + distance ; col++){
                if(row == currentRegion.getRow() && col == currentRegion.getCol()){
                    continue;
                }
                Region region = territory.getRegion(col,row);
                if(myRegion(region)){
                    return true;
                }
            }
        }
        return false;
    }

    // time to edit plan

}
