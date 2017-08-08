package dev.teerayut.calamaro.model;

import java.util.Date;

public class ReportModel {

	private String reportNumber;
	private Date reportDate;
	private String reportType;
	private String reportCurrency;
	private String reportBuyRate;
	private String reportSellRate;
	private String reportAmount;
	private String reportTotal;
	public String getReportNumber() {
		return reportNumber;
	}
	public ReportModel setReportNumber(String reportNumber) {
		this.reportNumber = reportNumber;
		return this;
	}
	public Date getReportDate() {
		return reportDate;
	}
	public ReportModel setReportDate(Date reportDate) {
		this.reportDate = reportDate;
		return this;
	}
	public String getReportType() {
		return reportType;
	}
	public ReportModel setReportType(String reportType) {
		this.reportType = reportType;
		return this;
	}
	public String getReportCurrency() {
		return reportCurrency;
	}
	public ReportModel setReportCurrency(String reportCurrency) {
		this.reportCurrency = reportCurrency;
		return this;
	}
	public String getReportBuyRate() {
		return reportBuyRate;
	}
	public ReportModel setReportBuyRate(String reportBuyRate) {
		this.reportBuyRate = reportBuyRate;
		return this;
	}
	public String getReportSellRate() {
		return reportSellRate;
	}
	public ReportModel setReportSellRate(String reportSellRate) {
		this.reportSellRate = reportSellRate;
		return this;
	}
	public String getReportAmount() {
		return reportAmount;
	}
	public ReportModel setReportAmount(String reportAmount) {
		this.reportAmount = reportAmount;
		return this;
	}
	public String getReportTotal() {
		return reportTotal;
	}
	public ReportModel setReportTotal(String reportTotal) {
		this.reportTotal = reportTotal;
		return this;
	}
	
	
	
}
