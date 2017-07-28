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
		sb.append("SELECT * FROM Currency");
		connectionDB = new ConnectionDB();
		try {
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
		sb.append("SELECT * FROM Currency WHERE currency_buy_code=" + code + " ");
		sb.append("OR currency_sell_code=" + code);
		
		connectionDB = new ConnectionDB();
		try {
			resultSet = connectionDB.dbQuery(sb.toString());
			if (resultSet.next()) {
				view.onProcessCurrency();
			} else {
				view.onFail("Fail : data not found (" + code + ")");
			}
		} catch(SQLException e) {
			resultSet = null;
			System.out.println(e.getSQLState());
			view.onFail("Fail : data not found (" + code + ")");
			connectionDB.closeAllTransaction();
		}
		connectionDB.closeAllTransaction();
	}

}
