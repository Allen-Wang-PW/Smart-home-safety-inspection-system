package UI;
import javax.swing.*;

import java.awt.event.*;

@SuppressWarnings("serial")
public class AddDeviceButton extends JButton{
	public JButton createButton() {
		JButton addbutton = new JButton();
		addbutton.setText("Add Devices");
		addbutton.addActionListener(new ButtonListener());
		return addbutton;
	}
	
	class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			AddDevicePanel adp = new AddDevicePanel();
			JFrame frame = adp.createFrame();
			frame.setTitle("Add Devices");
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		}
	}
}
