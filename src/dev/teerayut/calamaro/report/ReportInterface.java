package dev.teerayut.calamaro.report;

import java.util.List;

import dev.teerayut.calamaro.model.CalculateModel;
import dev.teerayut.calamaro.model.ReportModel;

public interface ReportInterface {

	public interface View {
		void showReport(List<CalculateModel> calculateModelsList);
		void onSuccess(String str);
	}
	
	public interface Presenter {
		void getReport(String date);
		void exportToExcel(List<CalculateModel> calculateModelsList);
	}
}
