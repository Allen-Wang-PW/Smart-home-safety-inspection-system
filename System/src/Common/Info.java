package Common;

import java.io.IOException;
import java.util.ArrayList;

import Common.Check;
import Common.CheckType;
import model.Fsm;
import model.FsmFix;
import model.Lha;
import model.LhaFix;

public class Info {
	static public ArrayList<Furniture> F_Array = new ArrayList<Furniture>();
	static public ArrayList<Rule> R_Array = new ArrayList<Rule>();
	static public ArrayList<LhaSpec> LhaArray = new ArrayList<LhaSpec>();
	static public ArrayList<FsmSpec> FsmArray = new ArrayList<FsmSpec>();
	static public ArrayList<FurListObj> FurList = new ArrayList<FurListObj>();
	
	static public CheckType Mode = CheckType.Fsm;
//	static public CheckType tempMode = CheckType.Fsm;
	
	static private Check check;
	
	public static void setMode(CheckType m) {
		Mode = m;
	}
	
	public static CheckType getMode() {
		return Mode;
	}
	
	public static String getFurName(int SN) {
		Furniture fur = findFur(SN);
		String name = Lha.furName(fur);
		return name;
	}
	
	public static void generate() throws IOException {
		if(Mode == CheckType.Fsm)
			check = new Fsm();
		else
			check = new Lha();
		check.generate();
	}
	
	public static void Check() throws IOException {
		if(Mode == CheckType.Fsm) {
			if(check.check()) {
				Data.check_text.append(Data.lineSeparator + "The Fsm system is safe" + Data.lineSeparator);
				
				Data.menu.buttonpanel.fix.setEnabled(false);
				Data.fixresultpanel.jbOK.setEnabled(false);
				Data.fixresultpanel.jbRefix.setEnabled(false);
			}
			else {
				Data.check_text.append(Data.lineSeparator + "The Fsm system is not safe" + Data.lineSeparator);
				Data.menu.buttonpanel.fix.setEnabled(true);
			}
		}
		else {
			if(check.check()) {
				Data.check_text.append(Data.lineSeparator + "The Lha system is safe" + Data.lineSeparator);
				
				Data.menu.buttonpanel.fix.setEnabled(false);
				Data.fixresultpanel.jbOK.setEnabled(false);
				Data.fixresultpanel.jbRefix.setEnabled(false);
			}
			else {
				Data.check_text.append(Data.lineSeparator + "The Lha system is not safe" + Data.lineSeparator);
				Data.menu.buttonpanel.fix.setEnabled(true);
			}
		}
	}
	
	public static void Fix() throws IOException {
		if(Mode == CheckType.Fsm) {
			FsmFix f = new FsmFix();
			f.setFsm((Fsm) check);
			f.Fix();
		}
		else{
			LhaFix lf = new LhaFix();
			lf.Fix();
		}
	}
	
	public static Furniture findFur(int SN) {
		for(Furniture fur : Info.F_Array) {
			if(fur.SN == SN)
				return fur;
		}
		return null;
	}
	
	public static Action findAction(int SN, String name) {
		Furniture fur = findFur(SN);
		for(Action act : fur.actionArr)
			if(act.name.equals(name))
				return act;
		return null;
	}
	
	public static Action findAction(Furniture fur, String name) {
		for(Action act : fur.actionArr)
			if(act.name.equals(name))
				return act;
		return null;
	}
	
	// find an variable of a furniture
	public static Variable findVar(Furniture fur, String name) {
		for(Variable var : fur.variArr) {
			if(var.name.equals(name))
				return var;
		}
		return null;
	}
	// important for getting Lha style specification
	static public String getLhaSpec() {
		String str = "forbidden={";
		for(int i = 0; i < Common.Info.LhaArray.size(); ++i) {
			LhaSpec spec = Common.Info.LhaArray.get(i);
			str += spec.getName() + "." + spec.getState();
			if(spec.getVarCondition() != null)
				str += "{" + spec.getVarCondition() + "}";
			
			if(i < Common.Info.LhaArray.size() - 1)
				str += "&";
		}
		
		str += "};";
		return str;
	}
}