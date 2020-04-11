
import java.util.Random;

import javax.swing.DefaultListModel;

@SuppressWarnings("serial")
public class CivListModel extends DefaultListModel<Civ> {
	CivList civs;
	Random rand;
	public CivListModel(CivList civs) {
		rand = new Random();
		this.civs = civs;
		setList();
	}
	
	@Override
	public Civ getElementAt(int arg0) {
		return civs.get(arg0);
	}

	@Override
	public int getSize() {
		return this.civs.size();
	}

	private void setList() {
		removeAllElements();
		for (Civ civ : civs ) {
			addElement(civ);
		}
	}
	
	public void updateList(CivList civs) {
		this.civs = civs;
		setList();
	}
	
	public CivList getList() {
		return civs;
	}
	
	public CivList getRandomCivPermutation() {
		CivList permutation = new CivList();

		if (this.size() > 0) {
			permutation.copy(this);
			
			for (int i = 0; i < 10000; i++) {
				Civ randomCiv = permutation.randomCiv();
				permutation.remove(randomCiv);
				permutation.add(randomCiv);
			}
		}
		return permutation;
		
	}
	
	

}
