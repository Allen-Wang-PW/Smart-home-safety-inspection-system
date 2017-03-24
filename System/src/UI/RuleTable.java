package UI;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import model.ChangeInfo;
import Common.Data;
import Common.Info;
import Common.Rule;

public class RuleTable {
	private JTable table;
	public static JPanel panel;
    
	public JPanel createPanel() {
		panel = new JPanel();
		JScrollPane scrollPanel = new JScrollPane();  
		RuleTableModel tableModel = new RuleTableModel(Info.R_Array);//创建StudentTableModel，传入内容数据集合  
		table = new JTable(tableModel);//使用接受TableModel参数的构造方法创建JTable  
		
		table.getColumnModel().getColumn(0).setPreferredWidth(20);//设置列宽
		table.getColumnModel().getColumn(1).setPreferredWidth(120);
		table.getColumnModel().getColumn(2).setPreferredWidth(90);
		table.getColumnModel().getColumn(3).setPreferredWidth(20);
		table.getColumnModel().getColumn(4).setPreferredWidth(20);
		table.getColumnModel().getColumn(5).setPreferredWidth(40);
		table.getColumnModel().getColumn(6).setPreferredWidth(120);
		table.getColumnModel().getColumn(7).setPreferredWidth(100);
		table.getColumnModel().getColumn(8).setPreferredWidth(100);
		table.getColumnModel().getColumn(9).setPreferredWidth(100);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();//单元格渲染器  
		tcr.setHorizontalAlignment(JLabel.CENTER);//居中显示  
		table.setDefaultRenderer(Object.class, tcr);//设置渲染器
		
		
		try {
			DefaultTableCellRenderer mycolor = new DefaultTableCellRenderer() {  
			  
			    /**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				public Component getTableCellRendererComponent(JTable table,  
			            Object value, boolean isSelected, boolean hasFocus,  
			            int row, int column) { 
//					int flag = 1;
//					if (rows != null && cols != null) {
//						for (int i = 0; i < rows.length; i++) {
//							if (row == rows[i]) {
//								for (int j = 0; j < cols.length; j++) {
//									if (column == cols[j]) {
//										setForeground(Color.RED);
//										flag = 0;
//									}
//								}
//							}
//						}
//					}
//					if (flag == 1)
//						setForeground(Color.BLACK);
					if (Data.modifycell[row][column] == 1)
						setForeground(Color.RED);
					else
						setForeground(Color.BLACK);
//                if (Double.parseDouble(table.getValueAt(row, 11).toString()) > 0) {  
//                    setBackground(Color.red);  
//                }                    //如果需要设置某一个Cell颜色，需要加上column过滤条件即可  
			        return super.getTableCellRendererComponent(table, value,  
			                isSelected, hasFocus, row, column);  
			    }  
			};  
			
//			table.getColumnModel().getColumn(0).setCellRenderer(mycolor);
			mycolor.setHorizontalAlignment(JLabel.CENTER);//居中显示 
			for (int i = 0; i < 8; i++)
				table.getColumnModel().getColumn(i).setCellRenderer(mycolor);
//			if (cols != null) {
//				for (int i = 0; i < cols.length; i++) {
//					table.getColumnModel().getColumn(cols[i]).setCellRenderer(mycolor);
//				}
//			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		table.getColumnModel().getColumn(8).setCellEditor(new MyRender("Modify"));
		table.getColumnModel().getColumn(8).setCellRenderer(new MyRender("Modify"));
		table.getColumnModel().getColumn(9).setCellEditor(new MyRender("Delete"));
		table.getColumnModel().getColumn(9).setCellRenderer(new MyRender("Delete"));
		table.setRowSelectionAllowed(false);

		scrollPanel.setViewportView(table);//将table放入scrollPanel
		scrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		panel.setLayout(null);
		scrollPanel.setBounds(10, 10, 750, 100);
		panel.add(scrollPanel);
		return panel;
	}
	
	class RuleTableModel extends AbstractTableModel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private ArrayList<Rule> rulelist;
		private JButton[] modifybuts;
		private JButton[] deletebuts;
		
//		private String headName[];
		private Object obj[][];
		
		public RuleTableModel(ArrayList<Rule> list){//构造方法，在创建对象时就将数据传入  
			int size = list.size();
			this.rulelist = list;
			Object obj[][] = new Object[size][10];
			modifybuts = new JButton[size];
			deletebuts = new JButton[size];
			for (int i = 0; i < size; i++) {
				modifybuts[i] = new JButton("Modify");
				deletebuts[i] = new JButton("Delete");
				if (list.get(i).sigTrig != null) {
					obj[i][0] = "IF";
					obj[i][1] = Info.getFurName(list.get(i).srcSN);
					obj[i][2] = list.get(i).sigTrig;
					obj[i][3] = obj[i][4] = "";
					obj[i][5] = "THEN";
					obj[i][6] = Info.getFurName(list.get(i).dstSN);
					obj[i][7] = list.get(i).dstAct;
					obj[i][8] = modifybuts[i];
					obj[i][9] = deletebuts[i];
				}
				else {
					obj[i][0] = "IF";
					obj[i][1] = Info.getFurName(list.get(i).srcSN);
					obj[i][2] = list.get(i).trigger.var;
					obj[i][3] = list.get(i).trigger.rel.getStr_f();
					obj[i][4] = list.get(i).trigger.value;
					obj[i][5] = "THEN";
					obj[i][6] = Info.getFurName(list.get(i).dstSN);
					obj[i][7] = list.get(i).dstAct;
					obj[i][8] = modifybuts[i];
					obj[i][9] = deletebuts[i];
				}
			}
			this.obj = obj;
	    }  
		
		@Override
		public int getRowCount() {
			// TODO Auto-generated method stub
			return this.rulelist.size();
		}

		@Override
		public int getColumnCount() {
			// TODO Auto-generated method stub
			return 10;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			// TODO Auto-generated method stub
			return this.obj[rowIndex][columnIndex];
		}
		
		public boolean isCellEditable(int rowIndex, int columnIndex) {
//			if (columnIndex == 0 || columnIndex == 5)
//				return false;
//			return true;
			if (columnIndex == 8 || columnIndex == 9)
				return true;
			return false;
		}
		
		public void setrule(ArrayList<Rule> rule) {  
	        this.rulelist = rule;  
	    }
		
		public String getColumnName(int col) {
			return " ";
		}
		
		public void setValueAt(Object value, int rowIndex, int columnIndex){     
			this.obj[rowIndex][columnIndex] = value;
			this.fireTableCellUpdated(rowIndex, columnIndex);     
		}     
	}
}

class MyRender extends AbstractCellEditor implements TableCellRenderer,ActionListener, TableCellEditor{
	 
    private static final long serialVersionUID = 1L;
    private JButton button =null;
    private int column;
    private int row;
    private int flag;
    public MyRender(String name){
        button = new JButton(name);
        if (name.equals("Modify"))
        	this.flag = 1;
        else
        	this.flag = 2;
        button.addActionListener(this);
    }
 
    @Override
    public Object getCellEditorValue() {
        // TODO Auto-generated method stub
        return null;
    }
 
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        // TODO Auto-generated method stub
        return button;
    }
 
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub    	
    	System.out.println("row : " + this.row + " col : " + this.column);
    	if (this.flag == 2) {	
				ChangeInfo.delRule(row);
				Data.display.refresh_rulelist();
				Data.display.refresh_specilist();
				Data.IsModified = 1;
    	}
    	else {
    		Rule rule = Info.R_Array.get(row);
    		ModifyFrame modify = new ModifyFrame();
    		@SuppressWarnings("unused")
			JFrame mf = modify.createFrame(rule, row);
    		Data.modifyrows.add(row);
    	}
    }
 
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        // TODO Auto-generated method stub
    	this.row = row;
    	this.column = column;
        return button;
    }
     
}