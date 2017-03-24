package UI;
import java.awt.Font;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import Common.Data;

@SuppressWarnings("serial")
public class CheckResultPanel extends JPanel {
	public CheckResultPanel() {
		setLayout(null);
		
		Data.check_text = new JTextArea();
		
		JScrollPane scroller = new JScrollPane(Data.check_text);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroller.setBounds(3, 0, 890, 456);
		scroller.setBorder(new TitledBorder("Verification Result"));
		
		Data.check_text.setFont(new Font("Courier", Font.BOLD, 13));
		Data.check_text.setEditable(false);
		Data.check_text.setText("");

		add(scroller);
	}
}