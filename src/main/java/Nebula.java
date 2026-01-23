import java.util.Scanner;

public class Nebula {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println(" Hello! I'm Nebula");
        System.out.println(" What can I do for you?");

        while (true) {
            String input = sc.nextLine().trim();

            if (input.equals("bye")) {
                System.out.println(" Bye. Hope to see you again soon!");
                break;
            }

            System.out.println(input);
        }

        sc.close();
    }
}