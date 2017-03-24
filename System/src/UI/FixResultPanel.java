package UI;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;

import Common.Data;
import Common.Info;
import Common.Rule;

@SuppressWarnings("serial")
public class FixResultPanel extends JPanel {
	public JButton jbOK;
	public JButton jbRefix;
	
	public FixResultPanel() {
		setLayout(null);		
		
		Data.fix_text = new JTextArea();
		
		JScrollPane scroller = new JScrollPane(Data.fix_text);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroller.setBounds(3, 0, 890, 410);
		scroller.setBorder(new TitledBorder("Fixing Suggestion"));

		Data.fix_text.setFont(new Font("Courier", Font.BOLD, 13));
		Data.fix_text.setEditable(false);
		Data.fix_text.setText("");
		
		add(scroller);
		
		jbOK = new JButton("OK");
		jbOK.setBounds(660, 420, 70, 30);
		jbOK.addActionListener(new okButtonListener());
		jbOK.setToolTipText("Make sure to change the rules to the recommended ones.");
		
		jbRefix = new JButton("Refix");
		jbRefix.setBounds(780, 420, 70, 30);
		jbRefix.addActionListener(new refixButtonListener());

		add(jbOK);
		add(jbRefix);
		
		jbOK.setEnabled(false);
		jbRefix.setEnabled(false);
	}
	
	class okButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			//TODO accept the fix suggestion and modify rules truely.
			for(Rule rule: Info.R_Array) {
				if(rule.newSigTrig != null) {
					rule.sigTrig = rule.newSigTrig;
					Data.display.refresh_rulelist();
					Data.IsModified = 1;
				}
				else if(rule.newTrigger != null) {
					rule.trigger = rule.newTrigger;
					Data.display.refresh_rulelist();
					Data.IsModified = 1;
				}
				else continue;
			}
			String tip = "The rule has been modified successfully!";
			JOptionPane.showMessageDialog(null, tip, "information", JOptionPane.INFORMATION_MESSAGE);
			Data.fix_text.append("The system has been fixed successfully" + Data.lineSeparator);
		}
	}

	class refixButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				//TODO some suggestion else
				Info.Fix();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
