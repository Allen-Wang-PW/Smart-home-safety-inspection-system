package UI;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.border.LineBorder;

import Common.Data;

@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class SpeciList extends JPanel{
	public DefaultListModel listmodel;
	public JList spelist;
	public JScrollPane scroller;
	public JPanel createPanel() {
		JPanel displayspeci = new JPanel();
		
		listmodel = new DataModel(1);
		spelist = new JList(listmodel);
		spelist.setBorder(new LineBorder(Color.BLACK, 1));
		spelist.addMouseListener(new MouseAdapter());
		
		scroller = new JScrollPane(spelist);
		if (Common.Info.getMode() == Common.CheckType.Fsm)
			scroller.setBorder(BorderFactory.createTitledBorder("Specifications_Fsm"));
		else 
			scroller.setBorder(BorderFactory.createTitledBorder("Specifications_Lha"));
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		displayspeci.setLayout(null);
		scroller.setBounds(10, 10, 470, 100);
		displayspeci.add(scroller);
		return displayspeci;
	}
	
	class DataModel extends DefaultListModel {
		DataModel(int flag) {
			if (flag == 1) {
				if (Common.Info.getMode() == Common.CheckType.Fsm) {
					for (Common.FsmSpec sta : Common.Info.FsmArray) {
						addElement(sta.spe);
					}
				}
				else {
					for (Common.LhaSpec sta : Common.Info.LhaArray) {
						String spe = sta.createSpe();
						addElement(spe);
					}
				}
			}
		}
	}
	
	class MouseAdapter implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			if (Common.Info.getMode() == Common.CheckType.Fsm) {
				if (e.getClickCount() == 2) {
					String tip = "What do you want to do?";
					String options[] = {"Cancel", "Modify", "Delete"};
					int choose = JOptionPane.showOptionDialog(null, tip, "Warning", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
//					System.out.println("choose : " + choose);
//					System.out.println("Click");
					int index = spelist.locationToIndex(e.getPoint());
//					System.out.println("index : " + index);
					String temp = listmodel.get(index).toString();
//					System.out.println("string : " + temp);
					if (choose == 2) {
						int no;
						for (no = 0; no < Common.Info.FsmArray.size(); no++) {
							String spe = Common.Info.FsmArray.get(no).spe;
							if (spe.equals(temp)) {
//								System.out.println("no : " + no);
								model.ChangeInfo.delSpecFsm(no);
								Common.Data.display.refresh_specilist();
								Data.IsModified = 1;
								break;
							}
						}
					}
					else if (choose == 1) {
						int no;
						for (no = 0; no < Common.Info.FsmArray.size(); no++) {
							String spe = Common.Info.FsmArray.get(no).spe;
							if (spe.equals(temp)) {
								break;
							}
						}
						Common.FsmSpec spec = Common.Info.FsmArray.get(no);
						ModifyFsmSpecFrame modifyspe = new ModifyFsmSpecFrame();
						@SuppressWarnings("unused")
						JFrame mf = modifyspe.createFrame(spec);
						Data.modifysperows.add(no);
						model.ChangeInfo.delSpecFsm(no);
					}
				}
			}
			else {
				if (e.getClickCount() == 2) {
					String tip = "Do you want to delete this specification?";
					String options[] = {"No", "Yes"};
					int choose = JOptionPane.showOptionDialog(null, tip, "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
					System.out.println("choose : " + choose);
					System.out.println("Click");
					int index = spelist.locationToIndex(e.getPoint());
					System.out.println("index : " + index);
					String temp = listmodel.get(index).toString();
					System.out.println("string : " + temp);
					if (choose == 1) {
						int no;
						for (no = 0; no < Common.Info.LhaArray.size(); no++) {
							String spe = Common.Info.LhaArray.get(no).createSpe();
							if (spe.equals(temp)) {
								System.out.println("no : " + no);
								model.ChangeInfo.delSpecLha(no);
								Common.Data.display.refresh_specilist();
								Data.IsModified = 1;
								break;
							}
						}
					}
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
