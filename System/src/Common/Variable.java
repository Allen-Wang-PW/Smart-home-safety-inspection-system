package Common;

import java.util.ArrayList;

public class Variable {		// 内部变量，一定是int型数
	public String name;		// 变量名
	public boolean pub;		// 是否为外部可见
	public int init;		// 初始值
	public int lowBond;		// 最低取值
	public int highBond;	// 最高取值

	public ArrayList<transNext> nextArr;	// used to generate SMV file
}