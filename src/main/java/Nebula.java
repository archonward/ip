import java.util.Scanner;

public class Nebula {
    private static final int MAX_ITEMS = 100;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String[] items = new String[MAX_ITEMS];
        boolean[] isDone = new boolean[MAX_ITEMS];
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
                String textToAdd = input.substring(4).trim();

                if (count >= MAX_ITEMS) {
                    System.out.println("I can't store more than " + MAX_ITEMS + " items.");
                    continue;
                }

                items[count] = textToAdd;
                isDone[count] = false;
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
                        String mark = isDone[i] ? "[X]" : "[ ]";
                        System.out.println(" " + (i + 1) + ". " + mark + " " + items[i]);
                    }
                }
                continue;
            }

            if (input.startsWith("mark ")) {
                int idx = Integer.parseInt(input.substring(5).trim()) - 1;
                if (idx >= 0 && idx < count) {
                    isDone[idx] = true;
                    System.out.println(" Marked as done: " + items[idx]);
                }
                continue;
            }

            // Optional: allow reverting
            if (input.startsWith("unmark ")) {
                int idx = Integer.parseInt(input.substring(7).trim()) - 1;
                if (idx >= 0 && idx < count) {
                    isDone[idx] = false;
                    System.out.println(" Marked as not done: " + items[idx]);
                }
                continue;
            }

            System.out.println(input);
        }

        sc.close();
    }
}