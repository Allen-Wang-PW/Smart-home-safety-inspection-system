package Common;

public class Location {
	public String name;
	public String invariant;
	public String dynamic = new String();
	
	public void getDyna(State state, boolean signal) {
		if(signal)
			dynamic = "signal_t'==0";
		if(state.dynamic.length > 0) {
			if(signal)
				dynamic += "&";
			dynamic += state.dynamic[0].name + "'==" + state.dynamic[0].rate;
		}
	}
	
	public void sigGetDyna(String variable) {
		dynamic = "signal_t'==1";
		if(variable != null)
			dynamic += "&" + variable + "'==0";
	}
}
