import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.swing.*;


/**
 * This BigTwoTable class implemets the CardGameTable interface and it is used to build the GUI for the game and handle all the user actions.
 * 
 * @author Lau Kin Fung
 *
 */
public class BigTwoTable implements CardGameTable{
	
	private BigTwoClient game;						// a card game associates with this table
	private boolean[] selected;						// a boolean array indicating which cards are selected
	public int activePlayer;						// an integer specifying the index of the active player
	private JFrame frame;							// the main window of the game
	private JPanel bigTwoPanel;						// panel showing the cards of each player and the cards played by the pervious player
	private JPanel chatInputPanel;
	private JButton playButton;						// button for players to play cards
	private JButton passButton;						// button for players to pass
	private JButton deselectAllButton;				// button for deselecting all the selected cards
	private JTextArea msgArea;						// text area for showing the game status and the message of the end of the game
	private JTextArea serverMsgArea;
	private JTextField messageInputArea;
	private Image[][] cardImages;					// 2D array storing the images of the front face of the cards
	private Image cardBackImage;					// image storing the back image of the card
	private Image cardBackImage_rotate;				// image storing the rotated back image of the card
	private Image[] avatars;						// array storing the image of the avatars
	private JLabel messageLabel;

	private JPanel button;							// Panel storing the three buttons (Pass, Play, DeselectAll)
	private Rectangle[] rect_CheckContainMouse;		// rectangle array is used to detect the mouse click using contain method. Each element in the array should have the same coordinate as the cards
	private JMenuBar Menubar;						// Menu Bar in the game
	private JMenu game_menu;						// Game menu in the Menubar
	private JMenu message_menu;						// Message menu in Menubar
	private JMenuItem ClearGameMessageArea;			// menu item used to clear the game message area
	private JMenuItem ClearChatMessageArea;			// menu item used to clear the chat message area
	private JMenuItem ConnectMenuItem;				// restart menu item in the menu
	private JMenuItem QuitMenuItem;					// quit menu item in the menu
	
	private boolean Animation = true;				// boolean value states that whether it is the beginning of the game. If true, animation is played
	private int[] NumOfCardsDrawn = new int[2];		// integer array storing the number of cards needed to draw when we play the animation at the beginning of the game
	private int deliver_counter;					// a counter used in the animation is to count the number of times that the cards are delivered
	private boolean begin = false;					// boolean value states that
	private boolean needPaint = false;				// boolean value states that whether the big two panel should paint the avatars' image. It is used to avoid error.
	
	/**
	 * This is a the constructor of the BigTwoTable, which is used to create a new BigTwoTable.
	 * 
	 * @param game reference to a card game associates with this table
	 */
	public BigTwoTable(BigTwoClient game){
		this.game = game;
		
	}
	
	/**
	 * This setter is used to set the index of the active player that is the current player.
	 * 
	 * @param activePlayer the index of the active player
	 * @see CardGameTable#setActivePlayer(int)
	 */
	@Override
	public void setActivePlayer(int activePlayer) {
		this.activePlayer = activePlayer;
	}

	/**
	 * This getter is used to get an array of indices of the cards selected.
	 * 
	 * @return an array of indices of the cards selected
	 * @see CardGameTable#getSelected()
	 */
	@Override
	public int[] getSelected() {
		ArrayList<Integer> CardIdx = new ArrayList<Integer>();
		for(int i=0 ; i<selected.length ; i++){
			if(selected[i] == true){
				CardIdx.add(i);
			}
		}
		int[] select = new int[CardIdx.size()];
		for(int i=0 ; i<CardIdx.size() ; i++){
			select[i] = CardIdx.get(i);
			
		}
		return select;
	}
 
	/**
	 * This method is used to reset the list of selected cards.
	 * 
	 * @see CardGameTable#resetSelected()
	 */
	@Override
	public void resetSelected() {
		for(int i=0 ; i<selected.length ; i++){
			selected[i] = false;
		}
	}

	/**
	 * This method is used to repaint the GUI.
	 * 
	 * @see CardGameTable#repaint()
	 */
	@Override
	public void repaint() {
		bigTwoPanel.repaint();
	}

	/**
	 * This method is used to paint the specified string to the message area of the GUI.
	 * 
	 * @param msg the specified string needed to be printed to the message area
	 * @see CardGameTable#printMsg(java.lang.String)
	 */
	@Override
	public void printMsg(String msg) {
		msgArea.append(msg);
	}
	
	public void printMsgChat(String msg) {
		serverMsgArea.append(msg);
	}
	
	/**
	 * This method is used to clear the message area of the GUI.
	 * 
	 * @see CardGameTable#clearMsgArea()
	 */
	@Override
	public void clearMsgArea() {
		msgArea.setText("");
	}

	/**
	 * This method is used to clear the chat message area of the GUI.
	 */
	public void clearChatMsgArea(){
		messageInputArea.setText("");
	}
	
	/**
	 * This method is used to set the value of needPaint.
	 * @param value a specified boolean value the player wants to set
	 */
	public void setneedPaint(boolean value){
		this.needPaint = value;
	}
	
	/**
	 * This method is used to create the GUI of the big two game.
	 */
	public void setup(){
		
		frame = new JFrame("Big Two (" + game.getPlayerName() + ")");	// create a new frame object
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// close the operation after the close button of the frame is clicked
		
		// set the layout of the frame to be grid bag layout
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.3, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.3, 0.001, Double.MIN_VALUE};
		frame.setLayout(gridBagLayout);
		
		
		bigTwoPanel = new BigTwoPanel();	// create a new big two panel object
		bigTwoPanel.setLayout(new GridBagLayout());	// set the layout of big two panel to be grid bag layout
		bigTwoPanel.setBackground(new Color(0,153,76));	// set the background color of the big two panel
		
		rect_CheckContainMouse = new Rectangle[13];	// create an array of size 13 to store the rectangle used to detect the mouse click
		selected = new boolean[13];	// create an array of size 13 to store the cards that are selected by the active player
		
		playButton = new JButton("Play");	// create a button object named "Play" to allow players to play cards
		playButton.addActionListener(new PlayButtonListener()); // add a play button listener object to the playButton
		passButton = new JButton("Pass");	// create a button object named "Pass" to allow players to pass
		passButton.addActionListener(new PassButtonListener());	// add a pass button listen object to the passButton
		deselectAllButton = new JButton("Deselect All");	// create a button object named "Deselect All" to allow players to deselect all the selected cards
		deselectAllButton.addActionListener(new DeselectAllButtonListener());	// add a DeselectAll button listener object to the deselectAllButton
		
		Menubar = new JMenuBar();	// create a menu bar object
		game_menu = new JMenu("Game");	// create a menu object named "Game" to store two menu item
		ConnectMenuItem = new JMenuItem("Connect");	// create a menu item object named "Restart" to allow players to restart the game
		QuitMenuItem = new JMenuItem("Quit");	// create a menu item object named "Quit" to allow players to quit the game
		ConnectMenuItem.addActionListener(new ConnectMenuItemListener()); // add a restart menu item listener object to the RestartMenuItem
		QuitMenuItem.addActionListener(new QuitMenuItemListener());	// add a quit menu item listener object to the QuitMenuItem
		message_menu = new JMenu("Message"); //create a menu object named "Message" to store two menu item
		ClearGameMessageArea = new JMenuItem("Clear game history"); // create a menu item object named "Clear game history" to allow players to clear the game message area
		ClearChatMessageArea = new JMenuItem("Clear Chat history"); // create a menu item object named "Clear chat history" to allow players to clear the chat message area
		ClearChatMessageArea.addActionListener(new ClearChatMessageAreaListener()); 	// add a ClearChatMessageArea listen object to the ClearChatMessageArea
		ClearGameMessageArea.addActionListener(new ClearGameMessageAreaListener());     // add a ClearGameMessageArea listen object to the ClearGameMessageArea
		
		msgArea = new JTextArea();	// create a new text area object for printing game message
		
		// create an image array of size 4 to store the image of the avatars
		avatars = new Image[4];
		avatars[0] = new ImageIcon("Avatars/batman_256.png").getImage();
		avatars[1] = new ImageIcon("Avatars/flash_256.png").getImage();
		avatars[2] = new ImageIcon("Avatars/superman_256.png").getImage();
		avatars[3] = new ImageIcon("Avatars/green_lantern_256.png").getImage();
		
		// create an 2D array haing 4 rows and 13 columns to store the card images
		cardImages = new Image[4][13];
		for(int i=0 ; i<4 ; i++){
			for(int j=0 ; j<13 ; j++){
				cardImages[i][j] = new ImageIcon("CardsImage/" + j + i + ".gif").getImage();
			}
		}
		
		cardBackImage = new ImageIcon("CardsImage/b.gif").getImage();	// create a image icon object to store the back image of the card
		cardBackImage_rotate = new ImageIcon("CardsImage/b_rotate.gif").getImage();	// create a image icon object to store the rotated (90 degrees) back image of the card
		
		
		// create a panel named "button" that store the three buttons
		button = new JPanel();
		button.add(playButton);
		button.add(passButton);
		button.add(deselectAllButton);
		
		// set the gridBagConstraints and add the button panel to the left bottom of the frame.
		GridBagConstraints gbc_button = new GridBagConstraints();
		gbc_button.gridwidth = 13;
		gbc_button.fill = GridBagConstraints.BOTH;
		gbc_button.gridx = 0;
		gbc_button.gridy = 8;
		frame.getContentPane().add(button, gbc_button);

		// create a scroll pane object to store the msgArea
		JScrollPane scroll = new JScrollPane();
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		msgArea.setEditable(false);	// the msgArea cannot be edited
		scroll.setViewportView(msgArea);
		
		// set the menu bar and add the menu bar to the frame
		game_menu.add(ConnectMenuItem);
		game_menu.add(QuitMenuItem);
		message_menu.add(ClearGameMessageArea);
		message_menu.add(ClearChatMessageArea);
		Menubar.add(game_menu);
		Menubar.add(message_menu);
		frame.setJMenuBar(Menubar);
		
		// set the grid bag constraints and add the big two panel to the left side of the frame
		GridBagConstraints gbc_BigTwoPanel = new GridBagConstraints();
		gbc_BigTwoPanel.gridheight = 8;
		gbc_BigTwoPanel.gridwidth = 13;
		gbc_BigTwoPanel.fill = GridBagConstraints.BOTH;
		gbc_BigTwoPanel.gridx = 0;
		gbc_BigTwoPanel.gridy = 0;
		frame.add(bigTwoPanel, gbc_BigTwoPanel);
		
		//set the grid bag constraints and add the scroll pane to the right side of the frame
		GridBagConstraints gbc_scroll = new GridBagConstraints();
		gbc_scroll.gridheight = 4;
		gbc_scroll.gridwidth = 2;
		gbc_scroll.fill = GridBagConstraints.BOTH;
		gbc_scroll.gridx = 13;
		gbc_scroll.gridy = 0;
		scroll.setPreferredSize(new Dimension(20, frame.getHeight()/2));
		frame.add(scroll, gbc_scroll);
		
		//create a new chat message area and add it in the frame
		serverMsgArea = new JTextArea();
		JScrollPane scroll_server = new JScrollPane();
		scroll_server.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll_server.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		serverMsgArea.setEditable(false);
		scroll_server.setViewportView(serverMsgArea);
		GridBagConstraints gbc_scroll_server = new GridBagConstraints();
		gbc_scroll_server.gridheight = 3;
		gbc_scroll_server.gridwidth = 2;
		gbc_scroll_server.fill = GridBagConstraints.BOTH;
		gbc_scroll_server.gridx = 13;
		gbc_scroll_server.gridy = 4;
		scroll_server.setPreferredSize(new Dimension(20, frame.getHeight()/2));
		frame.add(scroll_server, gbc_scroll_server);
		
		// create a new chat panel for the players to type their message
		chatInputPanel = new JPanel();
		messageLabel = new JLabel("Message");
		messageInputArea = new JTextField(8);
		messageInputArea.addActionListener(new ChatInputListener());
		chatInputPanel.add(messageLabel);
		chatInputPanel.add(messageInputArea);
		
		// add the chat panel to the frame
		GridBagConstraints gbc_chatInputPanel = new GridBagConstraints();
		gbc_chatInputPanel.gridheight = 2;
		gbc_chatInputPanel.gridwidth = 2;
		gbc_chatInputPanel.fill = GridBagConstraints.BOTH;
		gbc_chatInputPanel.gridx = 13;
		gbc_chatInputPanel.gridy = 7;
		chatInputPanel.setPreferredSize(new Dimension(20, frame.getHeight()/2));
		frame.add(chatInputPanel, gbc_chatInputPanel);
			
		frame.setSize(640, 480);	// set the size of the frame
		frame.setVisible(true);		// set the frame to be visible
		
		// disable the pass, play and deselectAll button
		this.passButton.setEnabled(false);
		this.playButton.setEnabled(false);
		this.deselectAllButton.setEnabled(false);
	}
	
	/**
	 * This method is used to reset the GUI. It initializes all the variables.
	 * 
	 * 
	 * @see CardGameTable#reset()
	 */
	@Override
	public void reset() {
		for(int i=0 ; i<game.getPlayerList().size() ; i++){
			game.getPlayerList().get(i).removeAllCards();
		}
		for(int i=0 ; i<game.getHandsOnTable().size() ; i++){
			game.getHandsOnTable().remove(i);
		}
		Animation = true;
		NumOfCardsDrawn[0] = 0;
		NumOfCardsDrawn[1] = 0;
		repaint();
		clearMsgArea();
		clearChatMsgArea();
	}

	/**
	 * This method is used to enable user interactions with the GUI.
	 * 
	 * @see CardGameTable#enable()
	 */
	@Override
	public void enable() {
		frame.setEnabled(true);
	}

	/**
	 * This method is used to disable user interations with the GUI.
	 * 
	 * @see CardGameTable#disable()
	 */
	@Override
	public void disable() {
		frame.setEnabled(false);	
	}
	
	/**
	 * This method is used to disable the connect menu item.
	 */
	public void disableConnectMenuItem(){
		ConnectMenuItem.setEnabled(false);
	}
	
	/**
	 * This method is used to enable the connect menu item.
	 */
	public void enableConnectMenuItem(){
		ConnectMenuItem.setEnabled(true);
	}
	
	/**
	 * This method is used to start the animation and paint the big two panel.
	 * It is called when the client receives start message.
	 */
	public void setbegingame(){
		begin = true;
		Thread animation = new Thread((Runnable) bigTwoPanel);	// create a new thread to run the animation
		animation.start();	// start the animation
		Animation = false;  
		repaint();
	}
	
	/**
	 * This method is used to disable all the button and the big two panel.
	 */
	public void disableButtons(){
		passButton.setEnabled(false);
		playButton.setEnabled(false);
		deselectAllButton.setEnabled(false);
	}
	
	/**
	 * This inner class extends the JPanel class and implements the mouse listener interface.
	 * It also implements the runnable, which is used to play the animation several times.
	 * 
	 * @author Lau Kin Fung
	 *
	 */
	class BigTwoPanel extends JPanel implements MouseListener, Runnable{
		
		private static final long serialVersionUID = -3788192458886508266L;
		
		private int anim_y_one;
		private int anim_y_two;
		private int anim_x_one;
		private int anim_x_two;
		private int anim_y_three;
		private int anim_x_three;
		private boolean drawHorizontalCard = false;
		private boolean drawVerticalCard = false;
		
		/**
		 * This constructor is used to add the this panel in the mouse listener.
		 */
		public BigTwoPanel(){
			this.addMouseListener(this);
		}	
		
		/**
		 * This method is used to draw the cardgame table including the card images, avatar images and so on.
		 * 
		 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
		 */
		@Override
		public void paintComponent(Graphics g){
			
			Graphics2D g2d = (Graphics2D) g;
			super.paintComponent(g2d);
			
			int WidthSpanedByAllCards = 20*12 + cardBackImage.getWidth(this);	// the width spaned by all the cards that players have

			if(begin){
				// draw the animation when it is the beginning of the game
				if(drawHorizontalCard){
					g2d.drawImage(cardBackImage,(bigTwoPanel.getWidth()-cardBackImage.getWidth(null))/2, (bigTwoPanel.getHeight()-cardBackImage.getHeight(null))/2, this);
					g2d.drawImage(cardBackImage_rotate, anim_x_one, anim_y_three, this);
					g2d.drawImage(cardBackImage_rotate, anim_x_two, anim_y_three, this);
				}else if(drawVerticalCard){
					g2d.drawImage(cardBackImage,(bigTwoPanel.getWidth()-cardBackImage.getWidth(null))/2, (bigTwoPanel.getHeight()-cardBackImage.getHeight(null))/2, this);
					g2d.drawImage(cardBackImage, anim_x_three, anim_y_one, this);
					g2d.drawImage(cardBackImage, anim_x_three, anim_y_two, this);
				}
				for(int i=0 ; i<NumOfCardsDrawn[1] ; i++){
					g2d.drawImage(cardBackImage, (this.getWidth() - WidthSpanedByAllCards)/2 + 20*i, this.getHeight()-cardBackImage.getHeight(this)-5, this);
					g2d.drawImage(cardBackImage, (this.getWidth() - WidthSpanedByAllCards)/2 + 20*i, 5, this);
				}
				for(int i=0 ; i<NumOfCardsDrawn[0] ; i++){
					g2d.drawImage(cardBackImage_rotate, this.getWidth() - cardBackImage_rotate.getWidth(this) - 5, (this.getHeight() - WidthSpanedByAllCards)/2 + 20*i, this);
					g2d.drawImage(cardBackImage_rotate, 5, (this.getHeight() - WidthSpanedByAllCards)/2 + 20*i, this);
				}
			}else if(!Animation && !game.endOfGame()){
				// if it is not the beginning of the game and the end of the game, draw the cards held by the players
				if(activePlayer != game.getPlayerID()){
					passButton.setEnabled(false);
					playButton.setEnabled(false);
					deselectAllButton.setEnabled(false);
				}else if(activePlayer == game.getPlayerID()){
					passButton.setEnabled(true);
					playButton.setEnabled(true);
					deselectAllButton.setEnabled(true);
				}
				
				for(int k=0 ; k<4 ; k++){
					int i=(k+game.getPlayerID())%4;
					
					WidthSpanedByAllCards = 20*(game.getPlayerList().get(i).getNumOfCards()-1) + cardBackImage.getWidth(this);
					for(int j=0 ; j<game.getPlayerList().get(i).getNumOfCards() ; j++){
						int cardrank, cardsuit;
						cardrank = game.getPlayerList().get(i).getCardsInHand().getCard(j).rank;
						cardsuit = game.getPlayerList().get(i).getCardsInHand().getCard(j).suit;
						
						
						if(i == game.getPlayerID()){
							rect_CheckContainMouse[j] = new Rectangle(cardBackImage.getWidth(this), cardBackImage.getHeight(this));
							
							if(i!=activePlayer)
								g2d.drawImage(cardImages[cardsuit][cardrank], (this.getWidth() - WidthSpanedByAllCards)/2 + 20*j, this.getHeight()-cardBackImage.getHeight(this)-5, this);

							if(selected[j] == true && i == activePlayer){
								// draw the rectangle having the same coordinates as the card image in order to check the mouse click
								rect_CheckContainMouse[j].setLocation((this.getWidth() - WidthSpanedByAllCards)/2 + 20*j, this.getHeight()-cardBackImage.getHeight(this)-15);
								g2d.drawImage(cardImages[cardsuit][cardrank], (this.getWidth() - WidthSpanedByAllCards)/2 + 20*j, this.getHeight()-cardBackImage.getHeight(this)-15, this);
							}else if(selected[j] == false && i == activePlayer){
								rect_CheckContainMouse[j].setLocation((this.getWidth() - WidthSpanedByAllCards)/2 + 20*j, this.getHeight()-cardBackImage.getHeight(this)-5);
								g2d.drawImage(cardImages[cardsuit][cardrank], (this.getWidth() - WidthSpanedByAllCards)/2 + 20*j, this.getHeight()-cardBackImage.getHeight(this)-5, this);
							}
						}else if(k == 1){
							g2d.drawImage(cardBackImage_rotate, this.getWidth() - cardBackImage_rotate.getWidth(this) - 5, (this.getHeight() - WidthSpanedByAllCards)/2 + 20*j, this);
						}else if(k == 2){
							g2d.drawImage(cardBackImage, (this.getWidth() - WidthSpanedByAllCards)/2 + 20*j, 5, this);
						}else if(k == 3){
							g2d.drawImage(cardBackImage_rotate, 5, (this.getHeight() - WidthSpanedByAllCards)/2 + 20*j, this);
						}
					}
					
				}
				
				// draw the hand played by the previous player in the middle of the big two panel
				if(game.getHandsOnTable().size() > 0){
					
					Hand previous = game.getHandsOnTable().get(game.getHandsOnTable().size()-1);
					for(int i=0 ; i<previous.size() ; i++){
						WidthSpanedByAllCards = 20*(previous.size()-1) + cardBackImage.getWidth(this);
						int cardrank, cardsuit;
						cardrank = previous.getCard(i).rank;
						cardsuit = previous.getCard(i).suit;
						g2d.drawImage(cardImages[cardsuit][cardrank], (this.getWidth() - WidthSpanedByAllCards)/2 + 20*i, (bigTwoPanel.getHeight()-cardBackImage.getHeight(null))/2, this);
					}
				}
			}else if(game.endOfGame() && !Animation){
				
				// if the game ends, draw all the front of the card images held by the players
					passButton.setEnabled(false);
					playButton.setEnabled(false);
					deselectAllButton.setEnabled(false);
				
				for(int k=0 ; k<4 ; k++){
					int i=(k+game.getPlayerID())%4;
					
					if(game.getPlayerList().get(i).getNumOfCards() == 0){
						Font temp = g2d.getFont();
						g2d.setFont(new Font("default", Font.BOLD, 32));
						FontMetrics fontMetrics = g2d.getFontMetrics();
						g2d.drawString(game.getPlayerList().get(i).getName() + " wins the game", (this.getWidth() - fontMetrics.stringWidth(game.getPlayerList().get(i).getName() + "Wins the game"))/2, (this.getHeight() - fontMetrics.getHeight())/2);
						g2d.setFont(temp);
					}
					
					WidthSpanedByAllCards = 20*(game.getPlayerList().get(i).getNumOfCards()-1) + cardBackImage.getWidth(this);
					for(int j=0 ; j<game.getPlayerList().get(i).getNumOfCards() ; j++){
						int cardrank, cardsuit;
						cardrank = game.getPlayerList().get(i).getCardsInHand().getCard(j).rank;
						cardsuit = game.getPlayerList().get(i).getCardsInHand().getCard(j).suit;
						
						
						if(i == game.getPlayerID()){
							g2d.drawImage(cardImages[cardsuit][cardrank], (this.getWidth() - WidthSpanedByAllCards)/2 + 20*j, this.getHeight()-cardBackImage.getHeight(this)-5, this);
						}else if(k == 1){
							g2d.drawImage(cardImages[cardsuit][cardrank], this.getWidth() - cardBackImage_rotate.getWidth(this) - 5, (this.getHeight() - WidthSpanedByAllCards)/2 + 20*j, this);
						}else if(k == 2){
							g2d.drawImage(cardImages[cardsuit][cardrank], (this.getWidth() - WidthSpanedByAllCards)/2 + 20*j, 5, this);
						}else if(k == 3){
							g2d.drawImage(cardImages[cardsuit][cardrank], 5, (this.getHeight() - WidthSpanedByAllCards)/2 + 20*j, this);
						}
					}
					
				}
			
			}
			// After playing the animation, this will draw the avatar images	
			if(needPaint){
				for(int k=0 ; k<4 ; k++){
					int i=(k+game.getPlayerID())%4;
					if(i == activePlayer){
						g2d.setColor(Color.BLUE);
					}
					if(game.getPlayerList().get(i).getName() != "" && game.getPlayerList().get(i).getName() != null ){
						WidthSpanedByAllCards = 20*(game.getPlayerList().get(i).getNumOfCards()-1) + cardBackImage.getWidth(this);
						if(i == game.getPlayerID()){
							g2d.drawImage(avatars[i], (this.getWidth()-WidthSpanedByAllCards)/2-80, this.getHeight()-cardBackImage.getHeight(this)-20, avatars[i].getWidth(this)/3, avatars[i].getHeight(this)/3, this);
							g2d.drawString("You", (this.getWidth() - WidthSpanedByAllCards)/2-50, this.getHeight()-15);
						}else if(k == 1){
							g2d.drawImage(avatars[i], this.getWidth()-cardBackImage_rotate.getWidth(this)-5, (this.getHeight()-WidthSpanedByAllCards)/2+WidthSpanedByAllCards/3, avatars[i].getWidth(this)/3, avatars[i].getHeight(this)/3, this);
							g2d.drawString(game.getPlayerList().get(i).getName(), this.getWidth() - 55, (this.getHeight() - WidthSpanedByAllCards)/2 - 5 );
						}else if(k == 2){
							g2d.drawImage(avatars[i], (this.getWidth()-WidthSpanedByAllCards)/2+WidthSpanedByAllCards/3, 5, avatars[i].getWidth(this)/3, avatars[i].getHeight(this)/3, this);
							g2d.drawString(game.getPlayerList().get(i).getName(), (this.getWidth() - WidthSpanedByAllCards)/2-50, 20);
						}else if(k == 3){
							g2d.drawImage(avatars[i], 5, (this.getHeight()-WidthSpanedByAllCards)/2+WidthSpanedByAllCards/3, avatars[i].getWidth(this)/3, avatars[i].getHeight(this)/3, this);
							g2d.drawString(game.getPlayerList().get(i).getName(), 5, (this.getHeight() - WidthSpanedByAllCards)/2 - 5 );
						}
				}
					g2d.setColor(Color.BLACK);
			}
		}
	}
		
		/**
		 * This method is the logic of the animation.
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			for(deliver_counter=0 ; deliver_counter<13 ; deliver_counter++){
				anim_y_one = (bigTwoPanel.getHeight()-cardBackImage.getHeight(null))/2;
				anim_y_two = (bigTwoPanel.getHeight()-cardBackImage.getHeight(null))/2;
				anim_y_three = (bigTwoPanel.getHeight()-cardBackImage.getHeight(null))/2;
				anim_x_one = (bigTwoPanel.getWidth()-cardBackImage_rotate.getWidth(null))/2;
				anim_x_two = (bigTwoPanel.getWidth()-cardBackImage_rotate.getWidth(null))/2;
				anim_x_three = (bigTwoPanel.getWidth()-cardBackImage.getWidth(null))/2;
				
				// this make the cards move horizontally
				drawHorizontalCard = true;
				for(int i=0 ; i<(this.getWidth()-cardBackImage_rotate.getWidth(null)-10)/4 ; i++){
					anim_x_one += 2;
					anim_x_two -= 2;
					repaint();
					try { TimeUnit.NANOSECONDS.sleep(10);
					} catch (Exception ex) { }
				}
				NumOfCardsDrawn[0]++;
				drawHorizontalCard = false;
				
				// this make the cards move vertically
				drawVerticalCard = true;
				for(int i=0 ; i<(this.getHeight()-cardBackImage.getHeight(null)-10)/4; i++){
					anim_y_one += 2;
					anim_y_two -= 2;
					repaint();
					try { TimeUnit.NANOSECONDS.sleep(10);
					} catch (Exception ex) { }
				}
				NumOfCardsDrawn[1]++;
				drawVerticalCard = false;
				
			}
			Animation = false;					// this means that the animation ends
			passButton.setEnabled(true);		// enable the pass button
			playButton.setEnabled(true);		// enable the play button
			deselectAllButton.setEnabled(true);	// enable the deselectAll button
			begin = false;						// this means that the animation ends
			repaint();							// repaint in order to update the above action
		}
		
		/**
		 * This method is used to detect the mouse click.
		 * If the player selects a specified card, the card will be raised.
		 * 
		 * @param e a mouse event
		 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			int x,y;
			x = e.getX();
			y = e.getY();
			// Since the rectangles have the same coordinates as the cards, we can simply use contains() to check mouse click
			for(int i=game.getPlayerList().get(activePlayer).getNumOfCards()-1 ; i>=0 ; i--){
				if(rect_CheckContainMouse[i].contains(x, y) == true && selected[i] == false){
					selected[i] = true;
					break;
				}else if(rect_CheckContainMouse[i].contains(x, y) == true && selected[i] == true){
					selected[i] = false;
					break;
				}
			}
			repaint();
		}

		/**
		 * Unimplemented
		 * 
		 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
		 */
		@Override
		public void mousePressed(MouseEvent e) {

			
		}

		/**
		 * Unimplemened
		 * 
		 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseReleased(MouseEvent e) {
			
			
		}

		/**
		 * Unimplemented
		 * 
		 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseEntered(MouseEvent e) {
			
			
		}

		/** 
		 * Unimplemented
		 * 
		 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseExited(MouseEvent e) {
			
			
		}

		
		
	}
	
	/**
	 * This inner class is the play button listener, which implements the action listener.
	 * 
	 * @author Lau Kin Fung
	 *
	 */
	class PlayButtonListener implements ActionListener{
		
		/**
		 * This method performs actions after clicking the play button.
		 * If the play button is clicked, it will ckeck whether it is a legal move and reset the selected array and check if the game ends.
		 * 
		 * @param e an action event
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			if(getSelected().length != 0){
				
				game.makeMove(game.getPlayerID(), getSelected());
				resetSelected();	// reset the selected array
				
				if(game.endOfGame()){	// check if the game ends
					// disable all the buttons and big two panel
					passButton.setEnabled(false);
					playButton.setEnabled(false);
					deselectAllButton.setEnabled(false);
					bigTwoPanel.setEnabled(false);
				}
				repaint();
			}
			
		}
		
		
	}
	
	/**
	 * This inner class is the pass button listener, which implements the action listener.
	 * 
	 * @author Lau Kin Fung
	 *
	 */
	class PassButtonListener implements ActionListener{
		
		/**
		 * This method performs actions after clicking the pass button.
		 * It checks if this move is legal and reset the selected array.
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			resetSelected();
			game.makeMove(activePlayer, getSelected());
			repaint();
		}
		
	}
	
	/**
	 * This inner class is the deselectAll button listener, which implements the action listener.
	 * 
	 * 
	 * @author Lau Kin Fung
	 *
	 */
	class DeselectAllButtonListener implements ActionListener{
		
		/**
		 * This method performs actions after clicking the deselectAll button.
		 * It resets the selected array.
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e){
			resetSelected();
			repaint();
		}
	}
	
	/**
	 * This inner class is the restart menu item listener, which implements the action listener.
	 * 
	 * @author Lau Kin Fung
	 *
	 */
	class ConnectMenuItemListener implements ActionListener{
		
		/**
		 * This method performs actions after choosing the restart menu item.
		 * It restarts reset the game and the GUI.
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			game.makeConnection();	
		}
		
	}
	
	
	/**
	 * This inner class is the quite menu item listener, which implements the action listener.
	 * 
	 * @author Lau Kin Fung
	 *
	 */
	class QuitMenuItemListener implements ActionListener{
		
		/**
		 * This method performs actions after choosing the quit menu item.
		 * It quits the game.
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			
			System.exit(0);
		}	
	}
	/**
	 * This inner class is the ClearGameMessageArea menu item listener, which implements the action listener.
	 * 
	 * @author Lau Kin Fung
	 *
	 */
	class ClearGameMessageAreaListener implements ActionListener{
		
		/**
		 * This method performs actions after choosing the ClearGameMessageArea menu item.
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			clearMsgArea();
		}	
	}
	
	/**
	 * This method is the ClearChatMessageArea menu item listener, which implements the action listener.
	 * 
	 * @author Lau Kin Fung
	 *
	 */
	class ClearChatMessageAreaListener implements ActionListener{
	
	/**
	 * This method performs actions after choosing the ClearChatMessageArea menu item.
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		serverMsgArea.setText("");
	}	
}
	
	/**
	 * This method is the messageInputArea listener, which implements the action listener.
	 * 
	 * @author Lau Kin Fung
	 *
	 */
	class ChatInputListener implements ActionListener{

		/**
		 * This method sends the message to server after the player press the "Enter" key.
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			if(!messageInputArea.getText().isEmpty() || messageInputArea.getText() == null){
				game.sendMessage(new CardGameMessage(CardGameMessage.MSG,game.getPlayerID(),messageInputArea.getText()));
			}
			messageInputArea.setText("");
			
		}
		
		
	}
	
}
