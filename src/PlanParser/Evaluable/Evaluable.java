package PlanParser.Evaluable;

import java.util.Map;

public interface Evaluable {
    long eval(Map<String, Long> bindings);
}
