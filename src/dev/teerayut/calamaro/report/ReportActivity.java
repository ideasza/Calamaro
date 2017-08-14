package dev.teerayut.calamaro.report;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import org.apache.log4j.BasicConfigurator;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.optionalusertools.DateChangeListener;
import com.github.lgooddatepicker.zinternaltools.DateChangeEvent;

import dev.teerayut.calamaro.connection.ConnectionDB;
import dev.teerayut.calamaro.model.CalculateModel;
import dev.teerayut.calamaro.model.ReportModel;
import dev.teerayut.calamaro.utils.ScreenCenter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JRViewer;
import net.sf.jasperreports.view.JasperViewer;

public class ReportActivity extends JDialog implements ReportInterface.View{

	public static final int ID_OK = 1;
    public static final int ID_CANCEL = 0;
    private int exitCode = ID_CANCEL;
    private ReportInterface.Presenter presenter;
    
    private String reportURL;
    private String report = null;
    private String dateReport;
    private DefaultTableModel model;
    private Object[][] objs;
    private Object[] columName = {"เลขที่ใบเสร็จ", "วันที่", "ประเภทรายการ", "สกุลเงิน", "อัตราซื้อ", "อัตราขาย", "จำนวน", "เงินไทย"};
    private List<ReportModel> reportModelsList = new ArrayList<ReportModel>();
    private List<CalculateModel> calculateModelList = new ArrayList<CalculateModel>();

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
    	//presenter = new ReportPresenter(this);
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
        setPreferredSize(new Dimension(width /6, height /6));
        setBounds(100, 100, width -100, height - 100);
        setTitle("CALAMARO - Reports");
        new ScreenCenter().centreWindow(this);
        getContentPane().setLayout(new java.awt.BorderLayout());
		
        toolPanel = new javax.swing.JPanel();
		toolPanel.setBounds(0, 0, width, 40);
		
		getContentPane().add(toolPanel, java.awt.BorderLayout.CENTER);
		
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
		datePicker1.getComponentToggleCalendarButton().setBounds(174, 0, 26, 35);
		datePicker1.getComponentDateTextField().setBounds(0, 0, 171, 35);
		datePicker1.setBounds(51, 5, 200, 35);
		datePicker1.setDateToToday();
		toolPanel.setLayout(null);
		datePicker1.setPreferredSize(new java.awt.Dimension(200, 35));
		toolPanel.add(datePicker1);
		datePicker1.setFont(new Font("Angsana New", Font.PLAIN, 30));
		datePicker1.setLayout(null);
		
		datePicker1.addDateChangeListener(new DateChangeListener() {
			
			@Override
			public void dateChanged(DateChangeEvent arg0) {
				dateReport = datePicker1.getDate().toString();
				//presenter.getReport(dateReport);
				createReport(dateReport);
			}
		});
		
		int offset = (int) datePicker1.getSize().getWidth() + 10;
		buttonExport = new JButton("Export");
		buttonExport.setFont(new Font("Tahoma", Font.BOLD, 14));
		buttonExport.setForeground(SystemColor.desktop);
		buttonExport.setBackground(SystemColor.controlHighlight);
		buttonExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//present.exportToExcel(dateReport, listReportData);
				dateReport = datePicker1.getDate().toString();
				createReport(dateReport);
			}
		});
		
		buttonExport.setOpaque(true);
		buttonExport.setIcon(new ImageIcon(getClass().getResource("/excel.png")));
		//toolPanel.add(buttonExport);
		
		centerPanel.setLayout(new java.awt.BorderLayout());
		centerPanel.setBorder(BorderFactory.createTitledBorder("รายงาน"));
		//getContentPane().add(centerPanel, java.awt.BorderLayout.CENTER);
		
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerifyInputWhenFocusTarget(false);
		scrollPane.setRequestFocusEnabled(false);
		//centerPanel.add(scrollPane, java.awt.BorderLayout.CENTER);
		scrollPane.setPreferredSize(new Dimension(1900, 800));
		
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
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		scrollPane.setViewportView(table);
		
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

	public void onFaile(String fail) {
		final ImageIcon icon = new ImageIcon(getClass().getResource("/fail32.png"));
        JOptionPane.showMessageDialog(null, fail, "Alert", JOptionPane.ERROR_MESSAGE, icon);
	}

	@Override
	public void onSuccess(String s) {
		final ImageIcon icon = new ImageIcon(getClass().getResource("/success32.png"));
		JOptionPane.showMessageDialog(null, s, "Success", JOptionPane.ERROR_MESSAGE, icon);
	}

	@Override
	public void showReport(List<CalculateModel> calculateModelsList) {
		this.calculateModelList.clear();
		this.calculateModelList = calculateModelsList;
		if (calculateModelList.size() == 0) {
			onFaile("ไม่มีข้อมูลรายงาน");
			return;
		}
		
		model = new DefaultTableModel(objs, columName);
		for (int i = 0; i < calculateModelList.size(); i++) {
			CalculateModel m = calculateModelList.get(i);
			
			model.addRow(new Object[] {m.getReportNumber(), 
					m.getReportDate(), 
					m.getReportType(), 
					m.getReportCurrency(),
					String.format("%.2f", Float.parseFloat(m.getReportBuyRate())),
					String.format("%.2f", Float.parseFloat(m.getReportSellRate())),
					String.format("%,.2f",Float.parseFloat(m.getReportAmount())),
					String.format("%,.2f",Float.parseFloat(m.getReportTotal()))
			});
		}
		
		table.setModel(model);
		JTableHeader header = table.getTableHeader();
    	header.setPreferredSize(new Dimension(100, 80));
		table.getTableHeader().setFont(new Font("Angsana New", Font.BOLD, 40));
		table.setFont(new Font("Angsana New", Font.PLAIN, 36));
		
		int columnsWidth = scrollPane.getSize().width;
		
		table.getColumnModel().getColumn(0).setPreferredWidth(columnsWidth / 6);
		table.getColumnModel().getColumn(1).setPreferredWidth(columnsWidth / 6);
		table.getColumnModel().getColumn(2).setPreferredWidth(columnsWidth / 6);
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
		table.getColumnModel().getColumn(7).setCellRenderer(rightRender);
	}
	
	@SuppressWarnings("deprecation")
	private void createReport(String date) {
		BasicConfigurator.configure();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("date", date);
		
		try {
			reportURL = getClass().getResource("/template/report_template.jrxml").getFile();
			File file = new File(reportURL);
			file = file.getAbsoluteFile();
			report = file.getPath();
			
			JasperReport ir = JasperCompileManager.compileReport(report);
			JasperPrint ip = JasperFillManager.fillReport(ir, param, new ConnectionDB().connect());
			JasperViewer.viewReport(ip, false);
			this.dispose();
		} catch (JRException je) {
			je.printStackTrace();
		}
	}
}
