package UI;

import javax.swing.DefaultComboBoxModel;

@SuppressWarnings({ "serial", "rawtypes" })
public class CheckModel extends DefaultComboBoxModel {
	@SuppressWarnings("unchecked")
	CheckModel(Common.Furniture fur){
		addElement("Default");
		addElement("State");
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
