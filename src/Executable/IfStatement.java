package Executable;
import Evaluable.*;


public class IfStatement implements Executable{
    Executable trueStatement;
    Executable falseStatement;
    Evaluable expression;

    public IfStatement(Executable trueStatement, Executable falseStatement, Evaluable expression) {
        this.trueStatement = trueStatement;
        this.falseStatement = falseStatement;
        this.expression = expression;
    }

    @Override
    public void execute() {
        if (expression.eval() > 0) trueStatement.execute();
        else falseStatement.execute();
    }
}
