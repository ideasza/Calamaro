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
	private ReportModel reportModel;
	private CalculateModel calculateModel;
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private List<ReportModel> reportModels = new ArrayList<ReportModel>();
	private List<CalculateModel> calculateModelsList = new ArrayList<CalculateModel>();
	
	
	private ReportInterface.View view;
	public ReportPresenter(ReportInterface.View view) {
		this.view = view;
	}
	
	@Override
	public void getReport(String date) {
		StringBuilder sb = new StringBuilder();
		sb.delete(0, sb.length());
		sb.append("SELECT * FROM Report ");
		sb.append("WHERE report_date like '"+ date +"%' ORDER BY report_id ASC");
		connectionDB = new ConnectionDB();
		try {
			resultSet = connectionDB.dbQuery(sb.toString());
			while(resultSet.next()) {
				reportModel = new ReportModel()
						.setReportNumber(resultSet.getString("report_number"))
						//.setReportDate(resultSet.getDate("report_date"))
						.setReportDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(resultSet.getString("report_date")))
						.setReportType(resultSet.getString("report_type"))
						.setReportCurrency(resultSet.getString("report_currency"))
						.setReportBuyRate(resultSet.getString("report_buy_rate"))
						.setReportSellRate(resultSet.getString("report_sell_rate"))
						.setReportAmount(resultSet.getString("report_amount"))
						.setReportTotal(resultSet.getString("report_total"));
				reportModels.add(reportModel);
			}
			view.showReport(reportModels);
		} catch(Exception e) {
			System.out.println("Report query : " + e.getMessage());
		}
	}

}
