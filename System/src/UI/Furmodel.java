package UI;

import javax.swing.DefaultComboBoxModel;

@SuppressWarnings({ "rawtypes", "serial", "unchecked" })
public class Furmodel extends DefaultComboBoxModel {
	Furmodel(Common.Furniture fur){
		addElement("Default");
		for (Common.Variable start : fur.variArr) {
			addElement(start.name);
		}
		for (Common.Transition start : fur.transArr) {
			if (start.signal)
				addElement(start.name);
		}
		for (Common.Action start : fur.actionArr) {
			if (start.signal)
				addElement(start.name);
		}
	}
}
