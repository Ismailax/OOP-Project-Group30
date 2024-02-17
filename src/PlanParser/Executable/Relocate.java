package PlanParser.Executable;

import java.util.Map;

public class Relocate implements Executable{
    public Relocate(){
        System.out.println("Parse relocate");
    }
    @Override
    public void execute(Map<String, Long> bindings) {
        System.out.println("Execute relocate");
    }
}
