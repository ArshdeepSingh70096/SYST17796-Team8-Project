package ca.sheridancollege.project;

import java.util.Scanner;

/**
 * Entry point for the UNO game.
 *
 * @author Emmanuel (Game Controller - Deliverable 3)
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Welcome to UNO ===");
        System.out.print("How many players? (2–4): ");

        int numPlayers = 0;
        while (numPlayers < 2 || numPlayers > 4) {
            try {
                numPlayers = Integer.parseInt(scanner.nextLine().trim());
                if (numPlayers < 2 || numPlayers > 4) {
                    System.out.print("Please enter 2, 3, or 4: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Enter a number (2–4): ");
            }
        }

        UnoGame game = new UnoGame("UNO");

        for (int i = 1; i <= numPlayers; i++) {
            System.out.print("Enter name for Player " + i + ": ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) name = "Player " + i;
            game.addPlayer(name);
        }

        game.play();
    }
}
