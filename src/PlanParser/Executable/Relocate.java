package PlanParser.Executable;

import java.util.Map;

public class Relocate implements Executable{
    public Relocate(){
        System.out.println("relocated");
    }
    @Override
    public void execute(Map<String, Long> bindings) {

    }
}
