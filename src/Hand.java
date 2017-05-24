
/**
 * This Hand class is a subclass of the CardList class, and is used to model a hand of cards. 
 * 
 * @author Lau Kin Fung
 * @version 2.0
 * @since 2016-10-13
 * 
 *
 */
public abstract class Hand extends CardList{
	
	private static final long serialVersionUID = 2834633786928334157L;
	
	private CardGamePlayer player;
	
	/**
	 * This abstract method is used to check if this is a valid hand.
	 * Every sub-class overrides this method.
	 * 
	 * @return true if this hand is value; otherwise return false
	 */
	public abstract boolean isValid();
	/**
	 * This abstract method is used to return a string specifying the type of this hand.
	 * 
	 * Every sub-class overrides this method.
	 * @return a string specifying the type of this hand
	 */
	public abstract String getType();
	
	/**
	 * This constructor is used to build a hand with the specified player and list of cards.
	 * 
	 * @param player a specified player
	 * @param cards cards played by the specified player
	 */
	public Hand(CardGamePlayer player, CardList cards){
		this.player = new CardGamePlayer(player.getName());
		this.removeAllCards();
		for(int i=0 ; i<cards.size() ; i++){
			this.addCard(cards.getCard(i));
		}
		this.sort();
	}
	
	/**
	 * This method is used to get the player of this hand.
	 * 
	 * @return player of this hand
	 */
	public CardGamePlayer getPlayer(){
		return player;
	}
	
	/**
	 * This method is used to get the top card of this hand.
	 * 
	 * @return the largest card
	 */
	public Card getTopCard(){
		return this.getCard(this.size()-1);
	}
	
	/**
	 * This method is used to check if this hand beats a specified hand.
	 * It can be used by Single, Pair and Triple sub-classes, which only needs to compare the ranks of the top cards.
	 * Other methods excluding Single, Pair and Triple sub-classes overrides this method.
	 * 
	 * @param hand a specified hand
	 * @return true if this hand beats the specified hand; otherwise return false
	 */
	public boolean beats(Hand hand){
		int ThisBiggestSuit = this.getTopCard().suit;
		int HandBiggestSuit = hand.getTopCard().suit;
		int ThisRank = checkRank(this.getTopCard().rank);
		int HandRank = checkRank(hand.getTopCard().rank);
		
		if(ThisRank > HandRank){
			return true;
		}else if(ThisRank < HandRank){
			return false;
		}else if(ThisBiggestSuit > HandBiggestSuit){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * This method is used to check the rank of the card.
	 * If the rank of the card is smaller than two, this method adds 13 to the rank index because index 0 is the largest and 1 is the second largest.
	 * 
	 * @param rank the original rank of a card in a hand
	 * @return a corrected rank for easier comparison
	 */
	public int checkRank(int rank){
		if(rank < 2){
			rank+=13;
		}
		return rank;
	}
	
}
