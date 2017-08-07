package dev.teerayut.calamaro.process;

import java.sql.ResultSet;
import java.util.List;

import dev.teerayut.calamaro.model.CalculateModel;


public class ProcessInterface {
	
	public interface View {
		void onSuccess(String success);
		void onFail(String fail);
		void onGenerateKey(ResultSet result);
	}
	
	public interface Presenter {
		void insertReceipt(List<CalculateModel> calculateList);
		void getLastKey();
	}

}
