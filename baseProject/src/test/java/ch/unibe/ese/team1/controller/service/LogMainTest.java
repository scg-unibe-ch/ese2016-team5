package ch.unibe.ese.team1.controller.service;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.Test;

import ch.unibe.ese.team1.log.LogMain;

/**
 * This class is responsible for testing the LogMain class. Everything is covered except the try and catch exception
 * of the filehandler. 
 * 
 * With this testscenario every other test e.g. in the controller tests is not necessary for the logging procedure.
 * 
 */

public class LogMainTest {
	
	private LogMain mainlog;
		
	@Test
	public void WriteReadLogFile(){
		String strLine = null;
		String teststring = null;
		mainlog = new LogMain();
		mainlog.log.warning("Testlog");
		
		// CREATE TESTLOG MESSAGE
		
		String ret = "";
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy HH:mm");
        Date d = new Date();
        ret += df.format(d);
        ret += " Testlog";
		
		// READ LOG FILE
		
		try{
			   FileInputStream fstream = new FileInputStream("controller.log");
			   BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			   strLine = "";
			   while ((strLine = br.readLine()) != null)   {
			     System.out.println (strLine);
			     teststring = strLine;
			     
			   }
			   br.close();
			} catch (Exception e) {
			     System.err.println("Error: " + e.getMessage());
			}
		
		assertEquals(teststring, ret);
	}
}
