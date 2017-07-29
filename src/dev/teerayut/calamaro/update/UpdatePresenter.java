package dev.teerayut.calamaro.update;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dev.teerayut.calamaro.connection.ConnectionDB;
import dev.teerayut.calamaro.model.CurrencyItem;


public class UpdatePresenter implements UpdateInterface.Presenter{
	
	private ResultSet resultSet;
	private ConnectionDB connectionDB;
	private PreparedStatement psmt;
	
	private CurrencyItem model;
	private List<CurrencyItem> modelList = new ArrayList<CurrencyItem>();
	
	private UpdateInterface.View view;
	public UpdatePresenter(UpdateInterface.View view) {
		this.view = view;
	}
	
	@Override
	public void LoadCurrency() {
		StringBuilder sb = new StringBuilder();
		sb.delete(0, sb.length());
		sb.append("SELECT * FROM Currency ORDER BY currency_id ASC");
		connectionDB = new ConnectionDB();
		try {
			resultSet = connectionDB.dbQuery(sb.toString());
			while(resultSet.next()) {
				model = new CurrencyItem();
				model.setName(resultSet.getString("currency_name"));
				model.setImage(resultSet.getString("currency_image"));
				model.setBuyRate(resultSet.getString("currency_buy_rate"));
				model.setSellRate(resultSet.getString("currency_sell_rate"));
            	modelList.add(model);
			}
			view.showCurrency(modelList);
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Override
	public void updateCurrency(int id, String value1, String value2, String value3) {
		StringBuilder sb = new StringBuilder();
		sb.delete(0, sb.length());
		sb.append("UPDATE Currency SET currency_name = ?, ");
		sb.append("currency_buy_rate = ?, ");
		sb.append("currency_sell_rate = ? ");
		sb.append("WHERE currency_id = ? ");
		System.out.println(id + ", " + value1 + ", " + value2 + ", " + value3);
		System.out.println(sb.toString());
		connectionDB = new ConnectionDB();
		try {
			psmt = connectionDB.dbUpdate(sb.toString());
			psmt.setString(1, value1);
			psmt.setString(2, value2);
			psmt.setString(3, value3);
			psmt.setInt(4, id);
			if (psmt.executeUpdate() != 1) {
				view.onFail("ไม่สามารถอัพเดทฐานข้อมูลได้");
			}
		} catch(Exception e) {
			view.onFail("Update : " + e.getMessage());
			System.out.println(e.getMessage());
		}
	}

}
