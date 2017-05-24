
/**
 * This Pair class is the sub-class of Hand, which models a hand of pair.
 * 
 * @author Lau Kin Fung
 * @version 2.0
 * @since 2016-10-13
 *
 */
public class Pair extends Hand{

	private static final long serialVersionUID = -7735787877198652522L;

	/**
	 * This constructor is used to build a pair with the specified player and list of cards.
	 * 
	 * @param player the player who plays this pair
	 * @param cards the cards that the player wants to play
	 */
	public Pair(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * This method is used to check if this pair is valid.
	 * Only two cards having the same rank is valid.
	 * 
	 * @return return true if this is a valid pair; otherwise, return false
	 * @see Hand#isValid()
	 */
	public boolean isValid() {
		if(this.size() == 2){
			if(this.getCard(0).rank == this.getCard(1).rank){
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
	 * @return a string (Pair)
	 * @see Hand#getType()
	 */
	public String getType(){
		return "Pair";
	}
	
}
