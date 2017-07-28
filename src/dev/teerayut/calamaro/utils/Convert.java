package dev.teerayut.calamaro.utils;

public class Convert {

	public String formatDecimal(float number) {
  	  float point = 0.004f; 
  	  if (Math.abs(Math.round(number) - number) < point) {
  	     return String.format("%,.2f", number);
  	  } else {
  	     return String.format("%,.2f", number);
  	  }
	}
	
	public static String formatNoPoint(float number) {
		float point = 0.004f; 
	  	  if (Math.abs(Math.round(number) - number) < point) {
	  	     return String.format("%,.0f", number);
	  	  } else {
	  	     return String.format("%,.0f", number);
	  	  }
	}
}
