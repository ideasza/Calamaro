package dev.teerayut.calamaro.report;

import java.util.List;

import dev.teerayut.calamaro.model.ReportModel;

public interface ReportInterface {

	public interface View {
		void showReport(List<ReportModel> reportModelsList);
		void onSuccess(String str);
	}
	
	public interface Presenter {
		void getReport(String date);
	}
}
