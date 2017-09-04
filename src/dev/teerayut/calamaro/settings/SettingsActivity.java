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
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SettingsActivity extends JDialog implements SettingsInterface.View {

	public static final int ID_OK = 1;
    public static final int ID_CANCEL = 0;
    private int exitCode = ID_CANCEL;
    
    public SettingsActivity(Frame owner) {
        super(owner);
        createGUI();
        presenter.requestMoneyBegin();
    }

    /**
     * @wbp.parser.constructor
     */
    public SettingsActivity(Dialog owner) {
        super(owner);        
        createGUI();
        presenter.requestMoneyBegin();
    }
    
    private Preferrence prefs;
    private SettingsInterface.Presenter presenter;
    private DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
    
    private javax.swing.JPanel topPanel;
    
    private JFileChooser chooser = new JFileChooser();;
    private File file;

    private JLabel lblNewLabel;
    private JTextField textField;
    private JTextField textField_1;
    private void createGUI() {
    	prefs = new Preferrence();
    	presenter = new SettingsPresenter(this);
    	int width = 680;
    	int height = 480;
    	setBounds(0, 0, 681, 350);
    	setTitle("ตั้งค่า");
    	setIconImage(new ImageIcon(getClass().getResource("/ic_calamaro.png")).getImage());
    	new ScreenCenter().centreWindow(this);
    	getContentPane().setLayout(null);
    	
    	topPanel = new javax.swing.JPanel();
    	topPanel.setBounds(0, 116, 660, 121);
    	topPanel.setBorder(BorderFactory.createTitledBorder("Database"));
    	getContentPane().add(topPanel);
    	topPanel.setLayout(null);
    	
    	textField = new JTextField();
    	textField.setFont(new Font("Angsana New", Font.PLAIN, 25));
    	textField.setBounds(60, 45, 425, 40);
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
    	btnNewButton.setBounds(497, 45, 97, 40);
    	topPanel.add(btnNewButton);
        
        
        File database = new File(Config.DB_PATH + Config.DB_FILE);
    	if (database.exists()) {
    		textField.setText(database.getAbsolutePath().toString());	
    	}
    	
    	
    	JButton button = new JButton("ตกลง");
    	button.setBounds(554, 250, 97, 40);
    	getContentPane().add(button);
    	button.setPreferredSize(new Dimension(90, 35));
    	
    	JPanel panel = new JPanel();
    	panel.setLayout(null);
    	panel.setBorder(new TitledBorder(null, "\u0E40\u0E07\u0E34\u0E19\u0E15\u0E31\u0E49\u0E07\u0E15\u0E49\u0E19", TitledBorder.LEADING, TitledBorder.TOP, null, null));
    	panel.setBounds(0, 0, 660, 113);
    	getContentPane().add(panel);
    	
    	textField_1 = new JTextField();
    	textField_1.addKeyListener(new KeyAdapter() {
    		@Override
    		public void keyPressed(KeyEvent e) {
    			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
    				presenter.insertMoneyBegin(textField_1.getText().toString());
    				presenter.requestMoneyBegin();
    			}
    		}
    	});
    	textField_1.setFont(new Font("Angsana New", Font.PLAIN, 25));
    	textField_1.setColumns(10);
    	textField_1.setBounds(60, 49, 425, 40);
    	panel.add(textField_1);
    	
    	JLabel label = new JLabel("เงินตั้งต้น -->");
    	label.setEnabled(false);
    	label.setFont(new Font("Tahoma", Font.PLAIN, 13));
    	label.setBounds(60, 20, 79, 25);
    	panel.add(label);
    	
    	lblNewLabel = new JLabel("");
    	lblNewLabel.setEnabled(false);
    	lblNewLabel.setBounds(140, 20, 173, 25);
    	panel.add(lblNewLabel);
    	
    	button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				prefs.setPreferrence("db_path", textField.getText().toString());
				presenter.insertMoneyBegin(textField_1.getText().toString());
        		/*final ImageIcon icon = new ImageIcon(getClass().getResource("/success32.png"));
		        JOptionPane.showMessageDialog(null, "บันทึกการตั้งค่าแล้ว", "Complete", JOptionPane.ERROR_MESSAGE, icon);*/
		        dispose();
		        
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

	@Override
	public void onSuccess(String success) {
		final ImageIcon icon = new ImageIcon(getClass().getResource("/success32.png"));
		JOptionPane.showMessageDialog(null, success, "Success", JOptionPane.INFORMATION_MESSAGE, icon);
	}

	@Override
	public void onFail(String fail) {
		final ImageIcon icon = new ImageIcon(getClass().getResource("/fail32.png"));
        JOptionPane.showMessageDialog(null, fail, "Alert", JOptionPane.ERROR_MESSAGE, icon);
	}

	@Override
	public void setMoneyToTextFeild(String money) {
		try {
			if (!money.isEmpty()) {
				lblNewLabel.setText(decimalFormat.format(Float.parseFloat(money.trim())));
			} else {
				lblNewLabel.setText("");
			}
		} catch (Exception e) {
			lblNewLabel.setText("");
			e.printStackTrace();
		}
	}
}
