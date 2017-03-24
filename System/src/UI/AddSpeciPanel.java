package UI;
import javax.swing.*;

@SuppressWarnings("serial")
public class AddSpeciPanel extends JFrame{
	public static JFrame frame;
	public JFrame createFrame() {
		if(Common.Info.getMode() == Common.CheckType.Fsm) {
			AddSpeciPanelFsm fsm = new AddSpeciPanelFsm();
			frame = fsm.createFrame();
		}
		else {
			AddSpeciPanelLha lhs = new AddSpeciPanelLha();
			frame = lhs.createFrame();
		}
		return frame;
	}
}
