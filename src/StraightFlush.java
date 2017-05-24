
/**
 * This StraightFlush class is the sub-class of Hand, which models a hand of straight flush.
 * 
 * @author Lau Kin Fung
 * @version 1.0
 * @since 2016-10-13
 */
public class StraightFlush extends Hand{

	private static final long serialVersionUID = -115570071976127674L;

	/**
	 * This constructor is used to build a straight flush with the specified player and list of cards.
	 *
	 * @param player the player who plays this single hand
	 * @param cards the cards that the player wants to play
	 */
	public StraightFlush(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * This method is used to get the top card of this straight, which is the card with the highest rank in a straight flush.
	 * 
	 * @return the top card
	 * @see Hand#getTopCard()
	 */
	public Card getTopCard(){
		return this.getCard(4);
	}
	
	/**
	 * This method overrides the beats method in hand class and is used to check if this straight flush beats a specified hand.
	 * A straight flush always beats any straights, flushes, full houses and quads.
	 * A straight flush having a top card with a higher rank beats a straight flush having a top card with a lower rank.
	 * If straight flushes have top cards with the same rank, the one having a top card with a higher suit beats one having a top card with a lower suit.
	 * 
	 * @param hand a specific hand (In fact, it is a hand played by the previous player)
	 * @return true if this straight flush beats a specific hand; otherwise return false.
	 * @see Hand#beats(Hand)
	 */
	public boolean beats(Hand hand){
		BigTwoCard thistop = new BigTwoCard(getTopCard().suit, getTopCard().rank);
		if(hand.getType() == "Straight" || hand.getType() == "Flush" || hand.getType() == "FullHouse" || hand.getType() == "Quad"){
			return true;	// if the specified hand is straight, flush, full house and quad, return true.
		}else{
			if(thistop.compareTo(hand.getTopCard()) == 1){
				return true;
			}else{
				return false;
			}
		}
		
	}
	
	/**
	 * This method is used to check if this straight flush is valid.
	 * If the hand consists of five cards with consecutive rank and the same suit, it is valid.
	 * 
	 * @return return true if this is a valid straight flush; otherwise, return false
	 * @see Hand#isValid()
	 */
	public boolean isValid() {
		boolean DifferentSuit = false;
		Card previousCard = this.getCard(0);
		if(this.size() == 5){
			for(int i=1 ; i<this.size() ; i++){
				if(previousCard.suit != this.getCard(i).suit){
					DifferentSuit = true;
				}
				if(previousCard.rank + 1 == 2){
					return false;
				}else{
					if(checkRank(previousCard.rank + 1) == checkRank(this.getCard(i).rank) ){
						previousCard = this.getCard(i);
					}else if(i != 4){
						return false;
					}
				}
			}
			if(DifferentSuit == false){
				return true;
			}else{
				return false;
			}	
		}else{
			return false;
		}
	}

	/**
	 * This method is used to return a string specifying the type of this hand.
	 * 
	 * @return a string (StraightFlush)
	 * @see Hand#getType()
	 */
	public String getType() {
		return "StraightFlush";
	}
	
}
