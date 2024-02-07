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
        switch (operator) {
            case "+" -> {
                System.out.println(lv + " + " + rv);
                return lv + rv;
            }
            case "-" -> {
                System.out.println(lv + " - " + rv);
                return lv - rv;
            }
            case "*" -> {
                System.out.println(lv + " * " + rv);
                return lv * rv;
            }
            case "/" -> {
                System.out.println(lv + " / " + rv);
                return lv / rv;
            }
            case "%" -> {
                System.out.println(lv + " % " + rv);
                return lv % rv;
            }
            case "^" -> {
                System.out.println(lv + " ^ " + rv);
                return (int) Math.pow(lv, rv);
            }
            default -> {
                return 0;
            }
        }
    }
}
