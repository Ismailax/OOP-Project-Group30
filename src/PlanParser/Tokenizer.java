package PlanParser;
import java.util.NoSuchElementException;
import PlanParser.Error.*;

public class Tokenizer {
    private String src, next;
    private int pos;

    public Tokenizer(String src) {
        this.src = src;
        pos = 0;
        computeNext();
    }
    public boolean hasNextToken() { return next != null; }

    public void checkNextToken(){
        if(!hasNextToken()) throw new NoSuchElementException("no more tokens");
    }

    public String peek() {
        checkNextToken();
        return next;
    }

    public String consume() {
        checkNextToken();
        String result = next;
        computeNext();
        return result;
    }

    private void computeNext(){
        StringBuilder s = new StringBuilder();
        while(pos < src.length() && Character.isWhitespace(src.charAt(pos))) pos++;
        if(pos == src.length()){
            next = null;
            return;
        }
        char c = src.charAt(pos);
        if(Character.isDigit(c)){
            s.append(c);
            for(pos++; pos < src.length() && Character.isDigit(src.charAt(pos)); pos++)
                s.append(src.charAt(pos));
            next = s.toString();
            return;
        } else if (Character.isLetter(c)) {
            s.append(c);
            for(pos++; pos < src.length(); pos++) {
                char nextChar = src.charAt(pos);
                if(Character.isLetterOrDigit(nextChar)) {
                    s.append(nextChar);
                } else {
                    break;
                }
            }
            next = s.toString();
            return;
        } else if(c == '+' || c == '-' || c == '*' || c == '/' ||c == '%' || c == '^' || c == '(' || c == ')' || c == '{' || c == '}' || c == '='){
            s.append(c);
            pos++;
        } else {
            throw new SyntaxError("unknown character: " + c);
        }
        next = s.toString();
    }


    public boolean peek(String s){
        if (!hasNextToken()) return false;
        return peek().equals(s);
    }

    public void consume(String s) throws SyntaxError {
        if(peek(s)) consume();
        else throw new SyntaxError(s + " expected");
    }
}
