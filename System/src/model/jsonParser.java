package model;
import java.io.*;
import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import Common.Action;
import Common.Assignment;
import Common.Dynamic;
import Common.Furniture;
import Common.FurnitureInfo;
import Common.Info;
import Common.Relation;
import Common.State;
import Common.Transition;
import Common.Trigger;
import Common.Variable;
import Common.sigVar;
import Common.transNext;


public class jsonParser {

	private static Boolean Debug = false;
	
	public static Furniture JsonParserObject(String filepath, int loc_x, int loc_y) throws FileNotFoundException {
		FileReader reader = new FileReader(new File(filepath));
		JsonReader jsonReader = Json.createReader(reader);
		JsonObject object = jsonReader.readObject();
		
		JsonObject Device = object.getJsonObject("Device");
		Furniture fur = new Furniture();
		
		// basic information
		fur.furname = new String();
		fur.furname = Device.getString("Type");
		fur.initState = new String();
		fur.initState = Device.getString("InitState");
		fur.furnitureInfo = new FurnitureInfo();
		fur.furnitureInfo.loc_x = loc_x;
		fur.furnitureInfo.loc_y = loc_y;

		//TODO: modify json files, add picpath
		fur.furnitureInfo.pic = new String();
		//fur.furnitureInfo.pic = Device.getString("IMG");
		fur.furnitureInfo.path = new String();
		fur.furnitureInfo.path = filepath;
		fur.furnitureInfo.description = new String();
		fur.furnitureInfo.description = Device.getString("Description");
		
		// init ArrayList
		fur.sigArr = new ArrayList<sigVar>();
		fur.nextSta = new ArrayList<transNext>();
		
		if(Debug) {
			System.out.println("SN: " + fur.SN);
			System.out.println("name: " + fur.furname);
			System.out.println("initState: " + fur.initState);
			System.out.println("description: " + fur.furnitureInfo.description);
			System.out.println();
		}
		
		// internal variable array
		JsonArray interVari = Device.getJsonArray("InternalVari");
		int num_interVari = interVari.size();
		fur.variArr = new Variable[num_interVari];
		for(int i = 0; i < num_interVari; ++i) {
			JsonObject variable = interVari.getJsonObject(i);
			fur.variArr[i] = new Variable();
			fur.variArr[i].nextArr = new ArrayList<transNext>();
			fur.variArr[i].name = new String();
			fur.variArr[i].name = variable.getString("Name");
			String tmp = variable.getString("Default");
			if(tmp.length() > 0)
				fur.variArr[i].init = Integer.parseInt(tmp);
			else
				fur.variArr[i].init = Integer.MAX_VALUE;
			
			String range = variable.getString("Range");
			String []low_high = new String[2];
			low_high = range.split("\\..", 2);
			fur.variArr[i].lowBond = Integer.parseInt(low_high[0]);
			fur.variArr[i].highBond = Integer.parseInt(low_high[1]);
			fur.variArr[i].pub = Boolean.parseBoolean(variable.getString("public"));
			
			if(Debug) {
				System.out.println("variArr[" + i + "].init: " + fur.variArr[i].init);
				System.out.println("variArr[" + i + "].name: " + fur.variArr[i].name);
				System.out.println("variArr[" + i + "].public: " + fur.variArr[i].pub);
				System.out.println("variArr[" + i + "].low: " + fur.variArr[i].lowBond);
				System.out.println("variArr[" + i + "].high: " + fur.variArr[i].highBond);
				System.out.println();
			}
		}
		
		// working start array
		JsonArray stateArr = Device.getJsonArray("WorkingState");
		int num_stateArr = stateArr.size();
		fur.stateArr = new State[num_stateArr];
		for(int i = 0; i < num_stateArr; ++i) {
			fur.stateArr[i] = new State();
			JsonObject workingState = stateArr.getJsonObject(i);
			fur.stateArr[i].name = workingState.getString("Name");
			fur.stateArr[i].invariant = workingState.getString("Invariant");
			fur.stateArr[i].description = workingState.getString("Description");
			
			JsonArray dynamic = workingState.getJsonArray("Dynamic");
			int num_dyna = dynamic.size();
			fur.stateArr[i].dynamic = new Dynamic[num_dyna];
			for(int j = 0; j < num_dyna; ++j) {
				JsonObject dyn_obj = dynamic.getJsonObject(j);
				fur.stateArr[i].dynamic[j] = new Dynamic();
				fur.stateArr[i].dynamic[j].name = dyn_obj.getString("VariableName");
				fur.stateArr[i].dynamic[j].rate = dyn_obj.getString("ChangeRate");
				
				if(Debug) {
					System.out.println("stateArr[" + i + "].dynamic[" + j + "].name: " + fur.stateArr[i].dynamic[j].name);
					System.out.println("stateArr[" + i + "].dynamic[" + j + "].rate: " + fur.stateArr[i].dynamic[j].rate);
				}
			}
			
			if(Debug) {
				System.out.println("stateArr[" + i + "].name: " + fur.stateArr[i].name);
				System.out.println("stateArr[" + i + "].invariant: " + fur.stateArr[i].invariant);
				System.out.println("stateArr[" + i + "].description: " + fur.stateArr[i].description);
				System.out.println();
			}
		}
		
		// trasition array
		JsonArray transArr = Device.getJsonArray("Transitions");
		int num_trans = transArr.size();
		fur.transArr = new Transition[num_trans];
		for(int i = 0; i < num_trans; ++i) {
			fur.transArr[i] = new Transition();
			JsonObject trans = transArr.getJsonObject(i);
			fur.transArr[i].name = trans.getString("Name");
			fur.transArr[i].begState = trans.getString("StartState");
			fur.transArr[i].endState = trans.getString("EndState");
			fur.transArr[i].signal = Boolean.parseBoolean(trans.getString("Signal"));
			
			String trig = trans.getString("Trigger");
			
			if(trig.length() != 0) {
				fur.transArr[i].trigger = new Trigger();
				if(trig.contains("<"))
					fur.transArr[i].trigger.rel = Relation.Smaller;
				else if(trig.contains(">"))
					fur.transArr[i].trigger.rel = Relation.Larger;
				else
					fur.transArr[i].trigger.rel = Relation.Equal;
				String []tmp = trig.split("<|>|==");

				fur.transArr[i].trigger.var = tmp[0];
				fur.transArr[i].trigger.value = tmp[1];
				
				if(Debug) {
					System.out.println("transArr[" + i + "].var: " + fur.transArr[i].trigger.var);
					System.out.println("transArr[" + i + "].rel: " + fur.transArr[i].trigger.rel);
					System.out.println("transArr[" + i + "].value: " + fur.transArr[i].trigger.value);
				}
			}
			
			JsonArray assArr = trans.getJsonArray("Assignments");
			int num_ass = assArr.size();
			fur.transArr[i].asgArr = new Assignment[num_ass];
			for(int j = 0; j < num_ass; ++j) {
				JsonObject ass = assArr.getJsonObject(j);
				fur.transArr[i].asgArr[j] = new Assignment();
				fur.transArr[i].asgArr[j].var = ass.getString("Item");
				fur.transArr[i].asgArr[j].valRst = ass.getString("Value");
				
				if(Debug) {
					System.out.println("transArr[" + i + "].asgArr[" + j + "].var: " + fur.transArr[i].asgArr[j].var);
					System.out.println("transArr[" + i + "].asgArr[" + j + "].valRst: " + fur.transArr[i].asgArr[j].valRst);
				}
			}
			
			if(Debug) {
				System.out.println("transArr[" + i + "].name: " + fur.transArr[i].name);
				System.out.println("transArr[" + i + "].begState: " + fur.transArr[i].begState);
				System.out.println("transArr[" + i + "].endState: " + fur.transArr[i].endState);
				System.out.println("transArr[" + i + "].signal: " + fur.transArr[i].signal);
				System.out.println();
			}
		}
		
		// api array
		JsonArray actionArr = Device.getJsonArray("API");
		int num_api = actionArr.size();
		fur.actionArr = new Action[num_api];
		for(int i = 0; i < num_api; ++i) {
			fur.actionArr[i] = new Action();
			JsonObject api = actionArr.getJsonObject(i);
			fur.actionArr[i].name = api.getString("Name");
			fur.actionArr[i].begState = api.getString("StartState");
			fur.actionArr[i].endState = api.getString("EndState");
			fur.actionArr[i].signal = Boolean.parseBoolean(api.getString("Signal"));
			
			String trig = api.getString("Trigger");
			if(trig.length() != 0) {
				fur.actionArr[i].trigger = new Trigger();
				if(trig.contains("<"))
					fur.actionArr[i].trigger.rel = Relation.Smaller;
				else if(trig.contains(">"))
					fur.actionArr[i].trigger.rel = Relation.Larger;
				else
					fur.actionArr[i].trigger.rel = Relation.Equal;
				String []tmp = trig.split("<|>|==");

				fur.actionArr[i].trigger.var = tmp[0];
				fur.actionArr[i].trigger.value = tmp[1];
				
				if(Debug) {
					System.out.println("actionArr[" + i + "].var: " + fur.actionArr[i].trigger.var);
					System.out.println("actionArr[" + i + "].rel: " + fur.actionArr[i].trigger.rel);
					System.out.println("actionArr[" + i + "].value: " + fur.actionArr[i].trigger.value);
				}
			}
			
			JsonArray assArr = api.getJsonArray("Assignments");
			int num_ass = assArr.size();
			fur.actionArr[i].asgArr = new Assignment[num_ass];
			for(int j = 0; j < num_ass; ++j) {
				JsonObject ass = assArr.getJsonObject(j);
				fur.actionArr[i].asgArr[j] = new Assignment();
				fur.actionArr[i].asgArr[j].var = ass.getString("Item");
				fur.actionArr[i].asgArr[j].valRst = ass.getString("Value");
				
				if(Debug) {
					System.out.println("actionArr[" + i + "].asgArr[" + j + "].var: " + fur.actionArr[i].asgArr[j].var);
					System.out.println("actionArr[" + i + "].asgArr[" + j + "].valRst: " + fur.actionArr[i].asgArr[j].valRst);
				}
			}
			
			if(Debug) {
				System.out.println("actionArr[" + i + "].name: " + fur.actionArr[i].name);
				System.out.println("actionArr[" + i + "].begState: " + fur.actionArr[i].begState);
				System.out.println("actionArr[" + i + "].endState: " + fur.actionArr[i].endState);
				System.out.println("actionArr[" + i + "].signal: " + fur.actionArr[i].signal);
				System.out.println();
				System.out.println();
			}
		}
		Info.F_Array.add(fur);
		return fur;
	}
}