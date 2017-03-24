package Common;

public class Convert {
	public String from;
	public String condition;
	public String label;
	public String to;
	public String asg;
	
	public void getCond(Transition trans) {
		if(trans.trigger != null)
			condition = trans.trigger.var + trans.trigger.rel.getStr_l() + trans.trigger.value;
		else
			condition = "true";
	}
	
	public void getAsg(Transition trans) {
		if(trans.asgArr.length > 0)
			asg = trans.asgArr[0].var + "':=" + trans.asgArr[0].valRst;
	}
	
	public void getAsg(Action act) {
		if(act.asgArr.length > 0)
			asg = act.asgArr[0].var + "':=" + act.asgArr[0].valRst;
	}
}