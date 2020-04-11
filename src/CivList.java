import java.util.ArrayList;
import java.util.Random;

public class CivList extends ArrayList<Civ> {
	Random rand;
	
	public CivList() {
		rand = new Random();
		
	}
	
	public void sortByCivName() {
		
	}
	
	public void sortByCivLeader() {
		
	}
	
	public void sortByCivTier() {
		
	}
	
	public void sortByCivType() {
		
	}

	

	
	public CivList compatibleType(ArrayList<String> typeArray) {
		CivList goodCivs = new CivList();
		for ( Civ civ : this ) {
			if (civ.isCompatibleType(typeArray)) {
				goodCivs.add(civ);
			}
		}
		return goodCivs;
	}
	
	public CivList compatibleTier(ArrayList<String> tierArray) {
		CivList goodCivs = new CivList();
		for ( Civ civ : this ) {
			if (civ.isCompatibleTier(tierArray)) {
				goodCivs.add(civ);
			}
		}
		return goodCivs;
	}
	
	public void printCivs() {
		for (Civ civ : this ) {
			System.out.println(civ);
		}
	}
	
	public void copy(CivListModel cl) {
		for (Civ civ : this ) {
			remove(civ);
		}
		for (Civ civ : cl.getList() ) {
			add(civ);
		}
	}
	
	public Civ randomCiv() {
		int index = rand.nextInt(size());
		return get(index);
	}
	
	
	
}
