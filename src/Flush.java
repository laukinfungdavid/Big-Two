
/**
 * This Flush class is the sub-class of Hand, which models a hand of flush.
 * 
 * @author Lau Kin Fung
 * @version 1.0
 * @since 2016-10-13
 */
public class Flush extends Hand{

	private static final long serialVersionUID = -6661011501454063170L;

	/**
	 * This constructor is used to build a flush with the specified player and list of cards.
	 * 
	 * @param player the player who plays this flush
	 * @param cards the cards that the player wants to play
	 */
	public Flush(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * This method is used to get the top card of this flush, which is the card with the highest rank.
	 * 
	 * @return the top card
	 * @see Hand#getTopCard()
	 */
	public Card getTopCard(){
		return this.getCard(4);
	}
	
	/**
	 * This method overrides the beats method in hand class and is used to check if this flush beats a specified hand.
	 * A flush always beats any straights. A flush with a higher suit beats a flush with a lower suit. 
	 * 
	 * @param hand a specific hand (In fact, it is a hand played by the previous player)
	 * @return true if this flush beats a specific hand; otherwise return false.
	 * @see Hand#beats(Hand)
	 */
	public boolean beats(Hand hand){
		if(hand.getType() == "FullHouse" || hand.getType() == "Quad" || hand.getType() == "StraightFlush"){
			return false;					//since these types of hand all beats flush, return false.
		}else if(hand.getType() == "Straight"){
			return true;
		}else{
			if(hand.getTopCard().suit < this.getTopCard().suit){
				return true;
			}else if(hand.getTopCard().suit > this.getTopCard().suit){
				return false;
			}else{
				return (hand.getTopCard().rank < this.getTopCard().rank);
			}
		}
		
	}
	
	/**
	 * This method is used to check if this flush is valid.
	 * If all cards have the same suit, it is valid.
	 * 
	 * @return return true if this is a valid flush; otherwise, return false
	 * @see Hand#isValid()
	 */
	public boolean isValid() {
		if(this.size() == 5){
			int SameSuit = 0; //Counter counting the number of cards that has same suit.
			for(int i=0 ; i<4 ; i++){
				for(int j=i+1 ; j<=4 ; j++){
					if(this.getCard(i).suit == this.getCard(j).suit){
						SameSuit++;
					}
				}
			}
			if(SameSuit == 10){	//There should be ten combinations during checking. {0,1},{0,2},{0,3},{0,4},{1,2},{1,3}...{3,4}
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	/**
	 * This method is used to return a string specifying the type of this hand
	 * 
	 * @return a string (Flush)
	 * @see Hand#getType()
	 */
	public String getType() {
		return "Flush";
	}

}
