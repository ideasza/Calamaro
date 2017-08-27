package dev.teerayut.calamaro.report;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import dev.teerayut.calamaro.connection.ConnectionDB;
import dev.teerayut.calamaro.model.CalculateModel;
import dev.teerayut.calamaro.utils.Config;
import dev.teerayut.calamaro.utils.Convert;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;


public class ReportPresenter implements ReportInterface.Presenter {
	
	private float GrandTotal = 0;
	private float GrandAmountBuy = 0;
	private float GrandAmountSell = 0;
	private DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
	
	private String fileName;
	private ResultSet resultSet;
	private ConnectionDB connectionDB;
	private CalculateModel calculateModel;
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private List<CalculateModel> calculateModelsList = new ArrayList<CalculateModel>();
	
	
	private ReportInterface.View view;
	public ReportPresenter(ReportInterface.View view) {
		this.view = view;
	}
	
	@Override
	public void getReport(String date) {
		calculateModelsList.clear();
		StringBuilder sb = new StringBuilder();
		sb.delete(0, sb.length());
		sb.append("SELECT * FROM Report ");
		sb.append("WHERE report_date LIKE '"+ date +"%' ORDER BY report_id ASC");
		connectionDB = new ConnectionDB();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			resultSet = connectionDB.dbQuery(sb.toString());
			while(resultSet.next()) {
				calculateModel = new CalculateModel();
				calculateModel.setReportNumber(resultSet.getString("report_number"));
				calculateModel.setReportDate(resultSet.getString("report_date"));
				calculateModel.setReportType(resultSet.getString("report_type"));
				calculateModel.setReportCurrency(resultSet.getString("report_currency"));
				calculateModel.setReportBuyRate(resultSet.getString("report_buy_rate"));
				calculateModel.setReportSellRate(resultSet.getString("report_sell_rate"));
				calculateModel.setReportAmount(resultSet.getString("report_amount"));
				calculateModel.setReportTotal(resultSet.getString("report_total"));
				calculateModelsList.add(calculateModel);
			}
			view.showReport(calculateModelsList);
		} catch(Exception e) {
			System.out.println("Report query : " + e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void exportToExcel(String date, List<CalculateModel> calculateModelsList) {
		File file = new File(Config.REPORT_PATH);
		File fileReport = null;
		if (!file.exists()) {
			 if (file.mkdirs()) {
				 fileName = Config.REPORT_PATH + date + ".xls";
		    } else {
		    	final ImageIcon icon = new ImageIcon(getClass().getResource("/icon/fail32.png"));
		    	JOptionPane.showMessageDialog(null, "ไม่สามารถสร้างโพลเดอร์ได้", "Alert", JOptionPane.ERROR_MESSAGE, icon);
		    }
		} else {
			fileName = Config.REPORT_PATH + date + ".xls";
		}
		try {
			WritableWorkbook workbook = Workbook.createWorkbook(new File(fileName));
			
			//*** Create Font ***//
			WritableFont fontHeader = new WritableFont(WritableFont.TIMES, 14);
			fontHeader.setColour(Colour.BLACK);
			
			WritableFont fontReport = new WritableFont(WritableFont.TIMES, 12);
			fontReport.setColour(Colour.BLACK);
			
			WritableSheet ws1 = workbook.createSheet("SheetReport", 0);
			
			//*** Header ***//
            WritableCellFormat cellFormat1 = new WritableCellFormat(fontHeader);
            cellFormat1.setAlignment(Alignment.CENTRE);
            cellFormat1.setVerticalAlignment(VerticalAlignment.CENTRE);
            //cellFormat1.setBackground(Colour.WHITE);
            
            //*** Data ***//
            WritableCellFormat cellFormat2 = new WritableCellFormat(fontReport);
            cellFormat2.setAlignment(jxl.format.Alignment.CENTRE);
            cellFormat2.setVerticalAlignment(VerticalAlignment.CENTRE);
            cellFormat2.setWrap(true);
            cellFormat2.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.HAIR,
            jxl.format.Colour.BLACK);
            
            WritableCellFormat cellFormatCurrency = new WritableCellFormat(fontReport);
            cellFormatCurrency.setAlignment(jxl.format.Alignment.RIGHT);
            cellFormatCurrency.setVerticalAlignment(VerticalAlignment.CENTRE);
            cellFormatCurrency.setWrap(false);
            cellFormatCurrency.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.HAIR,
            jxl.format.Colour.BLACK);
            
            WritableCellFormat cellFormat3 = new WritableCellFormat(fontReport);
    	    cellFormat3.setAlignment(jxl.format.Alignment.LEFT);
    	    cellFormat3.setVerticalAlignment(VerticalAlignment.CENTRE);
    	    
    	    WritableCellFormat cellFormat4 = new WritableCellFormat(fontReport);
    	    cellFormat4.setAlignment(jxl.format.Alignment.RIGHT);
    	    cellFormat4.setVerticalAlignment(VerticalAlignment.CENTRE);
            
    	    ws1.mergeCells(0, 0, 6, 0);
    	    Label lable = new Label(0, 0,"Report " + date, cellFormat1);
    	    ws1.addCell(lable);
    	    
    	    //*** Header ***//
    	    ws1.setColumnView(0, 22); // Column CustomerID
    	    ws1.addCell(new Label(0,1,"เลขที่ใบเสร็จ", cellFormat1));
    	    
    	    ws1.setColumnView(1, 12); // Column Name
    	    ws1.addCell(new Label(1,1,"ประเภท", cellFormat1));
    	    
    	    ws1.setColumnView(2, 18); // Column Email
    	    ws1.addCell(new Label(2,1,"สกุลเงิน", cellFormat1));
    	    
    	    ws1.setColumnView(3, 10); // Column CountryCode
    	    ws1.addCell(new Label(3,1,"อัตราซื้อ", cellFormat1));
    	    
    	    ws1.setColumnView(4, 10); // Column Budget
    	    ws1.addCell(new Label(4,1,"อัตราขาย", cellFormat1));
    	    
    	    ws1.setColumnView(5, 15); // Column Used
    	    ws1.addCell(new Label(5,1,"จำนวนเงิน", cellFormat1));
    	    
    	    ws1.setColumnView(6, 15); // Column Used
    	    ws1.addCell(new Label(6,1,"เงินไทย", cellFormat1));
    	    
    	    for (int i = 0; i < calculateModelsList.size(); i++) {
    	    	CalculateModel m = calculateModelsList.get(i);
    	    	ws1.addCell(new Label(0, (i + 2), m.getReportNumber(), cellFormat2));
	    	    ws1.addCell(new Label(1, (i + 2), m.getReportType().trim(), cellFormat2));
	    	    ws1.addCell(new Label(2, (i + 2), m.getReportCurrency().trim(), cellFormat2));
	    	    ws1.addCell(new Label(3, (i + 2), m.getReportBuyRate().trim(), cellFormat2));
	    	    ws1.addCell(new Label(4, (i + 2), m.getReportSellRate().trim(), cellFormatCurrency));
	    	    ws1.addCell(new Label(5, (i + 2), decimalFormat.format(Float.parseFloat(m.getReportAmount().trim())), cellFormatCurrency)); 
	 	    	ws1.addCell(new Label(6, (i + 2), decimalFormat.format(Double.parseDouble(m.getReportTotal().trim())), cellFormatCurrency)); 
	    	    
	    	    ws1.addCell(new Label(0, (calculateModelsList.size() + 2), "Grand Total" , cellFormat3));
	    	    
	    	    float grandAmountBuy = Float.parseFloat(m.getReportBuyRate().trim());
	    	    float grandAmountSell = Float.parseFloat(m.getReportSellRate().trim());
	    	    float grandTotal = Float.parseFloat(m.getReportTotal().trim());
	    	    GrandAmountBuy += grandAmountBuy;
	    	    GrandAmountSell += grandAmountSell;
	    	    GrandTotal += grandTotal;
	    	    
	    	    ws1.addCell(new Label(4, (calculateModelsList.size() + 2), new Convert().formatDecimal(GrandAmountBuy), cellFormat4));
	    	    ws1.addCell(new Label(5, (calculateModelsList.size() + 2), new Convert().formatDecimal(GrandAmountSell), cellFormat4));
	    	    ws1.addCell(new Label(6, (calculateModelsList.size() + 2), new Convert().formatDecimal(GrandTotal), cellFormat4));
    	    }

    	    workbook.write();
    	    workbook.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		}
		view.onSuccess("ส่งออกรายงานสำเร็จ");
		try {
			calculateModelsList.clear();
			Desktop.getDesktop().open(new File(Config.REPORT_PATH));
		} catch (IOException e) {
			final ImageIcon icon = new ImageIcon(getClass().getResource("/icon/fail32.png"));
            JOptionPane.showMessageDialog(null, e.getMessage(), "Alert", JOptionPane.ERROR_MESSAGE, icon);
		}
	}

	@Override
	public ResultSet getReportFromDate() {
		StringBuilder sb = new StringBuilder();
		sb.delete(0, sb.length());
		sb.append("SELECT report_create_date FROM Report ");
		sb.append("GROUP BY report_create_date");
		connectionDB = new ConnectionDB();
		try {
			resultSet = connectionDB.dbQuery(sb.toString());
			//view.setDateToComboBox(resultSet);
		} catch(Exception e) {
			System.out.println("Report query date : " + e.getLocalizedMessage());
			e.printStackTrace();
		}
		return null;
		
	}

}
