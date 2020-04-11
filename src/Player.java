import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Player extends JPanel {
	
	String name;
	Civ chosenCiv;
	JTextField jName;
	JLabel chosenCivLabel;
	JButton randomCiv;
	JButton removePlayer;
	
	ArrayList<String> typeArray;
	ArrayList<String> tierArray;
	
	static int playerRow = 0;
	
	
	public Player(String name) {
		this.name = name;
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		jName = new JTextField(name);
		jName.addKeyListener(addNameListener());
		jName.setAlignmentX(LEFT_ALIGNMENT);
		jName.setPreferredSize(new Dimension(100,15));
		
		chosenCivLabel = new JLabel();
		chosenCivLabel.setPreferredSize(new Dimension(195,15));
		
		randomCiv = new JButton("\u2684");
		randomCiv.setBorder(BorderFactory.createEmptyBorder());
		randomCiv.setOpaque(false);
		randomCiv.setContentAreaFilled(false);
		randomCiv.setBorderPainted(false);
		randomCiv.addMouseListener(setActiveBackground(Color.RED));
		
		removePlayer = new JButton("x");
		//removePlayer.addActionListener(new ActionListener() {
		//	public void actionPerformed(ActionEvent arg0) {removePlayer();}
		//});
		removePlayer.setBorder(BorderFactory.createEmptyBorder());
		removePlayer.setOpaque(false);
		removePlayer.setContentAreaFilled(false);
		removePlayer.setBorderPainted(false);
		removePlayer.setPreferredSize(new Dimension(25,30));
		removePlayer.addMouseListener(setActiveBackground(Color.RED));

		
		this.add(jName);
		this.add(chosenCivLabel);
		this.add(randomCiv);
		this.add(removePlayer);
		
		
		typeArray =	new ArrayList<String>();
		tierArray = new ArrayList<String>();
	}
	
	private KeyListener addNameListener() {
		KeyListener kl = new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent arg0) {}
			public void keyReleased(KeyEvent arg0) {
				setName(jName.getText());
			}
			public void keyPressed(KeyEvent arg0) {}
		};
		
		return kl;
	}
	
	public void setFilters(ArrayList<String> typeArray, ArrayList<String> tierArray) {
		this.typeArray = typeArray;
		this.tierArray = tierArray;
	}
	
	
	public void randomCiv() {
		typeArray = new ArrayList<String>();
		tierArray = new ArrayList<String>();
	}
	
	public void changeCiv(Civ civ) {
		setChosenCiv(civ);
		if (civ == null) chosenCivLabel.setText("");
		else chosenCivLabel.setText(civ.toString());
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Civ getChosenCiv() {
		return chosenCiv;
	}

	private void setChosenCiv(Civ chosenCiv) {
		this.chosenCiv = chosenCiv;
	}

	public String getName() {
		return name;
	}
	/*
	private void removePlayer() {
		Container parent1 = (Container) getParent();

		parent1.remove(this);
		parent1.revalidate();
		parent1.repaint();
	}
	*/
	public void unchoose() {
		this.setBackground(CivGui.playerBG);
		this.setBorder(BorderFactory.createEmptyBorder());
	}
	
	public void choose() {
		this.setBackground(CivGui.currentPlayerBG);
		this.setBorder(BorderFactory.createMatteBorder(4,0,4,0,Color.BLACK));
	}
	
	public void hover() {
		this.setBackground(CivGui.hoverPlayerBG);
	}
	
	public void unhover() {
		this.setBackground(CivGui.playerBG);
	}

	public ArrayList<String> getTypeArray() {
		return typeArray;
	}

	public void setTypeArray(ArrayList<String> typeArray) {
		this.typeArray = typeArray;
	}

	public ArrayList<String> getTierArray() {
		return tierArray;
	}

	public void setTierArray(ArrayList<String> tierArray) {
		this.tierArray = tierArray;
	}
	
	
	private MouseListener setActiveBackground(Color color) {
		MouseListener ml = new MouseListener() {
			public void mouseReleased(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {
				JButton jb = (JButton) arg0.getSource();
				jb.setOpaque(false);
				jb.setContentAreaFilled(false);
				jb.setBorderPainted(false);

			}
			public void mouseEntered(MouseEvent arg0) {
				JButton jb = (JButton) arg0.getSource();
				jb.setBackground(color);
				jb.setOpaque(true);
				jb.setContentAreaFilled(true);
				jb.setBorderPainted(true);
			}
			public void mouseClicked(MouseEvent arg0) {}
		};
		
		return ml;
	}
	
	
	
	
	

	
}
