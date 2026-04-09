package ca.sheridancollege.project;

import java.util.ArrayList;

/**
 * Represents a player in the UNO game.
 * Holds the player's hand of UnoCards.
 *
 * @author Emmanuel (Game Controller - Deliverable 3)
 */
public class UnoPlayer extends Player {

    private ArrayList<UnoCard> hand;

    public UnoPlayer(String name) {
        super(name);
        this.hand = new ArrayList<>();
    }

    public ArrayList<UnoCard> getHand() {
        return hand;
    }

    /** Add a card to this player's hand */
    public void addCard(UnoCard card) {
        hand.add(card);
    }

    /** Remove and return a card at the given index */
    public UnoCard playCard(int index) {
        return hand.remove(index);
    }

    /** True if the player has no cards left */
    public boolean hasEmptyHand() {
        return hand.isEmpty();
    }

    /** Print the player's hand with numbered options */
    public void showHand() {
        System.out.println("\n" + getName() + "'s hand:");
        for (int i = 0; i < hand.size(); i++) {
            System.out.println("  [" + i + "] " + hand.get(i));
        }
    }

    @Override
    public void play() {
        // Turn logic is handled by UnoGame
    }
}
