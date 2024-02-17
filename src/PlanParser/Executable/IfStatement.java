package PlanParser.Executable;
import PlanParser.Evaluable.*;
import java.util.Map;

public class IfStatement implements Executable{
    private Executable trueStatement;
    private Executable falseStatement;
    private Evaluable expression;


    public IfStatement(Executable trueStatement, Executable falseStatement, Evaluable expression , Map<String, Long> bindings) {
        this.trueStatement = trueStatement;
        this.falseStatement = falseStatement;
        this.expression = expression;
    }

    @Override
    public void execute(Map<String, Long> bindings) {
        if (expression.eval(bindings) > 0) trueStatement.execute(bindings);
        else falseStatement.execute(bindings);
    }
}
