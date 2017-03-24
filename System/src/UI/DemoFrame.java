package UI;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.border.LineBorder;

import Common.Data;

@SuppressWarnings("serial")
public class DemoFrame extends JFrame {
	static final int WIDTH = 920;
	static final int HEIGHT = 616;
	
	public DemoFrame() {
		AnimationPanel demoPanel = new AnimationPanel();
		demoPanel.setBounds(0, 0, 920, 110);
		add(demoPanel);
		
		Data.demofurpanel = new DemoFurPanel();
		Data.demofurpanel.setBorder(new LineBorder(Color.BLACK, 1));
		Data.demofurpanel.setBounds(10, 120, 900, 486);
		add(Data.demofurpanel);
		
		setLayout(null);
		setTitle("Demo");
		setSize(WIDTH,HEIGHT);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}
}
