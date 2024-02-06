import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String constructionPlan_A = "t = 1";
        Player p1 = new Player("A");
        p1.setPlan(constructionPlan_A);
        p1.eval();

    }
}
