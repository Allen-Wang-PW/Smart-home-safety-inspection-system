package Common;

public class Transition {
	public String name;			// transition名
	public String begState;		// 初始状态
	public String endState;		// 终止状态
	public Trigger trigger;		// 触发事件
	public Assignment[] asgArr;	// 赋值
	public Boolean signal;		// 是否发出信号
}
