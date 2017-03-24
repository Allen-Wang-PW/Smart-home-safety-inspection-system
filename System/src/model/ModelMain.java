/*package model;

import java.io.FileNotFoundException;

public class ModelMain {	
	private static int testCase = 4;
	public static void main(String[] args) {
		boolean initial = true;
		init(initial);
	}
	
	static void init(boolean initial) {
		try {
			if(initial) {
				Common.Info.setMode(Common.CheckType.Fsm);
				Io.initDevice();
				
				addDevice();
				addRule();
				addSpec();
				
//				Common.Info.Check();
				Io.saveIntoFile();
			}
			else {
				Io.initFromFile();
				Common.Info.setMode(Common.CheckType.Lha);
//				Common.Info.Check();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	static void addDevice() throws FileNotFoundException {
		if(testCase == 0 || testCase == 1) {
			ChangeInfo.addDevice("data/json/Demo1/Demo2_1.json", 0, 0);
			ChangeInfo.addDevice("data/json/Demo1/Demo2_2.json", 0, 0);
			ChangeInfo.addDevice("data/json/Demo1/Demo2_3.json", 0, 0);
		}
		else if(testCase == 2) {
			ChangeInfo.addDevice("data/json/Demo1/Demo2_1.json", 0, 0);
			ChangeInfo.addDevice("data/json/Door.json", 0, 0);
			ChangeInfo.addDevice("data/json/Demo2/Alarm.json", 0, 0);
		}
		else {
			ChangeInfo.addDevice("data/json/Sun_Detector.json", 0, 0);
			ChangeInfo.addDevice("data/json/Smoke_Detector.json", 0, 0);
			ChangeInfo.addDevice("data/json/Rain_Detector.json", 0, 0);
			ChangeInfo.addDevice("data/json/Outside_Light_Detector.json", 0, 0);
			
			ChangeInfo.addDevice("data/json/Kitchen_Window.json", 0, 0);
			ChangeInfo.addDevice("data/json/Bedroom_Window.json", 0, 0);
			ChangeInfo.addDevice("data/json/Livingroom_Window.json", 0, 0);
			
			ChangeInfo.addDevice("data/json/AC_Cooler.json", 0, 0);
			ChangeInfo.addDevice("data/json/Alarm.json", 0, 0);
			ChangeInfo.addDevice("data/json/TV.json", 0, 0);
			ChangeInfo.addDevice("data/json/Door.json", 0, 0);
			
			ChangeInfo.addDevice("data/json/Bedroom_Light.json", 0, 0);
			ChangeInfo.addDevice("data/json/Bathroom_Light.json", 0, 0);
			ChangeInfo.addDevice("data/json/Livingroom_Light.json", 0, 0);
			ChangeInfo.addDevice("data/json/Kitchen_Light.json", 0, 0);
			
			ChangeInfo.addDevice("data/json/Kitchen_Human_Detector.json", 0, 0);
			ChangeInfo.addDevice("data/json/Bathroom_Human_Detector.json", 0, 0);
			ChangeInfo.addDevice("data/json/Bedroom_Human_Detector.json", 0, 0);
			ChangeInfo.addDevice("data/json/Livingroom_Human_Detector.json", 0, 0);
		}
	}

	static void addRule() {
		if(testCase == 0) {
			ChangeInfo.addRule(1, "distance", Common.Relation.Equal, 30, 2, "BEGIN");
			ChangeInfo.addRule(1, "distance", Common.Relation.Equal, 15, 3, "BEGIN");
			ChangeInfo.addRule(2, "READY", 3, "PREPARE");
		}
		else if(testCase == 1) {
			ChangeInfo.addRule(1, "distance", Common.Relation.Equal, 60, 2, "BEGIN");
			ChangeInfo.addRule(1, "distance", Common.Relation.Equal, 25, 3, "BEGIN");
			ChangeInfo.addRule(2, "READY", 3, "PREPARE");
		}
		else if(testCase == 2) {
			ChangeInfo.addRule(1, "distance", Common.Relation.Equal, 30, 3, "TURN_OFF");
			ChangeInfo.addRule(1, "distance", Common.Relation.Equal, 28, 2, "OPEN");
		}
		else {
			ChangeInfo.addRule(8, "temperature", Common.Relation.Larger, 30, 8, "Activate");
			ChangeInfo.addRule(2, "smoke_level", Common.Relation.Equal, 19, 2, "Level_DOWN");
			ChangeInfo.addRule(3, "Find_Rain", 6, "CLOSE");
		}
	}
	
	static void addSpec() {
		if(testCase == 0) {
			ChangeInfo.addSpec("Bathtube_3.Waiting&GPS_1.Running{distance<15}&Water_Heater_2.Heating&Trigger0.Ready&Trigger1.Ready&Trigger2.Ready");
		}
		else if(testCase == 1) {
			ChangeInfo.addSpec("Bathtube_3.Ready{temperature<40}&GPS_1.Running{distance==0}&Water_Heater_2.Keeping&Trigger0.Ready&Trigger1.Ready&Trigger2.Ready");
		}
		else if(testCase == 2) {
			ChangeInfo.addSpec("ALARM_3.Checking&Door_2.Opened&GPS_1.Running&Trigger0.Ready&Trigger1.Ready");
		}
		else {
			if(Common.Info.getMode() == Common.CheckType.Fsm) {
				ChangeInfo.addSpec("CTLSPEC AG(_2.smoke_level<20);");
				ChangeInfo.addSpec("CTLSPEC AG(_8.temperature<30);");
			}
			else {
				ChangeInfo.addSpec("Smoke_Detector_2.LOW{smoke_level>20}&Trigger0.Ready&Trigger1.Ready&Trigger2.Ready");
				ChangeInfo.addSpec("AC_Cooler_8.Pause{temperature>15}");
			}
		}
	}
}
*/