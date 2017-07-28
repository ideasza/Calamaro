package dev.teerayut.calamaro.show;

import java.util.List;

import dev.teerayut.calamaro.model.CurrencyItem;

public interface ShowInterface {
	
	public interface View {
		void showCurrency(List<CurrencyItem> modelList);
	}
	
	public interface Presenter {
		void LoadCurrency();
		void LoadCurrencyOffset(int limit, int offset);
	}

}
