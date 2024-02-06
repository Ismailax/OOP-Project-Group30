package Executable;
import java.util.LinkedList;
import java.util.List;

public class BlockStatement implements Executable{
    List<Executable> statements;

    public BlockStatement() {
        statements = new LinkedList<>();
    }

    public void addStatement(Executable statement) {
        statements.add(statement);
    }

    @Override
    public void execute() {
        for (Executable statement : statements)
            statement.execute();
    }
}
