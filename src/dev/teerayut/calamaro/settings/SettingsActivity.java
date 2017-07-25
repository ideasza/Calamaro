package dev.teerayut.calamaro.settings;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterState;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import dev.teerayut.calamaro.utils.Config;
import dev.teerayut.calamaro.utils.Preferrence;
import dev.teerayut.calamaro.utils.ScreenCenter;

public class SettingsActivity extends JDialog{

	public static final int ID_OK = 1;
    public static final int ID_CANCEL = 0;
    private int exitCode = ID_CANCEL;
    
    public SettingsActivity(Frame owner) {
        super(owner);
        createGUI();
    }

    /**
     * @wbp.parser.constructor
     */
    public SettingsActivity(Dialog owner) {
        super(owner);        
        createGUI();
    }
    
    private Preferrence prefs;
    
    private javax.swing.JPanel topPanel;
    
    private JFileChooser chooser = new JFileChooser();;
    private File file;

    private JTextField textField;
    private void createGUI() {
    	prefs = new Preferrence();
    	int width = 680;
    	int height = 480;
    	setBounds(0, 0, 681, 248);
    	setTitle("ตั้งค่า");
    	setIconImage(new ImageIcon(getClass().getResource("/ic_calamaro.png")).getImage());
    	new ScreenCenter().centreWindow(this);
    	getContentPane().setLayout(null);
    	
    	topPanel = new javax.swing.JPanel();
    	topPanel.setBounds(0, 0, 660, 135);
    	topPanel.setBorder(BorderFactory.createTitledBorder("Database"));
    	getContentPane().add(topPanel);
    	topPanel.setLayout(null);
    	
    	textField = new JTextField();
    	textField.setFont(new Font("Angsana New", Font.PLAIN, 25));
    	textField.setBounds(60, 53, 425, 40);
    	topPanel.add(textField);
    	textField.setColumns(10);
    	
    	JButton btnNewButton = new JButton("Browse");
    	btnNewButton.addMouseListener(new MouseAdapter() {
    		@Override
    		public void mouseClicked(MouseEvent arg0) {
    			FileNameExtensionFilter filter = new FileNameExtensionFilter("SQLite", "sqlite");
    			chooser.setFileFilter(filter);
    			int returnVal = chooser.showOpenDialog(null);
                if(returnVal == chooser.APPROVE_OPTION){
                	file = chooser.getSelectedFile();
                    String filename = file.getName();
                    textField.setText(file.getAbsolutePath().toString());
                    
                    createFolder();
                    copyFile(file.getAbsolutePath().toString(), Config.DB_PATH + Config.DB_FILE);
                }
    		}
    	});
    	btnNewButton.setBounds(497, 53, 97, 40);
    	topPanel.add(btnNewButton);
        
        
        File database = new File(Config.DB_PATH + Config.DB_FILE);
    	if (database.exists()) {
    		textField.setText(database.getAbsolutePath().toString());	
    	}
    	
    	
    	JButton button = new JButton("ตกลง");
    	button.setBounds(554, 148, 97, 40);
    	getContentPane().add(button);
    	button.setPreferredSize(new Dimension(90, 35));
    	button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				prefs.setPreferrence("db_path", textField.getText().toString());
        		final ImageIcon icon = new ImageIcon(getClass().getResource("/success32.png"));
		        JOptionPane.showMessageDialog(null, "บันทึกการตั้งค่าแล้ว", "Complete", JOptionPane.ERROR_MESSAGE, icon);
		        
			}
		});    	
    }
    
    
    @Override
    public void dispose() {
        super.dispose();
    }

    public int doModal() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setModal(true);
        setVisible(true);
        return exitCode;
    }
    
    private void copyFile(String from, String to){
    	Path FROM = Paths.get(from);
		Path TO = Paths.get(to);
		CopyOption[] options = new CopyOption[]{StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES}; 
		try {
			java.nio.file.Files.copy(FROM, TO, options);
		} catch (IOException e) {
			final ImageIcon icon = new ImageIcon(getClass().getResource("/fail32.png"));
            JOptionPane.showMessageDialog(null, "Copy file : " + e.getMessage(), "Alert", JOptionPane.ERROR_MESSAGE, icon);
		}
		
		final ImageIcon icon = new ImageIcon(getClass().getResource("/success32.png"));
		JOptionPane.showMessageDialog(null, "เพิ่มไฟล์ฐานข้อมูลแล้ว", "Success", JOptionPane.ERROR_MESSAGE, icon);
    }
    
    private void createFolder() {
		File file = new File(Config.DB_PATH);
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
}
