
package ca.sheridancollege.project;

/**
 * UnoPlayer extends the abstract Player class to rep Player and manages player actions
 * @author dan
 */
public class UnoPlayer extends Player {
    // Tracking player calling UNOOO for their last card
    private boolean saidUno;
    // collection of cards being held by the player at the moment
    private GroupOfCards hand; 
    
    //constructor to create player and initialize their hand
    public UnoPlayer(String name) {
        // passing player name to parent player class 
        super(name);
        this.hand = new GroupOfCards(7);
        // call Uno set to false as player cant call uno to start
        this.saidUno = false;
    }
    
    // gives access to the players hand for player
    public GroupOfCards getHand() {
        return hand;
    }
    
    //allowing other classes to see status of player calling Uno
    public boolean isSaidUno() {
        return saidUno;
    }
    
    // Remove card from the players hand
    public void playCard(UnoCard card) {
        // checking if the player actually has the card
        if (hand.getCards().contains(card)) {
            // removes the selected card from the ArrayList inside GroupOfCards
            hand.getCards().remove(card);
            // game statement for played card
            System.out.println(getName() + " played: " + card);
        } else {
            // game statement if card isnt valid
            System.out.println("Card not in hand!");
        }
    }

    // drawing a card from the deck and adding it to the players hand
    public void drawCard(UnoDeck deck) {
        // Take card from the deck using the Unodecks dealCard
        UnoCard drawnCard = deck.dealCard();
        // Adding card to the player's hand collection
        hand.getCards().add(drawnCard);
        // Resetting saidUno to false in case it was made true
        this.saidUno = false;
        // game statement that card has been drwan
        System.out.println(getName() + " drew a card.");
    }
    
    // gives players ability to call Uno when they have one card
    public void callUno() {
        // updating saidUno to true so player can call it
        saidUno = true;
        // game statement for UNOO
        System.out.println(getName() + " YELLED UNO!!");
    }
    
    // overriding play() from player class, start of players turn
    @Override
    public void play() {
        // game statement for  players turn
        System.out.println(getName() + "'s turn.");
    }
    
}
