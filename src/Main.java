public class Main {
    public static void main(String[] args) {
        String constructionPlan_A = "if (10) then { move up } else { move down }";
        Player p1 = new Player("A");
        p1.setPlan(constructionPlan_A);
        p1.eval();
    }
}