package UI;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.border.LineBorder;

import Common.Data;
import UI.MyRenderer;
import model.ChangeInfo;
@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class RuleList extends JPanel{
	public DefaultListModel listmodel;
	public JList rulelist;
	
	public JPanel createPanel() {
		JPanel displayrule = new JPanel();
		
		listmodel = new DataModel(1);
		rulelist = new JList(listmodel);
		rulelist.setBorder(new LineBorder(Color.BLACK, 1));
		rulelist.addMouseListener(new MouseAdapter());

		JScrollPane scroller = new JScrollPane(rulelist);
		scroller.setBorder(BorderFactory.createTitledBorder("Rules"));
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		displayrule.setLayout(null);
		scroller.setBounds(10, 10, 470, 100);
		displayrule.add(scroller);
		return displayrule;
	}
	
	public void highlight(int[] rows) {
		rulelist.setCellRenderer(new MyRenderer(rows, Color.RED));
		rulelist.repaint();
		rulelist.validate();
	}
	
	public void highlight(int row) {
		rulelist.setCellRenderer(new MyRenderer(row, Color.RED));
		rulelist.repaint();
		rulelist.validate();
	}

	class DataModel extends DefaultListModel {
		DataModel(int flag) {
			if (flag == 1) {
//				System.out.println("R_Array size : " + Common.Info.R_Array.size());
				for (Common.Rule sta : Common.Info.R_Array) {
					String rule = "";
					try {
						rule = sta.createRule();
					} catch(Exception ex) {
						ex.printStackTrace();
					}
//					System.out.println("Rule : " + rule);
					addElement(rule);
				}
			}
		}
	}
	
	class MouseAdapter implements MouseListener {
		@SuppressWarnings("unused")
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			if (e.getClickCount() == 2) {
				String tip = "What do you want to do?";
				String options[] = {"Cancel", "Modify", "Delete"};
				int choose = JOptionPane.showOptionDialog(null, tip, "Warning", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
				int index = rulelist.locationToIndex(e.getPoint());
				String temp = listmodel.get(index).toString();
				if (choose == 2) {	// delete
					int no;
					for (no = 0; no < Common.Info.R_Array.size(); no++) {
						String rule = Common.Info.R_Array.get(no).createRule();
						if (rule.equals(temp)) {
							ChangeInfo.delRule(no);
							Data.display.refresh_rulelist();
							Data.display.refresh_specilist();
							Data.IsModified = 1;
							break;
						}
					}
				}
				else if (choose ==1) {	// modify
					int no;
					for (no = 0; no < Common.Info.R_Array.size(); no++) {
						String rulestr = Common.Info.R_Array.get(no).createRule();
						if (rulestr.equals(temp)) {
							break;
						}
					}
					Common.Rule rule = Common.Info.R_Array.get(no);
					ModifyFrame modify = new ModifyFrame();
//					System.out.println("no = " + no);
					JFrame mf = modify.createFrame(rule, no);
					Data.modifyrows.add(no);
				}
			}
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
}