package UI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

@SuppressWarnings("serial")
class MyRenderer extends DefaultListCellRenderer {

    private Font font1;
    @SuppressWarnings("unused")
	private Font font2;
    private Color rowcolor;
    private int row;
    private int[] rows;

    public MyRenderer() {
        this.font1 = getFont();
        this.font2 = font1.deriveFont((float) (font1.getSize() + 10));
    }

    public MyRenderer(int row, Color color) {
        this.rowcolor = color;
        this.row = row;
    }

    public MyRenderer(int[] rows, Color color) {
        this.rowcolor = color;
        this.rows = rows;
    }

    @SuppressWarnings("rawtypes")
	public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (rows==null) {
            if (index == row) {
                setForeground(this.rowcolor);
//                setFont(getFont().deriveFont((float) (getFont().getSize() + 2)));
            }
        }
        else {
            for (int i = 0; i < rows.length; i++) {
                if (index == rows[i]) {
//					setBackground(this.rowcolor);
                    setForeground(this.rowcolor);
//					setFont(getFont().deriveFont((float) (getFont().getSize() + 2)));
                }
            }
        }

        return this;
    }
}
