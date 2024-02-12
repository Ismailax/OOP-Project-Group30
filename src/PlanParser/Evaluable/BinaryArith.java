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
//                System.out.println(lv + " + " + rv);
                return lv + rv;
            }
            case "-" -> {
//                System.out.println(lv + " - " + rv);
                return lv - rv;
            }
            case "*" -> {
//                System.out.println(lv + " * " + rv);
                return lv * rv;
            }
            case "/" -> {
//                System.out.println(lv + " / " + rv);
                return lv / rv;
            }
            case "%" -> {
//                System.out.println(lv + " % " + rv);
                return lv % rv;
            }
            case "^" -> {
//                System.out.println(lv + " ^ " + rv);
                return (int) Math.pow(lv, rv);
            }
            default -> {
                throw new EvalError("unknown operator " + operator);
            }
        }
    }
}
