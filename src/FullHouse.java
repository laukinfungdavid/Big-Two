
/**
 * This FullHouse class is the sub-class of Hand, which models a hand of full house.
 * 
 * @author Lau Kin Fung
 * @version 2.0
 * @since 2016-10-13
 */
public class FullHouse extends Hand{
	
	private static final long serialVersionUID = 1593063361428323710L;
	
	private int TopRankIdx = -1; //used to save the index of the card in the triplet with the highest suit
	
	/**
	 * This constructor is used to build a full house with the specified player and list of cards.
	 *
	 * @param player the player who plays this full house
	 * @param cards the cards that the player wants to play
	 */
	public FullHouse(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * This method is used to get the top card of this full house, which is the card in the triplet with the highest suit in a full house.
	 * There are two cases of full house after calling the sorting method in constructor which are aaabb and bbaaa.
	 * aaabb means the triplet(aaa) is sorted in front of the pair(bb) and bbaaa means the triplet(aaa) is sorted in after of the pair(bb)
	 * 
	 * @return the top card
	 * @see Hand#getTopCard()
	 */
	public Card getTopCard(){
		if(this.getCard(0).rank == this.getCard(1).rank && this.getCard(1).rank == this.getCard(2).rank && this.getCard(3).rank == this.getCard(4).rank){
			TopRankIdx = 2; 
		}else if(this.getCard(0).rank == this.getCard(1).rank && this.getCard(2).rank == this.getCard(3).rank && this.getCard(3).rank == this.getCard(4).rank){
			TopRankIdx = 4;
		}
		return this.getCard(TopRankIdx);
	}
	
	/**
	 * This method overrides the beats method in hand class and is used to check if this full house beats a specified hand.
	 * A full house always beats any straights and flushes. 
	 * A full house having a top card with a higher rank beats a full house having a top card with a lower rank.
	 * 
	 * @param hand a specific hand (In fact, it is a hand played by the previous player)
	 * @return true if this full house beats a specific hand; otherwise return false.
	 * @see Hand#beats(Hand)
	 */
	public boolean beats(Hand hand){
		if(hand.getType() == "Quad" || hand.getType() == "StraightFlush"){  
			return false;     //since these types of hand all beats flush, return false.
		}else{
			if(hand.getType() == "Flush" || hand.getType() == "Straight"){	// if the specified hand is straight and flush, return true.
				return true;
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
	 * This method is used to check if this flush is valid.
	 * Only the two cases (aaabb, bbaaa) of full house after calling the sorting method in constructor are valid
	 * aaabb means the triplet(aaa) is sorted in front of the pair(bb) and bbaaa means the triplet(aaa) is sorted in after of the pair(bb)
	 * 
	 * @return return true if this is a valid full house; otherwise, return false
	 * @see Hand#isValid()
	 */
	public boolean isValid() {
		if(this.size() == 5){
			if(this.getCard(0).rank == this.getCard(1).rank && this.getCard(1).rank == this.getCard(2).rank && this.getCard(3).rank == this.getCard(4).rank){
				return true;
			}else if(this.getCard(0).rank == this.getCard(1).rank && this.getCard(2).rank == this.getCard(3).rank && this.getCard(3).rank == this.getCard(4).rank){
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
	 * @return a string (FullHouse)
	 * @see Hand#getType()
	 */
	public String getType() {
		return "FullHouse";
	}


}
