package UI;
import javax.swing.*;

import Common.Data;

import java.awt.event.*;
import java.util.*;

@SuppressWarnings({ "serial", "rawtypes", "unchecked" })
public class AddRulePanel extends JFrame{
	static JFrame frame;
	static JPanel panel;
	static int num = 1;
	static int X = 150;
	static int Y = 30;
	static JButton confirm;
	static JButton cancel;
	static JButton add;

	ArrayList<JComboBox>devices_A = new ArrayList<JComboBox>();
	ArrayList<JComboBox>status_A = new ArrayList<JComboBox>();
	ArrayList<JComboBox>sym_A = new ArrayList<JComboBox>();
	ArrayList<JComboBox>condi_A = new ArrayList<JComboBox>();
	ArrayList<JComboBox>devices_B = new ArrayList<JComboBox>();
	ArrayList<JComboBox>action_B = new ArrayList<JComboBox>();
	
	public JFrame createFrame() {
		frame = new JFrame();
		frame.setSize(950, 150);
		AddRulePanel arp = new AddRulePanel();
		panel = arp.createPanel();
		frame.getContentPane().add(panel);
		
		return frame;
	}
	
	public static ComboBoxModel mode1;
	public static ComboBoxModel mode2;
	public static ComboBoxModel mode3;
	public static ComboBoxModel mode4;
	public static ComboBoxModel mode5;
	public static JComboBox combo1;
	public static JComboBox combo2;
	public static JComboBox combo3;
	public static JComboBox combo3_1;
	public static JComboBox combo4;
	public static JComboBox combo5;

	public JPanel createPanel() {
		panel = new JPanel();
		
		JLabel iflabel=new JLabel("IF");
		JLabel thenlabel=new JLabel("THEN");

		mode1 = new DeviceModel(Common.Info.F_Array);
		combo1 = new JComboBox(mode1);
		combo1.addItemListener(new Combox1Listener());
		
		String[] temp = {"Default"};
		mode2 = new AModel(temp);
		combo2 = new JComboBox(mode2);
		combo2.addItemListener(new Combox2Listener());
		
		ComboBoxModel mode3_1 = new AModel(Common.Data.Symbol);
		combo3_1 = new JComboBox(mode3_1);
		combo3_1.setVisible(false);
		
		mode3 = new AModel(temp);
		combo3=new JComboBox(mode3);
		combo3.setVisible(false);
		
		mode4 = new DeviceModel(Common.Info.F_Array);
		combo4 = new JComboBox(mode4);
		combo4.addItemListener(new Combox4Listener());
		
		mode5 = new AModel(temp);
		combo5=new JComboBox(mode5);
		
		devices_A.add(combo1);
		status_A.add(combo2);
		sym_A.add(combo3_1);
		condi_A.add(combo3);
		devices_B.add(combo4);
		action_B.add(combo5);
		
		panel.setLayout(null);
		iflabel.setBounds(20, 30, 20, 25);
		combo1.setBounds(50, 30, 150, 25);
		combo2.setBounds(200, 30, 150, 25);
		combo3_1.setBounds(350, 30, 100, 25);
		combo3.setBounds(450, 30, 100, 25);
		thenlabel.setBounds(550, 30, 40, 25);
		combo4.setBounds(600, 30, 150, 25);
		combo5.setBounds(750, 30, 150, 25);
		
		panel.add(iflabel);
		panel.add(combo1);
		panel.add(combo2);
		panel.add(combo3_1);
		panel.add(combo3);
		panel.add(thenlabel);
		panel.add(combo4);
		panel.add(combo5);
		
		add = new JButton("Add");
		add.setBounds(20, 30 + Y, 150, 25);
		add.addActionListener(new ButtonListener());
		panel.add(add);
		
		confirm = new JButton("Confirm");
		confirm.setBounds(175, 30 + 2 * Y, 150, 25);
		confirm.addActionListener(new ConfirmListener());
		cancel = new JButton("Cancel");
		cancel.setBounds(475, 30 + 2 * Y, 150, 25);
		cancel.addActionListener(new CancelListener());
		panel.add(cancel);
		panel.add(confirm);
		
		return panel;
	}
	
	class Combox1Listener implements ItemListener {
		public void itemStateChanged(ItemEvent event) {
			int index = combo1.getSelectedIndex() - 1;
			System.out.println(index);
			mode2 = new Furmodel(Common.Info.F_Array.get(index));
			combo2.setModel(mode2);
			panel.repaint();
			frame.repaint();
		}
	}
	
	class Combox2Listener implements ItemListener {
		public void itemStateChanged(ItemEvent event) {
			int index1 = combo1.getSelectedIndex() - 1;
			int index2 = combo2.getSelectedIndex() - 1;
			System.out.println("index2 " + index2);
			if (index2 < Common.Info.F_Array.get(index1).variArr.length) {
				combo3_1.setVisible(true);
				combo3.setVisible(true);
				try {
					mode3 = new VarModel(Common.Info.F_Array.get(index1).variArr[index2]);
				} catch(Exception ex) {
					combo3_1.setVisible(false);
					combo3.setVisible(false);
					ex.printStackTrace();
				}
				combo3.setModel(mode3);
				panel.repaint();
				frame.repaint();
			}
			else {
				combo3_1.setVisible(false);
				combo3.setVisible(false);
			}
		}
	}

	class Combox4Listener implements ItemListener {
		public void itemStateChanged(ItemEvent event) {
			int index = combo4.getSelectedIndex() - 1;
			mode5 = new ActionModel(Common.Info.F_Array.get(index).actionArr);
			combo5.setModel(mode5);
			panel.repaint();
			frame.repaint();
		}
	}
	
	class ButtonListener implements ActionListener {
		public void actionPerformed (ActionEvent event) {
			String tip = "The rule hasn't been finished!";
			if (combo1.getSelectedItem().toString().equals("Default") ||
					combo2.getSelectedItem().toString().equals("Default") ||
					combo4.getSelectedItem().toString().equals("Default") ||
					combo5.getSelectedItem().toString().equals("Default")) {
				JOptionPane.showMessageDialog(null, tip, "Warning", JOptionPane.WARNING_MESSAGE);
			}
			else {
				JLabel iflabel=new JLabel("IF");
				JLabel thenlabel=new JLabel("THEN");
			
				mode1 = new DeviceModel(Common.Info.F_Array);
				combo1 = new JComboBox(mode1);
				combo1.addItemListener(new Combox1Listener());
			
				String[] temp = {"Default"};
				mode2 = new AModel(temp);
				combo2 = new JComboBox(mode2);
				combo2.addItemListener(new Combox2Listener());

				ComboBoxModel mode3_1 = new AModel(Common.Data.Symbol);
				combo3_1 = new JComboBox(mode3_1);
				combo3_1.setVisible(false);

				mode3 = new AModel(temp);
				combo3 = new JComboBox(mode3);
				combo3.setVisible(false);
			
				mode4 = new DeviceModel(Common.Info.F_Array);
				combo4 = new JComboBox(mode4);
				combo4.addItemListener(new Combox4Listener());
			
				mode5 = new AModel(temp);
				combo5 = new JComboBox(mode5);
			
				devices_A.add(combo1);
				status_A.add(combo2);
				sym_A.add(combo3_1);
				condi_A.add(combo3);
				devices_B.add(combo4);
				action_B.add(combo5);
			
				iflabel.setBounds(20, 30 + num * Y, 20, 25);
				combo1.setBounds(50, 30 + num * Y, 150, 25);
				combo2.setBounds(200, 30 + num * Y, 150, 25);
				combo3_1.setBounds(350, 30 + num * Y, 100, 25);
				combo3.setBounds(450, 30 + num * Y, 100, 25);
				thenlabel.setBounds(550, 30 + num * Y, 40, 25);
				combo4.setBounds(600, 30 + num * Y, 150, 25);
				combo5.setBounds(750, 30 + num * Y, 150, 25);
				num++;

				add.setBounds(20, 30 + num * Y, 150, 25);
				confirm.setBounds(175, 30 + (num+1) * Y, 150, 25);
				cancel.setBounds(475, 30 + (num+1) * Y, 150, 25);
				frame.setSize(900, 200 + (num-2) * Y);
				frame.repaint();
				panel.add(iflabel);
				panel.add(combo1);
				panel.add(combo2);
				panel.add(combo3_1);
				panel.add(combo3);
				panel.add(thenlabel);
				panel.add(combo4);
				panel.add(combo5);
				panel.repaint();
			}
		}
	}
	
	class ConfirmListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			String tip = "The rule hasn't been finished!";
			if (combo1.getSelectedItem().toString().equals("Default") ||
					combo2.getSelectedItem().toString().equals("Default") ||
					combo4.getSelectedItem().toString().equals("Default") ||
					combo5.getSelectedItem().toString().equals("Default")) {
				JOptionPane.showMessageDialog(null, tip, "Warning", JOptionPane.WARNING_MESSAGE);
			}
			else {
				for (int i = 0; i < devices_A.size(); i++) {
					String temp = "IF " + 
							devices_A.get(i).getSelectedItem().toString() + "." +
							status_A.get(i).getSelectedItem().toString();
					if (!condi_A.get(i).getSelectedItem().equals("Default")) {
						temp = temp +
								" " +
								sym_A.get(i).getSelectedItem().toString() +
								condi_A.get(i).getSelectedItem().toString();
					}
					temp = temp +
							" THEN " +
							devices_B.get(i).getSelectedItem().toString() + "." +
							action_B.get(i).getSelectedItem().toString();
					Common.Data.RuleList.add(temp);
				
					int src_sn = 0;
					int dst_sn = 0;
					System.out.println("device" + devices_A.get(i).getSelectedItem().toString());
					for (Common.Furniture sta : Common.Info.F_Array) {
						String tempName = sta.furname + "_" +Integer.toString(sta.SN);
						if (devices_A.get(i).getSelectedItem().toString().equals(tempName)) {
							src_sn = sta.SN;
							break;
						}
					}
					for (Common.Furniture sta : Common.Info.F_Array) {
						String tempName = sta.furname + "_" +Integer.toString(sta.SN);
						if (devices_B.get(i).getSelectedItem().toString().equals(tempName)) {
							dst_sn = sta.SN;
							break;
						}
					}
//				System.out.println("src_sn : " + src_sn);
					String action = action_B.get(i).getSelectedItem().toString();
					if (condi_A.get(i).getSelectedItem().equals("Default")) {
						String api = status_A.get(i).getSelectedItem().toString();
						model.ChangeInfo.addRule(src_sn, api, dst_sn, action);
						Data.IsModified = 1;
					}
					else {
						String var = status_A.get(i).getSelectedItem().toString();
						Common.Relation rel;
						String sym = sym_A.get(i).getSelectedItem().toString();
						if (sym.equals("="))
							rel = Common.Relation.Equal;
						else if (sym.equals(">"))
							rel = Common.Relation.Larger;
						else
							rel = Common.Relation.Smaller;
						int value = Integer.parseInt(condi_A.get(i).getSelectedItem().toString());
						model.ChangeInfo.addRule(src_sn, var, rel, value, dst_sn, action);
						Data.IsModified = 1;
					}
//				System.out.println("Fsize : " + Common.Info.F_Array.size());
//				System.out.println("Rsize : " + Common.Info.R_Array.size());
//				System.out.println("SrcSn : " + Common.Info.R_Array.get(0).srcSN);
//				System.out.println(temp);
				}
			
				Common.Data.display.refresh_rulelist();
				num = 1;
				Data.IsModified = 1;
				frame.dispose();
			}
		}
	}
	
	class CancelListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			num = 1;
			frame.dispose();
		}
	}

}
