package UI;

import javax.swing.DefaultComboBoxModel;

@SuppressWarnings({ "rawtypes", "serial", "unchecked" })
public class VaribleModel extends DefaultComboBoxModel{
	VaribleModel(Common.Variable[] var){
		addElement("Default");
		for (Common.Variable sta : var) {
			addElement(sta.name);
		}
	}
}
