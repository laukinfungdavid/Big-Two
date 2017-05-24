
/**
 * This BigTwoCard is the subclass of Card, which is used to model a card used in a Big Two card game.
 * 
 * @author Lau Kin Fung
 * @version 1.0
 * @since 2016-10-13
 *
 */
public class BigTwoCard extends Card{
	
	private static final long serialVersionUID = -5822018182891811204L;

	/**
	 * This is the constructor for buidling a card with the specified suit and rank.
	 * 
	 * @param suit an integer between 0 and 3
	 * @param rank an integer between 0 and 12
	 */
	public BigTwoCard(int suit, int rank){
		super(suit, rank);
	}
	
	/**
	 * This method overrides the compareTo(Card card) in superclass Card and is used to compare this card with the specified card for order.
	 * 
	 * @param card a specific card that is compare with this card.
	 * @return -1, 0, or 1 if this card is less than, equal to, or greater than the specified card respectively
	 * @see Card#compareTo(Card)
	 */
	public int compareTo(Card card){
		int thisrank = this.rank;
		int cardrank = card.rank;
		//add 12 to the index that is less than 2 so it is easy to compare.
		if(thisrank < 2){
			thisrank = thisrank + 13;
		}	
		if(card.rank < 2){
			cardrank = cardrank + 13;
		}
		if(thisrank > cardrank){
			return 1;
		}else if(thisrank < cardrank){
			return -1;
		}else if(this.suit > card.suit){
			return 1;
		}else if(this.suit < card.suit){
			return -1;
		}else{
			return 0;
		}
	}
}
