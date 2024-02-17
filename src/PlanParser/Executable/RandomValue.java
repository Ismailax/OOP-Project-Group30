package PlanParser.Executable;

import PlanParser.Evaluable.Evaluable;

import java.util.Map;
import java.util.Random;

public class RandomValue implements Evaluable {
    private static final Random random = new Random();

    @Override
    public long eval(Map<String, Long> bindings) {
        return random.nextInt(1000);
    }
}
