package model;

import java.io.FileNotFoundException;

import Common.Info;
import Common.FsmSpec;
import Common.Furniture;
import Common.LhaSpec;
import Common.Relation;
import Common.Rule;
import Common.Trigger;

public class ChangeInfo {	
	private static int SN_num = 0;
	private static Boolean Debug = false;
	
	// add a new device
	public static void addDevice(String pathname, int loc_index, int loc_x, int loc_y) throws FileNotFoundException {
		Furniture fur = jsonParser.JsonParserObject(pathname, loc_x, loc_y);
		fur.furnitureInfo.loc_index = loc_index;
		fur.SN = setSN(getSN() + 1);
		ChangeInfo.addFurSpec(fur.SN, fur.initState, null);
		
		if(Debug)
			System.out.println("add info from " + pathname + " successfully.");		
	}
	
	// add a new rule
	public static void addRule(int src_SN, String var, Relation rel, int value, int dst_SN, String action) {
		Rule rule = new Rule();
		
		rule.srcSN = src_SN;
		rule.trigger = new Trigger();
		rule.trigger.var = var;
		rule.trigger.rel = rel;
		rule.trigger.value = String.valueOf(value);
		
		rule.dstSN = dst_SN;
		rule.dstAct = action;
		
		addRuleSpec(Info.R_Array.size());
		Info.R_Array.add(rule);
	}
	
	// add a new rule
	public static void addRule(int src_SN, String api, int dst_SN, String action) {
		Rule rule = new Rule();
		
		rule.srcSN = src_SN;
		rule.sigTrig = api;
		
		rule.dstSN = dst_SN;
		rule.dstAct = action;
	
		addRuleSpec(Info.R_Array.size());
		Info.R_Array.add(rule);
	}

	// change a specific rule
	public static void changeRule(int ruleNo, int src_SN, String api, int dst_SN, String action) {
		Common.Rule rule = new Common.Rule();
		
		rule.srcSN = src_SN;
		rule.sigTrig = api;
			
		rule.dstSN = dst_SN;
		rule.dstAct = action;
			
		Common.Info.R_Array.set(ruleNo, rule);
	}
		
	// change a specific rule
	public static void changeRule(int ruleNo, int src_SN, String var, Common.Relation rel, int value, int dst_SN, String action) {
		Common.Rule rule = new Common.Rule();
		System.out.println("change : " + ruleNo);
		rule.srcSN = src_SN;
		rule.trigger = new Common.Trigger();
		rule.trigger.var = var;
		rule.trigger.rel = rel;
		rule.trigger.value = String.valueOf(value);
		
		rule.dstSN = dst_SN;
		rule.dstAct = action;
			
		Common.Info.R_Array.set(ruleNo, rule);
	}

	// add a new specification
	public static void addSpecLha(int furnitureSN, String state, String cond) {
		changeFurSpec(furnitureSN, state, cond);
	}
	
	// add a new specification
	public static void addSpecFsm(int no, String first, String second, String spe) {
		FsmSpec spec = new FsmSpec();
		spec.spe = spe;
		spec.str = "CTLSPEC ";
		
		switch(no)
		{
		case 1:
			spec.str += "AG( " + first + " )"; 
			break;
		case 2:
			spec.str += "AF( " + first + " )"; 
			break;
		case 3:
			spec.str += "AG !( " + first + " )"; 
			break;
		case 4:
			spec.str += "AG( (" + first + ") -> AX( " + second + " ) )";
			break;
		case 5:
			spec.str += "AG( (" + first + ") -> AF (" + second + " )";
			break;
		case 6:
			spec.str = "LTLSPEC G( (" + first + ") -> FG (" + second + " )";
			break;
		}
		
		Info.FsmArray.add(spec);
	}
	
	// add a new specification
	public static void addSpecLha(String name, String state, String cond) {
		LhaSpec spec = new LhaSpec();
		spec.setName(name);
		spec.setState(state);
		spec.setVarCondition(cond);
		Info.LhaArray.add(spec);
	}
	
	public static void addSpecFsm(String str, String spe) {
		FsmSpec spec = new FsmSpec();
		spec.str = str;
		spec.spe = spe;
		Info.FsmArray.add(spec);
	}
	
	// delete a rule
	public static boolean delRule(int no) {
		if(Info.R_Array.size() <= no)
			return false;
		
		Info.R_Array.remove(no);
		
		// remove Lha specification of this rule
		for(int j = Info.LhaArray.size() - 1; j >= 0; --j) {
			LhaSpec spec = Info.LhaArray.get(j);
			if(spec.getName().contains("Trigger")) {
				Info.LhaArray.remove(j);
				break;
			}
		}
		
		return true;
	}
	
	// delete a specification: start from 0
	public static boolean delSpecLha(int no) {
		if(no >= Info.F_Array.size())
			return false;
		
		LhaSpec spec = Info.LhaArray.get(no);
		String name = spec.getName();
		for(Furniture fur : Info.F_Array) {
			if(name.contains(fur.furname)) {
				spec.setState(fur.initState);
				spec.setVarCondition(null);
				return true;
			}
		}
			
		System.out.println("Error: no such furniture");
		return false;
	}
		
	public static boolean delSpecFsm(int no) {
		Info.FsmArray.remove(no);
		return true;
	}
		
	// delete a furniture
	public static boolean delFur(int no) {
		int SN = Info.F_Array.get(no).SN;
		String SN_str = "_" + String.valueOf(SN);
		System.out.println("fur deleted : " + SN_str);
		
		// remove rules containing fur
		for(int i = 0; i < Info.R_Array.size(); ++ i) {
			Rule rule = Info.R_Array.get(i);
			if(rule.srcSN == SN || rule.dstSN == SN) {
				delRule(i--);
			}
		}
			
		// remove fsm specification containing fur
		for(int i = 0; i < Info.FsmArray.size(); ++i) {
			FsmSpec spec = Info.FsmArray.get(i);
			if(spec.str.contains(SN_str)) {
				Info.FsmArray.remove(i--);
			}
		}
		
		// remove lha specification containing fur
		for(int i = 0; i < Info.LhaArray.size(); ++i) {
			LhaSpec spec = Info.LhaArray.get(i);
			if(spec.getName().contains(SN_str))
				Info.LhaArray.remove(i--);
		}
		
		// remove furniture
		Info.F_Array.remove(no);
		return true;
	}

	// get current max-SN
	public static int getSN() {
		return SN_num;
	}

	public static int setSN(int x) {
		SN_num = x;
		return x;
	}
	
	public static void changeFurSpec(int furnitureSN, String state, String cond) {
		Furniture fur = Info.findFur(furnitureSN);
		String furName = Lha.furName(fur);
		for(LhaSpec spec : Info.LhaArray) {
			if(spec.getName().equals(furName)) {
				spec.setState(state);
				spec.setVarCondition(cond);
				return;
			}
		}
		
		System.out.println("Error: no such specification!");
	}
	
	public static void addFurSpec(int furnitureSN, String state, String cond) {
		LhaSpec spec = new LhaSpec();

		Furniture fur = Info.findFur(furnitureSN);
		spec.setName(Lha.furName(fur));
		spec.setState(state);
		spec.setVarCondition(cond);
		
		Info.LhaArray.add(spec);
	}

	public static void addRuleSpec(int ruleNo) {
		LhaSpec spec = new LhaSpec();
		
		spec.setName(Lha.ruleName(ruleNo));
		spec.setState("Ready");
		spec.setVarCondition(null);
		
		Info.LhaArray.add(spec);
	}
}