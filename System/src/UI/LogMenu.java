package UI;
import java.awt.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class LogMenu extends JPanel {
/*	public static void main(String[] args) {
		JFrame frame = new JFrame();
		LogMenu panel = new LogMenu();
		JPanel temp = panel.createPanel();
		frame.getContentPane().add(temp);
		frame.setSize(1000,600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}*/
	
	public LogMenu() {
		setBounds(0, 0, 1000, 500);
		setLayout(null);
		//label:欢迎来到智能家居安全验证系统
		JLabel label = new JLabel(new ImageIcon("data/images/labels/welcome.png"));
		label.setBounds(250, 0, 500, 60);
		add(label);
	}
	
	public void paintComponent(Graphics g) {
		Image image = new ImageIcon("data/images/background/MainBg2.jpg").getImage();
		g.drawImage(image, 140, 60, 720, 470, this);
	}
}
