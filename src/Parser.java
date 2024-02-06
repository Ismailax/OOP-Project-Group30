import Evaluable.*;
import Executable.*;

public class Parser {
    private Tokenizer tkz;
    public Parser(Tokenizer tkz){
        this.tkz = tkz;
    }

    public Executable parse() throws SyntaxError{
        Executable exc = parseStatement();
        if(tkz.hasNextToken()) throw new SyntaxError("leftover token");
        else return exc;
    }

    public Executable parseStatement() throws SyntaxError{
        if(tkz.peek("if")) return parseIfStatement();
        if(tkz.peek("while")) return parseWhileStatement();
        if (tkz.peek("{")) return parseBlockStatement();
        else return parseCommand();
    }

    private Executable parseIfStatement() throws SyntaxError{
        tkz.consume("if");
        tkz.consume("(");
        Evaluable expression = parseExpression();
        tkz.consume(")");
        tkz.consume("then");
        Executable trueStatement = parseStatement();
        tkz.consume("else");
        Executable falseStatement = parseStatement();

        return new IfStatement(trueStatement, falseStatement, expression);
    }

    private Executable parseWhileStatement() throws SyntaxError{
        tkz.consume("while");
        tkz.consume("(");
        Evaluable expression = parseExpression();
        tkz.consume(")");
        Executable statement = parseStatement();

        return new WhileStatement(statement, expression);
    }

    private Executable parseBlockStatement() throws SyntaxError {
        tkz.consume("{");
        BlockStatement block = new BlockStatement();
        while (!tkz.peek("}"))
            block.addStatement(parseStatement());
        tkz.consume("}");

        return block;
    }

    private Executable parseCommand() throws SyntaxError{
        if (tkz.peek("move") || tkz.peek("shoot") || tkz.peek("collect") || tkz.peek("invest") || tkz.peek("done") || tkz.peek("relocate"))
            return parseActionCommand();
        else
            return parseAssignmentStatement();
    }

    private Executable parseActionCommand() throws SyntaxError {
        if(tkz.peek("move")) return parseMoveCommand();
        if(tkz.peek("invest") || tkz.peek("collect")) return parseRegionCommand();
        if(tkz.peek("shoot")) return parseAttackCommand();
        if(tkz.peek("done")){
            // done
        }
        if(tkz.peek("relocate")){
            // relocate
        }
    }

    private Executable parseAssignmentStatement() throws SyntaxError{
        String identifier = tkz.consume();
        tkz.consume("=");
        Evaluable expression = parseExpression();
        if (reservedWords.contains(identifier))
            throw new SyntaxError("Use reserved word as identifier: " + identifier);
        return new AssignmentStatement(identifier, expression, bindings);
    }

    private Evaluable parseExpression() throws SyntaxError{
        Evaluable term = parseTerm();
        while (tkz.peek("+") || tkz.peek("-")) {
            String ops = tkz.consume();
            term = new BinaryArith(term, ops, parseTerm());
        }
        return term;
    }

    private Evaluable parseTerm() throws SyntaxError{
        Evaluable factor = parseFactor();
        while (tkz.peek("*") || tkz.peek("/") || tkz.peek("%")) {
            String ops = tkz.consume();
            factor = new BinaryArith(factor, ops, parseFactor());
        }
        return factor;
    }

    private Evaluable parseFactor() throws SyntaxError {
        Evaluable power = parsePower();
        while (tkz.peek("^")) {
            String ops = tkz.consume();
            power = new BinaryArith(power, ops, parsePower());
        }
        return power;
    }

    private Evaluable parsePower() throws SyntaxError{
        if (isNumeric(tkz.peek())) return new Int(Integer.parseInt(tkz.consume()));
        else if (tkz.peek("(")) { // brackets
            tkz.consume("(");
            Evaluable expression = parseExpression();
            tkz.consume(")");
            return expression;
        } else if (tkz.peek("opponent") || tkz.peek("nearby")) return parseInfoExpression();
        else if (tkz.hasNextToken() && !tkz.peek(")")) {
            String identifier = tkz.consume();
            if (reservedWords.contains(identifier))
                throw new SyntaxError("Use reserved word as variable name: " + identifier);
            return new Identifier(identifier);
        } else
            throw new SyntaxError("Missing Number, Identifier, SensorExpression or RandomValue");
    }

    private Evaluable parseInfoExpression() throws SyntaxError{
        if(tkz.peek("opponent")){
            tkz.consume();
            return new Opponent();
        }
        else if (tkz.peek("nearby") {
            tkz.consume("nearby");
            String direction = tkz.consume();
            return new Nearby(direction);
        }
    }



    public static boolean isNumeric(String num){
        if(num == null) return false;
        try{
            Integer.parseInt(num);
        }catch (NumberFormatException e){
            return false;
        }
        return true;
    }



}
