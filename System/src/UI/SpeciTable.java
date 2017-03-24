package UI;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import Common.Data;
import Common.FsmSpec;
import Common.Info;
import Common.LhaSpec;

public class SpeciTable {
	private JTable table;
	
	public JPanel createPanel() {
		JPanel displayspeci = new JPanel();
		
		JScrollPane scrollPanel = new JScrollPane();
		if (Common.Info.getMode() == Common.CheckType.Fsm) {
			SpeciFsmTableModel tableModel = new SpeciFsmTableModel(Info.FsmArray);//创建StudentTableModel，传入内容数据集合  
			table = new JTable(tableModel);//使用接受TableModel参数的构造方法创建JTable  
		}
		else {
			SpeciLhaTableModel tableModel = new SpeciLhaTableModel(Info.LhaArray);//创建StudentTableModel，传入内容数据集合  
			table = new JTable(tableModel);//使用接受TableModel参数的构造方法创建JTable  
		}
		
		table.getColumnModel().getColumn(0).setPreferredWidth(630);//设置列宽
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();//单元格渲染器  
		tcr.setHorizontalAlignment(JLabel.CENTER);//居中显示  
//		table.setDefaultRenderer(JButton.class, new ComboBoxCellRenderer());//设置渲染器  
		table.setDefaultRenderer(Object.class, tcr);//设置渲染器
		table.getColumnModel().getColumn(1).setCellEditor(new MySpeciRender("Delete"));
		table.getColumnModel().getColumn(1).setCellRenderer(new MySpeciRender("Delete"));
		
		scrollPanel.setViewportView(table);//将table放入scrollPanel
		scrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		displayspeci.setLayout(null);
		scrollPanel.setBounds(10, 10, 750, 100);
		displayspeci.add(scrollPanel);
		return displayspeci;
	}
	
	class SpeciFsmTableModel extends AbstractTableModel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private ArrayList<FsmSpec> Fsmlist;
		private JButton[] deletebuts;
		
//		private String headName[];
		private Object objFsm[][];
		
		public SpeciFsmTableModel(ArrayList<FsmSpec> list){//构造方法，在创建对象时就将数据传入  
			int size = list.size();
			this.Fsmlist = list;
			Object objFsm[][] = new Object[size][2];
			deletebuts = new JButton[size];
			for (int i = 0; i < size; i++) {
				deletebuts[i] = new JButton("Delete");
				objFsm[i][0] = list.get(i).spe;
				objFsm[i][1] = deletebuts[i];
			}
			this.objFsm = objFsm;
	    }  
		
		@Override
		public int getRowCount() {
			// TODO Auto-generated method stub
			return this.Fsmlist.size();
		}

		@Override
		public int getColumnCount() {
			// TODO Auto-generated method stub
			return 2;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			// TODO Auto-generated method stub
			return this.objFsm[rowIndex][columnIndex];
		}
		
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			if (columnIndex == 0)
				return false;
			return true;
		}
		
		public void setrule(ArrayList<FsmSpec> list) {  
	        this.Fsmlist = list;  
	    }
		
		public String getColumnName(int col) {
			return " ";
		}
	}
	
	class SpeciLhaTableModel extends AbstractTableModel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private ArrayList<LhaSpec> Lhalist;
		private JButton[] deletebuts;
		
//		private String headName[];
		private Object objLha[][];
		
		public SpeciLhaTableModel(ArrayList<LhaSpec> list){//构造方法，在创建对象时就将数据传入  
			int size = list.size();
			this.Lhalist = list;
			Object objLha[][] = new Object[size][2];
			deletebuts = new JButton[size];
			for (int i = 0; i < size; i++) {
				deletebuts[i] = new JButton("Delete");
				objLha[i][0] = list.get(i).createSpe();
				objLha[i][1] = deletebuts[i];
			}
			this.objLha = objLha;
	    }  
		
		@Override
		public int getRowCount() {
			// TODO Auto-generated method stub
			return this.Lhalist.size();
		}

		@Override
		public int getColumnCount() {
			// TODO Auto-generated method stub
			return 2;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			// TODO Auto-generated method stub
			return this.objLha[rowIndex][columnIndex];
		}	
		
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			if (columnIndex == 0)
				return false;
			return true;
		}
		
		public void setrule(ArrayList<LhaSpec> list) {  
	        this.Lhalist = list;  
	    }
		
		public String getColumnName(int col) {
			return " ";
		}
	}
}

class MySpeciRender extends AbstractCellEditor implements TableCellRenderer,ActionListener, TableCellEditor{
	 
    private static final long serialVersionUID = 1L;
    private JButton button =null;
    private int column;
    private int row;
    public MySpeciRender(String name){
        button = new JButton(name);
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
        // JOptionPane.showMessageDialog(null, "渲染器学期", "消息", JOptionPane.OK_OPTION);
    	
    	System.out.println("row : " + this.row + " col : " + this.column);
    	if (Common.Info.getMode() == Common.CheckType.Fsm) {
    		model.ChangeInfo.delSpecFsm(row);
			Common.Data.display.refresh_specilist();
			Data.IsModified = 1;
    	}
    	else {
    		model.ChangeInfo.delSpecLha(row);
			Common.Data.display.refresh_specilist();
			Data.IsModified = 1;
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

