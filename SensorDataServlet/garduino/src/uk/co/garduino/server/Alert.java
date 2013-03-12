package uk.co.garduino.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.http.HttpServletResponse;


public class Alert {
	private String sensorType;
	private String comparator;
	private int comparisonValue;
	
	// temp hardcoded email details
	private String to = "toby.philp@mac.com";
	private String from = "test@garduino.co.uk";
	private String mailserver = "smtp.gmail.com";
	private String user = "igrow.garduino";
	private String pass = "cJjbZFmNb5avhqcY";
	
	public static ArrayList<Alert> getAlertsOfTypeForNode(HttpServletResponse response, Connection dbCon, String sensorType, int node) {
		String query = "SELECT sensor_type, comparator, comparison_value FROM alerts WHERE node_id = ? AND sensor_type = ?;"; 
		
		// db select
    	try {
			PreparedStatement selectAlerts = dbCon.prepareStatement(query);
			selectAlerts.setInt(1, node);
			selectAlerts.setString(2, sensorType);
			
			ResultSet rAlerts = selectAlerts.executeQuery();
			
			
			// Build JSONObjects from resultset
			ArrayList<Alert> alerts = new ArrayList<Alert>();
			while (rAlerts.next()) {					
				Alert currentAlert = new Alert();
				currentAlert.setSensorType(rAlerts.getString("sensor_type"));
				currentAlert.setComparator(rAlerts.getString("comparator"));
				currentAlert.setComparisonValue(rAlerts.getInt("comparison_value"));
				alerts.add(currentAlert);
			}
    		
			dbCon.close();

    		return alerts;
    	} catch (SQLException ex) {
			PrintWriter out;
			try {
				out = new PrintWriter(response.getOutputStream());
				response.setContentType("text/html");
				out.print("Couldn't get data:" + ex);
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
    	}
	}
	
	public void setSensorType(String sensorType) {
		this.sensorType = sensorType;
	}
	
	public void setComparator(String comparator) {
		this.comparator = comparator;
	}
	
	public void setComparisonValue(int comparisonValue) {
		this.comparisonValue = comparisonValue;
	}
	
	public void doAlert(int currentValue) {
		if (this.compare(currentValue) == true) {
			EmailAlerts e = new EmailAlerts(to, from, mailserver, user, pass);
			e.setMessage("The current " + this.sensorType + " has reached " + currentValue);
			e.send();
		}
	}
	
	private boolean compare(int currentValue) {
		if (comparator.equals("lt") == true) {
			if (currentValue < comparisonValue) {
				return true;
			} else {
				return false;
			}
		} else if (comparator.equals("gt") == true) {
			if (currentValue > comparisonValue) {
				return true;
			} else {
				return false;
			}
		} else if (comparator.equals("eq") == true) {
			if (currentValue == comparisonValue) {
				return true;
			} else {
				return false;
			}
		} else if (comparator.equals("ne") == true) {
			if (currentValue != comparisonValue) {
				return true;
			} else {
				return false;
			}
		} 
		return true;
	}
}
