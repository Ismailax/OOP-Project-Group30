import GameState.Player;

public class Main {
    public static void main(String[] args) {
        String constructionPlan_A = "x = x + 1 if(x) then move up else {}";
        Player p1 = new Player("A");
        p1.setPlan(constructionPlan_A);
        p1.eval();
        System.out.println(p1.getBindings("x"));
    }
}

//        Tokenizer tkz = new Tokenizer(constructionPlan_A);
//        while (tkz.hasNextToken()) {
//            String token = tkz.consume();
//            System.out.println("Token: " + token);
//        }