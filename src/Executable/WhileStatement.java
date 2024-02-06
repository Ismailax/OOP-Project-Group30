package Executable;
import Evaluable.*;

public class WhileStatement implements Executable{
    Executable statement;
    Evaluable expression;

    public WhileStatement(Executable statement, Evaluable expression) {
        this.statement = statement;
        this.expression = expression;
    }

    @Override
    public void execute() {
        for(int i = 0; i < 10000 && expression.eval() > 0; i++){
            statement.execute();
        }
    }
}
