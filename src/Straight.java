
/**
 * This Straight class is the sub-class of Hand, which models a hand of straight.
 * 
 * @author Lau Kin Fung
 * @version 1.1
 * @since 2016-10-13
 */
public class Straight extends Hand{

	private static final long serialVersionUID = -1139694027623703735L;

	/**
	 * This constructor is used to build a straight with the specified player and list of cards.
	 * 
	 * @param player the player who plays this straight
	 * @param cards the cards that the player wants to play
	 */
	public Straight(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * This method is used to get the top card of this Straight, which is the card with the highest rank in a straight.
	 * 
	 * @return the top card
	 * @see Hand#getTopCard()
	 */
	public Card getTopCard(){
		return this.getCard(4);
	}
	
	/**
	 * This method overrides the beats method in hand class and is used to check if this straight beats a specified hand.
	 * A straight having a top card with a higher rank beats a straight having a top card with a lower rank. 
	 * If the straights have top cards with the same rank, the one having a top card with a higher suit beats the one having a top card with a lower suit. 
	 * 
	 * @param hand a specific hand (In fact, it is a hand played by the previous player)
	 * @return true if this straight beats a specific hand; otherwise return false.
	 * @see Hand#beats(Hand)
	 */
	public boolean beats(Hand hand){
		if(hand.getType() == "Flush" || hand.getType() == "FullHouse" || hand.getType() == "Quad" || hand.getType() == "StraightFlush"){
			return false;
		}else{
			BigTwoCard thistop = new BigTwoCard(getTopCard().suit, getTopCard().rank);
			if(thistop.compareTo(hand.getTopCard()) == 1){
				return true;
			}else{
				return false;
			}
		}
	}
	
	/**
	 * This method is used to check if this straight is valid.
	 * If the hand consists of five cards with consecutive rank, it is valid.
	 * 
	 * @return return true if this is a valid straight; otherwise, return false
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
						previousCard =  this.getCard(i);
					}else{
						return false;
					}
				}

			}
			if(DifferentSuit == true){
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
	 * @return a string (Straight)
	 * @see Hand#getType()
	 */
	public String getType() {
		return "Straight";
	}

}
