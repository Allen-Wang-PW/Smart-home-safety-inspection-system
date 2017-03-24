package Common;

import java.util.ArrayList;

public class Furniture {
	public int SN;							// 家具唯一的编号
	public String furname; 					// 家具名
	public String initState; 				// 家具初始状态，如没有为空字符串""

	public State[] stateArr; 				// 所有的状态列表
	public Variable[] variArr; 				// 所有的内部变量
	public Transition[] transArr;			// 所有的transition
	public Action[] actionArr; 				// 可以执行的action列表
	
	public FurnitureInfo furnitureInfo;		// 家具的属性信息
	public ArrayList<sigVar> sigArr;		// used to generate SMV file
	
	public ArrayList<transNext> nextSta;	// used to generate SMV file
}