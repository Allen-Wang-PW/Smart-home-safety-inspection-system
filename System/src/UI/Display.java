package UI;
import javax.swing.*;

import Common.Data;

@SuppressWarnings("serial")
public class Display extends JFrame {
	public static JPanel paneldl;
	public static JPanel panelrl;
	public static JPanel panelsl;
	
	public static int inc = 1;
	public static int X = 800;
	public static int Y = 450;
	
	public Display() {
		this.setTitle("Display");
		this.setSize(X, Y);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setLayout(null);
		
		DeviceTable dt = new DeviceTable();
		paneldl = dt.createPanel();
		paneldl.setBounds(0, 10, 800, 120);
		this.add(paneldl);
		
		RuleTable rt = new RuleTable();
		panelrl = rt.createPanel();
		panelrl.setBounds(0,140,800,120);
		this.add(panelrl);
		
		SpeciTable st = new SpeciTable();
		panelsl = st.createPanel();
		panelsl.setBounds(0, 290, 800, 120);
		this.add(panelsl);
	}
	
	public void refresh_devicelist() {
		this.remove(paneldl);
		DeviceTable dt = new DeviceTable();
		paneldl = dt.createPanel();
		paneldl.setBounds(0, 10, 800, 120);
		this.add(paneldl);
		this.setSize(X, (Y += inc));
		this.repaint();
		
		Data.furniturepanel.refresh();
		Data.demofurpanel.addDeviceRefresh();
		
//		System.out.println("After the modify of devices :");
//		for (Furniture sta : Info.F_Array) {
//			System.out.println(sta.furname + "_" + Integer.toString(sta.SN));
//		}
//		System.out.println("");
//		for (Rule sta : Info.R_Array) {
//			System.out.println(sta.createRule());
//		}
//		System.out.println("");
//		for (FsmSpec sta : Info.FsmArray) {
//			System.out.println(sta.spe);
//		}
	}
	
	public void refresh_rulelist() {
		this.remove(panelrl);
		RuleTable rt = new RuleTable();
		panelrl = rt.createPanel();
		panelrl.setBounds(0,140,800,120);
		this.add(panelrl);
		this.setSize(X, (Y += inc));
		this.repaint();
	}
	
	public void refresh_rulelist(int flag, int row) {
		this.remove(panelrl);
		RuleTable rt = new RuleTable();
		panelrl = rt.createPanel();
		panelrl.setBounds(0,140,800,120);
//		if (flag == 1)
//			rt.getRC(row, null);
		this.add(panelrl);
		this.setSize(X, (Y += inc));
		this.repaint();
	}
	
	public void refresh_rulelist(int flag, int[] rows, int[] cols) {
		this.remove(panelrl);
		RuleTable rt = new RuleTable();
		panelrl = rt.createPanel();
		panelrl.setBounds(0,140,800,120);
		this.add(panelrl);
		this.setSize(X, (Y += inc));
		this.repaint();
	}
	
	public void refresh_specilist() {
		this.remove(panelsl);
		SpeciTable st = new SpeciTable();
		panelsl = st.createPanel();
		panelsl.setBounds(0, 290, 800, 120);
		this.add(panelsl);
		this.setSize(X, (Y -= inc));
		this.repaint();
	}
}
