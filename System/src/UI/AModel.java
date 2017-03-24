package UI;
import javax.swing.*;

@SuppressWarnings({ "serial", "rawtypes", "unchecked" })
public class AModel extends DefaultComboBoxModel {
	AModel(String[] s){
		for (int i=0;i<s.length;i++)
			addElement(s[i]);
	}
}
