package dev.teerayut.calamaro.main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dev.teerayut.calamaro.connection.ConnectionDB;


public class MainPresenter implements MainInterface.Presenter {
	
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
			view.onFail("Fail : " + e.getLocalizedMessage());
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
				view.onFail("Fail : data not found (" + code + ")");
			}
		} catch(Exception e) {
			resultSet = null;
			//System.out.println(e.getSQLState());
			view.onFail("Fail : data not found (" + code + ")");
			connectionDB.closeAllTransaction();
		}
		connectionDB.closeAllTransaction();
	}

}
