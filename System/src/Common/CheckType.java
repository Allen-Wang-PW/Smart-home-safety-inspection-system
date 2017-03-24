package Common;

public enum CheckType {
	Fsm, Lha;
	
	public String getStr() {
		if(this.equals(Fsm))
			return "Fsm";
		else if(this.equals(Lha))
			return "Lha";
		else
			return null;
	}
	
	public static CheckType getCheckType(String type) {
		if(type.equals("Fsm"))
			return Fsm;
		else if(type.equals("Lha"))
			return Lha;
		else
			return null;
	}
}