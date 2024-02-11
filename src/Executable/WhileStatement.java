package Executable;
import Evaluable.*;

import java.util.Map;

public class WhileStatement implements Executable{
    private Executable statement;
    private Evaluable expression;

    public WhileStatement(Executable statement, Evaluable expression, Map<String, Integer> bindings) {
        this.statement = statement;
        this.expression = expression;
        this.execute(bindings);
    }

    @Override
    public void execute(Map<String, Integer> bindings) {
        for(int i = 0; i < 10000 && expression.eval(bindings) > 0; i++){
            statement.execute(bindings);
        }
    }
}
