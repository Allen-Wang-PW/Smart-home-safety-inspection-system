package Common;

public enum Relation {
	Equal, Larger, Smaller;

	public String getStr_f() {
		if(this.equals(Larger))
			return ">";
		else if(this.equals(Equal))
			return "=";
		else
			return "<";
	}
	
	public String getStr_l() {
		if(this.equals(Larger))
			return ">";
		else if(this.equals(Equal))
			return "==";
		else
			return "<";
	}
	
	public static Relation getRel(String s) {
		if(s.equals(">"))
			return Larger;
		else if(s.equals("="))
			return Equal;
		else
			return Smaller;
	}
}