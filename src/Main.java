public class Main {
    public static void main(String[] args) {
        String constructionPlan =
                "if(opponent % 5) then { invest 25 }" +
                        "else move up";
        Tokenizer tokenizer = new Tokenizer(constructionPlan);

        while (tokenizer.hasNextToken()) {
            String token = tokenizer.consume();
            System.out.println(token);
        }
    }
}
