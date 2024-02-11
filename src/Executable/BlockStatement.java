package Executable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BlockStatement implements Executable{
    private List<Executable> statements;
    Map<String, Integer> bindings;

    public BlockStatement(Map<String, Integer> bindings) {
        statements = new LinkedList<>();
        this.bindings = bindings;
    }

    public void addStatement(Executable statement) {
        statements.add(statement);
    }

    @Override
    public void execute(Map<String, Integer> bindings) {
        for (Executable statement : statements)
            statement.execute(bindings);
    }
}
