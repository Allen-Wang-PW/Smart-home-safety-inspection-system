package Common;

import java.util.ArrayList;

public class animationStateSetList {
	public static animationStateSet[] info;
	public static int ListSize = 50;	
	public static int ElementSize = 50;
//	private static boolean DEBUG = false;
	
	public static int actualListSize = 0;
	public static int actualElementSize = 0;

	public static void initList() {
		info = new animationStateSet[ListSize];
		for(int i = 0;i < ListSize;i++) {
			info[i] = new animationStateSet();
			info[i].element = new animationElement[ElementSize];
			for(int j = 0;j < ElementSize;j++) {
				info[i].element[j] = new animationElement();
				info[i].element[j].variable = new ArrayList<Var>();
			}
		}
			
//		info[0].element[0].SN = 1;//on off
//		info[0].element[0].state="off";
//		info[0].element[1].SN = 2;//on off
//		info[0].element[1].state="off";
//		info[0].element[2].SN = 3; //on off
//		info[0].element[2].state="off";
//		info[0].element[3].SN = 4;//on off
//		info[0].element[3].state="off";
//		info[0].element[4].SN = 5;//on off
//		info[0].element[4].state="off";
//
//		info[1].element[0].SN = 1;
//		info[1].element[0].state="on";
//		info[1].element[1].SN = 2;
//		info[1].element[1].state="off";
//		info[1].element[2].SN = 3;
//		info[1].element[2].state="off";
//		info[1].element[3].SN = 4;
//		info[1].element[3].state="off";
//		info[1].element[4].SN = 5;
//		info[1].element[4].state="off";
//
//		info[2].element[0].SN = 1;
//		info[2].element[0].state="off";
//		info[2].element[1].SN = 2;
//		info[2].element[1].state="on";
//		info[2].element[2].SN = 3;
//		info[2].element[2].state="on";
//		info[2].element[3].SN = 4;
//		info[2].element[3].state="off";
//		info[2].element[4].SN = 5;
//		info[2].element[4].state="off";
//
//		info[3].element[0].SN = 1;
//		info[3].element[0].state="off";
//		info[3].element[1].SN = 2;
//		info[3].element[1].state="on";
//		info[3].element[2].SN = 3;
//		info[3].element[2].state="off";
//		info[3].element[3].SN = 4;
//		info[3].element[3].state="on";
//		info[3].element[4].SN = 5;
//		info[3].element[4].state="on";
//		
//		info[4].element[0].SN = 1;
//		info[4].element[0].state="off";
//		info[4].element[1].SN = 2;
//		info[4].element[1].state="off";
//		info[4].element[2].SN = 3;
//		info[4].element[2].state="off";
//		info[4].element[3].SN = 4;
//		info[4].element[3].state="off";
//		info[4].element[4].SN = 5;
//		info[4].element[4].state="on";
//
//		info[5].element[0].SN = 1;
//		info[5].element[0].state="on";
//		info[5].element[1].SN = 2;
//		info[5].element[1].state="off";
//		info[5].element[2].SN = 3;
//		info[5].element[2].state="on";
//		info[5].element[3].SN = 4;
//		info[5].element[3].state="off";
//		info[5].element[4].SN = 5;
//		info[5].element[4].state="on";
//
//		info[6].element[0].SN = 1;
//		info[6].element[0].state="on";
//		info[6].element[1].SN = 2;
//		info[6].element[1].state="off";
//		info[6].element[2].SN = 3;
//		info[6].element[2].state="on";
//		info[6].element[3].SN = 4;
//		info[6].element[3].state="off";
//		info[6].element[4].SN = 5;
//		info[6].element[4].state="off";
//
//		info[7].element[0].SN = 1;
//		info[7].element[0].state="off";
//		info[7].element[1].SN = 2;
//		info[7].element[1].state="off";
//		info[7].element[2].SN = 3;
//		info[7].element[2].state="on";
//		info[7].element[3].SN = 4;
//		info[7].element[3].state="on";
//		info[7].element[4].SN = 5;
//		info[7].element[4].state="off";
//
//		info[8].element[0].SN = 1;
//		info[8].element[0].state="off";
//		info[8].element[1].SN = 2;
//		info[8].element[1].state="on";
//		info[8].element[2].SN = 3;
//		info[8].element[2].state="off";
//		info[8].element[3].SN = 4;
//		info[8].element[3].state="on";
//		info[8].element[4].SN = 5;
//		info[8].element[4].state="on";
//
//		info[9].element[0].SN = 1;
//		info[9].element[0].state="off";
//		info[9].element[1].SN = 2;
//		info[9].element[1].state="on";
//		info[9].element[2].SN = 3;
//		info[9].element[2].state="off";
//		info[9].element[3].SN = 4;
//		info[9].element[3].state="off";
//		info[9].element[4].SN = 5;
//		info[9].element[4].state="on";
	}
}
