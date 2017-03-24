package UI;

import javax.swing.DefaultComboBoxModel;

@SuppressWarnings({ "rawtypes", "serial", "unchecked" })
public class ActionModel extends DefaultComboBoxModel{
	ActionModel(Common.Action[] act){
		addElement("Default");
		for (Common.Action sta : act) {
			addElement(sta.name);
		}
	}
}
