
/**
 * This BigTwoDeck class is the subclass of Deck, which is used to model a deck of cards used in a Big Two card game.
 * 
 * @author Lau Kin Fung
 * @version 1.0
 * @since 2016-10-13
 *
 */
public class BigTwoDeck extends Deck{
	
	private static final long serialVersionUID = 8254481884293570270L;

	/** 
	 * This method overrides the initialize() in the superclass Deck.
	 * It initializes a deck of Big Two cards. 
	 * It removes all cards from the deck, creates 52 Big Two cards and add them to the deck.
	 * 
	 * @see Deck#initialize()
	 */
	public void initialize(){
		removeAllCards();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 13; j++) {
				BigTwoCard bigtwocard = new BigTwoCard(i, j);
				addCard(bigtwocard);
			}
		}
	}
}
