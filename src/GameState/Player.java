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
    private boolean isRegion; //เช็คว่าเจอ region ไหม
    private Region currentRegion;


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
    }

    public void removeRegion(Region region){
        ownedRegion.remove(region);
    }

    public long opponent(){
        List<Long> temp = new ArrayList<>();
        long direction,distance; // direction กูไม่รู้ว่ากำหนดยังไงเดี๋ยวค่อยไป implement direction อีกทีวะจะให้มันมองไปยังไง
        for(distance= 1 ; distance <= 100 ; distance++){
            for(direction = 1 ; direction <= 6 ; direction++){ // distance ไม่รู้ว่าจะ set loop ไปจบไหนเพราะรอเช็คว่าตก null หรือป่าว
                if(distance == null){ // ตก map แล้ว
                    break;
                }
                if(isRegion){ // Region คนอื่น ??
                    String str_comb_dd = Long.toString(distance) + direction;
                    return Long.parseLong(str_comb_dd);
                }
            }
        }
        return 0; // กรณีไม่เจอเมืองใครเลย ไว้ตรงนี้มั้ยวะ
    }

    public long nearby(/*ทิศทาง*/){
        long direction,distance;
        switch (/*ทิศทาง*/){
            case "up" :
                for (){

                }
        }
    }

    public void move(/*ทิศทาง*/){

    }

    public void invest(long num){
        if(!isRegion && /* player nearby แล้วมีพื้นที่ตัวเองอยู่ */) { //ถ้าไม่ใช่ region เรา (ว่าง) ต้องแบ่งมั้ยว่า isRegion เรา isRegion คนอื่น
            if (this.budget > currentRegion.getDeposit() + 1){
                this.budget -= currentRegion.getDeposit() +1;
                addRegion(currentRegion);
            }
            else {
                this.budget -= 1;
            }
        }
    }

    public Region getCurrentRegion(){
        return currentRegion;
    }

    public Region setCurrentRegion(Region currentRegion){
        return this.currentRegion = currentRegion;
    }

    public void moveToRegion(Region region){
        setCurrentRegion(region);
    }

    // time to edit plan

}
