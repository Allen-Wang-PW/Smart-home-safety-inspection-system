package UI;
import javax.swing.*;

import Common.Data;

import java.awt.event.*;

@SuppressWarnings( {"serial", "rawtypes", "unchecked" })
public class AddSpeciPanelLha extends JFrame {
	public static int X = 800;
	public static int Y = 150;
	public static void main(String[] args) {
		AddSpeciPanelLha temp = new AddSpeciPanelLha();
		JFrame fra = temp.createFrame();
		fra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fra.setVisible(true);
		
	}
	
	public static JFrame frame;
	public static JPanel panel;
	public JFrame createFrame() {
		frame = new JFrame();
		
		AddSpeciPanelLha temp = new AddSpeciPanelLha();
		panel = temp.createPanel();
		
		frame.setTitle("Lha");
		frame.getContentPane().add(panel);
		frame.setSize(X, Y);
		return frame;
	}
	
	public JComboBox device;
	public JComboBox attri;
	
	public JButton confirm;
	public JButton cancel;
	
	public JComboBox varfur; 
	public JComboBox symbox;
	public JComboBox valbox;

	public ComboBoxModel attrimodel;
	public ComboBoxModel varmodel;
	public ComboBoxModel valmodel;
	
	public JPanel createPanel() {
		panel = new JPanel();
		
		ComboBoxModel model = new DeviceModel(Common.Info.F_Array);
		device = new JComboBox(model);
		device.addItemListener(new Combox1Listener());
		device.setBounds(50, 20, 150, 30);
		
		String[] temp = {"Default"};
		attrimodel = new AModel(temp);
		attri = new JComboBox(attrimodel);
		attri.setBounds(210, 20, 150, 30);
		attri.addItemListener(new attriListener());
		
		varmodel = new AModel(temp);
		varfur = new JComboBox(varmodel);
		varfur.setBounds(360, 20, 150, 30);
		varfur.setVisible(false);
		varfur.addItemListener(new Combox2Listener());
		
		ComboBoxModel symmodel = new AModel(Common.Data.Symbol);
		symbox = new JComboBox(symmodel);
		symbox.setBounds(510, 20, 100, 30);
		symbox.setVisible(false);

		valmodel = new AModel(temp);
		valbox = new JComboBox(valmodel);
		valbox.setBounds(610, 20, 100, 30);
		valbox.setVisible(false);
		
		panel.setLayout(null);
		panel.add(device);
		panel.add(attri);
		panel.add(varfur);
		panel.add(symbox);
		panel.add(valbox);
		
		confirm = new JButton("Confirm");
		cancel = new JButton("Cancel");
		confirm.setBounds(150, 80, 100, 30);
		cancel.setBounds(450, 80, 100, 30);
		confirm.addActionListener(new ConfirmListener());
		cancel.addActionListener(new CancelListener());
		panel.add(confirm);
		panel.add(cancel);
		
		return panel;
	}
	
	class Combox1Listener implements ItemListener {
		public void itemStateChanged(ItemEvent event) {
			int index = device.getSelectedIndex() - 1;
			System.out.println(index);
			attrimodel = new StateModel(Common.Info.F_Array.get(index).stateArr);
			attri.setModel(attrimodel);
			panel.repaint();
			frame.repaint();
		}
	}
	
	class attriListener implements ItemListener {
		public void itemStateChanged(ItemEvent event) {
			int index1 = device.getSelectedIndex() - 1;
			if (Common.Info.F_Array.get(index1).variArr != null) {
				varmodel = new VaribleModel(Common.Info.F_Array.get(index1).variArr);
				if (varmodel.getSize() > 1) {
					varfur.setModel(varmodel);
					varfur.setVisible(true);
				}
			}
			else
				varfur.setVisible(false);
		}
	}
	
	class Combox2Listener implements ItemListener {
		public void itemStateChanged(ItemEvent event) {
			int index1 = device.getSelectedIndex() - 1;
			int index2 = attri.getSelectedIndex() - 1;
			System.out.println("index1=" + index1);
			System.out.println("index2=" + index2);
			if (varmodel.getSize() > 1) {
				int index3 = varfur.getSelectedIndex() - 1;
				symbox.setVisible(true);
				valbox.setVisible(true);
				try {
					valmodel = new VarModel(Common.Info.F_Array.get(index1).variArr[index3]);
				} catch(Exception ex) {
					symbox.setVisible(false);
					valbox.setVisible(false);
					ex.printStackTrace();
				}
				valbox.setModel(valmodel);
				panel.repaint();
				frame.repaint();
			}
			else {
				symbox.setVisible(false);
				valbox.setVisible(false);
			}
		}
	}

	class valListener implements ItemListener {
		public void itemStateChanged(ItemEvent event) {
			symbox.setVisible(true);
			valbox.setVisible(true);
		}
	}
	
	class ConfirmListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			String tip1 = "The device hasn't been chosen!";
			String tip2 = "The location hasn't been chosen!";
			if (device.getSelectedItem().toString().equals("Default")) {
				JOptionPane.showMessageDialog(null, tip1, "Warning", JOptionPane.WARNING_MESSAGE);
			}
			else if (attri.getSelectedItem().toString().equals("Default")) {
				JOptionPane.showMessageDialog(null, tip2, "Warning", JOptionPane.WARNING_MESSAGE);
			}
			else {
				String temp = "";
				String condi = "";
				String name = attri.getSelectedItem().toString();
				temp = device.getSelectedItem().toString() + "." +
						attri.getSelectedItem().toString();
				String sn = device.getSelectedItem().toString();
				String [] a = sn.split("_");
				int SN = Integer.parseInt(a[a.length - 1]);
				System.out.println("sn = " + a[a.length - 1]);
				if (!varfur.getSelectedItem().equals("Default")) {
					temp = temp + " " + 
							varfur.getSelectedItem().toString() + " " +
							symbox.getSelectedItem().toString() + " " +
							valbox.getSelectedItem().toString();
					condi = condi +
							varfur.getSelectedItem().toString();
					if (symbox.getSelectedItem().equals("="))
						condi = condi + "==";
					else
						condi = condi + symbox.getSelectedItem().toString();
					condi += valbox.getSelectedItem().toString();
				}
//			System.out.println("name = " + name);
//			System.out.println("condi = " + condi);
				model.ChangeInfo.addSpecLha(SN, name, condi);
				Data.IsModified = 1;
//			System.out.println(temp);
				Common.Data.SpeciList.add(temp);
//			for (Common.LhaSpec sta : Common.Info.LhaArray) {
//				System.out.println(sta.getName() + " " + sta.getState() + " " + sta.getVarCondition());
//			}
				Common.Data.display.refresh_specilist();
				frame.dispose();
			}
			Data.IsModified = 1;
		}
	}
	
	class CancelListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			frame.dispose();
		}
	}
}
