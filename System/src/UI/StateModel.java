package UI;

import javax.swing.DefaultComboBoxModel;

@SuppressWarnings({ "rawtypes", "serial", "unchecked" })
public class StateModel extends DefaultComboBoxModel {
	StateModel(Common.State state[]){
		addElement("Default");
		for (Common.State sta : state) {
			addElement(sta.name);
		}
	}
}
