package dev.teerayut.calamaro.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dev.teerayut.calamaro.connection.ConnectionDB;
import dev.teerayut.calamaro.model.CalculateModel;
import dev.teerayut.calamaro.receipt.Receive;
import dev.teerayut.calamaro.utils.DateFormate;

public class ProcessPresenter implements ProcessInterface.Presenter {
	
	private ResultSet resultSet;
	private PreparedStatement psmt;
	private ConnectionDB connectionDB;
	private ProcessInterface.View view;
	private static List<CalculateModel> calList = new ArrayList<CalculateModel>();
	public ProcessPresenter(ProcessInterface.View view) {
		this.view = view;
	}
	
	@Override
	public void insertReceipt(List<CalculateModel> calculateList) {
//		this.calList = calculateList;
		StringBuilder sb = new StringBuilder();
		sb.delete(0, sb.length());
		
		sb.append("INSERT INTO Report (report_number, report_date, report_type, report_currency, report_buy_rate, report_sell_rate"
				+ ", report_amount, report_total, report_create_date) ");
		sb.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
		
		connectionDB = new ConnectionDB();
		int is = 0;
		try {
			psmt = connectionDB.dbInsert(sb.toString());
			for (int i = 0; i < calculateList.size(); i++) {
				CalculateModel model = calculateList.get(i);
				psmt.setString(1, model.getReportNumber());
				psmt.setString(2, model.getReportDate());
				psmt.setString(3, model.getReportType());
				psmt.setString(4, model.getReportCurrency());
				psmt.setString(5, model.getReportBuyRate());
				psmt.setString(6, model.getReportSellRate());
				psmt.setString(7, model.getReportAmount());
				psmt.setString(8, model.getReportTotal());
				psmt.setString(9, new DateFormate().getDateOnly());
				psmt.executeUpdate();
			}
			
			
//			if (is == 1) {
//				new Receive().printReceipt(calList);
//				connectionDB.closeAllTransaction();
//				view.onSuccess("บันทึกรายการซื้อขายแล้ว");
//				calculateList.clear();
//			} else {
//				connectionDB.closeAllTransaction();
//				view.onFail("ไม่สามารถบันทึกข้อมูลได้");
//			}
		} catch(Exception e) {
			System.out.println("insert : " + e.getMessage());
			connectionDB.closeAllTransaction();
			view.onFail("ไม่สามารถบันทึกข้อมูลการซื้อขายได้  ( " + e.getMessage() + " )");
			
		}
		
		try {
			Receive receive = new Receive();
			for (int i = 0; i < 2; i++) {
				receive.printReceipt(calculateList);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("print : " + e.getMessage());
			e.printStackTrace();
		}
		connectionDB.closeAllTransaction();
		view.onSuccess("บันทึกรายการซื้อขายแล้ว");
		calculateList.clear();
	}
	
	@Override
	public void getLastKey() {
		StringBuilder sb = new StringBuilder();
		sb.delete(0, sb.length());
		
		sb.append("SELECT report_number ");
		sb.append("FROM Report ");
		sb.append("ORDER BY report_id DESC LIMIT 1");
		
		connectionDB = new ConnectionDB();
		try {
			resultSet = connectionDB.dbQuery(sb.toString());
			view.onGenerateKey(resultSet);
		} catch(Exception e) {
			resultSet = null;
			view.onFail("ไม่สามารถดึงข้อมูลล่าสุดได้  ( " + e.getMessage() + " )");
		}
		connectionDB.closeAllTransaction();
	}
}
