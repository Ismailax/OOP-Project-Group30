package Evaluable;

import java.util.Map;

public class Num implements Evaluable {
    int num;
    public Num(int num){
        this.num = num;
    }
    @Override
    public int eval(Map<String, Integer> bindings) {
        return num;
    }
}

