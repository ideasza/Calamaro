package dev.teerayut.calamaro.settings;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dev.teerayut.calamaro.connection.ConnectionDB;
import dev.teerayut.calamaro.model.CalculateModel;
import dev.teerayut.calamaro.receipt.Receive;
import dev.teerayut.calamaro.utils.DateFormate;

public class SettingsPresenter implements SettingsInterface.Presenter {

	private ResultSet resultSet;
	private PreparedStatement psmt;
	private ConnectionDB connectionDB;
	private SettingsInterface.View view;
	public SettingsPresenter(SettingsInterface.View view) {
		this.view = view;
	}
	
	@Override
	public void insertMoneyBegin(String value) {
		StringBuilder sb = new StringBuilder();
		sb.delete(0, sb.length());
		sb.append("INSERT INTO MoneyConfig (money_value, money_create_date) ");
		sb.append("VALUES (?, ?)");
		connectionDB = new ConnectionDB();
		int is = 0;
		try {
			psmt = connectionDB.dbInsert(sb.toString());
			psmt.setString(1, value);
			psmt.setString(2, new DateFormate().getDate());
			is = psmt.executeUpdate();
			if (is == 1) {
				//view.onSuccess("บันทึกข้อมูลแล้ว");
				connectionDB.closeAllTransaction();
			} else {
				connectionDB.closeAllTransaction();
				view.onFail("ไม่สามารถบันทึกข้อมูลได้");
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
			view.onFail("ไม่สามารถบันทึกเงินตั้งต้นได้ ( " + e.getMessage() + " )");
			connectionDB.closeAllTransaction();
		}
	}

	@Override
	public void requestMoneyBegin() {
		float m = 0;
		
		StringBuilder sb = new StringBuilder();
		sb.delete(0, sb.length());
		sb.append("SELECT money_value ");
		sb.append("FROM MoneyConfig ");
		sb.append("WHERE money_create_date LIKE '" + new DateFormate().getDateOnly() + "%'");
		//sb.append("ORDER BY money_id DESC LIMIT 1");
		connectionDB = new ConnectionDB();
		try {
			resultSet = connectionDB.dbQuery(sb.toString());
			while(resultSet.next()) {
				Float moneyfloat = Float.parseFloat(resultSet.getString("money_value"));
				m += moneyfloat;
			}
			//System.out.println(m);
			//String money = resultSet.getString("money_value");
			view.setMoneyToTextFeild(String.valueOf(m));
		} catch(SQLException e) {
			resultSet = null;
			System.out.println("Error: " + e.getMessage());
			connectionDB.closeAllTransaction();
		}
		connectionDB.closeAllTransaction();
	}

	@Override
	public void insertReportFromBugFix(String value) {
		StringBuilder sb = new StringBuilder();
		sb.delete(0, sb.length());
		
		sb.append("INSERT INTO Report (report_number, report_date, report_type, report_currency, report_buy_rate, report_sell_rate"
				+ ", report_amount, report_total, report_create_date) ");
		sb.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
		
		connectionDB = new ConnectionDB();
		int is = 0;
		try {
			psmt = connectionDB.dbInsert(sb.toString());
			psmt.setString(1, "solve");
			psmt.setString(2, new DateFormate().getDate());
			psmt.setString(3, "Buy");
			psmt.setString(4, "solve");
			psmt.setString(5, "solve");
			psmt.setString(6, "solve");
			psmt.setString(7, "solve");
			psmt.setString(8, value);
			psmt.setString(9, new DateFormate().getDateOnly());
			is = psmt.executeUpdate();
			if (is == 1) {
				connectionDB.closeAllTransaction();
			} else {
				connectionDB.closeAllTransaction();
				view.onFail("ไม่สามารถบันทึกข้อมูลได้");
			}
		} catch(Exception e) {
			System.out.println("insert : " + e.getMessage());
			connectionDB.closeAllTransaction();
			
		}
	}
}
