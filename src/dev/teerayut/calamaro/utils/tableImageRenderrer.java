package dev.teerayut.calamaro.utils;

import java.awt.Color;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class tableImageRenderrer extends DefaultTableCellRenderer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
		JLabel lb = new JLabel();
		lb.setIcon(new ImageIcon(getClass().getResource(arg1.toString())));
		lb.setBackground(Color.WHITE);
		lb.setBorder(null);
		lb.setHorizontalAlignment(JLabel.CENTER);
        return lb;
	}

}
