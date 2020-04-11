import java.util.ArrayList;

public class Civ {
	String country;
	String leader;
	ArrayList<String> type;
	String ability;
	ArrayList<Object> specialties;
	String tier;
	
	public Civ(String country, String leader, ArrayList<String> type, String ability, ArrayList<Object> specialties, String tier ) {
		this.country =country;
		this.leader = leader;
		this.type = type;
		this.ability = ability;
		this.specialties = specialties;
		this.tier = tier;
	}

	public String getLeader() {
		return leader;
	}

	public void setLeader(String leader) {
		this.leader = leader;
	}

	public ArrayList<String> getType() {
		return type;
	}

	public void setType(ArrayList<String> type) {
		this.type = type;
	}

	public String getAbility() {
		return ability;
	}

	public void setAbility(String ability) {
		this.ability = ability;
	}

	public ArrayList<Object> getSpecialties() {
		return specialties;
	}

	public void setSpecialties(ArrayList<Object> specialties) {
		this.specialties = specialties;
	}

	public String getTier() {
		return tier;
	}

	public void setTier(String tier) {
		this.tier = tier;
	}
	
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return country + " / " + leader;
	}
	
	public boolean isCompatibleType(ArrayList<String> typeArray) {
		for (String civType : getType()) {
			for (String type : typeArray ) {
				if (type.equals(civType)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isCompatibleTier(ArrayList<String> tierArray) {
		for (String tier : tierArray ) {
			if (tier.equals(getTier())) {
				return true;
			}
		}
		return false;
	}
	
	
}
