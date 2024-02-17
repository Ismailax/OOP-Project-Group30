import GameState.Player;

public class Main {
    public static void main(String[] args) {
        String constructionPlan_A = "x = 3 if(x-4) then move up else if (x-3) then move down else move upleft";
        Player p1 = new Player("A");
        p1.setPlan(constructionPlan_A);
        p1.eval();
        System.out.println(p1.getBindings("dir"));
    }
}

//        Tokenizer tkz = new Tokenizer(constructionPlan_A);
//        while (tkz.hasNextToken()) {
//            String token = tkz.consume();
//            System.out.println("Token: " + token);
//        }