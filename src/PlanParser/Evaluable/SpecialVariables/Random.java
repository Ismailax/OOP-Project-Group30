package PlanParser.Evaluable.SpecialVariables;

import PlanParser.Evaluable.Evaluable;

import java.util.Map;

public class Random implements Evaluable {
    private static final java.util.Random random = new java.util.Random();

    @Override
    public long eval(Map<String, Long> bindings) {
        return random.nextInt(1000);
    }
}
