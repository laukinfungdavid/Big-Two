
/**
 * This Quad class is the sub-class of Hand, which models a hand of quad.
 * 
 * @author Lau Kin Fung
 * @version 2.0
 * @since 2016-10-13
 */
public class Quad extends Hand{

	private static final long serialVersionUID = 4336998163424735246L;
	
	private int TopRankIdx = -1;
	/**
	 * This constructor is used to build a Quad with the specified player and list of cards.
	 * 
	 * @param player the player who plays this quad hand
	 * @param cards	the cards that the player wants to play
	 */
	public Quad(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * This method is used to get the top card of this Quad, which is the card in the quadruplet with the highest suit.
	 * 
	 * @return the top card
	 * @see Hand#getTopCard()
	 */
	public Card getTopCard(){
		if(this.getCard(0).rank == this.getCard(1).rank && this.getCard(1).rank == this.getCard(2).rank && this.getCard(2).rank == this.getCard(3).rank){
			TopRankIdx = 3;
		}else if(this.getCard(1).rank == this.getCard(2).rank && this.getCard(2).rank == this.getCard(3).rank && this.getCard(3).rank == this.getCard(4).rank){
			TopRankIdx = 4;
		}
		return this.getCard(TopRankIdx);
	}
	
	/**
	 * This method overrides the beats method in hand class and is used to check if this quad beats a specified hand.
	 * 	 
	 * @param hand a specific hand (In fact, it is a hand played by the previous player)
	 * @return true if this quad beats a specific hand; otherwise return false.
	 * @see Hand#beats(Hand)
	 */
	public boolean beats(Hand hand){
		if(hand.getType() == "StraightFlush"){
			return false;
		}else{
			if(hand.getType() == "Straight" || hand.getType() == "Flush" || hand.getType() == "FullHouse"){
				return true;	// if the specified hand is straight, flush and full house, return true.
			}else{
				if(checkRank(this.getTopCard().rank) > checkRank(hand.getTopCard().rank)){
					return true;
				}else{
					return false;
				}
			}
				
		}
		
	}
	
	/**
	 * This method is used to check if this quad is valid.
	 * 
	 * @return return true if this is a valid quad; otherwise, return false
	 * @see Hand#isValid()
	 */
	public boolean isValid() {
		if(this.size() == 5){
			if(this.getCard(0).rank == this.getCard(1).rank && this.getCard(1).rank == this.getCard(2).rank && this.getCard(2).rank == this.getCard(3).rank){
				return true;
			}else if(this.getCard(1).rank == this.getCard(2).rank && this.getCard(2).rank == this.getCard(3).rank && this.getCard(3).rank == this.getCard(4).rank){
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
	 * @return a string (Quad)
	 * @see Hand#getType()
	 */
	public String getType() {
		return "Quad";
	}
	
}
