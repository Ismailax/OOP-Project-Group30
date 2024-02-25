package PlanParser.Evaluable;

import PlanParser.Error.*;
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
    public long eval(Map<String, Long> bindings) {
        long lv = leftEval.eval(bindings);
        long rv = rightEval.eval(bindings);
        switch (operator) {
            case "+" -> {
                return lv + rv;
            }
            case "-" -> {
                return lv - rv;
            }
            case "*" -> {
                return lv * rv;
            }
            case "/" -> {
                return lv / rv;
            }
            case "%" -> {
                return lv % rv;
            }
            case "^" -> {
                return (int) Math.pow(lv, rv);
            }
            default -> {
                throw new EvalError("unknown operator " + operator);
            }
        }
    }
}
