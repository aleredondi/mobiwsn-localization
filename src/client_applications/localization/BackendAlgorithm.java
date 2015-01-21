package client_applications.localization;

public class BackendAlgorithm {

	private String name;
	private boolean selected;
	
	public BackendAlgorithm(String name)
	{
		this.name = name;
		this.selected = false;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public String toString()
	{
		return this.getName() + " (Selected:" + this.isSelected() + ")";
	}
	
	
}
