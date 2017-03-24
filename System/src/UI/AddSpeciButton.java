package UI;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class AddSpeciButton extends JButton{
	public JButton createButton() {
		JButton addbutton = new JButton();
		addbutton.setText("Add Specifications");
		addbutton.addActionListener(new ButtonListener());
		return addbutton;
	}
	
	class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			
			AddSpeciPanel arp = new AddSpeciPanel();
			JFrame frame = arp.createFrame();
			frame.setTitle("Add Specifications");
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		}
	}
}
