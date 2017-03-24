package Common;

public class Rule {
	public int srcSN;				// 第一个家具的编号
	public String sigTrig;			// 因为信号量改变而产生的规则
	public Trigger trigger;			// 字符串型变量取值
	
	public String newSigTrig;
	public Trigger newTrigger;
	
	public int dstSN;				// 第二个家具的编号
	public String dstAct;			// 第二个家具要执行的动作
	
	public String createRule() {
		
		String rule = "IF " + Info.getFurName(srcSN);
		if (sigTrig == null) {
			rule = rule + "." +
					trigger.var + " " +
					trigger.rel.getStr_f() + " " +
					trigger.value;
		}
		else {
			rule = rule + "." +
					sigTrig;
		}
		rule = rule + " THEN " +
				Info.getFurName(dstSN) + "." +
				dstAct;
		return rule;
	}
}