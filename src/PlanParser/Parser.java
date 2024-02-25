package PlanParser;

import java.util.*;

import Game.Gameplay;
import GameState.Player;
import PlanParser.Error.*;
import PlanParser.Evaluable.SpecialVariables.*;
import PlanParser.Evaluable.SpecialVariables.Random;
import PlanParser.Executable.*;
import PlanParser.Evaluable.*;

public class Parser {
    private Gameplay game;
    private Player player;
    private final Tokenizer tkz;
    private Map<String, Long> bindings;
    private final String[] reservedWordList = {"collect", "done", "down", "downleft", "downright", "else", "if", "invest", "move", "nearby", "opponent", "relocate", "shoot", "then", "up", "upleft", "upright", "while"};
    private final Set<String> reservedWords = new HashSet<>(List.of(reservedWordList));
    private final String[] specialVarList = {"rows", "cols", "currow", "curcol", "budget", "deposit", "int", "maxdeposit", "random"};
    private final Set<String> specialVariables = new HashSet<>(List.of(specialVarList));
    private final String[] directionList = {"up", "down", "upleft", "upright", "downleft", "downright"};
    private final Set<String> directions = new HashSet<>(List.of(directionList));

    public Parser(Tokenizer tkz){
        this.tkz = tkz;
        this.bindings = new HashMap<>();
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
    public void setGame(Gameplay game) {this.game = game;}

    public Executable parse(Map<String, Long> bindings) throws SyntaxError {
        this.bindings = bindings;
        BlockStatement statements = new BlockStatement(bindings);
        statements.addStatement(parseStatement());
        while (tkz.hasNextToken()) statements.addStatement(parseStatement());
        return statements;
    }

    public Executable parseStatement() throws SyntaxError {
        if(tkz.peek("if")) return parseIfStatement();
        if(tkz.peek("while")) return parseWhileStatement();
        if (tkz.peek("{")) return parseBlockStatement();
        else return parseCommand();
    }

    private Executable parseIfStatement() throws SyntaxError {
        tkz.consume("if");
        tkz.consume("(");
        Evaluable expression = parseExpression();
        tkz.consume(")");
        tkz.consume("then");
        Executable trueStatement = parseStatement();
        tkz.consume("else");
        Executable falseStatement = parseStatement();
        return new IfStatement(trueStatement, falseStatement, expression, bindings);
    }

    private Executable parseWhileStatement() throws SyntaxError {
        tkz.consume("while");
        tkz.consume("(");
        Evaluable expression = parseExpression();
        tkz.consume(")");
        return new WhileStatement(parseStatement(), expression, bindings);
    }

    private Executable parseBlockStatement() throws SyntaxError {
        tkz.consume("{");
        BlockStatement block = new BlockStatement(bindings);
        while (!tkz.peek("}"))
            block.addStatement(parseStatement());
        tkz.consume("}");
        return block;
    }

    private Executable parseCommand() throws SyntaxError {
        if (tkz.peek("move") || tkz.peek("shoot") || tkz.peek("collect") || tkz.peek("invest") || tkz.peek("done") || tkz.peek("relocate"))
            return parseActionCommand();
        else
            return parseAssignmentStatement();
    }

    private Executable parseActionCommand() throws SyntaxError {
        if(tkz.peek("move")) return parseMoveCommand();
        else if(tkz.peek("invest") || tkz.peek("collect")) return parseRegionCommand();
        else if(tkz.peek("shoot")) return parseAttackCommand();
        else if(tkz.peek("done")){
            tkz.consume("done");
            return new Done();
        }
        else if(tkz.peek("relocate")){
            tkz.consume("relocate");
            return new Relocate(game);
        }
        else throw new SyntaxError("Invalid command");
    }

    private Executable parseMoveCommand() throws SyntaxError {
        tkz.consume("move");
        String direction = tkz.consume();
        if (!directions.contains(direction)) throw new SyntaxError("Invalid direction");
        return new MoveCommand(game,direction);
    }

    private Executable parseRegionCommand() throws SyntaxError {
        String command = tkz.consume();
        Evaluable amount = parseExpression();
        if(command.equals("invest")) return new InvestCommand(amount, bindings,game);
        else if (command.equals("collect")) return new CollectCommand(amount, bindings, game);
        else throw new SyntaxError("Invalid command");
    }

    private Executable parseAttackCommand() throws SyntaxError {
        tkz.consume("shoot");
        String direction = tkz.consume();
        if (!directions.contains(direction)) throw new SyntaxError("Invalid direction");
        Evaluable expenditure = parseExpression();
        return new AttackCommand(direction, expenditure, bindings, game);
    }

    private Executable parseAssignmentStatement() throws SyntaxError {
        String identifier = tkz.consume();
        tkz.consume("=");
        if(specialVariables.contains(identifier)) return new NoOp();
        if(reservedWords.contains(identifier))
            throw new SyntaxError("Use reserved word as identifier: " + identifier);
        Evaluable expression;
        if(tkz.peek("rows") || tkz.peek("cols") || tkz.peek("currow") || tkz.peek("curcol") || tkz.peek("budget") || tkz.peek("deposit") || tkz.peek("int") || tkz.peek("maxdeposit") || tkz.peek("random")){
            expression = parseSpecialVariable();
        }else {
            expression = parseExpression();
        }
        return new AssignmentStatement(identifier, expression, bindings);
    }

    private Evaluable parseSpecialVariable() throws SyntaxError {
        String svName  = tkz.consume();
        return switch (svName) {
            case "rows" -> new Rows(game);
            case "col" -> new Cols(game);
            case "currow" -> new Currow(game);
            case "curcol" -> new Curcol(game);
            case "budget" -> new Budget(game);
            case "deposit" -> new Deposit(game);
            case "int" -> new Int(game);
            case "maxdeposit" -> new Maxdeposit(game);
            default -> new Random();
        };
    }

    private Evaluable parseExpression() throws SyntaxError {
        Evaluable term = parseTerm();
        while (tkz.peek("+") || tkz.peek("-")) {
            String op = tkz.consume();
            term = new BinaryArith(term, op, parseTerm());
        }
        return term;
    }

    private Evaluable parseTerm() throws SyntaxError {
        Evaluable factor = parseFactor();
        while (tkz.peek("*") || tkz.peek("/") || tkz.peek("%")) {
            String op = tkz.consume();
            factor = new BinaryArith(factor, op, parseFactor());
        }
        return factor;
    }

    private Evaluable parseFactor() throws SyntaxError {
        Evaluable power = parsePower();
        while (tkz.peek("^")) {
            String op = tkz.consume();
            power = new BinaryArith(power, op, parsePower());
        }
        return power;
    }

    private Evaluable parsePower() throws SyntaxError {
        if (isNumeric(tkz.peek())){
            return new Num(Long.parseLong(tkz.consume()));
        }
        else if (tkz.peek("(")) {
            tkz.consume("(");
            Evaluable expression = parseExpression();
            tkz.consume(")");
            return expression;
        } else if (tkz.peek("opponent") || tkz.peek("nearby")) return parseInfoExpression();
        else if (tkz.hasNextToken() && !tkz.peek(")")) {
            String identifier = tkz.consume();
            if (reservedWords.contains(identifier))
                throw new SyntaxError("Use reserved word as variable name: " + identifier);
            return new Identifier(identifier,bindings);
        } else
            throw new SyntaxError("");
    }

    private Evaluable parseInfoExpression() throws SyntaxError {
        if(tkz.peek("opponent")){
            tkz.consume();
            return new Opponent();
        }
        else if (tkz.peek("nearby")) {
            tkz.consume("nearby");
            String direction = tkz.consume();
            if (!directions.contains(direction)) throw new SyntaxError("Invalid direction");
            return new Nearby(direction);
        }
        else throw new SyntaxError("Invalid expression");
    }

    public static boolean isNumeric(String num) {
        if (num == null) return false;
        try {
            Long.parseLong(num);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }


}
