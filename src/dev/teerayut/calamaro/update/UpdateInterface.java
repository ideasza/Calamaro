package dev.teerayut.calamaro.update;

import java.util.List;

import dev.teerayut.calamaro.model.CurrencyItem;


public interface UpdateInterface {
	
	public interface View {
		void showCurrency(List<CurrencyItem> modelList);
		void onFail(String fail);
	}
	
	public interface Presenter {
		void LoadCurrency();
		void updateCurrency(int id, String value1, String value2, String value3);
	}

}
