package dev.teerayut.calamaro.show;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dev.teerayut.calamaro.connection.ConnectionDB;
import dev.teerayut.calamaro.model.CurrencyItem;


public class ShowPresenter implements ShowInterface.Presenter {
	
	private ConnectionDB connectionDB;
	
	private CurrencyItem model;
	private List<CurrencyItem> modelList = new ArrayList<CurrencyItem>();
	
	private ShowInterface.View view;
	public ShowPresenter(ShowInterface.View view) {
		this.view = view;
	}
	@Override
	public void LoadCurrency() {
		StringBuilder sb = new StringBuilder();
		sb.delete(0, sb.length());
		
		sb.append("SELECT currency_name, currency_image, currency_buy_rate, currency_sell_rate ");
		sb.append("FROM Currency");
		
		connectionDB = new ConnectionDB();
		CurrencyItem model = null;
		List<CurrencyItem> modelList = new ArrayList<CurrencyItem>();
		try {
			ResultSet resultSet = connectionDB.dbQuery(sb.toString());
			while(resultSet.next()) {
				model = new CurrencyItem();
				model.setName(resultSet.getString(1));
				model.setImage(resultSet.getString(2));
				model.setBuyRate(resultSet.getString(3));
				model.setSellRate(resultSet.getString(4));
            	modelList.add(model);
			}
			view.showCurrency(modelList);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	@Override
	public void LoadCurrencyOffset(int limit, int offset) {
		StringBuilder sb = new StringBuilder();
		sb.delete(0, sb.length());
		
		sb.append("SELECT currency_name, currency_image, currency_buy_rate, currency_sell_rate ");
		sb.append("FROM Currency ");
		sb.append("LIMIT " + limit + " OFFSET " + offset);
		
		connectionDB = new ConnectionDB();
		CurrencyItem model = null;
		List<CurrencyItem> modelList = new ArrayList<CurrencyItem>();
		try {
			ResultSet resultSet = connectionDB.dbQuery(sb.toString());
			while(resultSet.next()) {
				model = new CurrencyItem();
				model.setName(resultSet.getString(1));
				model.setImage(resultSet.getString(2));
				model.setBuyRate(resultSet.getString(3));
				model.setSellRate(resultSet.getString(4));
            	modelList.add(model);
			}
			view.showCurrency(modelList);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
