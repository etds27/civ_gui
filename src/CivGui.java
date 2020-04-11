import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;

/**
 * 
 * @author etds27
 * sources:
 * https://steamcommunity.com/sharedfiles/filedetails/?id=165514319
 * https://www.reddit.com/r/civ/comments/2dy6ls/filthyrobots_tier_list_all_civs_ranked_d/
 * https://www.youtube.com/watch?v=jhTdDH0VU5A
 * https://www.carlsguides.com/strategy/civilization5/civs-leaders/
 * 
 * Currently working on:
 * Random all with filters
 * polishing UI. Active Player vs non Active
 * 
 * Things to do:
 * Assign leader from list | done
 * Allow dont doubles bool | done
 * Copy filters per person
 * Copy filters all
 */
public class CivGui extends JFrame {
	static CivList civs;
	GridBagConstraints playerAddGBC;
	
	public PlayerBox playerBox;
	
	public Player currentPlayer;
	
	//Colors
	static Color playerBG = Color.LIGHT_GRAY;
	static Color currentPlayerBG = Color.WHITE;
	static Color hoverPlayerBG = Color.PINK;
	
	//Player Frame
	private JPanel playerPanel;
	private JTextField playerEnter;
	private JButton playerCreate;
	private JPanel addPlayerPanel;
	
	//Right Frame
	JPanel rightPanel;
	//Filter Frame
	JPanel filterPanel;
	GridBagConstraints filterGBC;
	
	JCheckBox allowDuplicateCivs = new JCheckBox("Allow Duplicates?");
	
	ArrayList<CheckBoxAndName> tierList;
	CheckBoxAndName allTier = new CheckBoxAndName("Tiers:");
	CheckBoxAndName sTier = new CheckBoxAndName("S");
	CheckBoxAndName aTier = new CheckBoxAndName("A");
	CheckBoxAndName bTier = new CheckBoxAndName("B");
	CheckBoxAndName cTier = new CheckBoxAndName("C");
	CheckBoxAndName dTier = new CheckBoxAndName("D");
	CheckBoxAndName fTier = new CheckBoxAndName("F");
	
	ArrayList<CheckBoxAndName> typeList;
	CheckBoxAndName allType = new CheckBoxAndName("Types:");
	CheckBoxAndName domType = new CheckBoxAndName("Domination");
	CheckBoxAndName infType = new CheckBoxAndName("Influential");
	CheckBoxAndName sciType = new CheckBoxAndName("Science");
	CheckBoxAndName marType = new CheckBoxAndName("Maritime");
	CheckBoxAndName regType = new CheckBoxAndName("Religious");
	CheckBoxAndName dipType = new CheckBoxAndName("Diplomatic");
	
	//Civ Frame
	JPanel civPanel;
	JList<Civ> civList;
	CivListModel civModel;

	
	public CivGui() {
		civs = new CivList();
		generateCivMap();
		
		
		setLayout(new BorderLayout());
		setupPlayerFrame();
		setupRightFrame();
		setupFilterFrame();
		setupCivFrame();
		
		civSearch();
		
		setSize(800, 600);
		
		setVisible(true);
		revalidate();
		repaint();
		
	}
	
	public static void main(String[] args) {
		String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

		for (int i = 0; i < fonts.length; i++) {
		   // System.out.println(fonts[i]);
		}
		setUIFont (new javax.swing.plaf.FontUIResource(Font.MONOSPACED,Font.BOLD,12));
		CivGui civGui = new CivGui();
		
	}
	
	public void setupPlayerFrame() {
		
		//Setting up player entry text widget
		playerEnter = new JTextField();
		playerEnter.setColumns(20);
		playerCreate = new JButton("Create Player");
		
		playerCreate.addActionListener(createPlayerActionListener());
		playerEnter.addActionListener(createPlayerActionListener());
		playerEnter.requestFocus();
		
		addPlayerPanel = new JPanel(new BorderLayout());
		addPlayerPanel.add(playerEnter, BorderLayout.CENTER);
		addPlayerPanel.add(playerCreate, BorderLayout.EAST);
		
		BorderLayout playerMainFrame = new BorderLayout();
		playerPanel = new JPanel(playerMainFrame);
		
		//Setting up player randomframe

		JPanel playerRandomPanel = new JPanel(new FlowLayout());
		
		JButton randomizeAll = new JButton("Randomize All!");
		randomizeAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				randomizeAllPlayers(true);
			}
		});
		JButton randomizeAllFilters = new JButton("Randomize All w/ Filters!");
		randomizeAllFilters.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				randomizeAllPlayers(false);
			}
		});
		
		JButton setAllPlayerFiltersToCurrent = new JButton("All filters");
		setAllPlayerFiltersToCurrent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setAllFiltersToCurrent();
			}
		});
		setAllPlayerFiltersToCurrent.setToolTipText("Set all player filters to what is currently selected");
		
		playerRandomPanel.add(setAllPlayerFiltersToCurrent);
		playerRandomPanel.add(randomizeAllFilters);
		playerRandomPanel.add(randomizeAll);
		playerPanel.add(playerRandomPanel, BorderLayout.PAGE_END);
		
		
		playerBox = new PlayerBox();
		JScrollPane playerBoxPane = playerBox.getPane();
		

		
		playerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		playerPanel.addKeyListener(scrollingPlayerListener());
		playerRandomPanel.addKeyListener(scrollingPlayerListener());
		
		add(addPlayerPanel, BorderLayout.NORTH);
		add(playerBoxPane, BorderLayout.CENTER);
		add(playerPanel, BorderLayout.SOUTH);
		

		
		
	}
	
	private void setupRightFrame() {
		rightPanel = new JPanel(new BorderLayout());
		add(rightPanel, BorderLayout.EAST);
	}
	
	private void setupFilterFrame() {
		
		
		
		tierList = new ArrayList<CheckBoxAndName>();
		tierList.add(sTier);
		tierList.add(aTier);
		tierList.add(bTier);
		tierList.add(cTier);
		tierList.add(dTier);
		tierList.add(fTier);
		
		typeList = new ArrayList<CheckBoxAndName>();
		typeList.add(dipType);
		typeList.add(domType);
		typeList.add(regType);
		typeList.add(infType);
		typeList.add(sciType);
		typeList.add(marType);
		
		filterPanel = new JPanel(new GridBagLayout());
		filterGBC = new GridBagConstraints();
		filterGBC.gridy = 0;
		filterGBC.anchor = GridBagConstraints.WEST;
		
		allTier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				toggleTiers();
				civSearch();
			}
		});
		filterPanel.add(allTier, filterGBC);
		filterGBC.gridy++;
		filterGBC.insets = new Insets(0,15,0,0);
		for (CheckBoxAndName tier : tierList) {
			tier.addActionListener(checkBoxListener());
			filterPanel.add(tier, filterGBC);
			filterGBC.gridy++;
		}
		
		allType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				toggleTypes();
				civSearch();
			}
		});
		filterGBC.insets = new Insets(0,0,0,0);
		filterPanel.add(allType, filterGBC);
		filterGBC.gridy++;
		filterGBC.insets = new Insets(0,15,0,0);
		for (CheckBoxAndName type : typeList) {
			type.addActionListener(checkBoxListener());
			filterPanel.add(type, filterGBC);
			filterGBC.gridy++;
		}
		
		
		selectAllTiers();
		selectAllTypes();
		checkAll();
		
		rightPanel.add(allowDuplicateCivs, BorderLayout.NORTH);
		rightPanel.add(filterPanel, BorderLayout.WEST);
	}
	
	private void setupCivFrame() {
		civs.printCivs();
		civPanel = new JPanel(new BorderLayout());
		civModel = new CivListModel(civs);
		civList = new JList<Civ>();
		civList.setModel(civModel);
		civList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		civList.setMinimumSize(new Dimension(32,0));
		JScrollPane listScrollPane = new JScrollPane(civList);
		
		civList.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mouseEntered(MouseEvent arg0) {}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				//If the civ is double clicked. Assign it to the current player
			  if (arg0.getClickCount() == 2) {
				  if (civList.getSelectedIndex() != -1 ) {
					  if ( currentPlayer != null ) {
						  Civ civ = civList.getSelectedValue();
						  currentPlayer.changeCiv(civ);
						  revalidate();
						  repaint();
					  }
				  }
			  }
			}
		});
		
		
		civPanel.add(listScrollPane, BorderLayout.EAST);
		
		rightPanel.add(civPanel, BorderLayout.CENTER);
		
		civList.update(null);
	}
	
	private ActionListener createPlayerActionListener() {
		ActionListener kl = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				createPlayerIntf();
			}
		};
		
		return kl;
	}
	
	private void createPlayerIntf() {
		String name = playerEnter.getText();

		if ( ! name.equals("")) {
			playerEnter.setText("");
			Player pa = new Player(name);
			pa.setTierArray(getSelectedTiers());
			pa.setTypeArray(getSelectedTypes());
			if (playerBox.getPlayerList().size() == 0) {
				setCurrentPlayer(pa);
			}
			
			addPlayerListeners(pa);
			playerBox.addPlayerToList(pa);
			revalidate();
			repaint();			
		} else if (doesPlayerExist(name)) {
			System.out.println("Player name " + name + " already exists");
		}
	}
	
	private boolean doesPlayerExist(String name) {
		for (Player playerFromList : playerBox.getPlayerList()) {
			if ( name.equals(playerFromList.getName()) ) {
				return true;
			}
		}
		return false;
	}
	
	public void civSearch(Player player) {
		System.out.println("CivSearch: playertier Array:  " + player.tierArray);
		System.out.println("CivSearch: playertype Array:  " + player.typeArray);
		
		civSearch(player.getTypeArray(), player.getTierArray());
	}
	
	public void civSearch() {
		ArrayList<String> typeArray;
		ArrayList<String> tierArray;

		typeArray = getSelectedTypes();
		tierArray = getSelectedTiers();
		
		civSearch(typeArray, tierArray);
	}
	
	public void civSearch(ArrayList<String> typeArray, ArrayList<String> tierArray) {
		CivList goodCivs = civs.compatibleType(typeArray);
		goodCivs = goodCivs.compatibleTier(tierArray);
		civModel.updateList(goodCivs);
	}
	
	public ArrayList<String> getSelectedTypes() {
		ArrayList<String> selectedTypes = new ArrayList<String>();
		for ( CheckBoxAndName type : typeList ) {
			if (type.isSelected()) {
				selectedTypes.add(type.name);
			}
		}
		return selectedTypes;
	}
	
	public void setSelectedTypes(ArrayList<String> typeList) {
		System.out.println("setSelectedTypes " + typeList.toString());
		for ( CheckBoxAndName typeBox : this.typeList ) {
			boolean typeFound = false;
			for (String type : typeList) {
				if (typeBox.name.equals(type)) {typeFound = true; break;}
			}
			if (typeFound) {
				typeBox.setSelected(true);
			} else {
				typeBox.setSelected(false);
			}
		}
	}
	
	public ArrayList<String> getSelectedTiers() {
		ArrayList<String> selectedTiers = new ArrayList<String>();
		for ( CheckBoxAndName tier : tierList ) {
			if (tier.isSelected()) {
				selectedTiers.add(tier.name);
			}
		}
		return selectedTiers;
	}
	
	public void setSelectedTiers(ArrayList<String> tierList) {
		System.out.println("setSelectedTiers " + tierList.toString());
		for ( CheckBoxAndName tierBox : this.tierList ) {
			boolean typeFound = false;
			for (String type : tierList) {
				if (tierBox.name.equals(type)) {typeFound = true; break;}
			}
			if (typeFound) {
				tierBox.setSelected(true);
			} else {
				tierBox.setSelected(false);
			}
		}
	}
	
	private void toggleTypes() {
		if (allType.isSelected()) {
			selectAllTypes();
		} else {
			deselectAllTypes();
		}
		setPlayerFilters(currentPlayer);
		if (currentPlayer != null) {
			setPlayerFilters(currentPlayer);
		}
	}
	
	private void toggleTiers() {
		if (allTier.isSelected()) {
			selectAllTiers();
		} else {
			deselectAllTiers();
		}
		if (currentPlayer != null) {
			setPlayerFilters(currentPlayer);
		}
	}
	
	public void selectAllTypes() {
		for ( CheckBoxAndName typeBox : this.typeList ) {
			typeBox.setSelected(true);
		}
	}
	
	public void deselectAllTypes() {
		for ( CheckBoxAndName typeBox : this.typeList ) {
			typeBox.setSelected(false);
		}
	}
	
	public void selectAllTiers() {
		for ( CheckBoxAndName tierBox : this.tierList ) {
			tierBox.setSelected(true);
		}
	}
	
	public void deselectAllTiers() {
		for ( CheckBoxAndName tierBox : this.tierList ) {
			tierBox.setSelected(false);
		}
	}
	


	private ActionListener checkBoxListener() {
		ActionListener al = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				civSearch();
				setPlayerFilters(currentPlayer);
				checkAll();

			}
		};
		
		return al;
	}
	

	
	private ActionListener playerRandomCivActionListener(Player pa) {
		ActionListener al = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (pa != currentPlayer) {
					setCurrentPlayer(pa);
				}
				setPlayerRandomCiv(pa, false);
			}
		};
		
		return al;
	}
	
	private void setPlayerRandomCiv(Player pa, boolean globalFilters) {
		CivList randomPermutation;
		
		if (! globalFilters) {
			//System.out.println("Setting PlayerSpecific Filters");
			
			civSearch(pa);
		}
		
		randomPermutation = civModel.getRandomCivPermutation();
		Set<Civ> existingCivs = new HashSet<Civ>();

		//If no dupes are on, skip creating the dupe list
		if (! allowDuplicateCivs.isSelected()) {
			for (Player player: playerBox.getPlayerList()) {
				Civ tempCiv = player.getChosenCiv();
				if (tempCiv != null) existingCivs.add(tempCiv);
			}
			System.out.println(existingCivs.size() + " < " + randomPermutation.size());
			if (existingCivs.size() > randomPermutation.size()) return;
			
			
			if ( randomPermutation.size() > 0) {
				for (Civ civ : existingCivs) {
					randomPermutation.remove(civ);
				}
			}
			if (randomPermutation.size() == 0) return;
		}
		
		System.out.println(randomPermutation);
		
		Civ civ = randomPermutation.randomCiv();
		
		pa.changeCiv(civ);
		System.out.println(pa.getChosenCiv());
		revalidate();
		repaint();
	
	}
	
	private void setCurrentPlayer(Player pa) {
		if (currentPlayer != null) {
			Player oldPlayer = currentPlayer;
			oldPlayer.unchoose();
		}
		currentPlayer = pa;
		
		currentPlayer.choose();
		setSelectedTiers(currentPlayer.getTierArray());
		setSelectedTypes(currentPlayer.getTypeArray());
		civSearch();
	}
	
	private MouseListener loadNewPlayerMouseListener(Player pa) {
		MouseListener ml = new MouseListener() {
			
			public void mouseReleased(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {
			}
			public void mouseEntered(MouseEvent arg0) {
				if (currentPlayer != pa) {
					pa.hover();
				}}
			public void mouseClicked(MouseEvent arg0) {
				

				setCurrentPlayer(pa);
				//setSelectedTiers(currentPlayer.getTierArray());
				//setSelectedTypes(currentPlayer.getTypeArray());
				
				checkAll();
			}
			public void mouseExited(MouseEvent arg0) {
				if (currentPlayer != pa) {
					pa.unhover();
				}
			}
		};
		
		return ml;
	}
	
	private void setPlayerFilters(Player pa) {
		System.out.println("setPlayerFilters " + getSelectedTypes().toString() + " " + getSelectedTiers().toString());
		
		if ( pa != null ) {
			pa.setFilters(getSelectedTypes(), getSelectedTiers());
		}
	}
	
	private KeyListener scrollingPlayerListener() {
		KeyListener kl = new KeyListener() {
			public void keyTyped(KeyEvent arg0) {
				System.out.println(arg0.getID() + " " + arg0.getKeyChar() + " " + arg0.getKeyCode());
				
			}
			public void keyReleased(KeyEvent arg0) {}
			public void keyPressed(KeyEvent arg0) {}
		};
		return kl;
	}
	
	private ActionListener removePlayerListener(Player player) {
		ActionListener al = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				removePlayer(player);				
			}
		};
		return al;
	}
	
	private void checkAll() {
		int i = 0;
		for (CheckBoxAndName cb : tierList) {
			if (cb.isSelected()) {
				i++;
			}
		}
		if (i == tierList.size()) { 
			allTier.setSelected(true);
		} else {
			allTier.setSelected(false);
		}
		
		int j = 0;
		for (CheckBoxAndName cb : typeList) {
			if (cb.isSelected()) {
				j++;
			}
		}
		if (j == typeList.size()) { 
			allType.setSelected(true);
		} else {
			allType.setSelected(false);
		}
		
	}
	
	private void removePlayer (Player pa) {
		playerBox.removePlayerFromList(pa);
	}
	
	private void randomizeAllPlayers(boolean useFilters) {
		Player resetLastPlayer = currentPlayer;

		for (Player player : playerBox.getPlayerList()) {
			player.changeCiv(null);
		}
		
		for (Player player : playerBox.getPlayerList()) {
			System.out.println(player.getName() + " randomized");
			setPlayerRandomCiv(player, useFilters);
		}
		System.out.println("Randomized All");
		System.out.println(resetLastPlayer.getTierArray());
		System.out.println(resetLastPlayer.getTypeArray());
		setCurrentPlayer(resetLastPlayer);
		
		civSearch();
	}

	
	public void setAllFiltersToCurrent() {
		for (Player player : playerBox.getPlayerList()) {
			//if (player == currentPlayer) continue;
			player.setFilters(getSelectedTypes(), getSelectedTiers());
		}
	}
	
	public static void generateCivMap() {
		ArrayList<String> types = new ArrayList<String>();
		String ability;
		ArrayList<Object> specialties = new ArrayList<Object>();
		types.add("Diplomatic");
		ability = "Gateway to Africa - +3 Gold and +1 Culture for each trade route with a different Civ/CS. Other Civs get +2 gold for each route sent to Morocco";
		civs.add(new Civ("Morocco" , "Ahmad al-Mansur", types, ability, specialties, "D"));
		
		types = new ArrayList<String>();
		specialties = new ArrayList<Object>();
		types.add("Diplomatic");
		ability = "Hellenic League - City-State Influence degrades at half and recovers at twice the normal rate, also eliminates Trespassing penalties.";
		civs.add(new Civ("Greece" , "Alexander", types, ability, specialties, "B"));
		
		types = new ArrayList<String>();
		specialties = new ArrayList<Object>();
		types.add("Domination");
		ability = "Treasures of Nineveh - Gain a free Tech discovered by the owner of the city when Conquering it through War. (Trade doesn't count, and only once per city).";
		civs.add(new Civ("Assyria" , "Ashurbanipal", types, ability, specialties, "D"));
		
		
		types = new ArrayList<String>();
		specialties = new ArrayList<Object>();
		types.add("Domination");
		ability = "River Warlord - Triple gold from pillaging Barb encampments and Cities. Land units gain War Canoe and Amphibious promotions - attack better from water with better sight and Combat Strength while embarked.";
		civs.add(new Civ("Songhai" , "Askia", types, ability, specialties, "C"));
		
		types = new ArrayList<String>();
		specialties = new ArrayList<Object>();
		types.add("Domination");
		ability = "Scourge of God - Double Raze speed. Uses City Names from other Civs. Starts with Animal Husbandry and earns +1 Production per Pasture";
		civs.add(new Civ("The Huns" , "Attila", types, ability, specialties, "A"));
		
		types = new ArrayList<String>();
		specialties = new ArrayList<Object>();
		types.add("Diplomatic");
		types.add("Domination");
		types.add("Infuential");
		types.add("Science");
		ability = "The Glory of Rome - +25% Production in other Cities toward any Building that exists in the Capital.";
		civs.add(new Civ("Rome" , "Augustus Caesar", types, ability, specialties, "C"));
		
		types = new ArrayList<String>();
		specialties = new ArrayList<Object>();
		types.add("Domination");
		ability = "Furor Teutonicus - Defeating a Barbarian in an Encampment gives you a 67% chance they join your side. Land unit maintenance costs 25% less.";
		civs.add(new Civ("Germany" , "Bismarck", types, ability, specialties, "C"));
		
		types = new ArrayList<String>();
		specialties = new ArrayList<Object>();
		types.add("Domination");
		types.add("Religious");
		ability = "Druidic Lore - +1 Faith per city with adjacent unimproved Forest tiles. Increases to +2 when 3 or more Forest are present.";
		civs.add(new Civ("The Celts" , "Boudicca", types, ability, specialties, "C"));
		
		types = new ArrayList<String>();
		specialties = new ArrayList<Object>();
		types.add("Diplomatic");
		types.add("Domination");
		types.add("Infuential");
		types.add("Science");
		ability = "Solidarity - Free Social Policy each time you advance an Era";
		civs.add(new Civ("Poland" , "Casimir III", types, ability, specialties, "S"));
		
		types = new ArrayList<String>();
		specialties = new ArrayList<Object>();
		types.add("Domination");
		types.add("Diplomatic");
		ability = "Siberian Riches - Strategic Resources give +1 Production. Horse, Iron, and Uranium give double quantity for larger armies or more trading.";
		civs.add(new Civ("Russia" , "Catherine", types, ability, specialties, "B"));
		
		types = new ArrayList<String>();
		specialties = new ArrayList<Object>();
		types.add("Diplomatic");
		types.add("Domination");
		types.add("Infuential");
		ability = "Achaemenid Legacy - Golden Ages last 50% longer. Units get +1 Movement and +10% Combat Strength during Golden Ages.";
		civs.add(new Civ("Persia" , "Darius I", types, ability, specialties, "S"));
		
		types = new ArrayList<String>();
		specialties = new ArrayList<Object>();
		types.add("Maritime");
		ability = "Phoenician Heritage - Coastal Cities get a free Harbor. Units may cross mountain after the first Great General is born, but will take 50 damage if ending a turn on a Mountain.";
		civs.add(new Civ("Carthage" , "Dido", types, ability, specialties, "F"));
		
		types = new ArrayList<String>();
		specialties = new ArrayList<Object>();
		types.add("Maritime");
		types.add("Diplomatic");
		ability = "Sun Never Sets - +2 Naval unit Movement and +1 Extra Spy when introduced in the Renaissance";
		civs.add(new Civ("England" , "Elizabeth", types, ability, specialties, "A"));
		
		types = new ArrayList<String>();
		specialties = new ArrayList<Object>();
		types.add("Diplomatic");
		types.add("Mercantile");
		ability = "Serenissima - No Building of Settlers nor Annexing of Cities - you can only fully control one City, but get double the Trade Routes your tech level would normally provide. Your only method of taking control over other cities is to Puppet them with the Merchant of Venice UU. Venice gets the ability to purchase in Puppeted Cities.";
		civs.add(new Civ("Venice" , "Enrico Dandolo", types, ability, specialties, "F"));
		
		types = new ArrayList<String>();
		specialties = new ArrayList<Object>();
		types.add("Diplomatic");
		types.add("Domination");
		types.add("Infuential");
		types.add("Science");
		ability = "Spice Islanders - First 3 Cities founded on continents other than where Indonesia started each provide 2 unique Luxury Resources and can never be Razed.";
		civs.add(new Civ("Indonesia" , "Gajah Mada", types, ability, specialties, "C"));
		
		types = new ArrayList<String>();
		specialties = new ArrayList<Object>();
		types.add("Diplomatic");
		ability = "Population Growth - Unhappiness from number of Citizens halved, while unhappiness from number of Cities doubled.";
		civs.add(new Civ("India" , "Gandhi", types, ability, specialties, "C"));
		
		types = new ArrayList<String>();
		specialties = new ArrayList<Object>();
		types.add("Domination");
		ability = "Mongol Terror - +30% Combat Strength when fighting City-State units or City-States themselves. All mounted units get +1 movement.";
		civs.add(new Civ("Mongolia" , "Genghis Khan", types, ability, specialties, "C"));
		
		types = new ArrayList<String>();
		specialties = new ArrayList<Object>();
		types.add("Diplomatic");
		ability = "Nobel Prize - Gain 90 Influence gifting Great People to City-States. Declarations of Friendship give Sweden and the friendly Civ a +10% GPP boost toward Great People.";
		civs.add(new Civ("Sweden" , "Gustavus Adolphus", types, ability, specialties, "D"));
		
		types = new ArrayList<String>();
		specialties = new ArrayList<Object>();
		types.add("Domination");
		ability = "Spirit of Adwa - +20% Combat Bonus to all Units when fighting a Civ with more Cities than Ethiopia.";
		civs.add(new Civ("Ethiopia" , "Haile Selassie", types, ability, specialties, "A"));
		
		types = new ArrayList<String>();
		specialties = new ArrayList<Object>();
		types.add("Domination");
		types.add("Maritime");
		ability = "Viking Fury - Embarked units gain 1 movement and pay only 1 movement to move from sea to land. Melee units do not lose movement to Pillage, providing them ample healing near enemy Cities.";
		civs.add(new Civ("Denmark" , "Harald Bluetooth", types, ability, specialties, "C"));
		
		types = new ArrayList<String>();
		specialties = new ArrayList<Object>();
		types.add("Diplomatic");
		types.add("Religious");
		ability = "Ships of the Desert - Caravans gain +50% extended range. Land/sea trade routes spread the home city's religion at double effectiveness. Get double Oil Strategic Resources.";
		civs.add(new Civ("Arabia" , "Harun al-Rashid", types, ability, specialties, "B"));
		
		types = new ArrayList<String>();
		specialties = new ArrayList<Object>();
		types.add("Diplomatic");
		types.add("Religious");
		ability = "The Great Warpath - Units and Caravans move through forest/jungle in your territory as if they were roads and these tiles can be used to establish City Connections (build roads in between).";
		civs.add(new Civ("Iroquois" , "Hiawatha", types, ability, specialties, "F"));
		
		types = new ArrayList<String>();
		specialties = new ArrayList<Object>();
		ability = "Seven Cities of Gold - Gold Bonus from discovering Natural Wonders. All tile yields from Natural Wonders doubled if you can get one within workable range.";
		civs.add(new Civ("Spain" , "Isabella", types, ability, specialties, "F"));
		
		types = new ArrayList<String>();
		specialties = new ArrayList<Object>();
		types.add("Influential");
		types.add("Maritime");
		ability = "Wayfinding - Units can Embark over Oceans immediately. +1 sight when Embarked. +10% Combat Strength if within 2 tiles of a Moai.";
		civs.add(new Civ("Polynesia" , "Kamehameha", types, ability, specialties, "F"));
		
		types = new ArrayList<String>();
		specialties = new ArrayList<Object>();
		types.add("Diplomatic");
		ability = "Mare Clausum - Resource diversity grants twice as much Gold for Portugal for all International Trade Routes.";
		civs.add(new Civ("Portugal" , "Maria I", types, ability, specialties, "D"));
		
		types = new ArrayList<String>();
		specialties = new ArrayList<Object>();
		types.add("Diplomatic");
		types.add("Influential");
		types.add("Science");
		ability = "Diplomatic Marriage - Allows you to spend gold to Annex or Puppet a City-State that has been an ally for 5 turns. This will cost you about 1500 gold or less.";
		civs.add(new Civ("Austria" , "Maria Theresa", types, ability, specialties, "A"));
		
		types = new ArrayList<String>();
		specialties = new ArrayList<Object>();
		types.add("Domination");
		ability = "Sacrificial Captives - Gains culture for the empire for each enemy unit killed";
		civs.add(new Civ("Aztec" , "Montezuma", types, ability, specialties, "B"));
		
		types = new ArrayList<String>();
		specialties = new ArrayList<Object>();
		types.add("Influential");
		ability = "City of Light - Museum and World Wonder theming bonuses are doubled in their Capital";
		civs.add(new Civ("France" , "Napoleon", types, ability, specialties, "F"));
		
		types = new ArrayList<String>();
		specialties = new ArrayList<Object>();
		types.add("Science");
		ability = "Ingenuity - Free Great Scientist at the invention of Writing. Even better, earn Great Scientists 50% faster.";
		civs.add(new Civ("Babylon" , "Nebuchadnezzar II", types, ability, specialties, "S"));
		
		types = new ArrayList<String>();
		specialties = new ArrayList<Object>();
		types.add("Maritime");
		types.add("Influential");
		ability = "+1 Culture from each Fishing Boat and +2 Culture from each Atoll";
		civs.add(new Civ("Japan" , "Oda Nobunaga", types, ability, specialties, "F"));
		
		types = new ArrayList<String>();
		specialties = new ArrayList<Object>();
		types.add("Diplomatic");
		types.add("Domination");
		types.add("Infuential");
		types.add("Science");
		ability = "The Long Count - After researching Theology, get a free Great Person of your choice every 394 years. You may choose only one of each type throughout the ages.";
		civs.add(new Civ("The Maya" , "Pacal", types, ability, specialties, "A"));
		
		types = new ArrayList<String>();
		specialties = new ArrayList<Object>();
		types.add("Diplomatic");
		types.add("Domination");
		types.add("Infuential");
		types.add("Science");
		ability = "Great Andean Road - Units ignore terrain costs when moving into any tile with Hills. There are no maintenance costs for improvements in hills, meaning roads, and half cost elsewhere.";
		civs.add(new Civ("The Inca" , "Pachacuti", types, ability, specialties, "A"));
		
		types = new ArrayList<String>();
		specialties = new ArrayList<Object>();
		types.add("Infuential");
		ability = "Carnival - Double Tourism output during Golden Ages. Earns Great Artists, Musicians, and Writers 50% faster during Golden Ages.";
		civs.add(new Civ("Brazil" , "Pedro II", types, ability, specialties, "D"));
		
		types = new ArrayList<String>();
		specialties = new ArrayList<Object>();
		types.add("Diplomatic");
		types.add("Domination");
		types.add("Infuential");
		types.add("Science");
		ability = "Great Expanse - Founded Cities start with additional territory. Units receive a combat bonus when fighting within friendly territory.";
		civs.add(new Civ("Shoshone" , "Pocatello", types, ability, specialties, "B"));
		
		types = new ArrayList<String>();
		specialties = new ArrayList<Object>();
		types.add("Diplomatic");
		types.add("Domination");
		types.add("Infuential");
		types.add("Science");
		ability = "Monument Builders - +20% Production towards Wonder Construction";
		civs.add(new Civ("Egypt" , "Ramesses II", types, ability, specialties, "S"));
		
		types = new ArrayList<String>();
		specialties = new ArrayList<Object>();
		types.add("Diplomatic");
		types.add("Infuential");
		ability = "Father Governs Children - 50% more Food, Culture, and Faith from Friendly or Allied City-States";
		civs.add(new Civ("Siam" , "Ramkhamhaeng", types, ability, specialties, "C"));
		
		types = new ArrayList<String>();
		specialties = new ArrayList<Object>();
		types.add("Science");
		ability = "Scholars of the Jade Hall - Each Specialist produces +2 Science, as do all Great Person Tile Improvements. Each time a scientific building/Wonder is built in the Capital, Korea gets a Research boost similar to a Research Agreement's completion.";
		civs.add(new Civ("Korea" , "Sejong", types, ability, specialties, "A"));
		
		types = new ArrayList<String>();
		specialties = new ArrayList<Object>();
		types.add("Domination");
		ability = "Iklwa - Melee units cost 50% less maintenance and all units require 25% less experience to earn promotions.";
		civs.add(new Civ("The Zulu" , "Shaka", types, ability, specialties, "B"));
		
		types = new ArrayList<String>();
		specialties = new ArrayList<Object>();
		types.add("Domination");
		types.add("Maritime");
		ability = "Barbary Corsairs - All melee naval ships have the Prize Ships promotion, allowing them to capture defeated ships. Naval unit maintenance only 1/3 the usual cost.";
		civs.add(new Civ("The Ottomans" , "Suleiman", types, ability, specialties, "F"));
		
		types = new ArrayList<String>();
		specialties = new ArrayList<Object>();
		types.add("Religious");
		types.add("Influential");
		types.add("Diplomatic");
		ability = "Patriarchate of Constantinople - Choose one more Belief than normal when you found a Religion.";
		civs.add(new Civ("Byzantium" , "Theodora", types, ability, specialties, "D"));
		
		types = new ArrayList<String>();
		specialties = new ArrayList<Object>();
		types.add("Domination");
		ability = "Manifest Destiny - All land military units have +1 sight. 50% discount when purchasing tiles.";
		civs.add(new Civ("America" , "Washington", types, ability, specialties, "C"));
		
		types = new ArrayList<String>();
		specialties = new ArrayList<Object>();
		types.add("Domination");
		types.add("Diplomatic");
		ability = "Dutch East India Company - Keep 50% of the Happiness benefits (+2 out of 4) from a Luxury Resource if your last copy is traded away.";
		civs.add(new Civ("The Netherlands" , "William", types, ability, specialties, "F"));
		
		types = new ArrayList<String>();
		specialties = new ArrayList<Object>();
		types.add("Domination");
		ability = "Art of War - Great Generals give +30% instead of +15% Combat Bonuses and their spawn rate is 50% faster.";
		civs.add(new Civ("China" , "Wu Zetian", types, ability, specialties, "B"));
	}

	public static void setUIFont (javax.swing.plaf.FontUIResource f){
	    java.util.Enumeration keys = UIManager.getDefaults().keys();
	    while (keys.hasMoreElements()) {
	      Object key = keys.nextElement();
	      Object value = UIManager.get (key);
	      if (value instanceof javax.swing.plaf.FontUIResource)
	        UIManager.put (key, f);
	      }
	    } 
	
	private void addPlayerListeners(Player pa) {
		pa.randomCiv.addActionListener(playerRandomCivActionListener(pa));
		pa.addMouseListener(loadNewPlayerMouseListener(pa));
		pa.removePlayer.addActionListener(removePlayerListener(pa));
		pa.setFilters(getSelectedTypes(), getSelectedTiers());
	}
}


