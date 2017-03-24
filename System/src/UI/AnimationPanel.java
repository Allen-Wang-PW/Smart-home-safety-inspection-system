package UI;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.ScrollPaneConstants;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;
import javax.swing.border.LineBorder;
import Common.Data;

@SuppressWarnings("serial")
public class AnimationPanel extends JPanel {
	static JButton startbutton;
	static JButton stopbutton;
	static JButton resetbutton;
	static JScrollPane scroller;

	public AnimationPanel() {
		Data.demo_text = new JTextArea("", 100, 65);
		scroller = new JScrollPane(Data.demo_text);
		
		Data.timer = new Timer(2000, new TimerListener());
		startbutton = new JButton("Start");
		stopbutton = new JButton("Stop ");
		resetbutton = new JButton("Reset");
		
		startbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("timer start");
				Data.timer.start();
			}
		});
		
		stopbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Data.timer.stop();
			}
		});
		
		resetbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Data.demofurpanel.reset();
				Data.timer.stop();
			}
		});
		
		startbutton.setBounds(10, 10, 100, 30);
		stopbutton.setBounds(10, 45, 100, 30);
		resetbutton.setBounds(10, 80, 100, 30);

		Data.demo_text.setBorder(new LineBorder(Color.BLACK, 1));
		scroller.setBorder(BorderFactory.createTitledBorder("Devices State Informations"));
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		Data.demo_text.setEditable(false);
		scroller.setBounds(120, 10, 790, 100);
		
		setLayout(null);
		add(startbutton);
		add(stopbutton);
		add(resetbutton);
		add(scroller);
	}
}

class TimerListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		Data.demofurpanel.animationRefresh();
	}
}
