package PlanParser.Executable;
import PlanParser.Evaluable.*;

import java.util.Map;

public class WhileStatement implements Executable{
    private Executable statement;
    private Evaluable expression;

    public WhileStatement(Executable statement, Evaluable expression, Map<String, Long> bindings) {
        this.statement = statement;
        this.expression = expression;
    }

    @Override
    public void execute(Map<String, Long> bindings) {
        for(int i = 0; i < 10000 && expression.eval(bindings) > 0; i++){
            statement.execute(bindings);
        }
    }
}
