package UI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.*;

import Common.Data;
import Common.Rule;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ModifyFrame {
	ArrayList<JComboBox>devices_A = new ArrayList<JComboBox>();
	ArrayList<JComboBox>status_A = new ArrayList<JComboBox>();
	ArrayList<JComboBox>sym_A = new ArrayList<JComboBox>();
	ArrayList<JComboBox>condi_A = new ArrayList<JComboBox>();
	ArrayList<JComboBox>devices_B = new ArrayList<JComboBox>();
	ArrayList<JComboBox>action_B = new ArrayList<JComboBox>();

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
	
	public static JFrame frame;
	public static JPanel panel;
	JButton confirm;
	JButton cancel;
	
	private int srcSN;				// 第一个家具的编号
	private int sigTrig;			// 因为信号量改变而产生的规则
	private int[] trigger = new int[2];			// 字符串型变量取值
	
	private int dstSN;				// 第二个家具的编号
	private int dstAct;			// 第二个家具要执行的动作
	
	public static int flag;
	
	public static int ruleNo;
	public JFrame createFrame(Common.Rule rule, int no) {
		ruleNo = no;
		frame = new JFrame();
		parsesrcSN(rule);
		System.out.println(rule.createRule());
		ModifyFrame mf = new ModifyFrame();
		panel = mf.createPanel();
		
		combo1.setSelectedIndex(srcSN);
		combo1.setEnabled(false);
		parsesigTrig(rule);
		System.out.println("sigTrig : " + sigTrig);
		combo2.setSelectedIndex(sigTrig + 1);
		if (flag == 1) {
			parseTrigger(rule);
			combo3.setSelectedIndex(trigger[1] + 1);
			combo3_1.setSelectedIndex(trigger[0]);
			flag = 0;
		}
		parsedstSN(rule);
		combo4.setSelectedIndex(dstSN);
		combo4.setEnabled(false);
		parsedstAct(rule);
		combo5.setSelectedIndex(dstAct + 1);
		
		frame.getContentPane().add(panel);
		frame.setLocationRelativeTo(null);
		frame.setSize(950, 150);
		frame.setVisible(true);
		
		return frame;
	}
	
	public void parsesrcSN(Common.Rule rule) {
		int no = 1;
		for (Common.Furniture sta : Common.Info.F_Array) {
			if (sta.SN == rule.srcSN) {
				srcSN = no;
				break;
			}
			no++;
		}
	}
	
	public void parsedstSN(Common.Rule rule) {
		int no = 1;
		for (Common.Furniture sta : Common.Info.F_Array) {
			if (sta.SN == rule.dstSN) {
				dstSN = no;
				break;
			}
			no++;
		}
	}
	
	public void parsesigTrig(Common.Rule rule) {
		int no = 0;
//		System.out.println(rule.createRule());
		Common.Furniture fur = Common.Info.F_Array.get(srcSN - 1);
//		System.out.println("rule.sigTrig = " + rule.sigTrig);
		String temp = "";
		if (rule.sigTrig != null)
			temp = rule.sigTrig;
		else
			temp = rule.trigger.var;
//		System.out.println("temp = " + temp);
		for (Common.Variable start : fur.variArr) {
//			System.out.println("variArr = " + start.name);
			if (start.name.equals(temp)) {
				sigTrig = no;
				return ;
			}
			no++;
		}
//		System.out.println("no = " + no);
		for (Common.Transition start : fur.transArr) {
			if (start.signal) {
				if (start.name.equals(temp)) {
					sigTrig = no;
					return ;
				}
			}
			no++;
		}
//		System.out.println("after transaArr no = " + no);
		for (Common.Action start : fur.actionArr) {
//			System.out.println("actionArr " + start.name);
			if (start.signal) {
				if (start.name.equals(temp)) {
//					System.out.println("in trig = " + sigTrig);
					sigTrig = no;
					return ;
				}
			}
			no++;
		}
	}
	
	public void parseTrigger(Common.Rule rule) {
		if (flag == 1) {
			int no = 0;
			flag = 0;
			Common.Variable var = Common.Info.F_Array.get(srcSN - 1).variArr[sigTrig];
			for (int i = var.lowBond; i <= var.highBond; i++) {
				String temp = Integer.toString(i);
				if (temp.equals(rule.trigger.value)) {
					trigger[1] = no;
					break;
				}
				no++;
			}

			no = 0;
			for (; ;) {
				if (Common.Data.Symbol[no].equals(rule.trigger.rel.getStr_f())) {
					trigger[0] = no;
					return ;
				}
				no++;
			}
		};
	}
	
	public void parsedstAct(Common.Rule rule) {
		int no = 0;
		for (Common.Action sta : Common.Info.F_Array.get(dstSN - 1).actionArr) {
			if (sta.name.toString().equals(rule.dstAct)) {
//				System.out.println("Act : " + rule.dstAct);
				dstAct = no;
				return ;
			}
			no++;
		}
	}
	
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
		
		confirm = new JButton("Confirm");
		confirm.setBounds(175, 70, 150, 25);
		confirm.addActionListener(new ConfirmListener());
		cancel = new JButton("Cancel");
		cancel.setBounds(475, 70, 150, 25);
		cancel.addActionListener(new CancelListener());
		panel.add(cancel);
		panel.add(confirm);
		
		return panel;
	}
	
	class Combox1Listener implements ItemListener {
		public void itemStateChanged(ItemEvent event) {
			int index = combo1.getSelectedIndex() - 1;
//			System.out.println(index);
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
//			System.out.println(combo2.getSelectedItem().toString());
//			System.out.println(index1 + "   " + index2);
			if (index2 < Common.Info.F_Array.get(index1).variArr.length) {
				combo3_1.setVisible(true);
				combo3.setVisible(true);
				try {
//					System.out.println("in flag");
					flag = 1;
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
				combo3_1.setSelectedIndex(0);
				combo3.setSelectedIndex(0);
				combo3_1.setVisible(false);
				combo3.setVisible(false);
				flag = 0;
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
	
	class ConfirmListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			
//			Data.column.clear();
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
//					System.out.println("device" + devices_A.get(i).getSelectedItem().toString());
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

					String action = action_B.get(i).getSelectedItem().toString();
					if (condi_A.get(i).getSelectedItem().equals("Default")) {
						String api = status_A.get(i).getSelectedItem().toString();
//						System.out.println("changed : " + ruleNo);
						Rule rawrule = Data.rawRuleList.get(ruleNo);
						if (src_sn != rawrule.srcSN)
							Data.modifycell[ruleNo][1] = 1;
//							Data.column.add(1);
						if (!api.equals(rawrule.sigTrig))
							Data.modifycell[ruleNo][2] = 1;
//							Data.column.add(2);
						if (dst_sn != rawrule.dstSN)
//							Data.column.add(6);
							Data.modifycell[ruleNo][6] = 1;
						if (!action.equals(rawrule.dstAct))
//							Data.column.add(7);
							Data.modifycell[ruleNo][7] = 1;
//						System.out.println("api : " + api);
						model.ChangeInfo.changeRule(ruleNo, src_sn, api, dst_sn, action);
						
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
						Rule rawrule = Data.rawRuleList.get(ruleNo);
						if (src_sn != rawrule.srcSN)
							Data.modifycell[ruleNo][1] = 1;
//							Data.column.add(1);
						if (rawrule.trigger == null) {
							System.out.println("null");
							Data.modifycell[ruleNo][2] = 1;
							Data.modifycell[ruleNo][3] = 1;
							Data.modifycell[ruleNo][4] = 1;
						}
						else {
							if (!var.equals(rawrule.trigger.var))
								Data.modifycell[ruleNo][2] = 1;
							if (!rel.equals(rawrule.trigger.rel))
								Data.modifycell[ruleNo][3] = 1;
							if (!Integer.toString(value).equals(rawrule.trigger.value))
								Data.modifycell[ruleNo][4] = 1;
						}
						if (dst_sn != rawrule.dstSN)
							Data.modifycell[ruleNo][6] = 1;
						if (!action.equals(rawrule.dstAct))
							Data.modifycell[ruleNo][7] = 1;
						model.ChangeInfo.changeRule(ruleNo, src_sn, var, rel, value, dst_sn, action);
						Data.IsModified = 1;
					}
				}

				Common.Data.display.refresh_rulelist();

				frame.dispose();
			}
		}
	}
	
	class CancelListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			frame.dispose();
		}
	}
}
