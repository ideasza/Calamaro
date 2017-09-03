
package dev.teerayut.calamaro.settings;

import java.sql.ResultSet;

public interface SettingsInterface {

	public interface View {
		void setMoneyToTextFeild(ResultSet rs);
		void onSuccess(String success);
		void onFail(String fail);
	}
	
	public interface Presenter {
		void insertMoneyBegin(String value);
		void requestMoneyBegin();
	}
}
