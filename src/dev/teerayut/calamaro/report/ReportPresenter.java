package dev.teerayut.calamaro.report;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import dev.teerayut.calamaro.connection.ConnectionDB;
import dev.teerayut.calamaro.model.CalculateModel;
import dev.teerayut.calamaro.model.ReportModel;


public class ReportPresenter implements ReportInterface.Presenter {
	
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
	public void exportToExcel(List<CalculateModel> calculateModelsList) {
		
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
			view.setDateToComboBox(resultSet);
		} catch(Exception e) {
			System.out.println("Report query date : " + e.getLocalizedMessage());
			e.printStackTrace();
		}
		return null;
		
	}

}
