package UI;
import javax.swing.*;

import Common.Data;

import java.awt.event.*;
import java.io.File;

public class Menu {
	static final int WIDTH = 1000;
	static final int HEIGHT = 625;
	
	static final int PICWIDTH = 900;
	static final int PICHEIGHT = 516;
	static final int PICX = 0;
	static final int PICY = 100;
	
	static JFrame frame;
	public LogMenu loginpanel;
	
	public OperatePanel operatepanel;
	public ButtonPanel buttonpanel;
	public JButton enterbutton;
	
	public static void main(String[] args) {
		Data.menu = new Menu();	
	}
	
	public Menu() {
		frame = new JFrame();
		frame.setTitle("智能家居安全检验");
		frame.setIconImage(new ImageIcon("data/images/others/sun.png").getImage());
	
		loginpanel = new LogMenu();
		loginpanel.setBounds(0, 0, 1000, 600);
		enterbutton = new JButton(new ImageIcon("data/images/buttons/enter.png"));
		enterbutton.setBounds(455, 545, 90, 40);
		enterbutton.addActionListener(new ButtonListener());
		loginpanel.add(enterbutton);
		frame.add(loginpanel);

		operatepanel = new OperatePanel();				
		operatepanel.setBounds(50, 15, 900, 516);
		operatepanel.setVisible(false);
		frame.add(operatepanel);
		
		MenuBar mb = new MenuBar();
		frame.setJMenuBar(mb);
		
		buttonpanel = new ButtonPanel();
		buttonpanel.setBounds(25, 525, 950, 100);
		buttonpanel.setVisible(false);
		frame.add(buttonpanel);
		
		Data.display = new Display();
		Data.display.setVisible(false);

		Data.demoframe = new DemoFrame();
		Data.demoframe.setVisible(false);

		frame.setLayout(null);
		frame.setSize(WIDTH,HEIGHT);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setVisible(true);
		frame.addWindowListener(new exitListener());
	}

	class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			//initial furnitures list
			model.Io.initDevice();
			Data.devices = new String[Common.Info.FurList.size() + 1];
			Data.jsonpaths = new String[Common.Info.FurList.size() + 1];
			Data.devices[0] = "Default";
			Data.jsonpaths[0] = "Default";
			
			int i = 1;
			for(Common.FurListObj obj:Common.Info.FurList) {
				Data.devices[i] = obj.name;
				Data.jsonpaths[i] = obj.jsonpath;
				i++;
			}

			frame.remove(loginpanel);

			operatepanel.setVisible(true);
			buttonpanel.setVisible(true);
		
			frame.repaint();
			Data.IsEntered = true;
		}
	}
	
	class exitListener implements WindowListener {
		public void windowOpened(WindowEvent event) {
			// TODO Auto-generated method stub
		}

		public void windowClosing(WindowEvent event) {
			if(Data.IsModified == 0) System.exit(0);
			
			String tip = "Do you want to quit without saving?";
			Object[] options = {"Cancel", "No", "Yes"};
			int index = JOptionPane.showOptionDialog(null, tip, null, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			System.out.println("index : " + index);
			if (index == 0) return;
			else if (index == 2) System.exit(0);
			else {
				JFileChooser jfc=new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.SAVE_DIALOG);
				jfc.setCurrentDirectory(new File("data/records"));
				int returnVal = jfc.showSaveDialog(new JLabel());
				String filename = "";
				try {
					if (returnVal == JFileChooser.APPROVE_OPTION) {
		                File file = jfc.getSelectedFile();
		                filename = file.getName();
//						System.out.println(file.getName());
		            }
				} catch (Exception ex) {
					ex.printStackTrace();
				}
//				System.out.println("filename : " + filename);
				model.Io.saveIntoFile(filename);
				Data.IsModified = 0;
				System.exit(0);
			}
		}

		@Override
		public void windowClosed(WindowEvent e) {
			// TODO Auto-generated method stub				
		}

		@Override
		public void windowIconified(WindowEvent e) {
			// TODO Auto-generated method stub				
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO Auto-generated method stub				
		}

		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			// TODO Auto-generated method stub			
		}		
	}
}