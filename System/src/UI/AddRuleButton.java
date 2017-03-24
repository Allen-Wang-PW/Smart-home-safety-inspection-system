package UI;
import javax.swing.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class AddRuleButton extends JButton{
	public JButton createButton() {
		JButton addbutton = new JButton();
		addbutton.setText("Add Rules");
		addbutton.addActionListener(new ButtonListener());
		return addbutton;
	}
	
	class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			JFrame frame;
			AddRulePanel arp = new AddRulePanel();
			frame = arp.createFrame();
			frame.setTitle("Add Rules");
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		}
	}
}
