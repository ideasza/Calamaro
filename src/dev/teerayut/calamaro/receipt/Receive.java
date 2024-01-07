package dev.teerayut.calamaro.receipt;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterName;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import dev.teerayut.calamaro.main.MainActivity;
import dev.teerayut.calamaro.model.CalculateModel;
import dev.teerayut.calamaro.process.ProcessPresenter;
import dev.teerayut.calamaro.utils.Convert;

public class Receive {	
	private String date, time, number;
	private float grandTotal = 0;
	private DecimalFormat df = new DecimalFormat("#,###.00");
	
	private PrinterServiceClass printerService;
	private PrinterOptionsClass printerOptions;
	static String printerName = "EPSON TM-T82 ReceiptSA4";
	
	public void printReceipt(List<CalculateModel> calModel) throws Exception {
		for(CalculateModel m : calModel) {
			String[] timeSplit = m.getReportDate().split(" ");
			date = timeSplit[0];
			time = timeSplit[1];
			number = m.getReportNumber();
		}
		printerService = new PrinterServiceClass();
		PrinterOptionsClass p = new PrinterOptionsClass();
		
        p.resetAll();
        p.initialize();
        p.newLine();
        p.alignCenter();
        p.setText("Calamaro Exchange Co.,Ltd.");
        p.newLine();
        p.alignCenter();
        p.setText("Tax ID : 0815560001531");
        p.newLine();
        p.setText("Date : " +  date + " " + time);
        p.newLine();
        p.setText("Receipt No. " + number);
        p.newLine();
        p.alignCenter();
        p.setText(" - RECEIPT BOUGHT - ");
        p.newLine();
        p.alignLeft();
        p.addLineSeperator();
        p.newLine();
        String headerItem = String.format("%-10s %6s %10s %11s", "Currency", "Rate", "Amount", "Total");
        p.setText(headerItem);
        p.newLine();
        p.addLineSeperator();
        p.newLine();
        for (int i = 0 ; i < calModel.size(); i++) {

        	float amout = Float.parseFloat(calModel.get(i).getReportAmount().trim());
        	float total = Float.parseFloat(calModel.get(i).getReportTotal().trim());
        	String fm = "%-10s %6s %10s %10s";
        	if (calModel.get(i).getReportBuyRate().length() > 6 || calModel.get(i).getReportSellRate().length() > 6) {
        		fm = "%-8s %6s %10s %11s";
        	} else {
        		fm = "%-10s %6s %10s %10s";
        	}
       	 	String item = String.format(fm, 
       	 	calModel.get(i).getReportCurrency().trim(), 
       	 	(calModel.get(i).getReportType().equals("Buy")) ? calModel.get(i).getReportBuyRate() : calModel.get(i).getReportSellRate(),
       	 	df.format(amout), 
       	 	df.format(total));
   
       	 	p.setText(item);

        	grandTotal += total;
        	p.newLine();
        }
        p.newLine();
        p.addLineSeperator2();
        p.newLine();
        String totalItem = String.format("%-10s %6.2s %10s %11s", "Total", "", "", df.format(grandTotal));
        p.setText(totalItem);
	    p.newLine();
        p.addLineSeperator2();
        p.newLine();
        p.alignCenter();
        p.setText("========== THANK YOU ==========");
        p.feed((byte)3);
        p.finit();

        feedPrinter(p.finalCommandSet());
        
        byte[] cutPaper = new byte[] {27, 105};
		printerService.printBytes(printerName, cutPaper);
	}
	
	private static boolean feedPrinter(String p) {
		System.out.println(p);
        try {
            AttributeSet attrSet = new HashPrintServiceAttributeSet(new PrinterName(printerName, null));
            DocPrintJob job = PrintServiceLookup.lookupPrintServices(null, attrSet)[0].createPrintJob();
	        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
	        byte[] bytes = p.getBytes("TIS-620");
	        Doc doc = new SimpleDoc(bytes, flavor, null);
	        System.out.println(job);
	        job.print(doc, null);
	        System.out.println("Done !");
	    } catch(javax.print.PrintException pex) {
	        System.out.println("Printer Error " + pex.getMessage());
	        final ImageIcon icon = new ImageIcon(MainActivity.class.getResource("/icon/fail32.png"));
	        JOptionPane.showMessageDialog(null, "Printer Error" + pex.getMessage(), "Alert", JOptionPane.ERROR_MESSAGE, icon);
	        return false;
	    } catch(Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	        return true;
    }
}
