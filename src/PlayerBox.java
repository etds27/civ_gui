import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.ScrollPane;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;


public class PlayerBox extends JPanel  {
	
	ArrayList<Player> playerList = new ArrayList<Player>();
	GridBagConstraints c = new GridBagConstraints();
	JScrollPane scrollPane;
	
	public PlayerBox() {
		
		c.anchor = GridBagConstraints.NORTHWEST;
		
		setLayout(new GridBagLayout());
		
		scrollPane = new JScrollPane(this);
		scrollPane.setBackground(Color.BLUE);
		
		c.anchor = GridBagConstraints.NORTH;
		c.gridx = 0;
		c.gridy = 100;
		c.weightx = 1;
		c.weighty = 1;
		
		add(new JPanel(), c);
		
		c.gridy = 0;
		c.weighty = 0;
		
	}
	
	public JScrollPane getPane() {
		return scrollPane;
	}
	
	public void removePlayerFromList(Player player) {
		if (playerList.contains(player)) {
			playerList.remove(player);
			remove(player);
			revalidate();
			repaint();
		}
	}
	
	public void addPlayerToList(Player player) {
		playerList.add(player);
		add(player, c);
		c.gridy++;
		revalidate();
		repaint();
	}

	public ArrayList<Player> getPlayerList() {
		return playerList;
	}

	public void setPlayerList(ArrayList<Player> playerList) {
		this.playerList = playerList;
	}

	
	
	
}
