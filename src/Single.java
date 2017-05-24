
/**
 * This Single class is the sub-class of Hand, which models a hand of single.
 * 
 * @author Lau Kin Fung
 * @version 2.0
 * @since 2016-10-13
 *
 */
public class Single extends Hand{

	private static final long serialVersionUID = 178749520333021015L;

	/**
	 * This constructor is used to build a single with the specified player and list of cards.
	 * 
	 * @param player the player who plays this single hand
	 * @param cards the cards that the player wants to play
	 */
	public Single(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * This method is used to check if this single is valid.
	 * If the number of cards in the hand is one, it is valid.
	 * 
	 * @return return true if this is a valid single; otherwise, return false
	 * @see Hand#isValid()
	 */
	public boolean isValid() {
		if(this.size() == 1){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * This method is used to return a string specifying the type of this hand.
	 * 
	 * @return a string (Single)
	 * @see Hand#getType()
	 */
	public String getType(){
		return "Single";
	}
	
}
