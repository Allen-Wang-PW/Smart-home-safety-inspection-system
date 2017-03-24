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
import Common.Furniture;
import Common.Info;

public class DeviceTable {
	public static JPanel displaydevice;
	private JTable table;
	
	public JPanel createPanel() {
		displaydevice = new JPanel();
		
		JScrollPane scrollPanel = new JScrollPane();  
		DeviceTableModel tableModel = new DeviceTableModel(Info.F_Array);//创建StudentTableModel，传入内容数据集合  
		table = new JTable(tableModel);//使用接受TableModel参数的构造方法创建JTable  
		
		table.getColumnModel().getColumn(0).setPreferredWidth(200);//设置列宽
		table.getColumnModel().getColumn(1).setPreferredWidth(430);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();//单元格渲染器  
		tcr.setHorizontalAlignment(JLabel.LEFT);//居中显示  
//		table.setDefaultRenderer(JButton.class, new ComboBoxCellRenderer());//设置渲染器  
		table.setDefaultRenderer(Object.class, tcr);//设置渲染器
		table.getColumnModel().getColumn(2).setCellEditor(new MyDeviceRender("Delete"));
		table.getColumnModel().getColumn(2).setCellRenderer(new MyDeviceRender("Delete"));
		
		scrollPanel.setViewportView(table);//将table放入scrollPanel
		scrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		displaydevice.setLayout(null);
		scrollPanel.setBounds(10, 10, 750, 100);
		displaydevice.add(scrollPanel);
		return displaydevice;
	}
	
	class DeviceTableModel extends AbstractTableModel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private ArrayList<Furniture> devicelist;
		private JButton[] deletebuts;
		
		private String headName[] = {"Furniture Name", "Furniture Description", ""};
		private Object obj[][];
		
		public DeviceTableModel(ArrayList<Furniture> list){//构造方法，在创建对象时就将数据传入  
			int size = list.size();
			this.devicelist = list;
			Object obj[][] = new Object[size][3];
			deletebuts = new JButton[size];
			for (int i = 0; i < size; i++) {
				deletebuts[i] = new JButton("Delete");
				obj[i][0] = list.get(i).furname + "_" + Integer.toString(list.get(i).SN);
				obj[i][1] = list.get(i).furnitureInfo.description;
				obj[i][2] = deletebuts[i];
			}
			this.obj = obj;
	    }  
		
		@Override
		public int getRowCount() {
			// TODO Auto-generated method stub
			return this.devicelist.size();
		}

		@Override
		public int getColumnCount() {
			// TODO Auto-generated method stub
			return 3;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			// TODO Auto-generated method stub
			return this.obj[rowIndex][columnIndex];
		}
		
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			if (columnIndex == 0)
				return false;
			return true;
		}
		
		public void setrule(ArrayList<Furniture> list) {  
	        this.devicelist = list;  
	    }
		
		public String getColumnName(int col) {
			return headName[col];
		}
	}
}

class MyDeviceRender extends AbstractCellEditor implements TableCellRenderer,ActionListener, TableCellEditor{
	 
    private static final long serialVersionUID = 1L;
    private JButton button =null;
    private int column;
    private int row;
    public MyDeviceRender(String name){
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
    	System.out.println("row : " + this.row + " col : " + this.column);	
    	model.ChangeInfo.delFur(row);
		Data.IsModified = 1;
		Data.display.refresh_devicelist();
		Data.display.refresh_rulelist();
		Data.display.refresh_specilist();
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
