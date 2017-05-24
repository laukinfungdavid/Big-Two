
/**
 * This Triple class is the sub-class of Hand, which models a hand of triple.
 * 
 * @author Lau Kin Fung
 * @version 1.0
 * @since 2016-10-13
 */
public class Triple extends Hand{

	private static final long serialVersionUID = 8981520558077026951L;

	/**
	 * This constructor is used to build a triple with the specified player and list of cards.
	 *
	 * @param player the player who plays this single hand
	 * @param cards the cards that the player wants to play
	 */
	public Triple(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}

	/**
	 * This method is used to get the top card of this Triple, which is the card with the highest suit in a triple.
	 * 
	 * @return true if this is a valid triple; otherwise, return false
	 * @see Hand#getTopCard()
	 */
	public Card getTopCard(){
		return this.getCard(2);
	}

	/**
	 * This method is used to check if this straight flush is valid.
	 * If the hand consists of three cards with the same rank, it is valid.
	 * 
	 * @return return true if this is a valid triple; otherwise, return false
	 * @see Hand#isValid()
	 */
	public boolean isValid() {
		if(this.size() == 3){
			int CardsWithSameRank = 0;
			for(int i=0 ; i<2 ; i++){
				for(int j=i+1 ; j<=2 ; j++){
					if(this.getCard(i).rank == this.getCard(j).rank){
						CardsWithSameRank++;
					}
				}
			}
			if(CardsWithSameRank == 3){
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
	 * @return a string (Triple)
	 * @see Hand#getType()
	 */
	public String getType() {
		return "Triple";
	}

}
