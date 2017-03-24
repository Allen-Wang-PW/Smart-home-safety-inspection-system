package Common;

public class Action {
	public String name;				// action名称
	public String begState; 		// 初始State
	public String endState; 		// 终止State
	public Trigger trigger;			// 触发条件
	public Boolean signal;			// 是否发出信号
	public Assignment[] asgArr; 	// 执行transitions
}
