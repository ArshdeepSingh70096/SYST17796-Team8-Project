package ca.sheridancollege.project;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * UnoGame - Game Controller for the UNO card game.
 *
 * Responsibilities:
 *  - Main game flow and turn management
 *  - Card play validation
 *  - Special card effects (Skip, Reverse, Draw Two, Wild, Wild Draw Four)
 *  - Winner detection
 *
 * @author Emmanuel (Game Controller - Deliverable 3)
 */
public class UnoGame extends Game {

    // ─── Constants ────────────────────────────────────────────────────────────
    private static final int STARTING_HAND_SIZE = 7;

    // ─── Game State ───────────────────────────────────────────────────────────
    private UnoDeck deck;
    private ArrayList<UnoCard> discardPile;

    private int currentPlayerIndex;   // whose turn it is
    private int direction;            // +1 = clockwise, -1 = counter-clockwise
    private String currentColor;      // active colour (can differ from top card when Wild played)

    private boolean gameOver;
    private Scanner scanner;

    // ─── Constructor ──────────────────────────────────────────────────────────
    public UnoGame(String name) {
        super(name);
        discardPile = new ArrayList<>();
        direction = 1;
        gameOver = false;
        scanner = new Scanner(System.in);
    }

    // =========================================================================
    //  SETUP
    // =========================================================================

    /** Add a player to the game before calling play() */
    public void addPlayer(String playerName) {
        getPlayers().add(new UnoPlayer(playerName));
    }

    /** Deal STARTING_HAND_SIZE cards to each player and flip the first discard */
    private void setupGame() {
        deck = new UnoDeck();

        // Deal cards
        for (Player p : getPlayers()) {
            UnoPlayer up = (UnoPlayer) p;
            for (int i = 0; i < STARTING_HAND_SIZE; i++) {
                up.addCard(deck.drawCard());
            }
        }

        // Flip first card — re-draw if it's a Wild to keep things simple
        UnoCard topCard = deck.drawCard();
        while (topCard.getType().equals("Wild") || topCard.getType().equals("WildDraw4")) {
            deck.getCards().add(topCard); // put it back at the bottom
            deck.shuffle();
            topCard = deck.drawCard();
        }
        discardPile.add(topCard);
        currentColor = topCard.getColor();

        System.out.println("=== " + getName() + " ===");
        System.out.println("Starting card: " + topCard);
        System.out.println("Players: " + getPlayers().size());
        System.out.println();
    }

    // =========================================================================
    //  MAIN GAME LOOP
    // =========================================================================

    @Override
    public void play() {
        if (getPlayers().size() < 2) {
            System.out.println("Need at least 2 players to start.");
            return;
        }

        setupGame();
        currentPlayerIndex = 0;

        while (!gameOver) {
            takeTurn((UnoPlayer) getPlayers().get(currentPlayerIndex));

            // Check winner after every turn
            if (((UnoPlayer) getPlayers().get(currentPlayerIndex)).hasEmptyHand()) {
                gameOver = true;
            } else {
                advanceTurn();
            }
        }

        declareWinner();
    }

    // =========================================================================
    //  TURN LOGIC
    // =========================================================================

    /**
     * Handle one player's full turn:
     *  1. Show hand & top card
     *  2. Let them pick a card to play or draw
     *  3. Validate and apply the card
     */
    private void takeTurn(UnoPlayer player) {
        UnoCard topCard = getTopCard();

        System.out.println("────────────────────────────────");
        System.out.println("Turn: " + player.getName());
        System.out.println("Top card : " + topCard + "  |  Active colour: " + currentColor);
        player.showHand();

        // Find playable cards
        ArrayList<Integer> playable = getPlayableIndices(player);

        int choice;
        if (playable.isEmpty()) {
            // No playable card — must draw
            System.out.println("\nNo playable cards. Drawing a card...");
            UnoCard drawn = drawCardForPlayer(player);
            System.out.println("Drew: " + drawn);

            // Check if the drawn card can be played immediately
            if (isPlayable(drawn, topCard)) {
                System.out.println("Drawn card is playable! Playing it...");
                player.getHand().remove(drawn);
                applyCard(drawn, player);
            } else {
                System.out.println("Cannot play drawn card. Turn ends.");
            }

        } else {
            // Ask player to choose a card
            choice = promptCardChoice(player, playable);
            UnoCard played = player.playCard(choice);
            applyCard(played, player);
        }
    }

    /**
     * Prompt the current player to pick one of their playable cards.
     * Loops until a valid index is entered.
     */
    private int promptCardChoice(UnoPlayer player, ArrayList<Integer> playable) {
        while (true) {
            System.out.print("\nEnter card number to play " + playable + ": ");
            try {
                int input = Integer.parseInt(scanner.nextLine().trim());
                if (playable.contains(input)) {
                    return input;
                }
                System.out.println("That card can't be played. Choose from " + playable);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    // =========================================================================
    //  CARD VALIDATION
    // =========================================================================

    /**
     * A card is playable if it matches the active colour, matches the top
     * card's type/number, or is a Wild card.
     */
    public boolean isPlayable(UnoCard card, UnoCard topCard) {
        if (card.getType().equals("Wild") || card.getType().equals("WildDraw4")) {
            return true;
        }
        if (card.getColor().equals(currentColor)) {
            return true;
        }
        if (card.getType().equals("Number") && card.getNumber() == topCard.getNumber()
                && topCard.getType().equals("Number")) {
            return true;
        }
        if (!card.getType().equals("Number") && card.getType().equals(topCard.getType())) {
            return true;
        }
        return false;
    }

    /** Returns a list of hand indices the player can legally play */
    private ArrayList<Integer> getPlayableIndices(UnoPlayer player) {
        ArrayList<Integer> playable = new ArrayList<>();
        UnoCard topCard = getTopCard();
        for (int i = 0; i < player.getHand().size(); i++) {
            if (isPlayable(player.getHand().get(i), topCard)) {
                playable.add(i);
            }
        }
        return playable;
    }

    // =========================================================================
    //  SPECIAL CARD EFFECTS
    // =========================================================================

    /**
     * Place a card on the discard pile and apply its effect.
     */
    private void applyCard(UnoCard card, UnoPlayer currentPlayer) {
        discardPile.add(card);
        System.out.println(currentPlayer.getName() + " played: " + card);

        switch (card.getType()) {

            case "Number":
                // Number cards — just update active colour
                currentColor = card.getColor();
                break;

            case "Skip":
                currentColor = card.getColor();
                advanceTurn(); // skip next player
                UnoPlayer skipped = (UnoPlayer) getPlayers().get(currentPlayerIndex);
                System.out.println("  ⛔ " + skipped.getName() + " is SKIPPED!");
                break;

            case "Reverse":
                currentColor = card.getColor();
                direction *= -1; // flip direction
                System.out.println("  🔄 Direction REVERSED!");
                // With 2 players, Reverse acts like Skip
                if (getPlayers().size() == 2) {
                    advanceTurn();
                }
                break;

            case "Draw2":
                currentColor = card.getColor();
                advanceTurn(); // effect targets next player
                UnoPlayer draw2Target = (UnoPlayer) getPlayers().get(currentPlayerIndex);
                System.out.println("  +2 " + draw2Target.getName() + " draws 2 cards and is SKIPPED!");
                drawCards(draw2Target, 2);
                break;

            case "Wild":
                currentColor = chooseColor(currentPlayer);
                System.out.println("  🌈 Wild! Colour changed to: " + currentColor);
                break;

            case "WildDraw4":
                currentColor = chooseColor(currentPlayer);
                advanceTurn(); // effect targets next player
                UnoPlayer wd4Target = (UnoPlayer) getPlayers().get(currentPlayerIndex);
                System.out.println("  🌈+4 " + wd4Target.getName() + " draws 4 cards and is SKIPPED!");
                System.out.println("  Colour changed to: " + currentColor);
                drawCards(wd4Target, 4);
                break;

            default:
                currentColor = card.getColor();
                break;
        }
    }

    /**
     * Prompt the current player to choose a colour after playing a Wild.
     */
    private String chooseColor(UnoPlayer player) {
        String[] colors = {"Red", "Blue", "Green", "Yellow"};
        System.out.println("\n" + player.getName() + ", choose a colour:");
        for (int i = 0; i < colors.length; i++) {
            System.out.println("  [" + i + "] " + colors[i]);
        }
        while (true) {
            System.out.print("Enter 0-3: ");
            try {
                int pick = Integer.parseInt(scanner.nextLine().trim());
                if (pick >= 0 && pick < colors.length) {
                    return colors[pick];
                }
            } catch (NumberFormatException e) {
                // fall through
            }
            System.out.println("Invalid. Enter 0, 1, 2, or 3.");
        }
    }

    // =========================================================================
    //  DRAW LOGIC
    // =========================================================================

    /** Draw one card for a player, reshuffling discard into deck if needed */
    private UnoCard drawCardForPlayer(UnoPlayer player) {
        reshuffleIfNeeded();
        UnoCard card = deck.drawCard();
        if (card != null) {
            player.addCard(card);
        }
        return card;
    }

    /** Draw multiple cards for a player */
    private void drawCards(UnoPlayer player, int count) {
        for (int i = 0; i < count; i++) {
            drawCardForPlayer(player);
        }
    }

    /**
     * If the deck is empty, take the discard pile (minus the top card),
     * shuffle it, and use it as the new deck.
     */
    private void reshuffleIfNeeded() {
        if (deck.getCards().isEmpty()) {
            System.out.println("Deck empty — reshuffling discard pile...");
            UnoCard top = discardPile.remove(discardPile.size() - 1);
            for (Card c : discardPile) {
                deck.getCards().add(c);
            }
            discardPile.clear();
            discardPile.add(top);
            deck.shuffle();
        }
    }

    // =========================================================================
    //  TURN ORDER
    // =========================================================================

    /** Move currentPlayerIndex to the next player based on current direction */
    private void advanceTurn() {
        int size = getPlayers().size();
        currentPlayerIndex = (currentPlayerIndex + direction + size) % size;
    }

    // =========================================================================
    //  WINNER DETECTION
    // =========================================================================

    @Override
    public void declareWinner() {
        // The winner is the player with an empty hand
        for (Player p : getPlayers()) {
            UnoPlayer up = (UnoPlayer) p;
            if (up.hasEmptyHand()) {
                System.out.println("\n🎉 " + up.getName() + " wins " + getName() + "! 🎉");
                printFinalScores();
                return;
            }
        }
        System.out.println("Game ended with no winner detected.");
    }

    /** Print remaining card counts for all players at game end */
    private void printFinalScores() {
        System.out.println("\n── Final card counts ──");
        for (Player p : getPlayers()) {
            UnoPlayer up = (UnoPlayer) p;
            System.out.println("  " + up.getName() + ": " + up.getHand().size() + " card(s) remaining");
        }
    }

    // =========================================================================
    //  HELPERS
    // =========================================================================

    public UnoCard getTopCard() {
        return discardPile.get(discardPile.size() - 1);
    }

    public String getCurrentColor() {
        return currentColor;
    }

    public int getDirection() {
        return direction;
    }
}
