package UI;
import javax.swing.*;

import Common.Data;

import java.awt.event.*;

@SuppressWarnings({ "serial", "rawtypes", "unchecked" })
public class AddDevicePanel extends JFrame{
	final int X = 150;
	final int Y = 30;
	public static int num = 1;
	JButton button;
	JButton confirm;
	JButton cancel;
	public static JPanel panel;
	public static JFrame frame;
	
	public JFrame createFrame() {
		frame = new JFrame();
		AddDevicePanel adp = new AddDevicePanel();
		JPanel pane = adp.createPanel();
		frame.getContentPane().add(pane);
		frame.setSize(600,250);
		return frame;
	}
	
	public JPanel createPanel() {
		panel = new JPanel();
		
		JLabel devicelab = new JLabel();
		devicelab.setText("Devices");
		JLabel loclab = new JLabel();
		loclab.setText("Locations");
		JLabel numlab = new JLabel();
		numlab.setText("Number");
		
		panel.setLayout(null);
		devicelab.setBounds(50, 10, 150, 30);
		loclab.setBounds(250, 10, 150, 30);
		numlab.setBounds(450, 10, 150, 30);
		panel.add(devicelab);
		panel.add(loclab);
		panel.add(numlab);
		
		ComboBoxModel mode1=new AModel(Common.Data.devices);
		JComboBox combo1=new JComboBox(mode1);

		ComboBoxModel mode2=new AModel(Common.Data.locations);
		JComboBox combo2=new JComboBox(mode2);
		
		ComboBoxModel mode3=new AModel(Common.Data.numbers);
		JComboBox combo3=new JComboBox(mode3);

		Common.Data.DevicesList.add(combo1);
		Common.Data.LocationsList.add(combo2);
		Common.Data.NumbersList.add(combo3);	

		combo1.setBounds(10, 50, X, Y);
		combo2.setBounds(210, 50, X, Y);
		combo3.setBounds(410, 50, X, Y);
		
		panel.add(combo1);
		panel.add(combo2);
		panel.add(combo3);
		
		button = new JButton("Add");
		button.setBounds(10, 70 + Y, X, 30);
		button.addActionListener(new ButtonListener());
		panel.add(button);
		
		confirm = new JButton("Confirm");
		cancel = new JButton("Cancel");
		confirm.setBounds(130, 100 + 2 * Y, X, 30);
		confirm.addActionListener(new ConfirmListener());
		cancel.setBounds(320, 100 + 2 * Y, X, 30);
		cancel.addActionListener(new CancelListener());
		panel.add(confirm);
		panel.add(cancel);
		return panel;
	}
	
	class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			String tip1 = "The device hasn't been chosen!";
			String tip2 = "The location hasn't been chosen!";
//			System.out.println("ListSize : " + Common.Data.DevicesList.size());
//			System.out.println("ListName : " + Common.Data.DevicesList.get(Common.Data.DevicesList.size()-1).getSelectedItem().toString());
			if (Common.Data.DevicesList.get(Common.Data.DevicesList.size()-1).getSelectedItem().toString().equals("Default")) {
				JOptionPane.showMessageDialog(null, tip1, "Warning", JOptionPane.WARNING_MESSAGE);
			}
			else if (Common.Data.LocationsList.get(Common.Data.LocationsList.size()-1).getSelectedItem().toString().equals("Default")) {
				JOptionPane.showMessageDialog(null, tip2, "Warning", JOptionPane.WARNING_MESSAGE);
			}
			else {
				ComboBoxModel mode1=new AModel(Common.Data.devices);
				JComboBox combo1=new JComboBox(mode1);
			
				ComboBoxModel mode2=new AModel(Common.Data.locations);
				JComboBox combo2=new JComboBox(mode2);
			
				ComboBoxModel mode3=new AModel(Common.Data.numbers);
				JComboBox combo3=new JComboBox(mode3);

				combo1.setBounds(10, 50 + num * Y, X, Y);
				combo2.setBounds(210, 50 + num * Y, X, Y);
				combo3.setBounds(410, 50 + num * Y, X, Y);
				try {
					frame.setSize(600, 250 + num * Y);
				} catch (Exception ex) {
					System.out.println("Exception");
				}
				num++;
				panel.add(combo1);
				panel.add(combo2);
				panel.add(combo3);
			
				Common.Data.DevicesList.add(combo1);
				Common.Data.LocationsList.add(combo2);
				Common.Data.NumbersList.add(combo3);
			
				button.setBounds(10, 70 + num * Y, X, 30);
				confirm.setBounds(130, 100 + (num+1) * Y, X, 30);
				cancel.setBounds(320, 100 + (num+1) * Y, X, 30);
				panel.repaint();
			}
		}
	}
	
	class ConfirmListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			String tip1 = "The device hasn't been chosen!";
			String tip2 = "The location hasn't been chosen!";
//			System.out.println("ListSize : " + Common.Data.DevicesList.size());
//			System.out.println("ListName : " + Common.Data.DevicesList.get(Common.Data.DevicesList.size()-1).getSelectedItem().toString());
			if (Common.Data.DevicesList.get(Common.Data.DevicesList.size()-1).getSelectedItem().toString().equals("Default")) {
				JOptionPane.showMessageDialog(null, tip1, "Warning", JOptionPane.WARNING_MESSAGE);
			}
			else if (Common.Data.LocationsList.get(Common.Data.LocationsList.size()-1).getSelectedItem().toString().equals("Default")) {
				JOptionPane.showMessageDialog(null, tip2, "Warning", JOptionPane.WARNING_MESSAGE);
			}
			else {
				try {
//				Common.Info.F_Array.clear();
//				model.ChangeInfo.setSN(0);

//				for(int i = 0; i < Common.Data.DevicesList.size(); i++) {
					int i;
					for (i = Common.Data.iterator; i < Common.Data.DevicesList.size(); i++) {
						String temp_number = Common.Data.numbers[Common.Data.NumbersList.get(i).getSelectedIndex()];
						int num = Integer.parseInt(temp_number);
						String furname = Common.Data.DevicesList.get(i).getSelectedItem().toString();
						int j;
						for (j = 0; j < Common.Data.devices.length; j++) {
							if (furname.equals(Common.Data.devices[j]))
								break;
						}
						String jsonpath = Common.Data.jsonpaths[j];
						for(int k = 0; k < num; k++) {
							String location = Common.Data.LocationsList.get(i).getSelectedItem().toString();
							int location_index = Common.Data.getLocationIndex(location);				
							int location_x = Common.Data.x[location_index];
							int location_y = Common.Data.y[location_index];
							model.ChangeInfo.addDevice(jsonpath, location_index, location_x, location_y);
							Data.IsModified = 1;
						}
					}
					Data.iterator = i;
					System.out.println("iterator : " + Common.Data.iterator);
					
					Data.furniturepanel.refresh();
					Data.demofurpanel.addDeviceRefresh();
					
/*				for(int i = 0; i < Common.Info.F_Array.size(); i++) {
					System.out.println(Common.Info.F_Array.get(i).furname);
					System.out.println(Common.Info.F_Array.get(i).SN);
					System.out.println(Common.Info.F_Array.get(i).initState);
				}*/
				
					frame.dispose();
					frame = null;
				} catch (Exception ex) {
					System.out.println("Exception");
					ex.printStackTrace();
				}
				Common.Data.display.refresh_devicelist();
				num = 1;
				Data.IsModified = 1;
			}
		}
	}
	
	class CancelListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {				
				frame.dispose();
				frame = null;
			} catch (Exception ex) {
				System.out.println("Exception");
				ex.printStackTrace();
			}
			num = 1;
		}
	}

}
