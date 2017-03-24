package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import Common.Action;
import Common.Assignment;
import Common.Check;
import Common.Data;
import Common.Dynamic;
import Common.FsmSpec;
import Common.Furniture;
import Common.Info;
import Common.Rule;
import Common.State;
import Common.Transition;
import Common.Variable;
import Common.Var;
import Common.animationStateSetList;
import Common.sigVar;
import Common.transNext;

public class Fsm implements Check {	
	private Boolean Debug = false;
	private PrintWriter writer;
	private String furPath = "data/tst/Fsm/FUR.txt";
	private void initProc() {
		for(Furniture fur : Info.F_Array) {

			fur.nextSta.clear();
			fur.sigArr.clear();
			
			// modify signal variable Arraylist	
			for(Action act : fur.actionArr) {
				if(act.signal == true) {
					sigVar sig = new sigVar();
					sig.name = act.name.toLowerCase() + "_a";
					sig.start = act.begState;
					sig.end = act.endState;
					fur.sigArr.add(sig);
				}
			}
			
			for(Transition trans : fur.transArr) {
				if(trans.signal == true) {
					sigVar sig = new sigVar();
					sig.name = trans.name.toLowerCase() + "_t";
					sig.start = trans.begState;
					sig.end = trans.endState;
					fur.sigArr.add(sig);
				}
			}
			
			// get next state array by transitions
			for(Transition trans : fur.transArr) {
				transNext next = new transNext();
				next.state = trans.begState;
				next.nextSta = trans.endState;
				if(trans.trigger != null)
					next.condition = getName(fur, trans.trigger.var) + trans.trigger.rel.getStr_f() + trans.trigger.value;
				
				fur.nextSta.add(next);
				if(Debug)
					System.out.println(fur.furname + " " + next.state + " " + next.condition + " " + next.nextSta);
			}
			
			// get next variable array by transitions
			for(Transition trans : fur.transArr) {
				for(Assignment asg : trans.asgArr) {
					transNext next = new transNext();
					next.state = trans.begState;
					next.nextVal = asg.valRst;
					if(trans.trigger != null)
						next.condition = getName(fur, trans.trigger.var) + trans.trigger.rel.getStr_f() + trans.trigger.value;
					Variable var = Info.findVar(fur, asg.var);
					
					var.nextArr.add(next);
					if(Debug)
						System.out.println(fur.furname + " " + next.state + " " + next.condition + " " + next.nextVal);
				}
			}
		}
		
		// get next state array by rules
		int temp = 0;
		for(Rule rule : Info.R_Array) {
			Furniture src = Info.findFur(rule.srcSN);
			Furniture fur = Info.findFur(rule.dstSN);
			String con = new String();
			
			// TODO: rule set by userf
			if(rule.sigTrig != null)
				con = getSig(rule.srcSN, rule.sigTrig) + "=TRUE";
			else
				con = getName(src, rule.trigger.var) + rule.trigger.rel.getStr_f() + rule.trigger.value;
			
			Action act = Info.findAction(fur, rule.dstAct);
			transNext next = new transNext();
			next.ruleNo = temp;
			next.state = act.begState;
			next.nextSta = act.endState;
			next.condition = con;

			if(act.trigger != null)
				next.condition += " & " + getName(fur, act.trigger.var) + act.trigger.rel.getStr_f() + act.trigger.value;
			fur.nextSta.add(next);
			
			if(Debug)
				System.out.println(fur.furname + " " + next.state + " " + next.condition + " " + next.nextSta);
			temp++;
		}
		
		// get next variable array by rules
		for(Rule rule : Info.R_Array) {
			Furniture src = Info.findFur(rule.srcSN);
			Furniture fur = Info.findFur(rule.dstSN);
			String con = new String();
			
			// TODO: rule set by user
			if(rule.sigTrig != null)
				con = getSig(rule.srcSN, rule.sigTrig) + "=TRUE";
			else
				con = getName(src, rule.trigger.var) + rule.trigger.rel.getStr_f() + rule.trigger.value;
			
			if(Debug)
				System.out.println("con: " + con);
			
			Action act = Info.findAction(fur, rule.dstAct);
			for(Assignment asg : act.asgArr) {
				transNext next = new transNext();
				next.state = act.begState;
				next.nextVal = asg.valRst;
				next.condition = con;
				if(act.trigger !=  null)
					next.condition += " & " + getName(fur, act.trigger.var) + act.trigger.rel.getStr_f() + act.trigger.value;
				
				Variable var = Info.findVar(fur, asg.var);
				var.nextArr.add(next);
				if(Debug)
					System.out.println(fur.furname + " " + next.state + " " + next.condition + " " + next.nextVal);
			}
		}
	}

	// get the name of a furniture module
	public String getName(Furniture fur) {
		/*if(Debug)
			return fur.furname.toLowerCase() + '_' + String.valueOf(fur.SN);
		else
			return '_' + String.valueOf(fur.SN);
			*/
		return fur.furname.toLowerCase() + '_' + String.valueOf(fur.SN);
	}
	
	// get the name of an internal variable of furniture fur
	public String getName(Furniture fur, Variable var) {
		return getName(fur) + '.' + var.name;
	}
	
	// get the name of a signal variable of furniture fur
	public String getName(Furniture fur, String name) {
		return getName(fur) + '.' + name;
	}
	
	// get the name of state of furniture fur
	public String getState(Furniture fur) {
		return getName(fur) + ".state";
	}
	
	// get the name of a signal variable of furniture
	public String getSig(int SN, String sig) {
		Furniture fur = Info.findFur(SN);
		for(Action act : fur.actionArr) {
			if(act.name.equals(sig))
				return getName(fur) + "." + sig.toLowerCase() + "_a";
		}
		
		for(Transition trans : fur.transArr) {
			if(trans.name.equals(sig))
				return getName(fur) + "." + sig.toLowerCase() + "_t";
		}
		
		return null;
	}
		
	public void outModule(PrintWriter writer) {
		for(Furniture fur : Info.F_Array) {
			writer.println("MODULE " + fur.furname + '_' + fur.SN);
			writer.println("VAR");
			writer.print("\tstate: {");
			
			// declare states
			for(int i = 0; i < fur.stateArr.length; ++i) {
				writer.print(fur.stateArr[i].name);
				if(i < fur.stateArr.length - 1)
					writer.print(", ");
				else
					writer.println("};");
			}
			
			// declare internal variables
			for(Variable var : fur.variArr) {
				writer.print('\t' + var.name + ": ");
				writer.println(var.lowBond + ".." + var.highBond + ';');
			}
			
			// declare signal variables
			for(sigVar sig : fur.sigArr) {
				writer.println('\t' + sig.name + ": boolean;");
			}
			
			// initial states and variables
			writer.println("ASSIGN");
			if(fur.initState.length() > 0 && fur.stateArr.length > 1) {
				writer.println("\tinit(state) := " + fur.initState + ';');
			}
			
			for(Variable var : fur.variArr) {
				if(var.init != Integer.MAX_VALUE)
					writer.println("\tinit(" + var.name + ") := " + var.init + ';');
			}
			
			// initial signal variables
			for(sigVar sig : fur.sigArr) {
				writer.println("\tinit(" + sig.name + ") := FALSE;");
			}
			
			writer.println();
		}
	}
	
	public void outMain(PrintWriter writer) {
		writer.println("MODULE main");
		writer.println("VAR");
		for(Furniture fur : Info.F_Array) {
			writer.println('\t' + getName(fur) + ": " + fur.furname + "_" + fur.SN + ';');
		}
		
		writer.println();
		writer.println("ASSIGN");
		
		// output state transitions
		for(Furniture fur : Info.F_Array) {
			if(fur.nextSta.size() > 0) {	
				writer.println("\tnext(" + getState(fur) + "):=");
				writer.println("\tcase");
				for(transNext next : fur.nextSta) {
					writer.print("\t\t" + getState(fur) + "=" + next.state);
					if(next.condition != null)
						writer.print(" & " + next.condition);
					writer.println(": " + next.nextSta + ";");
				}
				writer.println("\t\tTRUE: " + getState(fur) + ";");
				writer.println("\tesac;");
				writer.println();
			}
		}
		
		// output internal variable transitions
		for(Furniture fur : Info.F_Array) {
			for(Variable var : fur.variArr) {
				writer.println("\tnext(" + getName(fur, var) + "):=");
				writer.println("\tcase");
				for(transNext next : var.nextArr) {
					writer.print("\t\t" + getState(fur) + "=" + next.state);
					if(next.condition != null)
						writer.print(" & " + next.condition);
					writer.println(": " + next.nextVal + ";");
				}
				dirty(fur, var);
				writer.println("\t\tTRUE: " + getName(fur, var) + ";");
				writer.println("\tesac;");
				writer.println();
			}
		}
		
		// output signal variable transitions
		for(Furniture fur : Info.F_Array) {
			for(sigVar sig : fur.sigArr) {
				writer.println("\tnext(" + getName(fur, sig.name) + "):=");
				writer.println("\tcase");
				writer.println("\t\t" + getState(fur) + "=" + sig.start + " " +
						"& next(" + getState(fur) + ")=" + sig.end + ": TRUE;");
				writer.println("\t\tTRUE: FALSE;");
				writer.println("\tesac;");
				writer.println();
			}
		}
	}
	
	// output specifications
	public void outSpec(PrintWriter writer) {	
		for(FsmSpec spec : Info.FsmArray) {
			writer.println(spec.str);
		}
	}
	
	// dirty work...eh
	private void dirty(Furniture fur, Variable var) {
		// in case variable out of range, TODO: range!!!
		writer.print("\t\t" + getName(fur, var) + "=" + var.highBond);
		writer.println(": {toint(" + getName(fur, var) + "), toint(" + getName(fur, var) + ")-1};");
		
		writer.print("\t\t" + getName(fur, var) + "=" + var.lowBond);
		writer.println(": {toint(" + getName(fur, var) + "), toint(" + getName(fur, var) + ")+1};");
		
		// dynamic in each state
		for(State s : fur.stateArr) {
			for(Dynamic d : s.dynamic) {
				if(d.name.equals(var.name)) {
					String []tmp = d.rate.split("\\[|\\]|\\,");
					if(tmp.length == 1) {
						int diff = Integer.parseInt(tmp[0]);
						writer.print("\t\t" + getState(fur) + " = " + s.name + ": ");
						if(diff > 0)
							writer.println("toint(" + getName(fur, var) + ")+" + diff + ";");
						else if(diff < 0)
							writer.println("toint(" + getName(fur, var) + ")" + diff + ";");
						else
							writer.println(getName(fur, var) + ";");
					}
					else {
						int diff1 = Integer.parseInt(tmp[1]);
						int diff2 = Integer.parseInt(tmp[2]);
						writer.print("\t\t" + getState(fur) + " = " + s.name + ": ");
						if(diff1 >= 0)
							writer.print("{toint(" + getName(fur, var) + ")+" + diff1 + ", ");
						else
							writer.print("{toint(" + getName(fur, var) + ")" + diff1 + ", ");
						
						writer.print("toint(" + getName(fur) + "." + var.name + ")" + ", ");
						
						if(diff2 >= 0)
							writer.println("toint(" + getName(fur, var) + ")+" + diff2 + "};");
						else
							writer.println("toint(" + getName(fur, var) + ")" + diff2 + "};");
					}
				}
			}
		}
	}
	
	public void generate() throws FileNotFoundException {
		initProc();
		File outFile = new File("data/tst/Fsm/FUR.txt");
		writer = new PrintWriter(outFile);
		outModule(writer);
		outMain(writer);
		outSpec(writer);
		writer.close();
	}

	public void simulate() throws IOException{
		String cmd = "/home/peter/NuSMV/NuSMV-2.6.0-Linux/bin/NuSMV " + furPath;
		Process p = Runtime.getRuntime().exec(cmd); 
		BufferedReader output = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		
		boolean stateBegin = false;
		boolean traceBegin = false;
//		animationStateSetList assl = null;
		
		int setIndex = 0;//actualListSize
		int elementIndex = 0;//actualElementSize

		while((line = output.readLine()) != null){
				if(line.startsWith("  -> State:")){
					if(line.endsWith(".1 <-")){//A trace begins
//						if(assl != null){//not first trace
//							assl.actualListSize = setIndex;
//							InfoList.add(assl);
//						}
//						assl = new animationStateSetList();
						
						animationStateSetList.actualListSize = setIndex;

						animationStateSetList.initList();
						traceBegin = true;
						stateBegin = true;
						
						setIndex = 0;
						elementIndex = 0;
					}
					else //A state set begins
						traceBegin = false;
				}
				
				if(traceBegin) {
					traceBegin = false;
					elementIndex = 0;
					int SN = 0;
					while((line = output.readLine()) != null && (!line.startsWith("-- specification")) && (!line.startsWith("  -> State:"))){	
						//int _index = line.indexOf('_');
						int pointIndex = line.indexOf('.');
						int equalIndex = line.indexOf('=');
						if(line.substring(pointIndex + 1, equalIndex).contains("state")){//only include _x.state
							//int _index = line.indexOf('_');
							int _index = line.lastIndexOf('_');
							SN = Integer.parseInt(line.substring(_index + 1, pointIndex));
							String state = line.substring(equalIndex + 2);
							animationStateSetList.info[setIndex].element[SN - 1].SN = SN;
							animationStateSetList.info[setIndex].element[SN - 1].state = state;
							animationStateSetList.info[setIndex].element[SN - 1].isChanged = false;
							elementIndex++;
//							Data.demo_text.append(line + Data.lineSeparator);
						}
						else if(!line.substring(pointIndex + 1, equalIndex).contains("_a")
								&&!line.substring(pointIndex + 1, equalIndex).contains("_t")){				
							String Name = line.substring(pointIndex + 1, equalIndex);
							String value = line.substring(equalIndex + 2);
							Var temp = new Var();
							temp.setName(Name);
							temp.setValue(value);
							animationStateSetList.info[setIndex].element[SN - 1].variable.add(temp);
						}
					}
					animationStateSetList.actualElementSize = elementIndex;
					setIndex++;
				}

				else if(stateBegin) {//begins with another state set,not "  -> State: x.x <-"
					stateBegin = false;
					for(int i=0;i < animationStateSetList.actualElementSize;i++){
						for(int j=0;j<animationStateSetList.info[setIndex - 1].element[i].variable.size();j++){
							Var temp = new Var();
							temp.setValue(animationStateSetList.info[setIndex - 1].element[i].variable.get(j).getValue());
							temp.setName(animationStateSetList.info[setIndex - 1].element[i].variable.get(j).getName());
							animationStateSetList.info[setIndex].element[i].variable.add(temp);
						}
					}
					while(!line.startsWith("  -> State:")) {
						//int _index = line.indexOf('_');
						int _index = line.lastIndexOf('_');
						int pointIndex = line.indexOf('.');
						int equalIndex = line.indexOf('=');
						int SN = 0;
						//states that have changed
						if(line.contains("state")) {//only include _x.state
							SN = Integer.parseInt(line.substring(_index + 1, pointIndex));
							String state = line.substring(equalIndex + 2);
							animationStateSetList.info[setIndex].element[SN - 1].SN = SN;
							animationStateSetList.info[setIndex].element[SN - 1].state = state;
							animationStateSetList.info[setIndex].element[SN - 1].isChanged = true;
						}
						else if(!line.contains("_a")&&!line.contains("_t")){
							//SN may cause exception
							SN = Integer.parseInt(line.substring(pointIndex - 1, pointIndex));
							String name = line.substring(pointIndex + 1, equalIndex);
							String value = line.substring(equalIndex + 2);
							int index = animationStateSetList.info[setIndex].element[SN - 1].getIndex(name);
							animationStateSetList.info[setIndex].element[SN - 1].variable.get(index).setValue(value);
						}
						line = output.readLine();
						if(line == null)//result is over
							break;
						if(line.startsWith("  -> State:"))
							{stateBegin = true;}
						if(line.startsWith("-- specification"))
							{break;}
					}
					
					for(int i = 0;i < elementIndex;i++){//state not changed
						if(!animationStateSetList.info[setIndex].element[i].isChanged){
							animationStateSetList.info[setIndex].element[i].SN = animationStateSetList.info[setIndex - 1].element[i].SN;
							animationStateSetList.info[setIndex].element[i].state = animationStateSetList.info[setIndex - 1].element[i].state;
						}
						animationStateSetList.info[setIndex].element[i].isChanged = false;
					}
					setIndex++;
					if(line == null){
						animationStateSetList.actualListSize = setIndex;
//						InfoList.add(assl);
						return ;
					}
				}//stateBegin condition
		}//the outermost loop
	}

	public boolean check() throws IOException {
		Data.check_text.setText("Checking..." + Data.lineSeparator + "Please wait" + Data.lineSeparator);
		String cmd = "/home/peter/NuSMV/NuSMV-2.6.0-Linux/bin/NuSMV ./data/tst/Fsm/FUR.txt";
//		System.out.println(cmd);	
		Data.check_text.append(cmd + Data.lineSeparator + Data.lineSeparator);
		boolean noError = false;
		boolean result = true;
		boolean outputBegin = false;
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			BufferedReader output = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while((line = output.readLine()) != null) {
				if(line.startsWith("-- specification")) {
					noError = true;
					outputBegin = true;
					if(line.endsWith("false"))result = false;
				}
				if(outputBegin)
					Data.check_text.append(line + Data.lineSeparator);
				//System.out.println(line);
			}
		} catch (IOException e) {
		e.printStackTrace();
		}
		assert(noError == true);
		//simulation
		simulate();
		return result;
	}
}