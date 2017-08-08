package dev.teerayut.calamaro.report;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

import dev.teerayut.calamaro.model.CalculateModel;
import dev.teerayut.calamaro.model.ReportModel;
import dev.teerayut.calamaro.utils.ScreenCenter;

public class ReportActivity extends JDialog implements ReportInterface.View{

	public static final int ID_OK = 1;
    public static final int ID_CANCEL = 0;
    private int exitCode = ID_CANCEL;
    private ReportInterface.Presenter presenter;
    
    private String dateReport;
    private DefaultTableModel model;
    private Object[][] objs;
    private Object[] columName = {"เลขที่ใบเสร็จ", "วันที่", "ประเภทรายการ", "สกุลเงิน", "อัตราซื้อ", "อัตราขาย", "จำนวน", "เงินไทย"};
    private List<ReportModel> reportModelsList = new ArrayList<ReportModel>();
    private List<CalculateModel> calculateModelsList = new ArrayList<CalculateModel>();

    public ReportActivity(Frame owner) {
        super(owner);
        createGUI();
    }

    /**
     * @wbp.parser.constructor
     */
    public ReportActivity(Dialog owner) {
        super(owner);        
        createGUI();
    }
    
    private JTable table;
    private JButton buttonExport;
	private JScrollPane scrollPane;
    private javax.swing.JPanel topPanel;
	private javax.swing.JPanel toolPanel;
    private javax.swing.JPanel centerPanel;
    private javax.swing.JPanel bottomPanel;
    private javax.swing.JLabel lblCompanyName;
	private javax.swing.JLabel lblCoID;
    
    private void initWidget() {
    	presenter = new ReportPresenter(this);
		topPanel = new javax.swing.JPanel();
		topPanel.setBounds(0, 0, 1902, 75);
		bottomPanel = new javax.swing.JPanel();
		bottomPanel.setBounds(0, 1013, 1902, 20);
		centerPanel = new javax.swing.JPanel();
		
		lblCompanyName = new javax.swing.JLabel();
		lblCoID = new javax.swing.JLabel();
		
		scrollPane = new javax.swing.JScrollPane();
	}

	/**
	 * Create the frame.
	 */
	public void createGUI() {
		initWidget();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screenSize.width;
    	int height = screenSize.height;
    	GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int h = gd.getDisplayMode().getHeight();
		int w = gd.getDisplayMode().getHeight();
        setPreferredSize(new Dimension(width - 100, height - 100));
        setBounds(100, 100, width -100, height - 100);
        setTitle("CALAMARO - Reports");
        new ScreenCenter().centreWindow(this);
        getContentPane().setLayout(null);
		
        toolPanel = new javax.swing.JPanel();
		toolPanel.setBounds(0, 0, width, 40);
		
		getContentPane().add(toolPanel);
		toolPanel.setLayout(null);
		
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		int subtractYearValue = 543;

		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
		int currentDate= Calendar.getInstance().get(Calendar.DATE);
		
		if (currentYear > 2500) {
			currentYear = currentYear - subtractYearValue;
		}
		
		calendar.set(currentYear , currentMonth , currentDate);
		date.setTime(calendar.getTimeInMillis());
		
		DatePickerSettings dateSettings = new DatePickerSettings();
		dateSettings.setFormatForDatesCommonEra("yyyy-MM-dd");
		DatePicker datePicker1 = new DatePicker(dateSettings);
		datePicker1.setDateToToday();
		datePicker1.setBounds(0, 0, 200, 41);
		datePicker1.setPreferredSize(new java.awt.Dimension(200, 35));
		toolPanel.add(datePicker1);
		datePicker1.setFont(new Font("Angsana New", Font.PLAIN, 30));
		
		int offset = (int) datePicker1.getSize().getWidth() + 10;
		JButton buttonReport = new JButton("โหลดรายงาน");
		buttonReport.setBounds(210, 0, 200, 40);
		buttonReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dateReport = datePicker1.getDate().toString();
				presenter.getReport(dateReport);
			}
		});
		toolPanel.add(buttonReport);
		
		offset = (int)buttonReport.getSize().getWidth() + 10;
		buttonExport = new JButton("Export");
		buttonExport.setBounds(422, 0, 200, 40);
		buttonExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//present.exportToExcel(dateReport, listReportData);
			}
		});
		
		buttonExport.setIcon(new ImageIcon(getClass().getResource("/excel.png")));
		toolPanel.add(buttonExport);
		
		centerPanel.setLayout(new java.awt.BorderLayout());
		centerPanel.setBorder(BorderFactory.createTitledBorder("รายงาน"));
		getContentPane().add(centerPanel);
		
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerifyInputWhenFocusTarget(false);
		scrollPane.setRequestFocusEnabled(false);
		centerPanel.add(scrollPane, java.awt.BorderLayout.CENTER);
		scrollPane.setPreferredSize(new Dimension(1900, 880));
		
		table =  new javax.swing.JTable() {
		    public boolean isCellEditable(int rowIndex, int colIndex) {
		    	if(colIndex == 0) {
		    		return false; // Disallow Column 0
		    	} else if (colIndex == 1) {
		    		return false;   // Allow the editing 
		    	} else if (colIndex == 2) {
		    		return false;   // Allow the editing 
		    	} else if (colIndex == 3) {
		    		return false; // Disallow Column 0
		    	} else if (colIndex == 4) {
		    		return false;   // Allow the editing 
		    	} else if (colIndex == 5) {
		    		return false;   // Allow the editing 
		    	} else if (colIndex == 6) {
		    		return false;   // Allow the editing 
		    	} else if (colIndex == 7) {
		    		return false;   // Allow the editing 
		    	} else {
		    		return false;
		    	}
		    }
		};
		
		table.setFocusable(false);
		table.setForeground(Color.BLACK);
		table.setFillsViewportHeight(true); 
		table.setRowSelectionAllowed(false);
		table.setEnabled(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		scrollPane.setViewportView(table);
		
		if (reportModelsList.size() == 0) {
			buttonExport.setEnabled(false);
		}
		
		pack();
        setLocationRelativeTo(getParent());
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

	@Override
	public void showReport(List<ReportModel> reportModels) {
		this.reportModelsList = reportModels;
		if (reportModelsList.size() == 0) {
			onFaile("ไม่มีข้อมูลรายงาน");
			return;
		} else {
			buttonExport.setEnabled(true);
		}
		
		model = new DefaultTableModel(new Object[0][0], columName);
		for (int i = 0; i < reportModelsList.size(); i++) {
			ReportModel m = reportModelsList.get(i);
			model.addRow(new Object[] {m.getReportNumber().trim(), 
					m.getReportDate(), 
					m.getReportType().trim(), 
					m.getReportCurrency().trim(),
					String.format("%.2f", m.getReportBuyRate().trim()),
					String.format("%.2f", m.getReportSellRate().trim()),
					String.format("%,.2f",m.getReportAmount().trim()),
					String.format("%,.2f",m.getReportTotal().trim())
			});
		}
		
		table.setModel(model);
		JTableHeader header = table.getTableHeader();
    	header.setPreferredSize(new Dimension(100, 80));
		table.getTableHeader().setFont(new Font("Angsana New", Font.BOLD, 44));
		table.setFont(new Font("Angsana New", Font.BOLD, 38));
		
		int columnsWidth = scrollPane.getSize().width;
		
		table.getColumnModel().getColumn(0).setPreferredWidth(columnsWidth / 15);
		table.getColumnModel().getColumn(1).setPreferredWidth(columnsWidth / 4);
		table.getColumnModel().getColumn(2).setPreferredWidth(columnsWidth / 11);
		table.getColumnModel().getColumn(3).setPreferredWidth(columnsWidth / 11);
		table.getColumnModel().getColumn(4).setPreferredWidth(columnsWidth / 8);
		table.getColumnModel().getColumn(5).setPreferredWidth(columnsWidth / 8);
		table.getColumnModel().getColumn(6).setPreferredWidth((columnsWidth / 5) - 35);
		table.getColumnModel().getColumn(7).setPreferredWidth((columnsWidth / 5) - 35);
		table.setRowHeight(38);
		
		DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
		centerRender.setHorizontalAlignment(JLabel.CENTER);
		table.getColumnModel().getColumn(0).setCellRenderer(centerRender);
		table.getColumnModel().getColumn(1).setCellRenderer(centerRender);
		table.getColumnModel().getColumn(2).setCellRenderer(centerRender);
		table.getColumnModel().getColumn(3).setCellRenderer(centerRender);
		
		DefaultTableCellRenderer rightRender = new DefaultTableCellRenderer();
		rightRender.setHorizontalAlignment(JLabel.RIGHT);
		table.getColumnModel().getColumn(4).setCellRenderer(rightRender);
		table.getColumnModel().getColumn(5).setCellRenderer(rightRender);
		table.getColumnModel().getColumn(6).setCellRenderer(rightRender);
	}

	public void onFaile(String fail) {
		final ImageIcon icon = new ImageIcon(getClass().getResource("/fail32.png"));
        JOptionPane.showMessageDialog(null, fail, "Alert", JOptionPane.ERROR_MESSAGE, icon);
	}

	@Override
	public void onSuccess(String s) {
		final ImageIcon icon = new ImageIcon(getClass().getResource("/success32.png"));
		JOptionPane.showMessageDialog(null, s, "Success", JOptionPane.ERROR_MESSAGE, icon);
	}
}
