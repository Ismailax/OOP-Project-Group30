package PlanParser.Executable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

public class BlockStatement implements Executable{
    private List<Executable> statements;
    Map<String, Long> bindings;

    public BlockStatement(Map<String, Long> bindings) {
        statements = new LinkedList<>();
        this.bindings = bindings;
    }

    public void addStatement(Executable statement) {
        statements.add(statement);
    }

    @Override
    public void execute(Map<String, Long> bindings) {
        for (Executable statement : statements) {
            if(statement instanceof Done) break;
            else statement.execute(bindings);
        }
    }
}
