package UI;

import javax.swing.*;

@SuppressWarnings("serial")
public class AddPanel extends JPanel {

	static JButton adddevicebutton;
	static JButton addrulebutton;
	static JButton addspecibutton;

	public JPanel createPanel() {
		JPanel panel = new JPanel();
				
		AddDeviceButton devicebutton = new AddDeviceButton();
		AddRuleButton rulebutton = new AddRuleButton();
		AddSpeciButton specibutton = new AddSpeciButton();
		
		adddevicebutton = devicebutton.createButton();
		addrulebutton = rulebutton.createButton();
		addspecibutton = specibutton.createButton();
		
		adddevicebutton.setBounds(50, 20, 150, 30);
		addrulebutton.setBounds(375, 20, 150, 30);
		addspecibutton.setBounds(700, 20, 150, 30);
		
		panel.setLayout(null);
		panel.add(adddevicebutton);
		panel.add(addrulebutton);
		panel.add(addspecibutton);
		
		return panel;
	}
}