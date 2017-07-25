package dev.teerayut.calamaro.main;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import dev.teerayut.calamaro.settings.SettingsActivity;
import dev.teerayut.calamaro.utils.Config;
import dev.teerayut.calamaro.utils.Preferrence;
import dev.teerayut.calamaro.utils.ScreenCenter;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.border.TitledBorder;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;


public class MainActivity extends JFrame implements MainInterface.View{

	private JMenu edit, report;
	private JPanel contentPane;
	private Point moniter1 = null;
	private Point moniter2 = null;
	private int width, height, w, h;
	private Preferrence prefs;
	private MainPresenter presenter;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainActivity frame = new MainActivity();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	private javax.swing.JPanel topPanel;
	private javax.swing.JPanel bottomPanel;
	private javax.swing.JPanel centerPanel;
	
	private javax.swing.JPanel centerTop;
	private javax.swing.JPanel centerLeft;
	private javax.swing.JPanel centerRight;
	private javax.swing.JPanel centerBottom;
	private javax.swing.JPanel leftInnerCenter;
	private javax.swing.JPanel rightInnerCenter;
	
	private javax.swing.JTextField textField;

	private javax.swing.JLabel lblCompanyName;
	private javax.swing.JLabel lblCoID;
	
	private javax.swing.JLabel lblSource;
	
	private javax.swing.JLabel lblCurrency;
	
	private javax.swing.JScrollPane scrollPane;
	
	private void initWidget() {
		presenter = new MainPresenter(this);
		topPanel = new javax.swing.JPanel();
		bottomPanel = new javax.swing.JPanel();
		centerPanel = new javax.swing.JPanel();
		centerTop = new javax.swing.JPanel();
		centerTop.setBorder(new TitledBorder(null, "Currency", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		centerTop.setBackground(new Color(204, 255, 255));
		centerLeft = new javax.swing.JPanel();
		centerRight = new javax.swing.JPanel();
		centerBottom = new javax.swing.JPanel();
		leftInnerCenter = new javax.swing.JPanel();
		rightInnerCenter = new javax.swing.JPanel();
		textField = new javax.swing.JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					JOptionPane.showMessageDialog(MainActivity.this, textField.getText().toString());
				}
			}
		});
		lblCompanyName = new javax.swing.JLabel();
		lblCoID = new javax.swing.JLabel();
		lblSource = new javax.swing.JLabel();
		scrollPane = new javax.swing.JScrollPane();
		/*lblCurrency = new javax.swing.JLabel();
		lblCurrency.setBorder(new LineBorder(new Color(0, 0, 0)));
		lblCurrency.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblCurrency.setHorizontalAlignment(SwingConstants.CENTER);
		lblCurrency.setOpaque(true);
		lblCurrency.setBackground(Color.WHITE);*/
	}
	
	private void getScreenPoint() {	
		for (GraphicsDevice gd : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
			if (moniter1 == null) {
				moniter1 = gd.getDefaultConfiguration().getBounds().getLocation();
			} else if (moniter2 == null) {
				moniter2 = gd.getDefaultConfiguration().getBounds().getLocation();
			}
		}
		if (moniter2 == null) {
			moniter2 = moniter1;
		}
	}
	
	public MainActivity() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				prefs = new Preferrence();
				File database = new File(Config.DB_PATH + Config.DB_FILE);
				if (database.exists()) {
					prefs.setPreferrence("settings_open", "1");
					lblSource.setText(" jdbc:sqlite: " + new File(Config.DB_PATH + Config.DB_FILE).getAbsolutePath().toString());
	                lblSource.setIcon(new ImageIcon(getClass().getResource("/database_connect.png")));
				} else {
					prefs.setPreferrence("settings_open", "0");
					edit.setEnabled(false);
	                report.setEnabled(false);
	                lblSource.setText(" jdbc:sqlite: Disconnect");
	                lblSource.setIcon(new ImageIcon(getClass().getResource("/database_not_connect.png")));
	                final ImageIcon icon = new ImageIcon(getClass().getResource("/warning.png"));
		            JOptionPane.showMessageDialog(null, "ไม่พบฐานข้อมูล", "Warning", JOptionPane.WARNING_MESSAGE, icon);
				}
			}
		});
		initWidget();
		getScreenPoint();
		menu();
		
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		width = (int) dimension.getWidth();
		height = (int) dimension.getHeight();
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		h = gd.getDisplayMode().getHeight();
		w = gd.getDisplayMode().getHeight();
		
		setTitle("CALAMARO");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new java.awt.Dimension(1280, 720));
		setBounds(0, 0, 1280, 720);
		setExtendedState(MAXIMIZED_BOTH);
		setIconImage(new ImageIcon(getClass().getResource("/ic_calamaro.png")).getImage());
		getContentPane().setLayout(new java.awt.BorderLayout());
		new ScreenCenter().centreWindow(this);
		
		/***********************Bottom************************/
		bottomPanel.setPreferredSize(new java.awt.Dimension(w, 50));
		bottomPanel.setLayout(new java.awt.BorderLayout());
		getContentPane().add(bottomPanel, java.awt.BorderLayout.SOUTH);
		
		lblSource.setForeground(new Color(0, 51, 102));
		lblSource.setFont(new Font("Angsana New", Font.BOLD, 24));
		lblSource.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		bottomPanel.add(lblSource, java.awt.BorderLayout.LINE_END);
		/****************************************************/
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				textField.requestFocus();
				textField.setFocusable(true);
			}
		});
		
		topPanel();
		centerPanel();
	}
	
	private void topPanel() {
		topPanel.setLayout(new java.awt.BorderLayout());
		getContentPane().add(topPanel, java.awt.BorderLayout.NORTH);
		
		lblCompanyName.setForeground(new Color(0, 51, 102));
		lblCompanyName.setFont(new Font("Angsana New", Font.BOLD, 60));
		lblCompanyName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		topPanel.add(lblCompanyName, java.awt.BorderLayout.CENTER);
		
		lblCoID.setForeground(new Color(0, 51, 102));
		lblCoID.setFont(new Font("Angsana New", Font.PLAIN, 30));
		lblCoID.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		topPanel.add(lblCoID, java.awt.BorderLayout.AFTER_LAST_LINE);
		
		lblCompanyName.setIcon(new ImageIcon(getClass().getResource("/ic_calamaro_tp.png")));
		lblCompanyName.setText("CALAMARO EXCHANGE CO.,LTD.");
		lblCoID.setText("เลขที่ผู้เสียภาษี 0815560001531");
	}
	
	private void centerPanel() {
		FlowLayout flowCenter = new FlowLayout(FlowLayout.CENTER);
		centerPanel.setLayout(new java.awt.BorderLayout());
		getContentPane().add(centerPanel, java.awt.BorderLayout.CENTER);
		
		leftInnerCenter.setPreferredSize(new java.awt.Dimension(100, 40));
		centerPanel.add(leftInnerCenter, java.awt.BorderLayout.WEST);
		rightInnerCenter.setPreferredSize(new java.awt.Dimension(100, 40));
		centerPanel.add(rightInnerCenter, java.awt.BorderLayout.EAST);
		
		scrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setViewportView(centerTop);
		centerPanel.add(scrollPane, java.awt.BorderLayout.CENTER);
		flowCenter.setHgap(10);
		flowCenter.setVgap(10);
		centerTop.setLayout(flowCenter);

		for (int i = 0; i < 21; i++) {
			lblCurrency = new javax.swing.JLabel();
			lblCurrency.setBorder(new LineBorder(new Color(0, 0, 0)));
			lblCurrency.setFont(new Font("Tahoma", Font.BOLD, 18));
			lblCurrency.setHorizontalAlignment(SwingConstants.CENTER);
			lblCurrency.setOpaque(true);
			lblCurrency.setBackground(Color.WHITE);
			lblCurrency.setPreferredSize(new java.awt.Dimension(280, 125));
			lblCurrency.setText("USD | " + (i + 1) + " | " + (i * 4) + " ");
			lblCurrency.setIcon(new ImageIcon(getClass().getResource("/flag/usd.png")));
			centerTop.add(lblCurrency);
		}
		scrollPane.add(centerTop);
		
		centerPanel.add(centerBottom, java.awt.BorderLayout.SOUTH);
		centerBottom.add(centerLeft, java.awt.BorderLayout.WEST);
		
		textField.setPreferredSize(new java.awt.Dimension(200, 40));
		textField.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		centerBottom.add(textField, java.awt.BorderLayout.CENTER);
		textField.setFocusable(true);
		textField.requestFocus();
		
		centerBottom.add(centerRight, java.awt.BorderLayout.WEST);
	}
	
	private void menu() {
		JMenuBar menu = new JMenuBar();
		setJMenuBar(menu);
		edit = new JMenu("\u0E41\u0E01\u0E49\u0E44\u0E02\u0E40\u0E23\u0E17");
		edit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				/*updateCurrencyActivity update = new updateCurrencyActivity(MainActivity.this);
				if(update.doModal() == updateCurrencyActivity.ID_OK) {
					for (int i = model.getRowCount() - 1; i >= 0; i--) {
					    model.removeRow(i);
					}
					for (int i = modelSell.getRowCount() - 1; i >= 0; i--) {
					    modelSell.removeRow(i);
					}
		        } else {
		        	buyRate();
		        }
				update.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				update.setModal(true);*/
			}
		});
		menu.add(edit);
		
		report = new JMenu("\u0E23\u0E32\u0E22\u0E07\u0E32\u0E19");
		report.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				/*ReportActivity report = new ReportActivity(MainActivity.this);
				if(report.doModal() == ReportActivity.ID_OK) {}
				report.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				report.setModal(true);*/
			}
		});
		menu.add(report);
		
		JMenu settings = new JMenu("\u0E15\u0E31\u0E49\u0E07\u0E04\u0E48\u0E32");
		settings.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				SettingsActivity settings = new SettingsActivity(MainActivity.this);
				if(settings.doModal() == SettingsActivity.ID_OK) {
					System.out.println("modal");
		        } else {
		        	File database = new File(Config.DB_PATH + Config.DB_FILE);
					if (database.exists() && prefs.getPreferrence("settings_open") != null) {
		                edit.setEnabled(true);
		                report.setEnabled(true);
		                prefs.setPreferrence("settings_open", "1");
		                lblSource.setText(" jdbc:sqlite: " + new File(Config.DB_PATH + Config.DB_FILE).getAbsolutePath().toString());
		                lblSource.setIcon(new ImageIcon(getClass().getResource("/database_connect.png")));
					}
		        }
				settings.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				settings.setLocationRelativeTo(null);
				settings.setModal(true);
			}
		});
		menu.add(settings);
	}
}
