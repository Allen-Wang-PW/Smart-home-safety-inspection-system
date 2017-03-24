package UI;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;

import Common.Data;

@SuppressWarnings("serial")
public class OperatePanel extends JPanel{
	public JTabbedPane pane;
	
	public OperatePanel() {
		pane = new JTabbedPane();

		Data.furniturepanel = new FurniturePanel();
		Data.checkresultpanel = new CheckResultPanel();
		Data.fixresultpanel = new FixResultPanel();
		
		pane.addChangeListener(new PaneListener());

		pane.addTab("Furniture", Data.furniturepanel);
		pane.setEnabledAt(0,true);
		pane.addTab("Check", Data.checkresultpanel);
		pane.setEnabledAt(1, true);
		pane.addTab("Fix", Data.fixresultpanel);
		pane.setEnabledAt(2, true);
		
		pane.setSize(900, 486);
		pane.setTabPlacement(JTabbedPane.TOP);
		pane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		add(pane);
	}
	
	class PaneListener implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			if (e.getSource() == pane) {
				pane.setPreferredSize(new Dimension(900, 486));
			}
		}
	}
}
