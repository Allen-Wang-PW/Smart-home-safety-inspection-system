package model;

import java.util.Scanner;

import Common.*;

import java.io.PrintWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

enum RuleType {Variable,Signal};

class LhaRange {//For example,temperature>30
	int ruleNo;
	RuleType rt;//variable
	String init;//30
	int lowBond;//-5
	int highBond;//40
	String name;//"para_0"
	String condition;//"temperature>30"
	String temp;//"temperature>"
	String changed;
}

public class LhaFix {
	private static String MenShen = "/home/peter/Desktop/MenShen";
	private static String MenShenBachPath = "/home/peter/Desktop/MenShen/bin/bach";
	private static String bachPath = "/home/peter/Desktop/tool_IFTTT/src/bach";
	private static String redlogPath = "/home/peter/reduce-algebra/bin/redcsl"; 
	
	public String lhaPath ="/home/peter/Desktop/System/data/tst/Lha/FUR.txt"; 
	private String paraPath = "/home/peter/Desktop/System/data/tst/Lha/para.txt";
	private String smtPath = "/home/peter/Desktop/System/data/tst/Lha/smt.txt";
	//public String tempPath = "/home/peter/Desktop/tool_IFTTT/temp184824.bha";
	
	private ArrayList<String> para = new ArrayList<String>();
	private static ArrayList<String> output = new ArrayList<String>();
	
	private ArrayList<LhaRange> range = new ArrayList<LhaRange>(); 
	
//	private animationStateSetList assl = new animationStateSetList();
	
	private static void doCmd(String cmd) throws IOException {//execute bach
		output.clear();
		Process p = Runtime.getRuntime().exec(cmd);
		BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		while((line = br.readLine()) != null)
			output.add(line);
		try {
			p.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		br.close();
		p.destroy();
	}
	
	//Only one line output
	private String docmd(String cmd) throws IOException {//execute bach
		Process p = Runtime.getRuntime().exec(cmd);
		BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line = br.readLine();
		br.close();
			return line;
	}
	
	
	//doFix returns "para_1000>=0 and 2*para_1000-53<0" or "false"
	private static String doFix(String bachOutput) throws IOException{
		output.clear();
		Process p = Runtime.getRuntime().exec(redlogPath);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
		BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		
		bw.flush();
		bw.write("load redlog;\n");
		bw.write("rlset ofsf;\n");
		
		bw.flush();	
		bw.write(bachOutput + "\n");
		bw.write("quit;\n");
		
		bw.flush();
		bw.write("exit\n");
		
		String constraint;
		while((constraint = br.readLine()) != null){
			if(constraint.contains("para"))break;
			if(constraint.contains("false"))return constraint;
		}
		String[] temp = constraint.split("\\r?\\n");
		constraint = "";
		for(int i = 0;i < temp.length;i++)
			if(temp[i].trim() != "")
				if(i == 0)
					constraint += temp[i];
				else
					constraint += " " + temp[i];
		return constraint;
	}
	
	public boolean CheckEnv() throws IOException{
		String cmd = MenShenBachPath + " /home/peter/Desktop/MenShen/config/bach_test.bha 10";
		String output = docmd(cmd);
		if(output.contains("BoundedVerification begin")){
			output = docmd(MenShen + "/bin/smt " + MenShen + "/config/smt_test.txt");
			if(output.contains("para_1000")){
				String path = redlogPath;
				Process p = Runtime.getRuntime().exec(path);
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
				BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
				
				bw.flush();
				bw.write("load redlog;\n");
				bw.write("rlset ofsf;\n");
				bw.flush();

				bw.write("quit;\n");
				bw.flush();
				bw.write("exit\n");
				
				String constraint;
				while((constraint = br.readLine()) != null){
					if(constraint.contains("1:"))return true;
				}
			}
		}
		return false;
	}
	
	@SuppressWarnings("unused")
	private Rule getRule(int ruleNo){
		return Info.R_Array.get(ruleNo);
	}
	
	private Variable getVariable(int SN,String varName){
		for(Variable v : Info.F_Array.get(SN - 1).variArr){//SN - 1 equals Furniture Index
			if(v.name.equals(varName))
				return v;
		}
		System.out.println("Fail to get Variable");
		assert(false);
		return null;
	}
	
	private String isReachable(){
		for(String s : output){
			if(s.equals("reachable"))
				return s;
			else if(s.equals("ok"))
				return s;
			else if(s.contains("rlqe all"))
				return s;
			else if(s.contains("Parse error")){
				System.out.println(s);
				assert(false);
			}
		}
		System.out.println("BACH PARSER ERROR");
		assert(false);
		return null;
	}
	
	public String doCheck(String path) throws IOException{//path => FUR.txt
		String cmd = bachPath + " " + path;
		String output = "";
		ArrayList<String> range;
		ArrayList<String> list_cons = new ArrayList<String>();
		while(true) {
			doCmd(cmd);
			output = isReachable();
			if(para.isEmpty() && output.equals("ok") || output.equals("reachable"))
				return output;
			if(!para.isEmpty() && output.equals("ok"))
				return "fine";
			
			//The following program is executed by paraFile
			String constraint = doFix(output);
			constraint = constraint.replace(" ", "").replace("(", " ( ").replace(")", " ) ").replace("and", " and ").replace("or", " or ");
			//constraint = constraint.replace(" ", "").replace("(", " ( ").replace(")", " ) ");
			if(constraint.equals("false")){
				System.out.println("redlog output is false");
				return "error";
			}
			list_cons.add(constraint);
			range = getParaRange(path);
			
			
			//writeSMT
			String str = "para:\r\n";
			for(int i = 0;i < range.size();i++)
				str += range.get(i) + "\r\n";
			
			str += "constraint:\r\n";
			for(int i = 0;i < list_cons.size();i++)
				str += list_cons.get(i) + "\r\n";
			File f = new File(smtPath);
			if(f.exists()){f.delete();f = new File(smtPath);}
			PrintWriter pw = new PrintWriter(f);
			pw.print(str);
			pw.close();
			
			
			para.clear();
			para = FindNewPara();
			if(para == null){
				System.out.println("smt can't find new parameter");
				return "error";
			}
			changeParaValue(path);//FUR.txt => FUR.txt
		}
	}
	
	private ArrayList<String> FindNewPara() throws IOException{
		ArrayList<String> result = new ArrayList<String>();
		//String cmd = MenShen + "/bin/smt /home/peter/Desktop/MenShen/config/smt_test.txt";
		//String cmd = MenShen + "/bin/smt data/tst/Lha/smt.txt";
		String cmd = MenShen + "/bin/smt " + smtPath; 
		doCmd(cmd);
		
		if(!output.get(0).equals("error")){
			for(int i = 0;i < output.size();i++)
				result.add(output.get(i));
			return result;
		}
		return null;
	}
	
	private void changeParaValue(String path) throws FileNotFoundException{
		File f = new File(path);
		File temp = new File("data/tst/Lha/temp.txt");
		Scanner input = new Scanner(f);
		PrintWriter pw = new PrintWriter(temp);
		for(int i = 0;((i < para.size()) ||  input.hasNext());i++){
			String p = "qwertyuiop", v = "qwertyuiop";//p,v needs to be initialized randomly
			if(i < para.size()){
				p = para.get(i).split("\t")[0];
				v = para.get(i).split("\t")[1];
				int _index = p.indexOf('_');
				int index = Integer.parseInt(p.substring(_index + 1));
				range.get(index).changed = v;
			}
			
			while(input.hasNextLine()){
				String line = input.nextLine();
				if(line.contains(p + ":")) {
					String pre = line.substring(0, line.indexOf(':') + 1);
					String last = line.substring(line.indexOf("in") - 1);
					line = pre + v + last;
					pw.println(line);
					break;
				}
				else
					pw.println(line);
			}
		}
		input.close();
		pw.close();
		f.delete();
		temp.renameTo(f);
	}
	
	private ArrayList<String> getParaRange(String path) throws IOException{
		ArrayList<String> result = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(new File(path)));
		ArrayList<String> array = new ArrayList<String>();
		boolean begin = false;
		String line;
		while((line = br.readLine()) != null)
			array.add(line);
		for(int j = 0;j < array.size();j++){
			if(array.get(j).contains("relation:")){
				begin = false;
				break;
			}
			if(begin) {
				String temp = "";
				//pre = "para_0",last = "0\t50"
				String pre = array.get(j).substring(0, array.get(j).indexOf(':'));
				String last = array.get(j).substring(array.get(j).indexOf('[') + 1, array.get(j).indexOf(']')).replace(',', '\t');
				String value = array.get(j).substring(array.get(j).indexOf(':') + 1, array.get(j).indexOf("in")).trim();
				temp = pre + "\t" + last + "\t" + value;
				result.add(temp);
			}
			if(array.get(j).equals("variable"))
				begin = true;
		}
		br.close();
		return result;
	}
	
	private void initDefaultRange() {
		int paraNo = 0;//equals ruleNo
		for(Rule rule : Info.R_Array){
			LhaRange r = new LhaRange();
			r.ruleNo = paraNo;
			if(rule.sigTrig!=null){//Needs extral process
				r.rt = RuleType.Signal;
				r.condition = rule.sigTrig;
				//TODO
			}
			else {
				r.condition = rule.trigger.var + rule.trigger.rel.getStr_l() + rule.trigger.value;//"smoke_level==19"
				r.temp = rule.trigger.var + rule.trigger.rel.getStr_l();//"smoke_level=="
				r.rt = RuleType.Variable;//variable
				r.init = rule.trigger.value;//"19"
				r.name = "para" + "_" +String.valueOf(paraNo);//"para_0"
				r.highBond = getVariable(rule.srcSN,rule.trigger.var).highBond;//50(int)
				r.lowBond = getVariable(rule.srcSN,rule.trigger.var).lowBond;//0(int)
			}
			paraNo++;
			range.add(r);
		}
	}
	private int getParaIndex(String line){
		for(int i = 0;i < range.size();i++){
			if(line.contains(range.get(i).condition))//condition can't be null
				return i;
		}
		//System.out.println("Fail to get parameter NO");
		//assert(false);
		return -1;
	}
	
	private void initParaFile() throws IOException {
		File src = new File(lhaPath);//Not para File
		File dst = new File(paraPath);//initial paraFile in this method
		BufferedReader br = new BufferedReader(new FileReader(src));
		BufferedWriter bw = new BufferedWriter(new FileWriter(dst));
		String line;
		boolean Begin = false;//add "para_1000:73 in [0,150];"
		//boolean afterBegin = false;//"temperature>30" => "temperature>para_0"
		while((line = br.readLine()) != null){
			int i;
			String paraLine = line;
			if(Begin){
				for(LhaRange lr : range){
					if(lr.rt == RuleType.Variable)
						bw.write(lr.name + ":" + lr.init +" in " + "[" + String.valueOf(lr.lowBond) + "," + String.valueOf(lr.highBond) + "];\n");
					//Signal rules don't need to be changed
				}
			}
			
			if(line.contains("relation:"))
				Begin = false;
			if(line.contains("variable"))
				Begin = true;
			
			if((i = getParaIndex(line)) != -1&&!line.contains("forbidden"))
				paraLine = line.replace(range.get(i).condition, range.get(i).temp + range.get(i).name);
			
			bw.write(paraLine + "\n");
		}
		br.close();
		bw.close();
		//src.delete();
		//dst.renameTo(src);
	}
	
	
	public void Fix() throws IOException{
		Data.fix_text.setText("Fixing..." + Data.lineSeparator + "Please wait" + Data.lineSeparator);
		if(!CheckEnv()){
			Data.fix_text.append("The environment needed for checking not exists" + Data.lineSeparator);
			assert(false);
		}
		String firstTime = doCheck(lhaPath);
		if(firstTime.equals("ok")){
			Data.fix_text.append(Data.lineSeparator + "The Lha model is safe" + Data.lineSeparator);
		}
		else if(firstTime.equals("error")){//not possible
//			System.out.println("The Lha model can't be fixed");
			Data.fix_text.append(Data.lineSeparator + "The Lha model can't be fixed" + Data.lineSeparator);
		}
		// ??????
		else if(firstTime.equals("fine")){//temp184824.bha for testing
//			System.out.println("Test temp184824.bha Successfully");
			Data.fix_text.append("Test temp184824.bha Successfully" + Data.lineSeparator);
		}
		else {//"reachable"
			//simulate();
			//System.out.println("Simulation exits");
			//Lha_complete and Lha_case4
			initDefaultRange();
			initParaFile();//para FUR.txt => para.txt		
			if(doCheck(paraPath).equals("error")) {
//				System.out.println("The Lha model can't be fixed");
				Data.fix_text.append(Data.lineSeparator + "The Lha model can't be fixed" + Data.lineSeparator);
			}
			else { 
//				System.out.println("The Lha model has been fixed successfully");
				Data.fix_text.append("Original conflicting rules:" + Data.lineSeparator);
				// print original rules
				Data.fix_text.append(Data.lineSeparator);
				Data.fix_text.append("Recommended modification as follow:" + Data.lineSeparator);
				printSuggestion();
				Data.fix_text.append(Data.lineSeparator);
			}
		}
			//temp184824.bha
			/*if(doCheck(tempPath).equals("error"))
				System.out.println("The Lha model can't be fixed");
			else System.out.println("The Lha model has been fixed successfully");*/
	}

	public void printSuggestion(){
		for(LhaRange lr : range){
			if(!lr.changed.equals(lr.init)){
				Rule r = Info.R_Array.get(lr.ruleNo);
				String rule = "IF " + Info.getFurName(r.srcSN);
				if (r.sigTrig == null) {
					rule = rule + "." +
							r.trigger.var + " " +
							r.trigger.rel.getStr_f() + " " +
							lr.changed;
					r.newTrigger = new Trigger();
					
					r.newTrigger.var = new String(r.trigger.var);
					r.newTrigger.rel = r.trigger.rel;
					r.newTrigger.value = new String(lr.changed);
				}
				else {//warning,this can't be triggered
					rule = rule + "." +
							r.sigTrig;
				}
				rule = rule + " THEN " +
						Info.getFurName(r.dstSN) + "." +
						r.dstAct;
				Data.fix_text.append(rule + Data.lineSeparator);
			}
		}
	}
}
