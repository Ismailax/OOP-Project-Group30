package Evaluable;

import java.util.Map;

public class BinaryArith implements Evaluable{
    Evaluable leftEval, rightEval;
    String operator;

    public BinaryArith(Evaluable leftEval, String operator, Evaluable rightEval) {
        this.leftEval = leftEval;
        this.rightEval = rightEval;
        this.operator = operator;
    }

    @Override
    public int eval(Map<String, Integer> bindings) {
        int lv = leftEval.eval(bindings);
        int rv = rightEval.eval(bindings);
        if(operator.equals("+")) return lv + rv;
        else if(operator.equals("-")) return lv - rv;
        else if(operator.equals("*")) return lv * rv;
        else if(operator.equals("/")) return lv / rv;
        else if(operator.equals("%")) return lv % rv;
        else if(operator.equals("^")) return (int) Math.pow(lv, rv);
        else return 0;
    }
}
