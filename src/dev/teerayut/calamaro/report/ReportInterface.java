package dev.teerayut.calamaro.report;

import java.sql.ResultSet;
import java.util.List;

import dev.teerayut.calamaro.model.CalculateModel;

public interface ReportInterface {

	public interface View {
		void showReport(List<CalculateModel> calculateModelsList);
		void setDateToComboBox(ResultSet rs);
		void onSuccess(String str);
	}
	
	public interface Presenter {
		void getReport(String date);
		ResultSet getReportFromDate();
		void exportToExcel(List<CalculateModel> calculateModelsList);
	}
}
