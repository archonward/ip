import java.util.Scanner;

public class Nebula {
    private static final int MAX_ITEMS = 100;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String[] items = new String[MAX_ITEMS];
        int count = 0;

        System.out.println(" Hello! I'm Nebula");
        System.out.println(" What can I do for you?");

        while (true) {
            String input = sc.nextLine().trim();

            if (input.equals("bye")) {
                System.out.println(" Bye. Hope to see you again soon!");
                break;
            }

            if (input.startsWith("add ")) {
                String textToAdd = input.substring(4).trim(); // everything after "add "

                if (count >= MAX_ITEMS) {
                    System.out.println("I can't store more than " + MAX_ITEMS + " items.");
                    continue;
                }

                items[count] = textToAdd;
                count++;

                System.out.println(" Added: " + textToAdd);
                continue;
            }

            if (input.equals("list")) {
                if (count == 0) {
                    System.out.println(" Your list is empty.");
                } else {
                    System.out.println(" Here are your stored items:");
                    for (int i = 0; i < count; i++) {
                        System.out.println(" " + (i + 1) + ". " + items[i]);
                    }
                }
                continue;
            }

            // Level 1 behaviour: echo unknown commands
            System.out.println(input);
        }

        sc.close();
    }
}
