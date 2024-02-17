package PlanParser.Executable;

import java.util.Map;

public class Done implements Executable{
    public Done(){
        System.out.println("Parse done");
    }

    @Override
    public void execute(Map<String, Long> bindings) {
        System.out.println("Execute done");
    }
}
