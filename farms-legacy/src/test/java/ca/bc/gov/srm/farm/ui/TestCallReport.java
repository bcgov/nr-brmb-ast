package ca.bc.gov.srm.farm.ui;


import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Mainly just a test to see how to do this.
 */
public class TestCallReport {
	
	
  @Disabled
  @Test
  public void validateImportXml() {
		try {
			String urlString = "http://oas.vividsolutions.com:7779/reports/rwservlet?farm_benefit_notice+IN_YEAR=2009+IN_PIN=1579432";
			URL url = new URL(urlString);
			
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");    
			connection.setDoOutput(true);                           
			connection.connect();
			
			String fileName = "C:\\temp\\report.pdf";
			try(InputStream in = connection.getInputStream();
			    FileOutputStream out = new FileOutputStream(fileName);) {
			
  			byte[] buffer = new byte[256];
  			while (true) {
  				int bytesRead = in.read(buffer);
  				if (bytesRead == -1) {
  					break;
  				}
  				out.write(buffer, 0, bytesRead);
  	      out.flush();
  			}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
