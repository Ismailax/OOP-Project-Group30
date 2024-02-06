package Executable;
import Evaluable.*;
import java.util.Map;

public class IfStatement implements Executable{
    private Executable trueStatement;
    private Executable falseStatement;
    private Evaluable expression;

    public IfStatement(Executable trueStatement, Executable falseStatement, Evaluable expression) {
        this.trueStatement = trueStatement;
        this.falseStatement = falseStatement;
        this.expression = expression;
    }

    @Override
    public void execute(Map<String, Integer> bindings) {
        if (expression.eval(bindings) > 0) trueStatement.execute(bindings);
        else falseStatement.execute(bindings);
    }
}
