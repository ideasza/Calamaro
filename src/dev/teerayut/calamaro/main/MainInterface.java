package dev.teerayut.calamaro.main;

import java.sql.ResultSet;
import java.util.List;

import dev.teerayut.calamaro.model.CurrencyItem;

public interface MainInterface {
	
	public interface View {
		void onSuccess(String success);
		void onFail(String fail);
		void setCurrencyItem(ResultSet resultSet);
		void onProcessCurrency(ResultSet resultSet);
	}
	
	public interface Presenter {
		void requestCurrency();
		void getCurrency(String code);
	}

}
