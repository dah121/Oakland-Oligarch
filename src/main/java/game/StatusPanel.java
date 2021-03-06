package game;

import java.util.Random;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * @author Dan Gilmour
 */
public class StatusPanel extends JPanel {
	
	private JButton[] playerButtons;
	private Player[] playerList;
	private int num_players;
	private JButton[] propertyButtons;	
	private ActionListener mortgageListener;

	/**
	 * Initializes the status panel showing players and their properties
	 *
	 * @param	random	A psuedo-random number generator used to select the background color
	 * @param	ml		ActionListener for triggering the mortagaging and unmortgaging of properties
	 */
	public StatusPanel(Random random, ActionListener ml) {
		this.setBackground(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));
		this.setOpaque(true);
		this.setLayout(new GridBagLayout());
		mortgageListener = ml;
	}

	/**
	 * Sets up the playerButtons on the panel once the players have been loaded
	 *
	 * @param	_playerlist		The players in this game
	 * @param	tradeListeners	Establishes a trade between players on click
	 */
	public void setPlayers(Player[] _playerList, ActionListener[] tradeListeners) {
		playerList = _playerList;
		num_players = playerList.length;

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;

		playerButtons = new JButton[num_players];

		for (int id = 0; id < num_players; id++) {
			playerButtons[id] = new JButton(playerList[id].getName() + ": $" + playerList[id].getMoney());	
			playerButtons[id].addActionListener(tradeListeners[id]);
			playerButtons[id].setToolTipText("Trade with this player");
			c.gridy = id;
			add(playerButtons[id], c);
		}
	}

	/**
	 * Updates the panel with the new money values of the players. Also,
	 *		updates the properties for mortgaging and the player buttons.
	 *
	 * @param	player		The player whos turn it is
	 */
	public void update(Player player) {

		// Update the Player buttons text
		for (int i = 0; i < num_players; i++) {	//Visit each status button and update the test to indicate player currency
			playerButtons[i].setText(playerList[i].getName() + ": $" + playerList[i].getMoney());
			if (player.getId() == i) {
				playerButtons[i].setBackground(new Color(playerList[i].getColor()));
				playerButtons[i].setForeground(Color.BLACK);
				playerButtons[i].setToolTipText("View your details");
			} else {
				playerButtons[i].setBackground(Color.BLACK);
				playerButtons[i].setForeground(new Color(playerList[i].getColor()));
				playerButtons[i].setToolTipText("Trade with this player");
			}
		}

		// Remove the previous buttons
		Component[] buttonArr = this.getComponents();

		for (int i = num_players; i < buttonArr.length; i++) {
			this.remove(buttonArr[i]);
		}

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		// Make new Mortgage/Unmortgage buttons
		int index = 0;
		for (Property prop : player.getProperties()) {
			JButton propButton = new JButton();
			if (prop.getMortgaged()) {
				propButton.setText(prop.getName() + ": buy back for $" + prop.getPrice());
				if (player.getMoney() <= prop.getPrice()) {
					propButton.setEnabled(false);
				}
			} else {
				propButton.setText(prop.getName() + ": mortgage for $" + prop.getPrice() / 2);
			}
			propButton.addActionListener(mortgageListener);
			propButton.setActionCommand(Integer.toString(index));
			c.gridy = index + num_players + 2;
			this.add(propButton, c);
			index ++;
		}
			
	}
}
