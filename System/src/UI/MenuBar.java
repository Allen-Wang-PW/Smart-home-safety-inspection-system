package UI;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.JRadioButtonMenuItem;

import Common.CheckType;
import Common.Data;
import Common.Info;

@SuppressWarnings("serial")
public class MenuBar extends JMenuBar {
	static final int WIDTH = 1000;
	static final int HEIGHT = 600;
	
	static JFrame frame;
	public MenuBar() {
		//Set File
		JMenu filemenu=new JMenu("File");
		filemenu.setMnemonic('F');
//		JMenuItem newitem=new JMenuItem("New");
//		newitem.addActionListener(new newListener());
//		newitem.setAccelerator(KeyStroke.getKeyStroke('N',java.awt.Event.CTRL_MASK,false));
		JMenuItem openitem=new JMenuItem("Open");
		openitem.addActionListener(new openListener());
		openitem.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
		JMenuItem saveitem=new JMenuItem("Save");
		saveitem.addActionListener(new saveListener());
		saveitem.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
		JMenuItem closeitem=new JMenuItem("Close");
		closeitem.addActionListener(new closeListener());
		closeitem.setAccelerator(KeyStroke.getKeyStroke("ctrl Q"));

		filemenu.add(openitem);
		filemenu.addSeparator();
		filemenu.add(saveitem);
		filemenu.addSeparator();
		filemenu.add(closeitem);
		add(filemenu);

		//Set Window
		JMenu windowmenu = new JMenu("Window");
		windowmenu.setMnemonic('W');
		JMenuItem displayitem = new JMenuItem("Display");
		displayitem.setAccelerator(KeyStroke.getKeyStroke("ctrl D"));
		displayitem.addActionListener(new displayListener());
		windowmenu.add(displayitem);
		add(windowmenu);
		
		//set Add
		JMenu addmenu = new JMenu("Add");
		addmenu.setMnemonic('A');
		JMenuItem adddeviceitem = new JMenuItem("Add Device");
		adddeviceitem.setAccelerator(KeyStroke.getKeyStroke("ctrl shift released D"));
		adddeviceitem.addActionListener(new addDeviceListener());
		JMenuItem addruleitem = new JMenuItem("Add Rule");
		addruleitem.setAccelerator(KeyStroke.getKeyStroke("ctrl shift released R"));
		addruleitem.addActionListener(new addRuleListener());
		JMenuItem addspeitem = new JMenuItem("Add Specification");
		addspeitem.setAccelerator(KeyStroke.getKeyStroke("ctrl shift released S"));
		addspeitem.addActionListener(new addSpeciListener());
		addmenu.add(adddeviceitem);
		addmenu.addSeparator();
		addmenu.add(addruleitem);
		addmenu.addSeparator();
		addmenu.add(addspeitem);
		add(addmenu);
		
		//set Mode
		JMenu modemenu = new JMenu("Mode");
		modemenu.setMnemonic('M');
		JRadioButtonMenuItem fsmitem = new JRadioButtonMenuItem("Fsm",true);
//		System.out.println("init mode:" + Info.Mode);
		fsmitem.addActionListener(new fsmListener());
		JRadioButtonMenuItem lhaitem = new JRadioButtonMenuItem("Lha");
		lhaitem.addActionListener(new lhaListener());
		
		ButtonGroup group = new ButtonGroup();
		group.add(fsmitem);
		group.add(lhaitem);
		
		modemenu.add(fsmitem);
		modemenu.addSeparator();
		modemenu.add(lhaitem);
		add(modemenu);
		
		//Set Help
		JMenu helpmenu=new JMenu("Help");
		helpmenu.setMnemonic('H');
		JMenuItem aboutitem=new JMenuItem("About");
		aboutitem.setAccelerator(KeyStroke.getKeyStroke('A',java.awt.Event.CTRL_MASK,false));
		aboutitem.addActionListener(new aboutListener());
		helpmenu.add(aboutitem);
		add(helpmenu);
	}
			
	class openListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if(Data.IsModified == 1) {
				String tip = "System has been modified, save changes?";
				String title = "Save Resource";
				Object[] options = {"Yes", "No", "Cancel"};

				int index = JOptionPane.showOptionDialog(Menu.frame, tip, title, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				if (index == 0) {
					JFileChooser jfc=new JFileChooser();
					jfc.setFileSelectionMode(JFileChooser.SAVE_DIALOG);
					jfc.setCurrentDirectory(new File("data/records"));
					int returnVal = jfc.showSaveDialog(new JLabel());
					String filename = "";
					try {
						if (returnVal == JFileChooser.APPROVE_OPTION) {
							File file = jfc.getSelectedFile();
							filename = file.getName();
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					model.Io.saveIntoFile(filename);
				}
				else if (index == 1) {}
				else {
					return;
				}
			}
			JFileChooser jfc=new JFileChooser();
			jfc.setFileSelectionMode(JFileChooser.FILES_ONLY );
			jfc.setCurrentDirectory(new File("data/records"));
			jfc.showDialog(new JLabel(), "Open");

			String filename = null;
			try {
				filename = jfc.getSelectedFile().getName();
				
				Info.LhaArray.clear();
				Info.FsmArray.clear();
				Info.R_Array.clear();
				Info.F_Array.clear();			
				
				model.Io.initFromFile(filename);
				
				Data.IsModified = 0;
				
				Data.check_text.setText("");
				Data.fix_text.setText("");
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			System.out.println("filename : " + filename);
			
			

			// 点击open之后自动跳转到Furniture界面
			if(Data.IsEntered == false) {
				Data.menu.enterbutton.doClick();
			}
			else {
				Data.menu.operatepanel.pane.setSelectedIndex(0);
			}

			Data.menu.buttonpanel.fix.setEnabled(false);
			Data.menu.buttonpanel.demo.setEnabled(false);

			Data.furniturepanel.refresh();
			Data.demofurpanel.addDeviceRefresh();
		}
	}

	class closeListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			//添加关闭前应做的准备工作
			if(Data.IsModified == 0) {	//
				System.exit(0);
			}
			
			String tip = "Do you want to quit without saving?";
			Object[] options = {"Cancel", "No", "Yes"};
			int index = JOptionPane.showOptionDialog(null, tip, null, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			System.out.println("index : " + index);
			if (index == 0)
				;
			else if (index == 2) {
				System.exit(0);
			}
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
		            }
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				model.Io.saveIntoFile(filename);
				Data.IsModified = 0;
			}
		}
	}
	
	class saveListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			JFileChooser jfc = new JFileChooser();
			jfc.setFileSelectionMode(JFileChooser.CUSTOM_DIALOG);
			jfc.setCurrentDirectory(new File("data/records"));
			int returnVal = jfc.showSaveDialog(new JLabel());
			String filename = "";
			try {
				if (returnVal == JFileChooser.APPROVE_OPTION) {
	                File file = jfc.getSelectedFile();
	                filename = file.getName();
//					System.out.println(file.getName());
	            }
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			model.Io.saveIntoFile(filename);
			Data.IsModified = 0;
		}
	}
	
	class fsmListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			Info.Mode = CheckType.Fsm;
//			System.out.println("mode:" + Info.Mode);
		}
	}

	class lhaListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			Info.Mode = CheckType.Lha;
//			System.out.println("mode:" + Info.Mode);
		}		
	}	

	class displayListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if(Data.display != null) {
				Data.display.refresh_devicelist();
				Data.display.refresh_rulelist();
				Data.display.refresh_specilist();
				Data.display.setVisible(true);
			}
		}
	}

	class aboutListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			JFrame acFrame = new JFrame("About");

			JPanel panel = new JPanel();
			panel.setLayout(null);
			acFrame.add(panel);
			
			JTextArea text = new JTextArea();
			text.setLineWrap(true);
			text.setWrapStyleWord(true);
			text.setOpaque(true);
			
			String lineSeparator = System.getProperty("line.separator");
			String actxt ="智能家居安全验证系统" + lineSeparator + lineSeparator +
			"Thank for professor Bu Lei, Xiong Wen" + lineSeparator + 
			"Group of Wang Xizao, Wang Ya'nan, Ma Rao, Shen Siyuan." + lineSeparator + lineSeparator +
			"Copyright @2015-2017"
			;
			
			text.setText(actxt);			
			text.setBounds(10, 10, 285, 120);
			text.setBorder(new LineBorder(Color.RED, 1));
			text.setEditable(false);
			panel.add(text);
			
			JButton jbOK = new JButton("OK");
			jbOK.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent event) {
					acFrame.dispose();
				}
			});
			panel.add(jbOK);
			jbOK.setBounds(215, 140, 80, 25);
			
			acFrame.setSize(305, 175);
			acFrame.setResizable(false);
			acFrame.setLocationRelativeTo(null);
			acFrame.setVisible(true);
		}
	}	
	
	class addDeviceListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			AddDevicePanel adp = new AddDevicePanel();
			JFrame frame = adp.createFrame();
			frame.setTitle("Add Devices");
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		}
	}
	
	class addRuleListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			JFrame frame;
			AddRulePanel arp = new AddRulePanel();
			frame = arp.createFrame();
			frame.setTitle("Add Rules");
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		}
	}
	
	class addSpeciListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			
			AddSpeciPanel arp = new AddSpeciPanel();
			JFrame frame = arp.createFrame();
			frame.setTitle("Add Specifications");
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		}
	}
}
