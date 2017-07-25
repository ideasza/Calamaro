package dev.teerayut.calamaro.report;

public class ReportPresenter implements ReportInterface.Presenter {
	
	private ReportInterface.View view;
	public ReportPresenter(ReportInterface.View view) {
		this.view = view;
	}

}
