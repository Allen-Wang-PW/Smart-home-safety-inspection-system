package Common;

public class LhaSpec {
	private String name;
	private String state;
	private String varCondition;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public String getVarCondition() {
		return varCondition;
	}
	
	public void setVarCondition(String varCondition) {
		this.varCondition = varCondition;
	}
	
	public String createSpe() {
		String spe = "";
		spe = spe + getName() + "." + 
				getState();
		if (getVarCondition() != null) {
			spe = spe + getVarCondition();
		}
		return spe;
	}
}