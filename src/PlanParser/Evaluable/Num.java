package PlanParser.Evaluable;

import java.util.Map;

public class Num implements Evaluable {
    long num;
    public Num(long num){
        this.num = num;
    }
    @Override
    public long eval(Map<String, Long> bindings) {
        return num;
    }
}

