package ca.sheridancollege.project;
public class UnoActionHandler {

    public static void applyAction(UnoCard card) {

        if (card == null) return;

        String type = card.getType().toLowerCase();

        switch (type) {

            case "skip":
                System.out.println("Next player skipped!");
                break;

            case "reverse":
                System.out.println("Game direction reversed!");
                break;

            case "draw2":
                System.out.println("Next player draws 2 cards!");
                break;

            case "wild":
                System.out.println("Choose a color!");
                break;

            case "wilddraw4":
                System.out.println("Next player draws 4 cards!");
                break;

            default:
                // Number card → no action
                break;
        }
    }
}