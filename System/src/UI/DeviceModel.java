package UI;

import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;

@SuppressWarnings({ "rawtypes", "serial", "unchecked" })
public class DeviceModel extends DefaultComboBoxModel {
	DeviceModel(ArrayList<Common.Furniture> fur){
		addElement("Default");
		for (Common.Furniture sta : fur) {
			String temp = sta.furname + "_" + Integer.toString(sta.SN);
			addElement(temp);
		}
	}
}
