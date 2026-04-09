package ca.sheridancollege.project;

public class UnoDeck extends GroupOfCards {

    public UnoDeck() {
        super(108);
        createDeck();
        shuffle();
    }
    
    private void createDeck() {

        String[] colors = {"Red", "Blue", "Green", "Yellow"};

        for (String color : colors) {

            // One 0 card
            getCards().add(new UnoCard(color, "Number", 0));

            // Two of each 1–9
            for (int i = 1; i <= 9; i++) {
                getCards().add(new UnoCard(color, "Number", i));
                getCards().add(new UnoCard(color, "Number", i));
            }

            // Two of each action card
            for (int i = 0; i < 2; i++) {
                getCards().add(new UnoCard(color, "Skip", -1));
                getCards().add(new UnoCard(color, "Reverse", -1));
                getCards().add(new UnoCard(color, "Draw2", -1));
            }
        }

        // Wild cards (correct already)
        for (int i = 0; i < 4; i++) {
            getCards().add(new UnoCard("Black", "Wild", -1));
            getCards().add(new UnoCard("Black", "WildDraw4", -1));
        }
    }

    public UnoCard drawCard() {
        if (getCards().isEmpty()) {
            return null;
        }
        return (UnoCard) getCards().remove(0);
    }
}