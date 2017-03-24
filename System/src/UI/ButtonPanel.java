package UI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;

import Common.Data;
import Common.Info;

@SuppressWarnings("serial")
public class ButtonPanel extends JPanel {
	public JButton check;
	public JButton fix;
	public JButton demo;
	
	public ButtonPanel() {
		check = new JButton("Check");
		check.addActionListener(new checkButtonListener());
		fix = new JButton("Fix");
		fix.addActionListener(new fixButtonListener());
		demo = new JButton("Demo");
		demo.addActionListener(new demoButtonListener());

		setLayout(null);

		check.setBounds(150, 10, 100, 40);
		fix.setBounds(425, 10, 100, 40);
		demo.setBounds(700, 10, 100, 40);

		fix.setEnabled(false);
		demo.setEnabled(false);
		
		add(check);
		add(fix);
		add(demo);
	}
	
	class checkButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			Data.menu.operatepanel.pane.setSelectedIndex(1);
			try {
				Info.generate();
				Info.Check();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			Data.fix_text.setText("");
			demo.setEnabled(true);
//			fix.setEnabled(true);
		}
	}

	class fixButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			Data.menu.operatepanel.pane.setSelectedIndex(2);
			try {
				Info.Fix();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Data.fixresultpanel.jbOK.setEnabled(true);
			Data.fixresultpanel.jbRefix.setEnabled(true);
		}
	}
	
	class demoButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			Data.demoframe.setVisible(true);
		}
	}
}

