package dev.teerayut.calamaro.utils;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.JPanel;

public class ScreenCenter {

	public static void centreWindow(Window frame) {
	    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	    int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
	    int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
	    frame.setLocation(x, y);
	}
	
	public static void centrePanel(JPanel panel) {
	    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	    int x = (int) ((dimension.getWidth() - panel.getWidth()) / 2);
	    int y = (int) ((dimension.getHeight() - panel.getHeight()) / 2);
	    panel.setLocation(x, y);
	}
}
