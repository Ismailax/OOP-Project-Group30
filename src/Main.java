public class Main {
    public static void main(String[] args) {
        String constructionPlan_A = "x = 2 while(x) { move down x = x - 1 }";
        Player p1 = new Player("A");
        p1.setPlan(constructionPlan_A);
        p1.eval();
    }
}

//        Tokenizer tkz = new Tokenizer(constructionPlan_A);
//        while (tkz.hasNextToken()) {
//            String token = tkz.consume();
//            System.out.println("Token: " + token);
//        }