import javax.swing.JCheckBox;

public class CheckBoxAndName extends JCheckBox {
	
	String name;
	
	public CheckBoxAndName(String name) {
		super(name);
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name + " " + isSelected();
	}
}
