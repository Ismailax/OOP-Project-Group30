package PlanParser.Executable;

import Game.Gameplay;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PlanExecutor implements Executable{
    Gameplay game;
    private List<Executable> statements;
    Map<String, Long> bindings;

    public PlanExecutor(Map<String, Long> bindings) {
//        this.game = game;
        statements = new LinkedList<>();
        this.bindings = bindings;
    }

    public void addStatement(Executable statement) {
        statements.add(statement);
    }

    @Override
    public void execute(Map<String, Long> bindings) {
        for (Executable statement : statements) {
//            if(game.checkWinner() != null) return;
            if(statement instanceof Done){
                statement.execute(bindings);
                return;
            } else statement.execute(bindings);
        }
    }
}
