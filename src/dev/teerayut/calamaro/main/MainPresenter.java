package dev.teerayut.calamaro.main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

}
