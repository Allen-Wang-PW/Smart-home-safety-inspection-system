package Common;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.Timer;

import UI.AnimationPanel;
import UI.CheckResultPanel;
import UI.DemoFrame;
import UI.DemoFurPanel;
import UI.Display;
import UI.FixResultPanel;
import UI.FurniturePanel;
import UI.Menu;

/*
 * Main Bedroom (10, 175) ---- (300, 30)	40
 * Sub Bedroom (110, 380) ---- (300, 220)	40
 * Sitting Room (350, 30) ---- (580, 260)	40
 * Study (630, 30) ---- (760, 170)	40
 * Balcony (810, 30) ---- (850, 250)	40
 * Bathroom (350, 380) ---- (470, 300)	40
 * Dining room (610, 220) ---- (770, 300)	40
 * Kitchen (730, 380) ---- (850, 300)	40
 */

@SuppressWarnings("rawtypes")
public class Data {
	public static String[] devices;
	public static String[] jsonpaths;

	public static String[] locations = {"Default", "Main Bedroom", "Sub Bedroom", "Sitting room", "Study", "Balcony", "Bathroom", "Dining room", "Kitchen"};
	public static String[] numbers = { "1", "2", "3" };

//	public static int[] x_start = {-1, 10, 110, 350, 630, 810, 350, 610, 730};
//	public static int[] y_start = {-1, 175, 380, 30, 30, 30, 380, 220, 380};
//	public static int[] x = {-1, 10, 110, 350, 630, 810, 350, 610, 730};
//	public static int[] y = {-1, 175, 380, 30, 30, 30, 380, 220, 380};

	public static int[] x_start = {-1, 20, 110, 350, 630, 810, 350, 610, 730};
	public static int[] y_start = {-1, 30, 220, 30, 30, 30, 300, 220, 300};
	public static int[] x = {-1, 10, 110, 350, 630, 810, 350, 610, 730};
	public static int[] y = {-1, 175, 380, 30, 30, 30, 380, 220, 380};

	public static int[] x_end = {-1, 290, 270, 550, 750, 850, 470, 770, 850};
	public static int interval = 90;
	
	public static ArrayList<JComboBox> DevicesList = new ArrayList<JComboBox>();
	public static ArrayList<JComboBox> LocationsList = new ArrayList<JComboBox>();
	public static ArrayList<JComboBox> NumbersList = new ArrayList<JComboBox>();
	
	public static ArrayList<String> RuleList = new ArrayList<String>();
	public static ArrayList<String> SpeciList = new ArrayList<String>();
	public static ArrayList<imageInfo> imageList = new ArrayList<imageInfo>();

	public static ArrayList<Integer> modifyrows = new ArrayList<Integer>();
	public static ArrayList<Integer> modifysperows = new ArrayList<Integer>();
	
	public static ArrayList<Integer> column = new ArrayList<Integer>();
	public static int[][] modifycell = new int[100][8];
	public static int cell_cnt = 0;
	
//	public static ArrayList<Rule> rawRuleList = new ArrayList<Rule>();
	public static ArrayList<Rule> rawRuleList = Info.R_Array;
	public static FurniturePanel furniturepanel;
	public static CheckResultPanel checkresultpanel;
	public static FixResultPanel fixresultpanel;
	public static AnimationPanel animationpanel;
	public static DemoFurPanel demofurpanel;
	public static DemoFrame demoframe;
	
	public static Timer timer;
	public static Display display;
	public static Menu menu;
	public static int IsModified = 0;
	public static boolean IsEntered = false;
	
	public static JTextArea check_text;
	public static JTextArea fix_text;
	public static JTextArea demo_text;
	// system line-separator
	public static String lineSeparator = System.getProperty("line.separator");
	
	public static String[] module_Fsm = {"Default",
		"A holds forever",
		"A will happen later",
		"A never happens",
		"IF A happens, B should happen at the same time",
		"IF A happens, B should happen later",
		"IF A happens, B should happen later and last forever"
	};
	
	public static String[] Symbol = {"Default", ">", "=", "<"};
	public static String[] Equal = {"Default", "!=", "="};
	public static String[] Attribute = {"Default", "value", "signal", "state"};
	public static String[] Ranges = {"Default", "1", "2", "3", "4", "5"};
	
	final public static int MAXN = 10;
	public static int iterator = 0;
	
	public static String[] originName = {
		"AC_Cooler", "Alarm", "Door", "Human_Detector",
		"Light", "Outside_Light_Detector", "Rain_Detector",
		"Smoke_Detector", "Sun_Detector", "TV","Window"
	};
	
	public static String[] miniName = {
		"AC", "Alarm", "Door", "Human",
		"Light", "Lsensor", "Rain",
		"Smoke", "Sun", "TV", "Window"
	};
	
	public static int getLocationIndex(String str) {
		int result = 0;
		for(int i = 0; i < locations.length; i++) {
			if(str.equals(locations[i])) {
				result = i;break;
			}
		}
		return result;
	}
	
	public static String getShorterName(String name) {
		for(int i = 0; i < originName.length; i++) {
			if(name.equals(originName[i])) {
				return miniName[i];
			}
		}
		
		return name;
	}
}