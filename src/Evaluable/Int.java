package Evaluable;

import java.util.Map;

public class Int implements Evaluable {
    int num;
    public Int(int num){
        this.num = num;
    }
    @Override
    public int eval(Map<String, Integer> bindings) {
        return num;
    }
}

