package model;

import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Pattern;

import Common.*;

import java.util.regex.Matcher;

enum VariableType {
	INT,BOOLEAN,ENUM
}

enum BooleanType {
	TRUE,BOTH,FALSE
}

class Range {
	int ruleNo;
	String name;//necessary(variable name)
	VariableType type;//necessary
	int lowBond;//int(can be set by users)
	int highBond;//int(can be set by users)
	
	BooleanType booleanType;//boolean(can be set by users)
	
	//TODO whether state is necessary for Range is not sure
	ArrayList<String> state;//enum
	
	String assign;//used for fix
	String init;//used for print suggestion
	//For INT or ENUM, init = value;
	//"30" for example
	
	//For BOOLEAN,init = sigTrig,because initial assignment is always true
	//"Find_Rain" for example
}

public class FsmFix {
	public ArrayList<Range> Range = new ArrayList<Range>();
	private Fsm fsm = new Fsm();
	
	private Boolean Debug = false;
	
	String furPath = "data/tst/Fsm/FUR.txt";
	String paraPath = "data/tst/Fsm/paraFur.txt";
	String testPath = "data/tst/Fsm/test.txt";
	
	private File paraFile;
	//private PrintWriter paraWriter;
	
	private File testFile;
	//private PrintWriter testWriter;
	
//	ArrayList<animationStateSetList> InfoList = new ArrayList<animationStateSetList>();
	
	public void setFsm(Fsm fsm){
		this.fsm = fsm;
	}
	
	public static boolean stringEndsWithDigital(String s){
		if(s.charAt(s.length() - 1) >= '0' && s.charAt(s.length() - 1) <= '9')
			return true;
		else return false;
	}

	private static boolean isNumeric(String str){//whether a string is made up by digital characters, such as "123"	"412342"
		Pattern pattern = Pattern.compile("(^-)?[0-9]*");
		Matcher isNum = pattern.matcher(str);
		return isNum.matches();
	}

	 Variable getVar(int SN,String varName) {
		for(Furniture f : Info.F_Array) {
			if(f.SN == SN){
				for(Variable v : f.variArr) {
					if(v.name.equals(varName))
						return v;
				}
				System.out.println("Variable not found");
			}
		}
		System.out.println("Furniture not found");
		assert(false);
		return null;
	}
	
	//set by users
	@SuppressWarnings("unused")
	private void setRange(int ruleNo, int lowbond, int highbond){
		Range range = new Range();
		//assert(r.type == VariableType.INT);
		Rule r = Info.R_Array.get(ruleNo);
		range.name = "rule" + ruleNo;
		range.init = r.trigger.value;
		range.highBond = highbond;
		range.lowBond = lowbond;
		range.type = VariableType.INT;
		Range.add(range);
	}
	
	@SuppressWarnings("unused")
	private void setRange(int ruleNo, BooleanType bt){
		Range range = new Range();
		range.name = "rule" + ruleNo;
		range.type = VariableType.BOOLEAN;
		//assert(r.type == VariableType.BOOLEAN);
		range.booleanType = bt;
		if(bt == BooleanType.FALSE)
			range.assign = "FALSE";
		else if(bt == BooleanType.TRUE)
		{range.assign = "TRUE";}
		range.init = Info.R_Array.get(ruleNo).sigTrig;
		Range.add(range);
	}
	
	//TODO(about VariableType.ENUM)
	/*private ArrayList<String> cast(String s){//not finished
		ArrayList<String> state = new ArrayList<String>();
		return null;
	}
	private void setRange(Range r, ArrayList<String> state){
		assert(r.type == VariableType.ENUM);
		for(String s : state){
			r.state.add(s);
		}
	}*/
	
//	@SuppressWarnings("unused")
	private void setDefaultRange() {
		int ruleNo = 0;
		for(Rule r : Info.R_Array) {
			Range range = new Range();
			range.ruleNo = ruleNo;
			if(r.sigTrig == null) {
				Variable v = getVar(r.srcSN,r.trigger.var);
				range.name = "rule" + ruleNo;
				//Furniture fur = Info.findFur(r.srcSN);
				//range.name =fsm.getName(fur, v);
				if(isNumeric(r.trigger.value)){
					range.type = VariableType.INT;
					range.lowBond = v.lowBond;
					range.highBond = v.highBond;
					range.init = r.trigger.value;
				}
				else {//By now, there is no enum variable type,So the following structure is not finished
					//state is not a kind of variable
					//TODO
					range.type = VariableType.ENUM;
				}
			}
			else {
				//boolean variable type default => TRUE not BOTH
				range.name = Info.R_Array.get(ruleNo).sigTrig;
				range.type = VariableType.BOOLEAN;
				range.booleanType = BooleanType.TRUE;
				range.init = Info.R_Array.get(ruleNo).sigTrig;
				range.assign = "TRUE";
				/*range.name = "rule" + ruleNo;
				range.type = VariableType.BOOLEAN;
				range.booleanType = BooleanType.BOTH;
				range.init = Info.R_Array.get(ruleNo).sigTrig;*/
			}
			Range.add(range);
			ruleNo++;
		}
	}

	private void paraRule() {
		int sn = 0;
		for(int i = 0;i < Info.R_Array.size(); i++) {
			Rule r = Info.R_Array.get(i);
			if(r.sigTrig == null){
				r.trigger.value = "rule" + String.valueOf(sn);//"rule0"
			}
			else {
				r.sigTrig = r.sigTrig + "_" + Range.get(i).booleanType + "_rule" + String.valueOf(sn);//"Find_Rain_BOTH_rule2"
			}
			sn++;
		}
	}	
	
//	private void NegAllSpec(){
//		for(int i  = 0;i < Info.S_Array.size();i++){
//			Spec s = Info.S_Array.get(i);
//			String specType = s.str.substring(0, 7);
//			String spec = s.str.substring(8);
//			s.str = specType + " !(" + spec + ")";
//		}
//	}
	
//	private Spec NegSpec(Spec s){//negate one specified spec
//		String specType = s.str.substring(0, 7);
//		String spec = s.str.substring(8);
//		s.str = specType + " !(" + spec + ")";
//		return s;
//	}
	
	//temporary
	private FsmSpec NegCombinedSpec(){//Combine Specs of the same type and negate
		FsmSpec combine = new FsmSpec();
		//combine.type = CheckType.Fsm;
		combine.str = new String();
		String specType = new String();
		for(int i = 0;i < Info.FsmArray.size();i++){
			FsmSpec s = Info.FsmArray.get(i);
			specType = s.str.substring(0, 7);
			String spec ="(" + s.str.substring(8) + ")";
			if(i != Info.FsmArray.size() - 1)
				combine.str = combine.str + spec + "&";
			else combine.str = combine.str + spec;
		}
		combine.str = specType + " !(" + combine.str + ")";
		return combine;
	}
	
	
	private static String negSpec(String Spec){
		int index = Spec.indexOf("SPEC");
		assert(index!=-1);
		String end = Spec.substring(index + 5);
		String start = Spec.substring(0, index + 4);
		String NegSpec = start + " " + "!(" + end + ")";
		return NegSpec;
	}
	
	
	private void outFrozenVar(PrintWriter paraWriter){
		paraWriter.println("FROZENVAR");
		for(Range r : Range){
			switch(r.type){
				case INT : 
					paraWriter.print("\t" + r.name + ": ");
					paraWriter.println(r.lowBond + ".." + r.highBond + ";");
					break;
				case BOOLEAN:{
					switch (r.booleanType){
					case TRUE:break;
					case FALSE:break;
					case BOTH:
						paraWriter.print("\t" + r.name + ": ");
						paraWriter.println("boolean;");
						break;
					default:
						assert(false);
						break;
					}
					break;
				}
				case ENUM : {
					paraWriter.print("{");
					int i;
					for(i = 0;i < r.state.size() - 1;i++){
						paraWriter.print(r.state.get(i) + ",");
					}
					paraWriter.println(r.state.get(i) + "};");
					break;
				}
				default:assert(false);break;
			}
		}
		paraWriter.print("\n");
	}
	
	private void paraR_Array() {
		for(int i = 0;i < Info.R_Array.size();i++) {
			Rule rule = Info.R_Array.get(i);
			
			Furniture src = Info.findFur(rule.srcSN);
			Furniture fur = Info.findFur(rule.dstSN);//fur is dst
			String con = new String();
			
			// TODO: rule set by user
			if(rule.sigTrig != null){
				if(stringEndsWithDigital(rule.sigTrig)){//"Find_Rain_BOTH_rule2"
					//int endIndex = rule.sigTrig.indexOf("_rule");
					//String sigTrig = rule.sigTrig.substring(0, endIndex);//"Find_Rain_BOTH"
					if(rule.sigTrig.indexOf("TRUE") != -1){//BooleanType is TRUE
						int subEndIndex = rule.sigTrig.indexOf("_TRUE");
						String subSigTrig = rule.sigTrig.substring(0, subEndIndex);
						con = fsm.getSig(rule.srcSN, subSigTrig) + "=TRUE";
					}
					else if(rule.sigTrig.indexOf("FALSE") != -1){//BooleanType is FALSE
						int subEndIndex = rule.sigTrig.indexOf("_FALSE");
						String subSigTrig = rule.sigTrig.substring(0, subEndIndex);
						con = fsm.getSig(rule.srcSN, subSigTrig) + "=FALSE";
					}
					else{//BooleanType is BOTH
						int ruleNo = Info.R_Array.indexOf(rule);
						int subEndIndex = rule.sigTrig.indexOf("_BOTH");
						String subSigTrig = rule.sigTrig.substring(0, subEndIndex);
						con = fsm.getSig(rule.srcSN, subSigTrig) + "=rule" + String.valueOf(ruleNo);
					}
				}
				else
					con = fsm.getSig(rule.srcSN, rule.sigTrig) + "=TRUE";
			}
			else
				con = fsm.getName(src, rule.trigger.var) + rule.trigger.rel.getStr_f() + rule.trigger.value;
			
			Action act = Info.findAction(fur, rule.dstAct);
			transNext next = new transNext();
			for(int j = 0;j < fur.nextSta.size();j++)
				if(fur.nextSta.get(j).ruleNo ==i){
					next = fur.nextSta.get(j);
					break;
				}
			next.state = act.begState;
			next.nextSta = act.endState;
			next.condition = con;

			if(act.trigger != null)
				next.condition += " & " + fsm.getName(fur, act.trigger.var) + act.trigger.rel.getStr_f() + act.trigger.value;
			//fur.nextSta.add(next);
			
			if(Debug)
				System.out.println(fur.furname + " " + next.state + " " + next.condition + " " + next.nextSta);
		}
	}
	
	private Range getRange(String name){//rule0,rule1,etc
		for(Range r : Range){
			if(r.name.equals(name))return r;
		}
		System.out.println(name + " Not found in Range");
		assert(false);
		return null;
	}
	
	private void fetchAssignment(String line){
		int equalIndex = line.indexOf('=');
		String name = line.substring(4, equalIndex - 1);
		Range r = getRange(name);
		r.assign = line.substring(equalIndex + 2);
	}
	
	//Neg the Spec in SMV
	private static void ReadSpec_NegSpec_WriteBackSpec(File file)throws IOException{
		BufferedReader in = new BufferedReader(new FileReader(file.getPath()));
		File tempfile = new File("data/tst/Fsm/temp.txt");
		PrintWriter output = new PrintWriter(tempfile);
		String line;
		while((line=in.readLine())!=null){
			if(line.indexOf("SPEC")!=-1){//line has the substring "SPEC"
				String Neg = negSpec(line);
				output.println(Neg);
			}
			else output.println(line);
		}
		in.close();
		output.close();
		if(file.exists())file.delete();
		tempfile.renameTo(file);
	}
	
	private void FileCopy(File dst,File src) throws IOException{
		//assert(dst.isFile()&&src.isFile());
		
		FileReader reader = new FileReader(src);
		FileWriter writer = new FileWriter(dst);
		int len = 0;
		while((len=reader.read())!=-1){
			writer.write(len);
		}
		writer.flush();
		
		reader.close();
		writer.close();
	}
	
	private void AppendParaAssignment() throws IOException {
		testFile = new File(testPath);
		FileCopy(testFile,paraFile);
		
		String path = testFile.getPath();
		textAppend(path,"\nASSIGN\n");
		for(Range r : Range){
			if(r.type == VariableType.BOOLEAN &&r.booleanType != BooleanType.BOTH)
				continue;
			else 
				textAppend(path,"init(" + r.name + "):=" + r.assign + ";\n");
		}
	}
	
	public void paraGenerate() throws FileNotFoundException {
		//setRange(0,20,30);//0 refers ruleNo, 20 refers to lowBond, 30 refers to highBond
		//setRange(1,10,30);//same as above
		
		//setRange(2,BooleanType.TRUE);//2 refers to ruleNo,BooleanType.TRUE refers to this rule's signal is true(can't be changed during fixing) 
		//setRange(2,BooleanType.BOTH);
		
		//setRange(0,10,30);
		//setRange(1,BooleanType.TRUE);
		//setRange(2,20,30);
		
		setDefaultRange();
		paraRule();
		
		paraR_Array();
		//fsm.initProc();
		paraFile = new File(paraPath);
		PrintWriter paraWriter = new PrintWriter(paraFile);
		fsm.outModule(paraWriter);
		fsm.outMain(paraWriter);
		
		//S_Array is not changed during fixing
		FsmSpec s = NegCombinedSpec();
		paraWriter.println(s.str);
		
		//NegAllSpec();
		//fsm.outSpec(paraWriter);
		
		outFrozenVar(paraWriter);
		
		paraWriter.close();
	}
	
	public boolean check(File file) {
		String cmd="/home/peter/NuSMV/NuSMV-2.6.0-Linux/bin/NuSMV " + file.getPath();
		System.out.println(cmd);
		boolean isSafe = true;
		boolean parserError = false;
		try {
			Process p = Runtime.getRuntime().exec(cmd); 
			BufferedReader output = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while((line = output.readLine()) != null){
				if(line.startsWith("-- specification")){
					parserError = true;
					if(line.endsWith("false")) isSafe = false;
				}
				if(line.startsWith("    rule"))fetchAssignment(line);
				//System.out.println(line);
			}
		} catch (IOException e) {
		e.printStackTrace();
		}
		
		assert(parserError);
		return isSafe;
	}
	
	public static void textAppend(String fileName,String content){
		try{
			FileWriter writer = new FileWriter(fileName,true);
			writer.write(content);
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	//Not sure for correctness
	//TODO
	@SuppressWarnings("unused")
	public String INVAR_Constraint(){
		String invar_constraint = "";
		assert(!Range.isEmpty());
		for(int i=0;i<Range.size();i++){
			int ruleNo = Range.get(i).ruleNo;
			String name = Range.get(i).name;
			String assign = Range.get(i).assign;
			String temp = name + "=" + assign;
			
			if(i == 0)invar_constraint = "(" + temp + ")";
			else invar_constraint = invar_constraint + "&" + "(" + temp + ")";
		}
		String CONSTRAINT = "INVAR " + "!(" + invar_constraint + ")";
		return CONSTRAINT;
	}
	
	private void resetR_Array(){
		//Assume all rule condition is parameterized
		//that is to say Info.R_Array.size() equals Range.size()
		for(int i = 0;i<Info.R_Array.size();i++){
			Range range = Range.get(i);
			Rule rule = Info.R_Array.get(i);
			if(rule.sigTrig == null){//INT,ENUM
				rule.trigger.value = range.init;
			}
			else
				rule.sigTrig = range.init;
		}
	}
	
	public void Fix() throws IOException {
		Data.fix_text.setText("Fixing..." + Data.lineSeparator + "Please wait" + Data.lineSeparator);
		paraGenerate();//FUR.txt => paraFUR.txt
		if(check(paraFile)) {
//			System.out.println("The Fsm model can't be fixed");
			Data.fix_text.append(Data.lineSeparator + "The Fsm model can't be fixed" + Data.lineSeparator);
			return;
		}
		else{
//			System.out.println("The System can be fixed");
			Data.fix_text.append(Data.lineSeparator + "The System may be fixed" + Data.lineSeparator);
			AppendParaAssignment();//Append Assignments to testFile(test.txt)
			ReadSpec_NegSpec_WriteBackSpec(testFile);//change test.txt only
			
			while(!check(testFile)){
//				System.out.println("Another fix is needed");
				Data.fix_text.append("Another fix is needed" + Data.lineSeparator);
				String constraint = INVAR_Constraint();//the constraint to be added to paraFUR.txt
				FsmFix.textAppend(paraFile.getPath(),"\n" + constraint + "\n");
				if(check(paraFile)){//check method can fetch parameter assignment
//					System.out.println("The System can't be fixed correctly");
					Data.fix_text.append("The System can't be fixed correctly" + Data.lineSeparator);
					return ;
				}
				else{
//					System.out.println("The System can be fixed");
					Data.fix_text.append("The System can be fixed" + Data.lineSeparator);
					AppendParaAssignment();
					ReadSpec_NegSpec_WriteBackSpec(testFile);
				}
			}
			//Fix Successfully
			
			//reset R_Array to inital value
			resetR_Array();
			
			Data.fix_text.append("Original conflicting rules:" + Data.lineSeparator);
			//TODO print original rules
			Data.fix_text.append(Data.lineSeparator);
			Data.fix_text.append("Recommended modification as follow:" + Data.lineSeparator);
			printSuggestion();
			Data.fix_text.append(Data.lineSeparator);
//			System.out.println("The System has been fixed Successfully\n");
			//Data.fix_text.append("The System has been fixed Successfully" + Data.lineSeparator);
		}
	}
	
	

	
	public void printSuggestion(){
		for(int i = 0;i < Range.size();i++){
			Range r = Range.get(i);
			Rule rule = Info.R_Array.get(i);
			switch(r.type){
			case INT:
				if(!r.assign.equals(r.init)){
					rule.newTrigger = new Trigger();
					
					rule.newTrigger.var = new String(rule.trigger.var);
					rule.newTrigger.rel = rule.trigger.rel;
					rule.newTrigger.value = new String(r.assign);
					
					String newRule = "IF " + Info.getFurName(rule.srcSN) + "." + rule.trigger.var + rule.trigger.rel.getStr_f() + r.assign + ", THEN " + Info.getFurName(rule.dstSN) + " " + rule.dstAct;
//					System.out.println(newRule);
					Data.fix_text.append(newRule + Data.lineSeparator);
				}
				break;
			case BOOLEAN:
				if(r.assign.equals("FALSE")){//By now ,this instance can't happen 
					String var = fsm.getSig(rule.srcSN, rule.sigTrig);//_3.find_rain_t
					String newRule = "IF " + var + "=" + "FALSE, THEN " + Info.getFurName(rule.dstSN) + " " + rule.dstAct;
//					System.out.println(newRule);
					Data.fix_text.append(newRule + Data.lineSeparator);
				}
				break;
			//TODO now there is no ENUM instance
			case ENUM:break;
			}
		}
	}
}
