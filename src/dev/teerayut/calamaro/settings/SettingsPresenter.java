package dev.teerayut.calamaro.settings;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
				view.onSuccess("บันทึกข้อมูลแล้ว");
				connectionDB.closeAllTransaction();
			} else {
				connectionDB.closeAllTransaction();
				view.onFail("ไม่สามารถบันทึกข้อมูลได้");
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
			view.onFail("Insert receipt : " + e.getMessage());
			connectionDB.closeAllTransaction();
		}
	}

	@Override
	public void requestMoneyBegin() {
		StringBuilder sb = new StringBuilder();
		sb.delete(0, sb.length());
		sb.append("SELECT money_value ");
		sb.append("FROM MoneyConfig ");
		sb.append("ORDER BY money_id DESC LIMIT 1");
		connectionDB = new ConnectionDB();
		try {
			resultSet = connectionDB.dbQuery(sb.toString());
			String money = resultSet.getString("money_value");
			view.setMoneyToTextFeild(money);
		} catch(Exception e) {
			resultSet = null;
			System.out.println("Error: " + e.getMessage());
			connectionDB.closeAllTransaction();
		}
		connectionDB.closeAllTransaction();
	}
}
