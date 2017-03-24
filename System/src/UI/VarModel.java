package UI;

import javax.swing.DefaultComboBoxModel;

@SuppressWarnings({ "rawtypes", "serial", "unchecked" })
public class VarModel extends DefaultComboBoxModel {
	VarModel(Common.Variable var){
		addElement("Default");
		for (int i = var.lowBond; i <= var.highBond; i++) {
			String temp = Integer.toString(i);
			addElement(temp);
		}
	}
}
