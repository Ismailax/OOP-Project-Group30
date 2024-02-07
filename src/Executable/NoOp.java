package Executable;

import java.util.Map;

public class NoOp implements Executable{
    public NoOp(){
        System.out.println("No operation");
    }
    @Override
    public void execute(Map<String, Integer> bindings) {

    }
}
