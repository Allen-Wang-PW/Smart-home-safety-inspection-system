package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import Common.Action;
import Common.Automate;
import Common.Check;
import Common.Convert;
import Common.Data;
import Common.Furniture;
import Common.Info;
import Common.Location;
import Common.Relation;
import Common.Rule;
import Common.State;
import Common.Transition;
import Common.animationStateSetList;

public class Lha implements Check {
	
	private PrintWriter writer;
	private boolean Debug = false;
	private ArrayList<Automate> autoArr = new ArrayList<Automate>();
	
	// the name of a furniture
	public static String furName(Furniture fur) {
//		if (fur == null)
//			return null;
		return fur.furname + "_" + fur.SN;
	}
	
	// the name of a rule
	static public String ruleName(int i) {
		return "Trigger" + i;
	}
	
	// the name of the variable of a rule
	private String ruleVar(int i) {
		return "Trigger" + i + "_timer";
	}
	
	// the label of a transition
	private String transLabel(String fur, String trans) {
		return fur + "_" + trans;
	}
	
	// the label of a signaled action
	private String signalLabel(String fur, String action) {
		// TODO: signal???  "Signal_" + 
		return fur + "_" + action;
	}

	// the label of the convertion of a rule
	private String ruleLabel(int src, String var, Relation rel, String value, int dst, String action) {	
		Furniture sfur = Info.findFur(src);
		String sname = furName(sfur);
		
		Furniture dfur = Info.findFur(dst);
		String dname = furName(dfur);
		
		String s;
		if(rel.equals(Relation.Equal))
			s = "EQ";
		else if(rel.equals(Relation.Larger))
			s = "LT";
		else
			s = "ST";
		
		return sname + "_" + var + s + value + "_" + dname + "_" + action;
	}
	
	private String ruleLabel(int src, String signal, int dst, String action) {
		Furniture sfur = Info.findFur(src);
		String sname = furName(sfur);
		
		Furniture dfur = Info.findFur(dst);
		String dname = furName(dfur);

		return sname + "_" + signal + "_" + dname + "_" + action;
	}
	
	// the label when a condition satisfied
	private String conLabel(int src, String var, Relation rel, String value, int idx) {
		Furniture sfur = Info.findFur(src);
		String name = furName(sfur);
		
		String s;
		if(rel.equals(Relation.Equal))
			s = "EQ";
		else if(rel.equals(Relation.Larger))
			s = "LT";
		else
			s = "ST";
		
		return "sensor_" + name + "_" + var + s + value + "_" + idx;
	}
	
	private String conLabel(int src, String signal, int idx) {
		Furniture sfur = Info.findFur(src);
		String name = furName(sfur);
		return name + "_" + signal;
	}
	
	// find if a signal variable signal_t is needed
	private boolean findSig(Furniture fur) {
		for(Transition trans : fur.transArr) {
			if(trans.signal == true)
				return true;
		}
		for(Action act : fur.actionArr) {
			if(act.signal == true)
				return true;
		}
		return false;
	}
	
	// find the automation of a furniture in the array
	private Automate findAuto(int SN) {
		for(Automate auto : autoArr) {
			if(auto.name.contains(String.valueOf(SN)))
				return auto;
		}
		return null;
	}
	
	private void convertToAuto() {
		addFurAuto();
		addRuleAuto();
	}
	
	private void addFurAuto() {
		for(Furniture fur : Info.F_Array) {
			Automate auto = new Automate();
			auto.name = furName(fur);
			auto.signal = findSig(fur);
			
			// whether the furniture has an internal variable
			if(fur.variArr.length > 0) {
				auto.hasSensor = true;
				auto.variable = fur.variArr[0].name;
			}
			else {
				auto.hasSensor = false;
				auto.signal = true;
			}
			autoArr.add(auto);
			if(Debug)
				System.out.println(auto.name + " " + auto.variable);
					
			// add location S0
			Location init = new Location();
			init.name = "S0";
			init.invariant = "true";
			if(auto.signal)
				init.dynamic = "signal_t'==0";
			if(auto.hasSensor) {
				if(auto.signal)
					init.dynamic += "&";
				init.dynamic += auto.variable + "'==0";
			}
			auto.locArr.add(init);
			if(Debug)
				System.out.println("S0" + " " + init.dynamic + " true");
			
			// add convertion from S0 to initial state
			Convert beg = new Convert();
			beg.from = "S0";
			beg.to = fur.initState;
			beg.condition = "true";
			beg.label = auto.name + "_init";
			if(auto.hasSensor && fur.variArr[0].init != Integer.MAX_VALUE)
				beg.asg = auto.variable + "':=" + fur.variArr[0].init;
			auto.conArr.add(beg);
			
			// add locations based on states
			for(State state : fur.stateArr) {
				Location loc = new Location();
				loc.name = state.name;
				loc.invariant = state.invariant;
				loc.getDyna(state, auto.signal);
				
				auto.locArr.add(loc);
				//if(Debug)
					//System.out.println("Location: " + loc.name + " " + loc.dynamic + " " + loc.invariant);
			}
			
			// add convertions based on transitions
			for(Transition trans : fur.transArr) {
				Convert con = new Convert();
				con.from = trans.begState;
				con.to = trans.endState;
				con.getCond(trans);
				con.getAsg(trans);
				con.label = transLabel(auto.name, trans.name);
				auto.conArr.add(con);
				
				if(Debug)
					System.out.println("Convert: " + con.from + " " + con.to + " " + con.label + " " + con.asg + " " + con.condition);
			}
			
			// add locations and convertions based on signaled actions
			for(Action act : fur.actionArr) {
				if(act.signal) {
					Location loc = new Location();
					loc.name = "Signal_" + act.name;
					loc.invariant = "signal_t==0";
					loc.sigGetDyna(auto.variable);
					auto.locArr.add(loc);
					if(Debug)
						System.out.println("Location: " + loc.name + " " + loc.dynamic + " " + loc.invariant);
					
					Convert con = new Convert();
					con.from = loc.name;
					con.to = act.endState;
					con.condition = "true";
					con.asg = "signal_t':=0";
					con.label = signalLabel(auto.name, act.name);
					
					auto.conArr.add(con);
					if(Debug)
						System.out.println("Convert: " + con.from + " " + con.to + " " + con.label + " " + con.asg + " " + con.condition);
				}
			}
		
			if(Debug)
				System.out.println();
		}
	}
	
	private void addRuleAuto() {
		// add automations based on rules
		int idx = 0;
		for(Rule rule : Info.R_Array) {
			Automate auto = new Automate();
			auto.name = ruleName(idx);
			auto.signal = false;
			auto.hasSensor = true;
			auto.variable = ruleVar(idx);
			autoArr.add(auto);
			if(Debug)
				System.out.println(auto.name + " " + auto.variable);
			
			// add location S0
			Location loc = new Location();
			loc.name = "S0";
			loc.invariant = "true";
			loc.dynamic = auto.variable + "'==0";
			auto.locArr.add(loc);
			if(Debug)
				System.out.println("Location: " + loc.name + " " + loc.dynamic + " " + loc.invariant);
			
			Convert con = new Convert();
			con.from = "S0";
			con.to = "Ready";
			con.condition = "true";
			con.label = auto.name + "_init";
			con.asg = auto.variable + "':=0";
			auto.conArr.add(con);
			if(Debug)
				System.out.println("Convert: " + con.from + " " + con.to + " " + con.label + " " + con.asg + " " + con.condition);
			
			// add location ready
			Location ready = new Location();
			ready.name = "Ready";
			ready.invariant = "true";
			ready.dynamic = auto.variable + "'==0";
			auto.locArr.add(ready);
			if(Debug)
				System.out.println("Location: " + ready.name + " " + ready.dynamic + " " + ready.invariant);
			
			Convert con1 = new Convert();
			con1.from = "Ready";
			con1.to = "Waiting";
			con1.condition = "true";
			if(rule.sigTrig != null)
				con1.label = conLabel(rule.srcSN, rule.sigTrig, idx);
			else
				con1.label = conLabel(rule.srcSN, rule.trigger.var, rule.trigger.rel, rule.trigger.value, idx);
			con1.asg = auto.variable + "':=0";
			auto.conArr.add(con1);
			if(Debug)
				System.out.println("Convert: " + con1.from + " " + con1.to + " " + con1.label + " " + con1.asg + " " + con1.condition);
			
			// add location waiting
			Location waiting = new Location();
			waiting.name = "Waiting";
			waiting.invariant = auto.variable + "<=5";
			waiting.dynamic = auto.variable + "'==1";
			auto.locArr.add(waiting);
			if(Debug)
				System.out.println("Location: " + waiting.name + " " + waiting.dynamic + " " + waiting.invariant);
			
			Convert con2 = new Convert();
			con2.from = "Waiting";
			con2.to = "Ready";
			con2.condition = auto.variable + ">=3";
			con2.asg = auto.variable + "':=0";
			if(rule.sigTrig != null)
				con2.label = ruleLabel(rule.srcSN, rule.sigTrig, rule.dstSN, rule.dstAct);
			else
				con2.label = ruleLabel(rule.srcSN, rule.trigger.var, rule.trigger.rel, rule.trigger.value, rule.dstSN, rule.dstAct);
			auto.conArr.add(con2);
			if(Debug)
				System.out.println("Convert: " + con2.from + " " + con2.to + " " + con2.label + " " + con2.asg + " " + con2.condition);
		
			// add convertions to src furniture
			Automate src = findAuto(rule.srcSN);
			if(rule.sigTrig == null) {
				for(Location l : src.locArr) {
					if(l.name.contains("S0") == false && l.name.contains("Signal") == false) {
						Convert tmp = new Convert();
						tmp.from = l.name;
						tmp.to = l.name;
						tmp.condition = rule.trigger.var + rule.trigger.rel.getStr_l() + rule.trigger.value;
						tmp.label = conLabel(rule.srcSN, rule.trigger.var, rule.trigger.rel, rule.trigger.value, idx);
						src.conArr.add(tmp);
					}
				}
			}
			
			// add convertions to dst furniture
			Automate dst = findAuto(rule.dstSN);
			Action act = Info.findAction(rule.dstSN, rule.dstAct);
			Convert trans = new Convert();
			trans.from = act.begState;
			trans.to = "Signal_" + act.name;
			trans.condition = "true";
			
			trans.getAsg(act);
			if(rule.sigTrig != null)
				trans.label = ruleLabel(rule.srcSN, rule.sigTrig, rule.dstSN, rule.dstAct);
			else
				trans.label = ruleLabel(rule.srcSN, rule.trigger.var, rule.trigger.rel, rule.trigger.value, rule.dstSN, rule.dstAct);
			dst.conArr.add(trans);
			
			++idx;
			if(Debug)
				System.out.println();
		}
	}
	
	private void outInit(int depth) {
		writer.println("composedautomaton test");
		writer.println("variable");
		writer.println("relation:true;");
		writer.println("automaton");
		for(Automate auto : autoArr)
			writer.println(auto.name + ":" + auto.name + "(" + depth + ");");
	}
	
	void outSpec() {
		//TODO to be modified
		//if(ModelMain.case4 == Case4.Smaller)
		// smoke rain window ac_cooler
		//writer.println("forbidden={Smoke_Detector_1.LOW{smoke_level>=20}&Rain_Detector_2.Raining&Window_3.Closed&AC_Cooler_4.OFF{temperature>=30}&Trigger0.Ready&Trigger1.Ready&Trigger2.Ready};");
		//writer.println("forbidden={Smoke_Detector_1.LOW{smoke_level>=20}&Rain_Detector_2.Raining&Window_3.Closed&AC_Cooler_4.OFF&Trigger0.Ready&Trigger1.Ready&Trigger2.Ready};");
		//else{
		//	writer.println("forbidden={SunDetector_1.Day&Smoke_Detector_2.LOW{smoke_level>=20}&Rain_Detector_3.Raining&Outside_Light_Sensor_4.Working&Window_5.Closed&Window_6.Closed&Window_7.Closed&AC_Cooler_8.OFF{temperature>=30}&ALARM_9.OFF&TV_10.Closed&Door_11.Closed&Light_12.OFF&Light_13.OFF&Light_14.OFF&Light_15.OFF&Human_Detector_16.NoHuman&Human_Detector_17.NoHuman&Human_Detector_18.NoHuman&Human_Detector_19.NoHuman&Trigger0.Ready&Trigger1.Ready&Trigger2.Ready};");
		//}
		writer.println(Info.getLhaSpec());
	}
	
	public void generate() throws FileNotFoundException {
		convertToAuto();
		File outFile = new File("data/tst/Lha/FUR.txt");
		writer = new PrintWriter(outFile);
		outInit(10);
		writer.println();
		for(Automate auto : autoArr)
			auto.print(writer);
		outSpec();
		writer.close();
	}

	public boolean check() {
		Data.check_text.setText("Checking..." + Data.lineSeparator + "Please wait" + Data.lineSeparator);
		String cmd="/home/peter/Desktop/tool_IFTTT/src/bach ./data/tst/Lha/FUR.txt";
		Data.check_text.append(cmd + Data.lineSeparator + Data.lineSeparator);
		boolean outputBegin = true;
		
		try {
			Process p = Runtime.getRuntime().exec(cmd); 
			BufferedReader output = new BufferedReader(new InputStreamReader(p.getInputStream()));		
			String line;
			while((line = output.readLine()) != null){
				if(line.contains("Trigger"))
					outputBegin = false;
				if(line.equals("reachable")){
					simulate();
					return false;}
				else if(line.equals("ok"))return true;
				if(outputBegin)
					Data.check_text.append(line + Data.lineSeparator);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;//TODO
	}
	public void simulate() throws IOException{
		String cmd="/home/peter/Desktop/tool_IFTTT/src/bach ./data/tst/Lha/FUR.txt";
		Process p = Runtime.getRuntime().exec(cmd); 
		BufferedReader output = new BufferedReader(new InputStreamReader(p.getInputStream()));
		animationStateSetList.initList();
		String line;
		int actualElement = 0;
		int actualListSize = 0;
		animationStateSetList.actualListSize = 0;
		while((line = output.readLine()) != null){
			if(line.contains("Trigger"))
				break;
			actualElement++;
			String[] temp = line.split("[:^]");
			
			//AC_Cooler_1 => 1
			int _index = temp[0].lastIndexOf('_');
			String SNString = temp[0].substring(_index + 1);
			int SN = Integer.parseInt(SNString);
			/*int j = 0;
			for(int i = 1;!temp[i].equals("virtual_target");i+=2,j++){
				animationStateSetList.info[j].element[SN - 1].SN = SN;
				animationStateSetList.info[j].element[SN - 1].state = temp[i];
			}*/
			//remove Signal_... and S0 location
			int j = 0;
			for(int i = 3;!temp[i].equals("virtual_target");i+=2)
				if(temp[i].contains("Signal"))
					continue;
				else{
					animationStateSetList.info[j].element[SN - 1].SN = SN;
					animationStateSetList.info[j].element[SN - 1].state = temp[i];
					j++;
				}

			if(actualListSize < j)
				actualListSize = j;
		}
		for(int i=0;i<actualListSize;i++)
			for(int j = 0;j<actualElement;j++)
				if(animationStateSetList.info[i].element[j].state==null){
					animationStateSetList.info[i].element[j].SN = animationStateSetList.info[i - 1].element[j].SN;
					animationStateSetList.info[i].element[j].state = animationStateSetList.info[i - 1].element[j].state;
				}
		animationStateSetList.actualElementSize = actualElement;
		animationStateSetList.actualListSize = actualListSize;
	}
}
