import java.awt.Frame;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JOptionPane;


public class BigTwoClient implements CardGame, NetworkGame{
	
	private int numOfPlayers;						//an integer specifying the number of players
	private Deck deck;								//a deck of cards
	private ArrayList<CardGamePlayer> playerList;	//a list of players
	private ArrayList<Hand> handsOnTable;			//a list of hands played on the table
	private int playerID = -1;						//an integer specifying the playerID (i.e., index) of the local player
	private String playerName;						//a string specifying the name of the local player
	private String serverIP;						//a string specifying the IP address of the game server
	private int serverPort;							//an integer specifying the TCP port of the game server
	private Socket sock;							//a socket connection to the game server
	private ObjectOutputStream oos;					//an ObjectOutputStream for sending messages to the server
	private int currentIdx;							//an integer specifying the index of the player for the current turn
	private BigTwoTable table;						//a Big Two table which builds the GUI for the game and handles all user actions
	private int previousplayedplayer = -1;    		//index of previous player that played without pass. If the previous played player is same as the current player, this means that all other players pass after this player played the card.				
	private ObjectInputStream os;					//This ObjectInputStream is used to read the object from the server
	private Runnable threadJob;						//This is the threadJob used in thread_1, which is the server handler 
	private Thread thread_1;						//This thread accept the threadJob and used in make connection
	
	/**
	 * This is the constructor of BigTwoClient.
	 * It creates 4 players and add them to the player list.
	 * It creates a big two table to build the gui and handle user actions
	 * It makes a connection to the game server.
	 */
	public BigTwoClient(){
		String NameInput = JOptionPane.showInputDialog("Please input your name:");
		setPlayerName(NameInput);
		playerList = new ArrayList<CardGamePlayer>();
		handsOnTable = new ArrayList<Hand>();
		numOfPlayers = 4;
		CardGamePlayer one = new CardGamePlayer();
		CardGamePlayer two = new CardGamePlayer();
		CardGamePlayer three = new CardGamePlayer();
		CardGamePlayer four = new CardGamePlayer();
		one.setName("");
		two.setName("");
		three.setName("");
		four.setName("");
		playerList.add(one);
		playerList.add(two);
		playerList.add(three);
		playerList.add(four);
		table = new BigTwoTable(this);
		table.setup();
		makeConnection();
		
	}
	
	/**
	 * This method is used to get the player id.
	 * 
	 * @return the player id
	 * @see NetworkGame#getPlayerID()
	 */
	@Override
	public int getPlayerID() {
		return playerID;
	}

	/**
	 * This method is used to set the player id.
	 * 
	 * @param playerID the id that we want to set
	 * @see NetworkGame#setPlayerID(int)
	 */
	@Override
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

	/**
	 * This method is used to get the player name.
	 * 
	 * @return the player name
	 * @see NetworkGame#getPlayerName()
	 */
	@Override
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * This method is used to set the player name.
	 * 
	 * @param playerName the player name that we want to set
	 * @see NetworkGame#setPlayerName(java.lang.String)
	 */
	@Override
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	/**
	 * This method is used to get the server ip.
	 * 
	 * @return the server ip
	 * @see NetworkGame#getServerIP()
	 */
	@Override
	public String getServerIP() {
		return serverIP;
	}

	/**
	 * This method is used to set the server ip.
	 * 
	 * @param serverIP the server ip that we want to set
	 * @see NetworkGame#setServerIP(java.lang.String)
	 */
	@Override
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

	/**
	 * This method is used to get the server port.
	 * 
	 * @return the server port
	 * @see NetworkGame#getServerPort()
	 */
	@Override
	public int getServerPort() {
		return serverPort;
	}

	/**
	 * This method is used to set the server port.
	 * 
	 * @param serverPort the server port we want the set
	 * @see NetworkGame#setServerPort(int)
	 */
	@Override
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	
	/**
	 * This method is used to disconnect the client if the server is full.
	 */
	public void disconnect(){
		try {
			sock.close();
			os = null;
			oos = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method is used to make a socket connection with the game server.
	 * It creates an ObjectOuputStream for sending messages to the game server.
	 * It creates a thread for receiving messages from the game server.
	 * It sends join and ready messages to the game server. 
	 * 
	 * @see NetworkGame#makeConnection()
	 */
	@Override
	public void makeConnection() {
		setServerIP(JOptionPane.showInputDialog("Please input the IP", "127.0.0.1"));
		setServerPort(Integer.parseInt(JOptionPane.showInputDialog("Please input the server port", "2396")));
		try {
			sock = new Socket(getServerIP(),getServerPort());
			oos = new ObjectOutputStream(sock.getOutputStream());
			threadJob = new ServerHandler();
			thread_1 = new Thread(threadJob);
			thread_1.start();
			
			CardGameMessage message_1 = new CardGameMessage(CardGameMessage.JOIN, -1, playerName);
			sendMessage(message_1);
			CardGameMessage message_2 = new CardGameMessage(CardGameMessage.READY, -1, null);
			sendMessage(message_2);
			table.disableConnectMenuItem();
		} catch (IOException e) {
			table.printMsg("Error in making connection with the game server \n");
			table.enableConnectMenuItem();
			e.printStackTrace();
		}
	}

	/**
	 * This method is used to parse messages received from the game server.
	 * It should be called from the thread responsible for receiving messages from the game server. 
	 * Based on the message type, different actions will be carried out.
	 * 
	 * @param message the message received from the game server.
	 * @see NetworkGame#parseMessage(GameMessage)
	 */
	@Override
	public void parseMessage(GameMessage message) {
		if(message.getType() == CardGameMessage.PLAYER_LIST){
			playerID = message.getPlayerID();
			String[] tempName = (String[]) message.getData();		
			for(int i=0 ; i<4 ; i++){
				if(tempName[i] != null){
					this.playerList.get(i).setName(tempName[i]);
				}
			}
			table.setneedPaint(true);
			table.repaint();
		}else if(message.getType() == CardGameMessage.JOIN){
			
			playerList.get(message.getPlayerID()).setName((String) message.getData());
			table.repaint();
		}else if(message.getType() == CardGameMessage.FULL){
			disconnect();
			table.printMsg("The server is full.\nYou cannot join the game.\n");
			table.enableConnectMenuItem();
			table.repaint();
		}else if(message.getType() == CardGameMessage.QUIT){
			table.printMsg(playerList.get(message.getPlayerID()).getName() + " leaves the game.\n");
			sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
			currentIdx = -1;
			table.setActivePlayer(currentIdx);
			playerList.get(message.getPlayerID()).setName("");
			table.reset();
			table.repaint();
		}else if(message.getType() == CardGameMessage.READY){
			table.printMsg(playerList.get(message.getPlayerID()).getName() + " is ready!\n");
		}else if(message.getType() == CardGameMessage.START){
			start((BigTwoDeck) message.getData());
		}else if(message.getType() == CardGameMessage.MOVE){
			checkMove(message.getPlayerID(), (int[]) message.getData());
			table.repaint();
		}else if(message.getType() == CardGameMessage.MSG){
			table.printMsgChat((String) message.getData());
			table.repaint();
		}
	}

	/**
	 * This method is used to send the messages to the game server.
	 * It should be called when the client wants to communicate with the game server or other clients.
	 * 
	 * @param message the message that the client wants to send
	 * @see NetworkGame#sendMessage(GameMessage)
	 */
	@Override
	public void sendMessage(GameMessage message) {
		try {
			oos.writeObject(message);
			oos.flush();
		} catch (IOException e) {
			table.printMsg("Error in sending message\n");
			e.printStackTrace();
		}
	}

	/**
	 * This method is used to get the number of players
	 * 
	 * @return the number of players
	 * @see CardGame#getNumOfPlayers()
	 */
	@Override
	public int getNumOfPlayers() {
		return numOfPlayers;
	}

	/**
	 * This method is used to get the deck used in the game.
	 * 
	 * @return the deck used in the game
	 * @see CardGame#getDeck()
	 */
	@Override
	public Deck getDeck() {
		return deck;
	}

	/**
	 * This method is used to get the player list.
	 * 
	 * @return the player list
	 * @see CardGame#getPlayerList()
	 */
	@Override
	public ArrayList<CardGamePlayer> getPlayerList() {
		return playerList;
	}

	/**
	 * This method is used to get the list of hands played before.
	 * 
	 * @return the list of hands played before
	 * @see CardGame#getHandsOnTable()
	 */
	@Override
	public ArrayList<Hand> getHandsOnTable() {
		return handsOnTable;
	}

	/**
	 * This method is used to get the index of the player for the current turn.
	 * 
	 * @return the index of the player for the current turn
	 * @see CardGame#getCurrentIdx()
	 */
	@Override
	public int getCurrentIdx() {
		return currentIdx;
	}

	/**
	 * This method is used to start the game with a given shuffled deck of cards.
	 * It removes all the cards from the plaer and table.
	 * It then distributes the cards to the players and identify the player who holds the diamond 3.
	 * It set the currentIdx and active player in this class and BigTwoTable to the index of the player who holds the diamond 3 respectively.
	 * 
	 * @param deck a deck of cards used in the game
	 * @see CardGame#start(Deck)
	 */
	@Override
	public void start(Deck deck) {
		for(int i=0 ; i<playerList.size() ; i++){
			playerList.get(i).removeAllCards();
		}
		for(int i=0 ; i<handsOnTable.size() ; i++){
			handsOnTable.remove(i);
		}
		int whichplayer = 0;
		for(int i=0 ; i<deck.size() ; i++){
			playerList.get(whichplayer%4).addCard(deck.getCard(i));
			if(deck.getCard(i).rank == 2 && deck.getCard(i).suit == 0){
				currentIdx = whichplayer%4;
				table.setActivePlayer(currentIdx);
				table.printMsg(playerList.get(currentIdx%4).getName() + "'s turn: \n");
			}
			whichplayer++;
		}
		for(int i=0 ; i<4 ; i++){
			playerList.get(i).sortCardsInHand();
		}
		
		table.setbegingame();
		table.repaint();
		
	}

	/**
	 * This method is used for making a move by a player with the specified ID using the cards specified by the list of indices.
	 * It will be called when the player pressed the play or pass button.
	 * It will send a message of type move to the game server.
	 * 
	 * @see CardGame#makeMove(int, int[])
	 */
	@Override
	public void makeMove(int playerID, int[] cardIdx) {
		sendMessage(new CardGameMessage(CardGameMessage.MOVE, playerID, cardIdx));
	}

	/**
	 * This method is used to check whether the move of the player is legal or not.
	 * It will be called when a message of type move is received from the game server. 
	 * 
	 * @see CardGame#checkMove(int, int[])
	 */
	@Override
	public void checkMove(int playerID, int[] cardIdx) {
		boolean firstplayer = false;
		if( WhichPlayerStart() != -1){
			firstplayer = true;
			currentIdx = WhichPlayerStart();
			previousplayedplayer = -1;
		}
		
		if(cardIdx.length == 0 && firstplayer == false && previousplayedplayer != playerID){			//If the player did not input anything which means that the player pass.
			table.setActivePlayer(currentIdx);			
			currentIdx++;							//active player will be the next player.
			table.setActivePlayer(currentIdx%4);
			table.printMsg(playerList.get(currentIdx%4).getName() + "'s turn: \n");
			table.printMsg("{pass} \n");
		
		}else if(cardIdx.length == 0 && (firstplayer == true || previousplayedplayer == playerID)){  //player cannot pass if he holds the diamond three or all other players pass after he played the hand.
			table.setActivePlayer(currentIdx%4);
			previousplayedplayer = currentIdx;
			table.printMsg("{pass}");
			table.printMsg(" <== Not a legal move!!! \n");	
		
		}else{
			CardList input = playerList.get(playerID).play(cardIdx);		//create a new CardList to save the cards typed by the player.
			Hand WhichHand = composeHand(playerList.get(playerID),input);  //check which type of hand of the "input" CardList
			
			if(WhichHand == null){
				table.printMsg("" + input);
				table.printMsg(" <== Not a legal move!!! \n");					//if composeHand returns null, it is an illegal move and the player needs to type again
			
			}else if(LegalMove(handsOnTable, WhichHand, firstplayer) == false && previousplayedplayer != currentIdx){
				table.printMsg("{" + WhichHand.getType() + "} " + input);
				table.printMsg(" <== Not a legal move!!! \n");			// if the LegalMove() method returns false, it is not a legal move
			
			}else{
				input.sort();			//sort the "input" CardList and display in the console after checking the move is legal
				table.printMsg("{" + WhichHand.getType() + "} " + input + "\n");  
				for(int i=0 ; i<cardIdx.length ; i++){
					playerList.get(playerID).removeCards(input);			   //remove the cards held by the player if they are the same as the "input" CardList

				}
				
				handsOnTable.add(WhichHand);			//add the hand that played by the player in the arraylist handsOnTable
				previousplayedplayer = currentIdx;		//the current player will become the previousplayedplayer after finishing his round
				currentIdx++;							//current player will become the next player
				
				firstplayer = false;					//After the first player played, this value will become false so other players do not need to play diamond three
				if(!endOfGame()){
					table.printMsg(playerList.get(currentIdx%4).getName() + "'s turn: \n");
					table.setActivePlayer(currentIdx%4);
				}else{
					table.repaint();
					printEndOfGame();
					table.reset();
				}
					
			}
			
			input.removeAllCards();						//remove all the cards of the object "input" in order to be used in the next round
			
		}
		currentIdx %= 4;
	}
	
	private boolean LegalMove(ArrayList<Hand> HandsOnTable, Hand InputHand, boolean firstplayer){
		 //If it is the first player that plays card (firstplayer == true), the hand must contain diamond three card.
		 //Otherwise, the current player needs to played cards that greater than the previous hand played by another player.
		if(firstplayer == true){
			for(int i=0 ; i<InputHand.size() ; i++){
				if(InputHand.getCard(i).rank == 2 && InputHand.getCard(i).suit == 0){
					return true;
				}
			}
			return false;
		}else{
			//if the hand played before is Single or Pair or Triple and the hand played by the current player is not the same as the hand played before, then this must return false.
			if(HandsOnTable.get(handsOnTable.size()-1).getType() != InputHand.getType() && (HandsOnTable.get(handsOnTable.size()-1).getType() == "Single" || HandsOnTable.get(handsOnTable.size()-1).getType() == "Pair" || HandsOnTable.get(handsOnTable.size()-1).getType() == "Triple" || InputHand.getType() == "Single" || InputHand.getType() == "Pair" || InputHand.getType() == "Triple")){
				return false;
			}else{
				//if the hand played by the previous player is the same as the hand played by the current player or the number of cards is 5, then this depends on the beats method of the previous hand.
				//return true if the hand played by the current player can beat the hand played by the previous player.
				return (InputHand.beats(HandsOnTable.get(handsOnTable.size()-1)));
			}
		}
	}
	
	private int WhichPlayerStart(){
	//this method is used to find out the player holds the diamond 3.
		for(int i=0 ; i<playerList.size() ; i++){
			for(int j=0 ; j<playerList.get(i).getNumOfCards() ; j++){
				if(playerList.get(i).getCardsInHand().getCard(j).rank == 2 && playerList.get(i).getCardsInHand().getCard(j).suit == 0){
					return i;
				}
			}
		}
		return -1;
	}
	
	/**
	 * This method is used to check if the game ends.
	 * 
	 * @return true if the game ends; otherwise, return false
	 * @see CardGame#endOfGame()
	 */
	@Override
	public boolean endOfGame() {
		for(int i=0 ; i<playerList.size() ; i++){
			if(playerList.get(i).getNumOfCards() == 0){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * This method is used to print the information of the game after the end of the game.
	 * It sends a message of type ready to the game server after the player clicks the OK button.
	 * 
	 */
	public void printEndOfGame(){
		// print the message of the game
		String endGameMsg = "";
		
		endGameMsg += ("Game ends \n");
		for(int i=0 ; i<playerList.size() ; i++){
			if(playerList.get(i).getNumOfCards() != 0){
				if(playerID != i){
					endGameMsg += (playerList.get(i).getName() + " has " + playerList.get(i).getNumOfCards() + " cards in hand. \n");
				}else if(playerID == i){
					endGameMsg += ("You has " + playerList.get(i).getNumOfCards() + " cards in hand. \n");
				}
			}else{
				if(playerID != i){
					endGameMsg += (playerList.get(i).getName() + " wins the game. \n");
				}else if(playerID == i){
					endGameMsg += ("You wins the game. \n");
				}
					
			}
		}

		Object[] options = {"OK, Continue", "No, Leave"};
		int n = JOptionPane.showOptionDialog(new Frame(), endGameMsg, "End Game!!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		if(n == 0){
			sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
		}else if(n == 1){
			System.exit(0);
		}

		//JOptionPane.showMessageDialog(null, endGameMsg);
		
	}
	
	/**
	 * This method is used to check whether it is a valid hand or not.
	 * 
	 * @param player the player who plays this hand
	 * @param cards the cards played by the player
	 * @return return true if it is a valid hand; otherwise, return false
	 */
	public static Hand composeHand(CardGamePlayer player, CardList cards){
		Single single = new Single(player, cards);
		Pair pair = new Pair(player, cards);
		Triple triple = new Triple(player, cards);
		Straight straight = new Straight(player, cards);
		Flush flush = new Flush(player, cards);
		FullHouse fullhouse = new FullHouse(player, cards);
		Quad quad = new Quad(player, cards);
		StraightFlush straightflush = new StraightFlush(player, cards);
		if(single.isValid()){
			return single;
		}else if(pair.isValid()){
			return pair;
		}else if(triple.isValid()){
			return triple;
		}else if(straight.isValid()){
			return straight;
		}else if(flush.isValid()){
			return flush;
		}else if(fullhouse.isValid()){
			return fullhouse;
		}else if(quad.isValid()){
			return quad;
		}else if(straightflush.isValid()){
			return straightflush;
		}else{
			return null;
		}
	}
	
	/**
	 * This inner class is used to hand the server, and it implements the runnable interface.
	 * It implement the run() method from the Runnable interface and create a thread with an instance of this class as its job in the makeConnection() method from the NetworkGame interface for receiving messages from the game server.
	 * 
	 * @author Lau Kin Fung
	 *
	 */
	class ServerHandler implements Runnable{
		private CardGameMessage message;
		
		/**
		 * This constructor is used to create a new ObjectInputStream.
		 */
		public ServerHandler(){
			try {
				os = new ObjectInputStream(sock.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * This method is used to handle the message received from the game server.
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			try {
				while((message = (CardGameMessage) os.readObject()) != null) {
					parseMessage(message);
				}
			} catch (Exception e) {
				table.printMsg("Error in receiving messages from the game server \n");
				table.disableButtons();
				table.enableConnectMenuItem();
				for(int i=0 ; i<4 ; i++){
					if(i != playerID){
						playerList.get(i).setName("");
					}
				}
				currentIdx = -1;
				table.setActivePlayer(currentIdx);
				table.reset();
				table.repaint();
			} 
		}
	}

	/**
	 * This method is used to create an instance of BigTwoClient.
	 * 
	 * @param args Unused
	 */
	public static void main(String[] args){
		new BigTwoClient();
	}
	

}
