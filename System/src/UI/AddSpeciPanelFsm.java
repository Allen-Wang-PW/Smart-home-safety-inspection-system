package UI;
import javax.swing.*;

import Common.Data;

import java.awt.*;
import java.awt.event.*;

@SuppressWarnings( {"serial", "rawtypes", "unchecked" })
public class AddSpeciPanelFsm extends JFrame{
	public static JPanel panel;
	public static JFrame frame;
	public int Y_inc = 30;
	public static int X = 800;
	public static int Y = 200;
	
	public JComboBox moduleSelect;

	public JFrame createFrame() {
		frame = new JFrame();
		
		AddSpeciPanelFsm temp = new AddSpeciPanelFsm();
		panel = temp.createPanel();
		
		frame.setTitle("Fsm");
		frame.getContentPane().add(panel);
		frame.setSize(X, Y);
		return frame;
	}
	
	public JButton Confirm;
	public JButton Cancel;
	public int pos_y = 150;
	
	public JComboBox[] device;
	public JComboBox[] deviceA;
	public JComboBox[] deviceB;
	public JComboBox[] attri;
	public JComboBox[] attri1;
	public JComboBox[] attri2;
	
	public int num = 0;
	public int numA = 0;
	public int numB = 0;
	
	public JComboBox[] symbox;
	public JComboBox[] valbox;
	public JComboBox[] symboxA;
	public JComboBox[] valboxA;
	public JComboBox[] symboxB;
	public JComboBox[] valboxB;
	
	public JPanel createPanel() {
		panel = new JPanel();
		
		attri = new JComboBox[Common.Data.MAXN];
		attri1 = new JComboBox[Common.Data.MAXN];
		attri2 = new JComboBox[Common.Data.MAXN];
		device = new JComboBox[Common.Data.MAXN];
		deviceA = new JComboBox[Common.Data.MAXN];
		deviceB = new JComboBox[Common.Data.MAXN];
		symbox = new JComboBox[Common.Data.MAXN];
		valbox = new JComboBox[Common.Data.MAXN];
		symboxA = new JComboBox[Common.Data.MAXN];
		valboxA = new JComboBox[Common.Data.MAXN];
		symboxB = new JComboBox[Common.Data.MAXN];
		valboxB = new JComboBox[Common.Data.MAXN];
		
		ComboBoxModel moduleMode=new AModel(Common.Data.module_Fsm);
		moduleSelect = new JComboBox(moduleMode);
		
		JLabel moduleLabel = new JLabel();
		moduleLabel.setText("Fsm Module Select");
		
		panel.setLayout(null);
		moduleLabel.setBounds(20, 10, 200, 30);
		moduleSelect.setBounds(10, 30, 500, 50);

		JButton confirm = new JButton("Confirm");
		confirm.setBounds(520, 40, 100, 30);
		confirm.addActionListener(new ButtonListener());
		
		panel.add(moduleLabel);
		panel.add(moduleSelect);
		panel.add(confirm);
		return panel;
	}
	
	public JButton and;
	public JButton and1;
	public JButton and2;
	public JLabel label;
	public JLabel label1;
	public JLabel label2;
	public JLabel label3;
	public boolean changed = false;
	public boolean changed1 = false;
	public boolean changed2 = false;
	public String speci;
	public JLabel dot42;
	
	public ComboBoxModel attrimodel;
	public ComboBoxModel symmodel;
	public ComboBoxModel valmodel;
	class ButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			ComboBoxModel model = new DeviceModel(Common.Info.F_Array);
			Font font = new Font("Monospaced", Font.BOLD, 15);
			String[] temp = {"Default"};
			attrimodel = new AModel(temp);
			and = new JButton("&");
			and.addActionListener(new AndListener());
			
			and1 = new JButton("&");
			and1.addActionListener(new And1Listener());
			and2 = new JButton("&");
			and2.addActionListener(new And2Listener());
			
			Confirm = new JButton("Confirm");
			Cancel = new JButton("Cancel");
			Confirm.addActionListener(new ConfirmListener());
			Cancel.addActionListener(new CancelListener());
			panel.add(Confirm);
			panel.add(Cancel);
			int index = moduleSelect.getSelectedIndex();
			if (index > 3)
				pos_y = 240;
			Confirm.setBounds(150, pos_y, 100, 30);
			Cancel.setBounds(450, pos_y, 100, 30);
			
			if (index < 4) {
				device[num] = new JComboBox(model);
				device[num].addItemListener(new Combox1Listener());
				device[num].setBounds(10, 80, 150, 30);
				JLabel dot = new JLabel(".");
				dot.setFont(font);
				dot.setBounds(160, 90, 12, 12);
				attri[num] = new JComboBox(attrimodel);
				attri[num].addItemListener(new Combox2Listener());
				attri[num].setBounds(172, 80, 150, 30);
				and.setBounds(580, 80, 40, 30);
			
				symmodel = new AModel(Common.Data.Symbol);
				symbox[num] = new JComboBox(symmodel);
				symbox[num].setBounds(322, 80, 100, 30);
				valmodel = new AModel(temp);
				valbox[num] = new JComboBox(valmodel);
				valbox[num].setBounds(422, 80, 100, 30);
				symbox[num].setVisible(false);
				valbox[num].setVisible(false);
				panel.add(symbox[num]);
				panel.add(valbox[num]);
				panel.add(device[num]);
				panel.add(attri[num]);
				panel.add(and);
				panel.add(dot);
			}
			else {
				Y = 300;
				label1 = new JLabel("IF");
				label1.setBounds(20, 80, 150, 30);
				deviceA[numA] = new JComboBox(model);
				deviceA[numA].addItemListener(new Combox1AListener());
				deviceA[numA].setBounds(10, 110, 150, 30);
				JLabel dot41 = new JLabel(".");
				dot41.setFont(font);
				dot41.setBounds(160, 120, 12, 12);
				attri1[numA] = new JComboBox(attrimodel);
				attri1[numA].addItemListener(new Combox2AListener());
				attri1[numA].setBounds(172, 110, 150, 30);
				ComboBoxModel symmodelA = new AModel(Common.Data.Symbol);
				symboxA[numA] = new JComboBox(symmodelA);
				symboxA[numA].setBounds(322, 110, 100, 30);
				ComboBoxModel valmodelA = new AModel(Common.Data.Ranges);
				valboxA[numA] = new JComboBox(valmodelA);
				valboxA[numA].setBounds(422, 110, 100, 30);
				symboxA[numA].setVisible(false);
				valboxA[numA].setVisible(false);
				panel.add(symboxA[numA]);
				panel.add(valboxA[numA]);
				
				and1.setBounds(580, 110, 40, 30);
				label2 = new JLabel("happens,");
				label2.setBounds(20, 140, 150, 30);
				ComboBoxModel model1 = new DeviceModel(Common.Info.F_Array);
				deviceB[numB] = new JComboBox(model1);
				deviceB[numB].addItemListener(new Combox1BListener());
				deviceB[numB].setBounds(10, 170, 150, 30);
				dot42 = new JLabel(".");
				dot42.setFont(font);
				dot42.setBounds(160, 180, 12, 12);
				ComboBoxModel attrimodel1 = new AModel(Common.Data.Attribute);
				attri2[numB] = new JComboBox(attrimodel1);
				attri2[numB].addItemListener(new Combox2BListener());
				attri2[numB].setBounds(172, 170, 150, 30);
				and2.setBounds(580, 170, 40, 30);
				ComboBoxModel symmodelB = new AModel(Common.Data.Symbol);
				symboxB[numB] = new JComboBox(symmodelB);
				symboxB[numB].setBounds(322, 170, 100, 30);
				ComboBoxModel valmodelB = new AModel(Common.Data.Ranges);
				valboxB[numB] = new JComboBox(valmodelB);
				valboxB[numB].setBounds(422, 170, 100, 30);
				symboxB[numB].setVisible(false);
				valboxB[numB].setVisible(false);
				panel.add(symboxB[numB]);
				panel.add(valboxB[numB]);
				
				panel.add(label1);
				panel.add(deviceA[numA]);
				panel.add(dot41);
				panel.add(attri1[numA]);
				panel.add(and1);
				panel.add(label2);
				panel.add(deviceB[numB]);
				panel.add(dot42);
				panel.add(attri2[numB]);
				panel.add(and2);
			}
			switch(index) {
			case 1:
				label = new JLabel();
				label.setText("holds forever");
				label.setBounds(20, 110, 200, 30);
				frame.setSize(X, (Y += Y_inc));
				
				panel.add(label);
				panel.repaint();
				frame.repaint();
				System.out.println("1");
				break;
			case 2:
				label = new JLabel();
				label.setText("will happen later");
				label.setBounds(20, 110, 200, 30);
				frame.setSize(X, (Y += Y_inc));

				panel.add(label);
				panel.repaint();
				frame.repaint();
				System.out.println("2");
				break;
			case 3:			
				label = new JLabel();
				label.setText("never happens");
				label.setBounds(20, 110, 200, 30);
				frame.setSize(X, (Y += Y_inc));

				panel.add(label);
				panel.repaint();
				frame.repaint();
				System.out.println("3");
				break;
			case 4:
				label3 = new JLabel("should happen at the same time");
				label3.setBounds(20, 200, 250, 30);
				
				panel.add(label3);
				panel.repaint();
				frame.setSize(X, Y);
				frame.repaint();
				System.out.println("4");
				break;
			case 5:
				label3 = new JLabel("should happen later");
				label3.setBounds(20, 200, 250, 30);
				
				panel.add(label3);
				panel.repaint();
				frame.setSize(X, Y);
				frame.repaint();
				System.out.println("5");
				break;
			case 6:
				label3 = new JLabel("should happen later and last forever");
				label3.setBounds(20, 200, 250, 30);
				
				panel.add(label3);
				panel.repaint();
				frame.setSize(X, Y);
				frame.repaint();
				System.out.println("6");
				break;
			}
		}
	}
	
	class Combox1Listener implements ItemListener {
		public void itemStateChanged(ItemEvent event) {
			int index = device[num].getSelectedIndex() - 1;
			System.out.println(index);
			attrimodel = new CheckModel(Common.Info.F_Array.get(index));
			attri[num].setModel(attrimodel);
			panel.repaint();
			frame.repaint();
		}
	}
	
	class Combox1AListener implements ItemListener {
		public void itemStateChanged(ItemEvent event) {
			int index = deviceA[numA].getSelectedIndex() - 1;
			System.out.println(index);
			attrimodel = new CheckModel(Common.Info.F_Array.get(index));
			attri1[numA].setModel(attrimodel);
			panel.repaint();
			frame.repaint();
		}
	}
	
	class Combox1BListener implements ItemListener {
		public void itemStateChanged(ItemEvent event) {
			int index = deviceB[numB].getSelectedIndex() - 1;
			System.out.println(index);
			attrimodel = new CheckModel(Common.Info.F_Array.get(index));
			attri2[numB].setModel(attrimodel);
			panel.repaint();
			frame.repaint();
		}
	}
	
	class Combox2Listener implements ItemListener {
		public void itemStateChanged(ItemEvent event) {
			int index1 = device[num].getSelectedIndex() - 1;
			int index2 = attri[num].getSelectedIndex() - 2;
//			System.out.println("Common.Info.F_Array.get(index1).stateArr.length = " + Common.Info.F_Array.get(index1).stateArr.length + " index2 = " + index2);
			if (index2 + 1 == 0) {
				symbox[num].setVisible(true);
				valbox[num].setVisible(true);
				try {
					symmodel = new AModel(Common.Data.Equal);
					valmodel = new StateModel(Common.Info.F_Array.get(index1).stateArr);
				} catch(Exception ex) {
					symbox[num].setVisible(false);
					valbox[num].setVisible(false);
					ex.printStackTrace();
				}
				symbox[num].setModel(symmodel);
				valbox[num].setModel(valmodel);
				panel.repaint();
				frame.repaint();
			}
			else if ((index2 >= 0) &&
					(index2 < Common.Info.F_Array.get(index1).variArr.length)) {
				symbox[num].setVisible(true);
				valbox[num].setVisible(true);
				try {
					symmodel = new AModel(Common.Data.Symbol);
					valmodel = new VarModel(Common.Info.F_Array.get(index1).variArr[index2]);
				} catch(Exception ex) {
					symbox[num].setVisible(false);
					valbox[num].setVisible(false);
					ex.printStackTrace();
				}
				symbox[num].setModel(symmodel);
				valbox[num].setModel(valmodel);
				panel.repaint();
				frame.repaint();
			}
			else {
				symbox[num].setVisible(false);
				valbox[num].setVisible(false);
			}
		}
	}
	
	class Combox2AListener implements ItemListener {
		public void itemStateChanged(ItemEvent event) {
			int index1 = deviceA[numA].getSelectedIndex() - 1;
			int index2 = attri1[numA].getSelectedIndex() - 2;
//			System.out.println("index2 " + index2);
			if (index2 + 1 == 0) {
				symboxA[numA].setVisible(true);
				valboxA[numA].setVisible(true);
				try {
					symmodel = new AModel(Common.Data.Equal);
					valmodel = new StateModel(Common.Info.F_Array.get(index1).stateArr);
				} catch(Exception ex) {
					symboxA[numA].setVisible(false);
					valboxA[numA].setVisible(false);
					ex.printStackTrace();
				}
				symboxA[numA].setModel(symmodel);
				valboxA[numA].setModel(valmodel);
				panel.repaint();
				frame.repaint();
			}
			else if ((index2 >= 0) &&
					(index2 < Common.Info.F_Array.get(index1).variArr.length)) {
				symboxA[numA].setVisible(true);
				valboxA[numA].setVisible(true);
				try {
					symmodel = new AModel(Common.Data.Symbol);
					valmodel = new VarModel(Common.Info.F_Array.get(index1).variArr[index2]);
				} catch(Exception ex) {
					symboxA[numA].setVisible(false);
					valboxA[numA].setVisible(false);
					ex.printStackTrace();
				}
				symboxA[numA].setModel(symmodel);
				valboxA[numA].setModel(valmodel);
				panel.repaint();
				frame.repaint();
			}
			else {
				symboxA[numA].setVisible(false);
				valboxA[numA].setVisible(false);
			}
		}
	}
	
	class Combox2BListener implements ItemListener {
		public void itemStateChanged(ItemEvent event) {
			int index1 = deviceB[numB].getSelectedIndex() - 1;
			int index2 = attri2[numB].getSelectedIndex() - 2;
//			System.out.println("index2 " + index2);
			if (index2 + 1 == 0) {
				symboxB[numB].setVisible(true);
				valboxB[numB].setVisible(true);
				try {
					symmodel = new AModel(Common.Data.Equal);
					valmodel = new StateModel(Common.Info.F_Array.get(index1).stateArr);
				} catch(Exception ex) {
					symboxB[numB].setVisible(false);
					valboxB[numB].setVisible(false);
					ex.printStackTrace();
				}
				symboxB[numB].setModel(symmodel);
				valboxB[numB].setModel(valmodel);
				panel.repaint();
				frame.repaint();
			}
			else if ((index2 >= 0) &&
					(index2 < Common.Info.F_Array.get(index1).variArr.length)) {
				symboxB[numB].setVisible(true);
				valboxB[numB].setVisible(true);
				try {
					symmodel = new AModel(Common.Data.Symbol);
					valmodel = new VarModel(Common.Info.F_Array.get(index1).variArr[index2]);
				} catch(Exception ex) {
					symboxB[numB].setVisible(false);
					valboxB[numB].setVisible(false);
					ex.printStackTrace();
				}
				symboxB[numB].setModel(symmodel);
				valboxB[numB].setModel(valmodel);
				panel.repaint();
				frame.repaint();
			}
			else {
				symboxB[numB].setVisible(false);
				valboxB[numB].setVisible(false);
			}
		}
	}
	
	public static String str = "";
	class AndListener implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			String tip1 = "The device hasn't been chosen!";
			String tip2 = "The attribute hasn't been chosen!";
			if (device[num].getSelectedItem().toString().equals("Default")) {
				JOptionPane.showMessageDialog(null, tip1, "Warning", JOptionPane.WARNING_MESSAGE);
			}
			else if (attri[num].getSelectedItem().toString().equals("Default")) {
				JOptionPane.showMessageDialog(null, tip2, "Warning", JOptionPane.WARNING_MESSAGE);
			}
			else {
				num++;
				ComboBoxModel model = new DeviceModel(Common.Info.F_Array);
				device[num] = new JComboBox(model);
				device[num].setBounds(10, 80 + Y_inc * num, 150, 30);
				device[num].addItemListener(new Combox1Listener());
				JLabel dot = new JLabel(".");
				Font font = new Font("Monospaced", Font.BOLD, 15);
				dot.setFont(font);
				dot.setBounds(160, 90 + Y_inc * num, 12, 12);
				String[] temp = {"Default"};
				ComboBoxModel attrimodel = new AModel(temp);
				attri[num] = new JComboBox(attrimodel);
				attri[num].addItemListener(new Combox2Listener());
				attri[num].setBounds(172, 80 + Y_inc * num, 150, 30);
				and.setBounds(580, 80 + Y_inc * num, 40, 30);
				Confirm.setBounds(150, pos_y + Y_inc * num, 100, 30);
				Cancel.setBounds(450, pos_y + Y_inc * num, 100, 30);
				label.setBounds(20, 110 + Y_inc * num, 200, 30);
				JLabel andlabel = new JLabel("&");
				andlabel.setBounds(600, 80 + Y_inc * (num - 1), 40, 30);
				ComboBoxModel symmodel = new AModel(Common.Data.Symbol);
				symbox[num] = new JComboBox(symmodel);
				symbox[num].setBounds(322, 80 + Y_inc * num, 100, 30);
				ComboBoxModel valmodel = new AModel(temp);
				valbox[num] = new JComboBox(valmodel);
				valbox[num].setBounds(422, 80 + Y_inc * num, 100, 30);
				symbox[num].setVisible(false);
				valbox[num].setVisible(false);
				panel.add(symbox[num]);
				panel.add(valbox[num]);
				frame.setSize(X, (Y += Y_inc));
				panel.add(device[num]);
				panel.add(dot);
				panel.add(andlabel);
				panel.add(attri[num]);
				panel.repaint();
				frame.repaint();
			}
		}
	}
	
	public static String str1 = "", str2 = "";
	class And1Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			String tip1 = "The device hasn't been chosen!";
			String tip2 = "The attribute hasn't been chosen!";
			if (deviceA[numA].getSelectedItem().toString().equals("Default")) {
				JOptionPane.showMessageDialog(null, tip1, "Warning", JOptionPane.WARNING_MESSAGE);
			}
			else if (attri1[numA].getSelectedItem().toString().equals("Default")) {
				JOptionPane.showMessageDialog(null, tip2, "Warning", JOptionPane.WARNING_MESSAGE);
			}
			else {
				numA++;
				ComboBoxModel model = new DeviceModel(Common.Info.F_Array);
				deviceA[numA] = new JComboBox(model);
				deviceA[numA].addItemListener(new Combox1AListener());
				deviceA[numA].setBounds(10, 110 + Y_inc * numA, 150, 30);
				JLabel dot = new JLabel(".");
				Font font = new Font("Monospaced", Font.BOLD, 15);
				dot.setFont(font);
				dot.setBounds(160, 120 + Y_inc * numA, 12, 12);
				String[] temp = {"Default"};
				ComboBoxModel attrimodel = new AModel(temp);
				attri1[numA] = new JComboBox(attrimodel);
				attri1[numA].addItemListener(new Combox2AListener());
				attri1[numA].setBounds(172, 110 + Y_inc * numA, 150, 30);
				and.setBounds(580, 110 + Y_inc * numA, 40, 30);
				label2.setBounds(20, 140 + Y_inc * numA, 150, 30);
				label3.setBounds(20, 200 + Y_inc * numA, 250, 30);
				JLabel andlabel = new JLabel("&");
				andlabel.setBounds(600, 110 + Y_inc * (numA - 1), 40, 30);
				and1.setBounds(580, 110 + Y_inc * numA, 40, 30);
				ComboBoxModel symmodelA = new AModel(Common.Data.Symbol);
				symboxA[numA] = new JComboBox(symmodelA);
				symboxA[numA].setBounds(322, 110 + Y_inc * numA, 100, 30);
				ComboBoxModel valmodelA = new AModel(temp);
				valboxA[numA] = new JComboBox(valmodelA);
				valboxA[numA].setBounds(422, 110 + Y_inc * numA, 100, 30);
				symboxA[numA].setVisible(false);
				valboxA[numA].setVisible(false);
				panel.add(symboxA[numA]);
				panel.add(valboxA[numA]);
			
				deviceB[numB].setBounds(10, 170 + Y_inc * numA, 150, 30);
				dot42.setBounds(160, 180 + Y_inc * numA, 12, 12);
				attri2[numB].setBounds(172, 170 + Y_inc * numA, 150, 30);
				symboxB[numB].setBounds(322, 170 + Y_inc * numA, 100, 30);
				valboxB[numB].setBounds(422, 170 + Y_inc * numA, 100, 30);
				and2.setBounds(580, 170 + Y_inc * numA, 40, 30);
				Confirm.setBounds(150, pos_y + Y_inc * numA, 100, 30);
				Cancel.setBounds(450, pos_y + Y_inc * numA, 100, 30);
				frame.setSize(X, (Y += Y_inc));
				panel.add(deviceA[numA]);
				panel.add(dot);
				panel.add(andlabel);
				panel.add(attri1[numA]);
				panel.repaint();
				frame.repaint();
			}
		}
	}
	
	class And2Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			String tip1 = "The device hasn't been chosen!";
			String tip2 = "The attribute hasn't been chosen!";
			if (deviceB[numB].getSelectedItem().toString().equals("Default")) {
				JOptionPane.showMessageDialog(null, tip1, "Warning", JOptionPane.WARNING_MESSAGE);
			}
			else if (attri2[numB].getSelectedItem().toString().equals("Default")) {
				JOptionPane.showMessageDialog(null, tip2, "Warning", JOptionPane.WARNING_MESSAGE);
			}
			else {
				numB++;
				ComboBoxModel model = new DeviceModel(Common.Info.F_Array);
				deviceB[numB] = new JComboBox(model);
				deviceB[numB].addItemListener(new Combox1BListener());
				deviceB[numB].setBounds(10, 170 + Y_inc * (numA +numB), 150, 30);
				JLabel dot = new JLabel(".");
				Font font = new Font("Monospaced", Font.BOLD, 15);
				dot.setFont(font);
				dot.setBounds(160, 180 + Y_inc * (numA + numB), 12, 12);
				String[] temp = {"Default"};
				ComboBoxModel attrimodel = new AModel(temp);
				attri2[numB] = new JComboBox(attrimodel);
				attri2[numB].addItemListener(new Combox2BListener());
				attri2[numB].setBounds(172, 170 + Y_inc * (numA + numB), 150, 30);
				and2.setBounds(580, 170 + Y_inc * (numA + numB), 40, 30);
				label3.setBounds(20, 200 + Y_inc * (numA + numB), 250, 30);
				Confirm.setBounds(150, pos_y + Y_inc * (numA + numB), 100, 30);
				Cancel.setBounds(450, pos_y + Y_inc * (numA + numB), 100, 30);
				JLabel andlabel = new JLabel("&");
				andlabel.setBounds(600, 170 + Y_inc * (numA + numB - 1), 40, 30);
				ComboBoxModel symmodel = new AModel(Common.Data.Symbol);
				symboxB[numB] = new JComboBox(symmodel);
				symboxB[numB].setBounds(322, 170 + Y_inc * (numA + numB), 100, 30);
				ComboBoxModel valmodel = new AModel(temp);
				valboxB[numB] = new JComboBox(valmodel);
				valboxB[numB].setBounds(422, 170 + Y_inc * (numA + numB), 100, 30);
				symboxB[numB].setVisible(false);
				valboxB[numB].setVisible(false);
				panel.add(symboxB[numB]);
				panel.add(valboxB[numB]);
				frame.setSize(X, (Y += Y_inc));
				panel.add(deviceB[numB]);
				panel.add(dot);
				panel.add(andlabel);
				panel.add(attri2[numB]);
				panel.repaint();
				frame.repaint();
			}
		}
	}

	class ConfirmListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			String temp = "";
			String first = "";
			String second = "";
			int index = moduleSelect.getSelectedIndex();
			if (index < 4) {
				String tip1 = "The device hasn't been chosen!";
				String tip2 = "The attribute hasn't been chosen!";
				if (device[num].getSelectedItem().toString().equals("Default")) {
					JOptionPane.showMessageDialog(null, tip1, "Warning", JOptionPane.WARNING_MESSAGE);
				}
				else if (attri[num].getSelectedItem().toString().equals("Default")) {
					JOptionPane.showMessageDialog(null, tip2, "Warning", JOptionPane.WARNING_MESSAGE);
				}
				else {
					for (int i = 0; i <= num; i++) {
						str = str +
								device[i].getSelectedItem().toString() + "." +
								attri[i].getSelectedItem().toString();
						first = first +
								device[i].getSelectedItem().toString().toLowerCase() + ".";
						int index1 = device[i].getSelectedIndex() - 1;
						int index2 = attri[i].getSelectedIndex() - 2 - Common.Info.F_Array.get(index1).variArr.length;
						if ((index2 < Common.Info.F_Array.get(index1).transArr.length) &&
								(index2 >= 0)) {
							first = first + attri[i].getSelectedItem().toString() + "_t";
						}
						else if (index2 - Common.Info.F_Array.get(index1).transArr.length >= 0) {
							first = first + attri[i].getSelectedItem().toString() + "_a";
						}
						else {
							first = first + attri[i].getSelectedItem().toString();
						}
						if (!symbox[i].getSelectedItem().equals("Default")) {
							str = str +
									symbox[i].getSelectedItem().toString() +
									valbox[i].getSelectedItem().toString();
							first = first +
									symbox[i].getSelectedItem().toString() +
									valbox[i].getSelectedItem().toString();
						}
						if (i < num) {
							str += " & ";
							first += "&";
						}
					}
					temp += str;
					switch(index) {
					case 1:
						temp += " holds forever.";
						break;
					case 2:
						temp += " will happen later.";
						break;
					case 3:
						temp += " never happens.";
						break;
					}
					model.ChangeInfo.addSpecFsm(index, first, second, temp);
					Common.Data.SpeciList.add(temp);
					Common.Data.display.refresh_specilist();
					Data.IsModified = 1;
					str = "";
					str1 = "";
					str2 = "";
					frame.dispose();
				}
			}
			else {
				String tip1 = "The device hasn't been chosen!";
				String tip2 = "The attribute hasn't been chosen!";
				if (deviceA[numA].getSelectedItem().toString().equals("Default")) {
					JOptionPane.showMessageDialog(null, tip1, "Warning", JOptionPane.WARNING_MESSAGE);
				}
				else if (attri1[numA].getSelectedItem().toString().equals("Default")) {
					JOptionPane.showMessageDialog(null, tip2, "Warning", JOptionPane.WARNING_MESSAGE);
				}
				else if (deviceB[numB].getSelectedItem().toString().equals("Default")) {
					JOptionPane.showMessageDialog(null, tip1, "Warning", JOptionPane.WARNING_MESSAGE);
				}
				else if (attri2[numB].getSelectedItem().toString().equals("Default")) {
					JOptionPane.showMessageDialog(null, tip2, "Warning", JOptionPane.WARNING_MESSAGE);
				}
				else {
					for (int i = 0; i <= numA; i++) {
						str1 = str1 +
								deviceA[i].getSelectedItem().toString() + "." +
								attri1[i].getSelectedItem().toString();
						first = first +
								deviceA[i].getSelectedItem().toString().toLowerCase() + ".";
						int index1 = deviceA[i].getSelectedIndex() - 1;
						int index2 = attri1[i].getSelectedIndex() - 2 - Common.Info.F_Array.get(index1).variArr.length;
						if ((index2 < Common.Info.F_Array.get(index1).transArr.length) &&
								(index2 >= 0)) {
							first = first + attri1[i].getSelectedItem().toString() + "_t";
						}
						else if (index2 - Common.Info.F_Array.get(index1).transArr.length >= 0) {
							first = first + attri1[i].getSelectedItem().toString() + "_a";
						}
						else {
							first = first + attri1[i].getSelectedItem().toString();
						}
						if (!symboxA[i].getSelectedItem().equals("Default")) {
							str1 = str1 +
									symboxA[i].getSelectedItem().toString() +
									valboxA[i].getSelectedItem().toString();
							first = first +
									symboxA[i].getSelectedItem().toString() +
									valboxA[i].getSelectedItem().toString();
						}
						if (i < numA) {
							str1 += " & ";
							first += "&";
						}
					}
					for (int i = 0; i <= numB; i++) {
						str2 = str2 +
								deviceB[i].getSelectedItem().toString() + "." +
								attri2[i].getSelectedItem().toString();
						second = second +
								deviceB[i].getSelectedItem().toString().toLowerCase() + ".";
						int index1 = deviceB[i].getSelectedIndex() - 1;
						int index2 = attri2[i].getSelectedIndex() - 2 - Common.Info.F_Array.get(index1).variArr.length;
						if ((index2 < Common.Info.F_Array.get(index1).transArr.length) &&
								(index2 >= 0)) {
							second = second + attri2[i].getSelectedItem().toString() + "_t";
						}
						else if (index2 - Common.Info.F_Array.get(index1).transArr.length >= 0) {
							second = second + attri2[i].getSelectedItem().toString() + "_a";
						}
						else {
							second = second + attri2[i].getSelectedItem().toString();
						}
						if (!symboxB[i].getSelectedItem().equals("Default")) {
							str2 = str2 +
									symboxB[i].getSelectedItem().toString() +
									valboxB[i].getSelectedItem().toString();
							second = second +
									symboxB[i].getSelectedItem().toString() +
									valboxB[i].getSelectedItem().toString();
						}
						if (i < numB) {
							str2 += " & ";
							second += "&";
						}
					}
					switch(index) {
					case 4:
						temp += " should happen at the same time.";
						break;
					case 5:
						temp += " should happen later.";
						break;
					case 6:
						temp += " should happen later and last forever";
						break;
					}
					temp = "IF " + str1 + ", " + str2;
//					System.out.println("first = " + first);
//					System.out.println("second = " + second);
					model.ChangeInfo.addSpecFsm(index, first, second, temp);
					Common.Data.SpeciList.add(temp);
//					System.out.println(temp);
					Common.Data.display.refresh_specilist();
					str = "";
					str1 = "";
					str2 = "";
					frame.dispose();
				}
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
