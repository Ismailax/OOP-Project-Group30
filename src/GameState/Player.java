package GameState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import PlanParser.Parser;
import PlanParser.Tokenizer;

public class Player {
    private long currow;
    private long curcol;
    private final String name;
    private Map<String, Long> bindings;
    private String plan;
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

    public void eval(){
        Parser p = new Parser(new Tokenizer(plan));
        p.parse(bindings);
    }

    public void setPlan(String plan) {
        this.plan = plan;
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

    public long opponent(){ // currentRegion + 1
        long sightrow;
        long sightcol;
        for(sightcol = 1+currentRegion.getCol() ; sightcol < 100 ; sightcol++){
            for(sightrow = 1+currentRegion.getRow() ; sightrow <= 6 ; sightrow++){ // distance ไม่รู้ว่าจะ set loop ไปจบไหนเพราะรอเช็คว่าตก null หรือป่าว
                if(sightcol == null || sightrow == null){ // ตก map แล้ว
                    break;
                }
                if(isOtherOwnRegion){ // Region คนอื่น ??
                    String str_comb_dd = Long.toString(sightcol-currentRegion.getCol()) + Long.toString(sightrow-currentRegion.getRow());
                    return Long.parseLong(str_comb_dd);
                }
            }
        }
        return 0; // กรณีไม่เจอเมืองใครเลย ไว้ตรงนี้มั้ยวะ
    }

    public long nearby(int instantdirection){
        switch (instantdirection){
            case 1 : // up
                for (long i = currentRegion.getRow()+1 ; i < 100 ; i++){
                    otherRegion(territory.getRegion(currentRegion.getCol(),i));
                    if(isOtherOwnRegion){
                        return  100*(i - currentRegion.getRow()) + (long)String.valueOf(territory.getRegion(currentRegion.getCol(),i).getDeposit()).length();
                    }
                    else return 0;
                }
            case 2 : // down
                for (long i = currentRegion.getRow()-1 ; i < 100 ; i--){
                    otherRegion(territory.getRegion(currentRegion.getCol(),i));
                    if(isOtherOwnRegion){
                        return  100*(currentRegion.getRow() - i) + (long)String.valueOf(territory.getRegion(currentRegion.getCol(),i).getDeposit()).length();
                    }
                    else return 0;
                }
            case 3 : // left
                for (long i = currentRegion.getCol()-1 ; i < 100 ; i--){
                    otherRegion(territory.getRegion(i,currentRegion.getRow()));
                    if(isOtherOwnRegion){
                        return  100*(currentRegion.getCol() - i) + (long)String.valueOf(territory.getRegion(i,currentRegion.getRow()).getDeposit()).length();
                    }
                    else return 0;
                }
            case 4 : // right
                for (long i = currentRegion.getCol()+1 ; i < 100 ; i++){
                    otherRegion(territory.getRegion(i,currentRegion.getRow()));
                    if(isOtherOwnRegion){
                        return  100*(i - currentRegion.getCol()) + (long)String.valueOf(territory.getRegion(i,currentRegion.getRow()).getDeposit()).length();
                    }
                    else return 0;
                }
            case 5 : // upleft
                for (long i = currentRegion.getRow()+1 ; i < 100 ; i++){
                    for(long j = currentRegion.getCol()-1 ; j < 100 ; j--){
                        otherRegion(territory.getRegion(j,i));
                        if(isOtherOwnRegion){
                            return  100*(i - currentRegion.getRow()) + (long)String.valueOf(territory.getRegion(j,i).getDeposit()).length();
                        }
                        else return 0;
                    }
                }
            case 6 : // upright
                for (long i = currentRegion.getRow()+1 ; i < 100 ; i++){
                    for(long j = currentRegion.getCol()+1 ; j < 100 ; j++){
                        otherRegion(territory.getRegion(j,i));
                        if(isOtherOwnRegion){
                            return  100*(i - currentRegion.getRow()) + (long)String.valueOf(territory.getRegion(j,i).getDeposit()).length();
                        }
                        else return 0;
                    }
                }
            case 7 : // downleft
                for (long i = currentRegion.getRow()-1 ; i < 100 ; i--){
                    for(long j = currentRegion.getCol()-1 ; j < 100 ; j--){
                        otherRegion(territory.getRegion(j,i));
                        if(isOtherOwnRegion){
                            return  100*(currentRegion.getRow() - i) + (long)String.valueOf(territory.getRegion(j,i).getDeposit()).length();
                        }
                        else return 0;
                    }
                }
            case 8 : // downright
                for (long i = currentRegion.getRow()+1 ; i < 100 ; i--){
                    for(long j = currentRegion.getCol()+1 ; j < 100 ; j++){
                        otherRegion(territory.getRegion(j,i));
                        if(isOtherOwnRegion){
                            return  100*(currentRegion.getRow() - i) + (long)String.valueOf(territory.getRegion(j,i).getDeposit()).length();
                        }
                        else return 0;
                    }
                }
        }
    }

    public void move(int instantdirection){
        switch (instantdirection){
            case 1 : // up
                this.currow += 1;
                break;
            case 2 : // down
                this.currow -= 1;
                break;
            case 3 : // left
                this.curcol -= 1;
                break;
            case 4 : // right
                this.curcol += 1;
                break;
            case 5 : // upleft
                this.currow += 1;
                this.curcol -= 1;
                break;
            case 6 : // upright
                this.currow += 1;
                this.curcol += 1;
                break;
            case 7 : // downleft
                this.currow -= 1;
                this.curcol -= 1;
                break;
            case 8 : // downright
                this.currow -= 1;
                this.curcol += 1;
                break;
        }
    }

    public void invest(long num){
        if(!myRegion(currentRegion) && !otherRegion(currentRegion) && /* เช็คว่ามีเมืองเรารอบๆมั้ย */); /* player nearby แล้วมีพื้นที่ตัวเองอยู่ */) { //ถ้าไม่ใช่ region เรา (ว่าง) ต้องแบ่งมั้ยว่า isRegion เรา isRegion คนอื่น
            if (this.budget > currentRegion.getDeposit() + 1){
                this.budget -= currentRegion.getDeposit() +1;
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
                    if(territory.getRegion(currentRegion.getCol(),currentRegion.getRow()+1).getDeposit() - stake < 0){
                        removeRegion(territory.getRegion(currentRegion.getCol(),currentRegion.getRow()+1));
                    }
                }
            case 2: // down
                if(this.budget < stake+1){

                }
                else {
                    if(territory.getRegion(currentRegion.getCol(),currentRegion.getRow()-1).getDeposit() - stake < 0){
                        removeRegion(territory.getRegion(currentRegion.getCol(),currentRegion.getRow()-1));
                    }
                }
            case 3: //left
                if(this.budget < stake+1){

                }
                else {
                    if(territory.getRegion(currentRegion.getCol()-1,currentRegion.getRow()).getDeposit() - stake < 0){
                        removeRegion(territory.getRegion(currentRegion.getCol()-1,currentRegion.getRow()));
                    }
                }
            case 4: // right
                if(this.budget < stake+1){

                }
                else {
                    if(territory.getRegion(currentRegion.getCol()+1,currentRegion.getRow()).getDeposit() - stake < 0){
                        removeRegion(territory.getRegion(currentRegion.getCol()+1,currentRegion.getRow()));
                    }
                }
            case 5: // upleft
                if(this.budget < stake+1){

                }
                else {
                    if(territory.getRegion(currentRegion.getCol()-1,currentRegion.getRow()+1).getDeposit() - stake < 0){
                        removeRegion(territory.getRegion(currentRegion.getCol()-1,currentRegion.getRow()+1));
                    }
                }
            case 6: //upright
                if(this.budget < stake+1){

                }
                else {
                    if(territory.getRegion(currentRegion.getCol()+1,currentRegion.getRow()+1).getDeposit() - stake < 0){
                        removeRegion(territory.getRegion(currentRegion.getCol()+1,currentRegion.getRow()+1));
                    }
                }
            case 7: //downleft
                if(this.budget < stake+1){

                }
                else {
                    if(territory.getRegion(currentRegion.getCol()-1,currentRegion.getRow()-1).getDeposit() - stake < 0){
                        removeRegion(territory.getRegion(currentRegion.getCol()-1,currentRegion.getRow()-1));
                    }
                }
            case 8: //downright
                if(this.budget < stake+1){

                }
                else {
                    if(territory.getRegion(currentRegion.getCol()+1,currentRegion.getRow()-1).getDeposit() - stake < 0){
                        removeRegion(territory.getRegion(currentRegion.getCol()+1,currentRegion.getRow()-1));
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

    // time to edit plan

}
