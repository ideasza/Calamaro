package dev.teerayut.calamaro.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;



public class Preferrence {

	private Properties prop = new Properties();
	public Preferrence() {
		File file = new File(Config.CONFIG_PATH);
		try {
			if (!file.exists()) {
			    if (file.mkdirs()) {
			    } else {
			    	final ImageIcon icon = new ImageIcon(getClass().getResource("/fail32.png"));
	                JOptionPane.showMessageDialog(null, "ไม่สามารถสร้างโฟลเดอร์ได้", "Alert", JOptionPane.ERROR_MESSAGE, icon);
	                
			    }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setPreferrence(String key, String value) {
		OutputStream output = null;
		InputStream in = null;
		File file = null;
		Properties prop = new Properties();
		try {
			file = new File(Config.CONFIG_PATH + "config.properties");
			if (file.exists()) {
				in = new FileInputStream(Config.CONFIG_PATH + "config.properties");
				prop.load(in);
				if (prop.getProperty(key) != null) {
					output = new FileOutputStream(Config.CONFIG_PATH + "config.properties");
				} else {
					output = new FileOutputStream(Config.CONFIG_PATH + "config.properties", true);
				}
			} else {
				output = new FileOutputStream(Config.CONFIG_PATH + "config.properties");
			}
			
			prop.setProperty(key, value);
			prop.store(output, null);
		} catch (IOException io) {
			io.printStackTrace();
			final ImageIcon icon = new ImageIcon(getClass().getResource("/fail32.png"));
	        JOptionPane.showMessageDialog(null, "config.prop : " + io.getMessage(), "Alert", JOptionPane.ERROR_MESSAGE, icon);
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public String getPreferrence(String key) {
		String str = "";
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream(Config.CONFIG_PATH + "config.properties");
			prop.load(input);
			str = prop.getProperty(key);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return str;
	}
}
