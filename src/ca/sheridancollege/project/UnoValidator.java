package ca.sheridancollege.project;
public class UnoValidator {

    public static boolean isValidMove(UnoCard playedCard, UnoCard topCard) {

        if (playedCard == null || topCard == null) {
            return false;
        }

        // Wild cards can always be played
        if (playedCard.getColor().equalsIgnoreCase("Black")) {
            return true;
        }

        // Same color
        if (playedCard.getColor().equalsIgnoreCase(topCard.getColor())) {
            return true;
        }

        // Same type (Skip, Reverse, Draw2)
        if (playedCard.getType().equalsIgnoreCase(topCard.getType())) {
            return true;
        }

        // Same number (only for number cards)
        if (playedCard.getType().equalsIgnoreCase("Number") &&
            topCard.getType().equalsIgnoreCase("Number") &&
            playedCard.getNumber() == topCard.getNumber()) {
            return true;
        }

        return false;
    }
}