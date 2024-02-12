package PlanParser.Executable;

import java.util.Map;

public interface Executable {
    void execute(Map<String, Long> bindings);
}
