package dev.teerayut.calamaro.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import dev.teerayut.calamaro.model.CurrencyItem;
import dev.teerayut.calamaro.process.ProcessActivity;
import dev.teerayut.calamaro.report.ReportActivity;
import dev.teerayut.calamaro.settings.SettingsActivity;
import dev.teerayut.calamaro.show.ShowActivity;
import dev.teerayut.calamaro.update.UpdateActivity;
import dev.teerayut.calamaro.utils.Config;
import dev.teerayut.calamaro.utils.DateFormate;
import dev.teerayut.calamaro.utils.Preferrence;
import dev.teerayut.calamaro.utils.ScreenCenter;


public class MainActivity extends JFrame implements MainInterface.View{

	private String reportFile;
	private JMenu edit, report;
	private JPanel contentPane;
	private Point moniter1 = null;
	private Point moniter2 = null;
	private int width, height, w, h;
	private Preferrence prefs;
	private MainPresenter presenter;
	private StringBuilder sb;
	private ShowActivity sc;
	private static String code;
	private CurrencyItem item;
	private List<CurrencyItem> currencyItems = new ArrayList<>();
	private static List<CurrencyItem> currencyItemsList;
	
	private FlowLayout flowCenter;
	private int centerWidth, centerHeight;

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
	private javax.swing.JPanel itemPanel;
	private javax.swing.JPanel itemPanelRight;
	
	private javax.swing.JPanel centerTop;
	private javax.swing.JPanel centerLeft;
	private javax.swing.JPanel centerRight;
	private javax.swing.JPanel centerBottom;
	private javax.swing.JPanel leftInnerCenter;
	private javax.swing.JPanel rightInnerCenter;
	
	private static javax.swing.JTextField textField;

	private javax.swing.JLabel lblCompanyName;
	private javax.swing.JLabel lblCoID;
	
	private javax.swing.JLabel lblSource;
	private javax.swing.JLabel lblTime;
	private JMenuItem menuItem;
	private JMenuItem menuItem_1;
	private JLabel lblNewLabel;
	
	private void initWidget() {
		presenter = new MainPresenter(this);
		topPanel = new javax.swing.JPanel();
		bottomPanel = new javax.swing.JPanel();
		bottomPanel.setBackground(new Color(204, 204, 204));
		centerPanel = new javax.swing.JPanel();
		centerTop = new javax.swing.JPanel();
		centerTop.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Currency", 
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		centerTop.setBackground(new Color(255, 255, 204));
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
					code = textField.getText().toString();
					presenter.getCurrency(code);
				}
			}
		});
		lblCompanyName = new javax.swing.JLabel();
		lblCoID = new javax.swing.JLabel();
		lblSource = new javax.swing.JLabel();
		lblTime = new javax.swing.JLabel();
		lblNewLabel = new JLabel();
		
		presenter.getMoneyBalance();
	}
	
	private void setTime(JLabel lbTime) {
		Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
            	lbTime.setText(new DateFormate().getDateWithTime());
            }
        }, 0, 1000);
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
//				sc = new ShowActivity();
				File database = new File(Config.DB_PATH + Config.DB_FILE);
				if (database.exists()) {
					prefs.setPreferrence("settings_open", "1");
					lblSource.setText(" jdbc:sqlite: " + new File(Config.DB_PATH + Config.DB_FILE).getAbsolutePath().toString());
	                lblSource.setIcon(new ImageIcon(getClass().getResource("/database_connect.png")));
	        		presenter.requestCurrency();
//	        		if (!sc.isShowing()) {
//						sc.setLocation(moniter2);
//			            sc.setVisible(true);
//					}
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
		w = gd.getDisplayMode().getWidth();
		
		setTitle("CALAMARO");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new java.awt.Dimension(1280, 720));
		setBounds(0, 0, 1280, 720);
		setExtendedState(MAXIMIZED_BOTH);
		setIconImage(new ImageIcon(getClass().getResource("/ic_calamaro.png")).getImage());
		getContentPane().setLayout(new java.awt.BorderLayout());
		new ScreenCenter().centreWindow(this);
		
		/***********************Bottom************************/
		bottomPanel.setPreferredSize(new java.awt.Dimension(w, 40));
		bottomPanel.setLayout(new java.awt.BorderLayout());
		getContentPane().add(bottomPanel, java.awt.BorderLayout.SOUTH);
		
		lblTime.setForeground(new Color(0, 51, 102));
		lblTime.setFont(new Font("Angsana New", Font.BOLD, 24));
		lblTime.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		setTime(lblTime);
		bottomPanel.add(lblTime, java.awt.BorderLayout.LINE_START);
		
		lblSource.setForeground(new Color(0, 51, 102));
		lblSource.setFont(new Font("Angsana New", Font.BOLD, 24));
		lblSource.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		bottomPanel.add(lblSource, java.awt.BorderLayout.LINE_END);
		
		lblNewLabel.setFont(new Font("Angsana New", Font.BOLD, 20));
		lblNewLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		bottomPanel.add(lblNewLabel, BorderLayout.CENTER);
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
		pack();
		setLocationRelativeTo(null);
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
		//lblCoID.setText("เลขที่ผู้เสียภาษี 0815560001531");
	}
	
	private void centerPanel() {
		flowCenter = new FlowLayout(FlowLayout.CENTER);
		centerPanel.setLayout(new java.awt.BorderLayout());
		getContentPane().add(centerPanel, java.awt.BorderLayout.CENTER);
		
		leftInnerCenter.setPreferredSize(new java.awt.Dimension(80, 40));
		centerPanel.add(leftInnerCenter, java.awt.BorderLayout.WEST);
		rightInnerCenter.setPreferredSize(new java.awt.Dimension(80, 40));
		centerPanel.add(rightInnerCenter, java.awt.BorderLayout.EAST);

		centerPanel.add(centerTop, java.awt.BorderLayout.CENTER);
		flowCenter.setHgap(15);
		flowCenter.setVgap(15);
		centerTop.setLayout(flowCenter);
		
		centerPanel.add(centerBottom, java.awt.BorderLayout.SOUTH);
		centerBottom.add(centerLeft, java.awt.BorderLayout.WEST);
		
		textField.setPreferredSize(new java.awt.Dimension(200, 40));
		textField.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		centerBottom.add(textField, java.awt.BorderLayout.CENTER);
		textField.setFocusable(true);
		textField.requestFocus();
		
		centerBottom.add(centerRight, java.awt.BorderLayout.WEST);
		centerWidth = (width - 280);
		centerHeight = (height - (int)(topPanel.getPreferredSize().getHeight() + centerBottom.getPreferredSize().getHeight()) - 40);
	}
	
	private void menu() {
		sc = new ShowActivity();
		JMenuBar menu = new JMenuBar();
		setJMenuBar(menu);
		edit = new JMenu("\u0E41\u0E01\u0E49\u0E44\u0E02\u0E40\u0E23\u0E17");
		edit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				UpdateActivity update = new UpdateActivity(MainActivity.this);
				if(update.doModal() == UpdateActivity.ID_OK) {
					
		        } else {
		        	currencyItems.clear();
		        	if (currencyItems.size() > 0) {
	                	currencyItems.clear();
	                	presenter.requestCurrency();
	                } else {
	                	presenter.requestCurrency();
	                }
		        }
				
				update.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				update.setModal(true);
			}
		});
		menu.add(edit);
		
		report = new JMenu("\u0E23\u0E32\u0E22\u0E07\u0E32\u0E19");
		menu.add(report);
		
		menuItem_1 = new JMenuItem("รายงาน");
		menuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ReportActivity report = new ReportActivity(MainActivity.this);
				if(report.doModal() == ReportActivity.ID_OK) {}
				report.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				report.setModal(true);
			}
		});
		report.add(menuItem_1);
		
		JMenu showRate = new JMenu("แสดงเรท");
//		showRate.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent arg0) {
//				sc = new ShowActivity();
//        		if (!sc.isShowing()) {
//					sc.setLocation(moniter2);
//		            sc.setVisible(true);
//				}
//			}
//		});
		menu.add(showRate);
		JMenuItem item1 = new JMenuItem("เปิด");
		JMenuItem item2 = new JMenuItem("ปิด");
		
		item1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
        		if (!sc.isShowing()) {
					sc.setLocation(moniter2);
		            sc.setVisible(true);
		            
		            item1.setEnabled(false);
					item2.setEnabled(true);
				}
			}
		});
		showRate.add(item1);
		item2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
        		if (sc.isShowing()) {
		            sc.setVisible(false);
		            item1.setEnabled(true);
					item2.setEnabled(false);
				}
			}
		});
		showRate.add(item2);
		
		if (sc.isShowing()) {
			item1.setEnabled(false);
			item2.setEnabled(true);
		} else {
			item1.setEnabled(true);
			item2.setEnabled(false);
		}
		
		JMenu settings = new JMenu("\u0E15\u0E31\u0E49\u0E07\u0E04\u0E48\u0E32");
		settings.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				SettingsActivity settings = new SettingsActivity(MainActivity.this);
				if(settings.doModal() == SettingsActivity.ID_OK) {
					System.out.println("modal");
		        } else {
		        	File database = new File(Config.DB_PATH + Config.DB_FILE);
					if (database.exists() && prefs.getPreferrence("settings_open") != null ) {
		                edit.setEnabled(true);
		                report.setEnabled(true);
		                prefs.setPreferrence("settings_open", "1");
		                lblSource.setText(" jdbc:sqlite: " + new File(Config.DB_PATH + Config.DB_FILE).getAbsolutePath().toString());
		                lblSource.setIcon(new ImageIcon(getClass().getResource("/database_connect.png")));
		                if (currencyItems.size() > 0) {
		                	
		                } else {
		                	presenter.requestCurrency();
		                }
		                if (!sc.isShowing()) {
							sc.setLocation(moniter2);
				            sc.setVisible(true);
						}
		                
		                presenter.getMoneyBalance();
					}
		        }
				settings.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				settings.setLocationRelativeTo(null);
				settings.setModal(true);
			}
		});
		menu.add(settings);
	}

	@Override
	public void setCurrencyItem(ResultSet resultSet) {
		currencyItems.clear();
		centerTop.removeAll();
		try {
			while(resultSet.next()) {
				item = new CurrencyItem();
				item.setName(resultSet.getString("currency_name").trim().toString());
				item.setImage(resultSet.getString("currency_image").trim().toString());
				item.setBuyRate(resultSet.getString("currency_buy_rate").trim().toString());
				item.setSellRate(resultSet.getString("currency_sell_rate").trim().toString());
				item.setBuyCode(resultSet.getString("currency_buy_code").trim().toString());
				item.setSellCode(resultSet.getString("currency_sell_code").trim().toString());
				currencyItems.add(item);
			}
			generateItem(currencyItems);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static List<CurrencyItem> getCurrencyItem() {
		return currencyItemsList;
	}
	
	public static String getCurrencyCode(){
		return code;
	}
	
	private void generateItem(List<CurrencyItem> listItem) {
		for (int i = 0; i < listItem.size(); i++) {
			CurrencyItem item = listItem.get(i);			
			itemPanel = new javax.swing.JPanel();
			
			int itemPanelW = (int)(centerWidth / 5);
			int itemPanelH = (int)(centerHeight / 8);
			
			if (w < 1920) {
				itemPanelW = itemPanelW + 20;
				flowCenter.setHgap(20);
				flowCenter.setVgap(25);
			} else if (w == 1024) {
				itemPanelW = (int)(centerWidth / 3) + 60;
				flowCenter.setHgap(2);
				flowCenter.setVgap(20);
			}
			
			if (w == 1280 && h == 1024) {
				itemPanelW = (int)(centerWidth / 5);
				itemPanelH = itemPanelH - 5;
			}
			
			
			
			if (w == 1400 && h == 1050 || w == 1680 && h == 1050) {
				flowCenter.setHgap(15);
				flowCenter.setVgap(10);
				itemPanelW = (int)(centerWidth / 4);
				itemPanelH = itemPanelH - 15;
			}
			
			if (w == 1366 && h == 768) {
				itemPanelW = itemPanelW - 20;
				itemPanelH = itemPanelH - 15;
				flowCenter.setHgap(8);
				flowCenter.setVgap(22);
				lblCompanyName.setFont(new Font("Angsana New", Font.BOLD, 50));
				lblCoID.setFont(new Font("Angsana New", Font.PLAIN, 25));
			}
			
			if (w == 800 && h == 600) {
				itemPanelW = (int)(centerWidth / 3);
				flowCenter.setHgap(10);
				flowCenter.setVgap(10);
			}
			
			centerTop.setLayout(flowCenter);
			
			itemPanel.setPreferredSize(new java.awt.Dimension(itemPanelW, itemPanelH));
			centerTop.add(itemPanel);
			itemPanel.setLayout(new java.awt.BorderLayout());
			itemPanel.setBorder(new LineBorder(new Color(128, 128, 128)));
			
			int panelW = (int)(itemPanel.getPreferredSize().getWidth());
			int panelH = (int)(itemPanel.getPreferredSize().getHeight());

			javax.swing.JLabel lblCurrencyName = new javax.swing.JLabel("");
			lblCurrencyName.setFont(new Font("Tahoma", Font.BOLD, 16));
			lblCurrencyName.setPreferredSize(new java.awt.Dimension(panelW, panelH));
			lblCurrencyName.setHorizontalAlignment(SwingConstants.LEADING);
			lblCurrencyName.setText(" " + item.getName());
			lblCurrencyName.setIcon(new ImageIcon(getClass().getResource("/flag/" + item.getImage())));
			
			itemPanelRight = new javax.swing.JPanel();
			itemPanelRight.setPreferredSize(new java.awt.Dimension((panelW / 3) - 20, panelH));
			itemPanelRight.setLayout(new java.awt.BorderLayout());
			
			itemPanel.add(lblCurrencyName, java.awt.BorderLayout.LINE_START);
			itemPanel.add(itemPanelRight, java.awt.BorderLayout.LINE_END);
			
			javax.swing.JLabel lblBuyCode = new javax.swing.JLabel();
			lblBuyCode.setFont(new Font("Tahoma", Font.PLAIN, 16));
			lblBuyCode.setPreferredSize(new java.awt.Dimension((panelW / 3), (panelH / 2)));
			lblBuyCode.setHorizontalAlignment(SwingConstants.CENTER);
			lblBuyCode.setText(item.getBuyCode());
			lblBuyCode.setBackground(new Color(152, 251, 152));
			lblBuyCode.setOpaque(true);
			
			javax.swing.JLabel lblSellCode = new javax.swing.JLabel();
			lblSellCode.setFont(new Font("Tahoma", Font.PLAIN, 16));
			lblSellCode.setPreferredSize(new java.awt.Dimension((panelW / 3), (panelH / 2)));
			lblSellCode.setHorizontalAlignment(SwingConstants.CENTER);
			lblSellCode.setText(item.getSellCode());
			lblSellCode.setBackground(new Color(240, 128, 128));
			lblSellCode.setOpaque(true);
			
			itemPanelRight.add(lblBuyCode, java.awt.BorderLayout.NORTH);
			itemPanelRight.add(lblSellCode, java.awt.BorderLayout.SOUTH);
		}
	}

	@Override
	public void onSuccess(String success) {
		final ImageIcon icon = new ImageIcon(getClass().getResource("/success32.png"));
		JOptionPane.showMessageDialog(null, success, "Success", JOptionPane.INFORMATION_MESSAGE, icon);
		textField.setText("");
	}

	@Override
	public void onFail(String fail) {
		final ImageIcon icon = new ImageIcon(getClass().getResource("/fail32.png"));
        JOptionPane.showMessageDialog(null, fail, "Alert", JOptionPane.ERROR_MESSAGE, icon);
        textField.setText("");
	}

	@Override
	public void onProcessCurrency(ResultSet resultSet) {
		currencyItemsList = new ArrayList<>();
		try {
			while(resultSet.next()) {
				item = new CurrencyItem();
				item.setName(resultSet.getString("currency_name").trim().toString());
				item.setBuyRate(resultSet.getString("currency_buy_rate").trim().toString());
				item.setSellRate(resultSet.getString("currency_sell_rate").trim().toString());
				item.setBuyCode(resultSet.getString("currency_buy_code").trim().toString());
				item.setSellCode(resultSet.getString("currency_sell_code").trim().toString());
				currencyItemsList.add(item);
			}
			processDialog();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void processDialog() {
		ProcessActivity processActivity = new ProcessActivity(MainActivity.this);
		processActivity.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		if(processActivity.doModal() == SettingsActivity.ID_OK) {
			System.out.println("modal");
        } else {
        	textField.setText("");
        	presenter.getMoneyBalance();
        }
		processActivity.setLocationRelativeTo(null);
		processActivity.setModal(true);
	}

	@Override
	public void onCheckMoneyBalance(float begin, float balance) {
		DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
		lblCoID.setText("ยอดเงินตั้งต้น : " + decimalFormat.format(begin) + "         " + "ยอดเงินคงเหลือ : " + decimalFormat.format(balance));
	}
}
