package PlanParser.Executable;

import java.util.Map;

public class Done implements Executable{
    public Done(){
        System.out.println("done");
    }

    @Override
    public void execute(Map<String, Long> bindings) {

    }
}
