package dev.teerayut.calamaro.main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dev.teerayut.calamaro.connection.ConnectionDB;
import dev.teerayut.calamaro.utils.DateFormate;


public class MainPresenter implements MainInterface.Presenter {
	
	private float MoneyBegin = 0;
	private float MoneyBalance = 0;
	private float AmountBuy = 0;
	private float AmountSell = 0;
	private ResultSet resultSet;
	private PreparedStatement psmt;
	private ConnectionDB connectionDB;
	private MainInterface.View view;
	public MainPresenter(MainInterface.View view) {
		this.view = view;
	}
	
	@Override
	public void requestCurrency() {
		StringBuilder sb = new StringBuilder();
		sb.delete(0, sb.length());
		sb.append("SELECT * FROM Currency WHERE currency_buy_code != 2 AND currency_buy_code != 3");
		connectionDB = new ConnectionDB();
		try {
			resultSet = null;
			resultSet = connectionDB.dbQuery(sb.toString());
			view.setCurrencyItem(resultSet);
		} catch(Exception e) {
			resultSet = null;
			view.onFail("ไม่สามารถดึงรายการสกุลเงินได้ ( " + e.getLocalizedMessage() + " )");
			connectionDB.closeAllTransaction();
		}
		connectionDB.closeAllTransaction();
	}

	@Override
	public void getCurrency(String code) {
		StringBuilder sb = new StringBuilder();
		sb.delete(0, sb.length());
		
		if (code.equals("1") || code.equals("41")) {
			sb.append("SELECT currency_name, currency_buy_rate, currency_sell_rate, currency_buy_code, currency_sell_code ");
			sb.append("FROM Currency ");
			sb.append("LIMIT 3");
		} else {
			sb.append("SELECT currency_name, currency_buy_rate, currency_sell_rate, currency_buy_code, currency_sell_code ");
			sb.append("FROM Currency ");
			sb.append("WHERE currency_buy_code=" + code + " ");
			sb.append("OR currency_sell_code=" + code);
		}
		
		connectionDB = new ConnectionDB();
		try {
			resultSet = connectionDB.dbQuery(sb.toString());
			//view.onProcessCurrency(resultSet);
			if (resultSet.isBeforeFirst()) {
				view.onProcessCurrency(resultSet);
			} else {
				view.onFail("ไม่สามารถดึงข้อมูลสกุลเงินได้ ( " + code + " )");
			}
		} catch(Exception e) {
			resultSet = null;
			//System.out.println(e.getSQLState());
			view.onFail("ไม่สามารถดึงข้อมูลสกุลเงินได้ ( " + code + " )");
			connectionDB.closeAllTransaction();
		}
		connectionDB.closeAllTransaction();
	}

	@Override
	public void getMoneyBalance() {
		getMoneyBegin();
		//MoneyBegin = getMoneyBegin();
		MoneyBalance = 0;
		getTotalUsage();
		if (AmountBuy == 0 && AmountSell == 0) {
			MoneyBalance = MoneyBegin;
		} else {
			if (AmountBuy > 0) {
		    	MoneyBalance = MoneyBegin - AmountBuy;
		    } 
			
			if (AmountSell > 0) {
		    	MoneyBalance = MoneyBalance + AmountSell;
		    }
		}
		view.onCheckMoneyBalance(MoneyBegin, MoneyBalance);
	}
	
	private void getMoneyBegin() {
		float MoneyB = 0;
		StringBuilder sb = new StringBuilder();
		sb.delete(0, sb.length());
		sb.append("SELECT money_id, money_value ");
		sb.append("FROM MoneyConfig ");
		sb.append("WHERE money_create_date LIKE '" + new DateFormate().getDateOnly() + "%'");
		//sb.append("ORDER BY money_id DESC LIMIT 1");
		connectionDB = new ConnectionDB();
		try {
			resultSet = connectionDB.dbQuery(sb.toString());
			while(resultSet.next()) {
				Float moneyfloat = Float.parseFloat(resultSet.getString("money_value"));
				MoneyB += moneyfloat;
			}
			MoneyBegin = MoneyB;
		} catch(Exception e) {
			System.out.println("Error getMoneyBegin: " + e.getMessage());
			connectionDB.closeAllTransaction();
			MoneyBegin = 0;
			//return 0;
		}
		connectionDB.closeAllTransaction();
		//return MoneyBegin;
	}
	
	private void getTotalUsage() {
		AmountBuy = 0;
		AmountSell = 0;
		StringBuilder sb = new StringBuilder();
		sb.delete(0, sb.length());
		sb.append("SELECT report_type, report_amount, report_total FROM Report ");
		sb.append("WHERE report_date LIKE '"+ new DateFormate().getDateOnly() +"%' ");
		connectionDB = new ConnectionDB();
		try {
			resultSet = connectionDB.dbQuery(sb.toString());
			while(resultSet.next()) {
				if (resultSet.getString("report_type").equals("Buy")) {
					AmountBuy += Float.parseFloat(resultSet.getString("report_total"));
				} else if (resultSet.getString("report_type").equals("Sell")) {
					AmountSell += Float.parseFloat(resultSet.getString("report_amount"));
				}
			}
			connectionDB.closeAllTransaction();
		} catch (Exception e) {
			System.out.println("Error getTotalUsage : " + e.getLocalizedMessage());
			e.printStackTrace();
			connectionDB.closeAllTransaction();
		}
		connectionDB.closeAllTransaction();
	}
}
