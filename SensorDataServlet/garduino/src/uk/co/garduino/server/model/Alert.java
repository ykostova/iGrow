package uk.co.garduino.server.model;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;


public class Alert {
	private String sensorType;
	private String comparator;
	private int comparisonValue;
	
	public static ArrayList<Alert> getAlertsOfTypeForNode(HttpServletResponse response, Connection dbCon, String sensorType, int node) {
		String query = "SELECT sensor_type, comparator, comparison_value FROM alerts WHERE node_id = ? AND sensor_type = ?;"; 
		
		// db select
    	try {
			PreparedStatement selectAlerts = dbCon.prepareStatement(query);
			selectAlerts.setInt(1, node);
			selectAlerts.setString(2, sensorType);
			
			ResultSet rAlerts = selectAlerts.executeQuery();

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
	
	public static void insertAlert (HttpServletResponse response, Connection dbCon, String sensorType, String comparator, int compValue, int node) {
		String query = "INSERT INTO alerts (sensor_type, comparator, comparison_value, node_id) VALUES (?, ?, ?, ?);"; 
		
		// db insert
    	try {
			PreparedStatement insertAlert = dbCon.prepareStatement(query);
			insertAlert.setString(1, sensorType);
			insertAlert.setString(2, comparator);
			insertAlert.setInt(3, compValue);
			insertAlert.setInt(4, node);
			insertAlert.executeUpdate();
			
			dbCon.close();
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
    	}
	}
	
	public static void deleteAlert (HttpServletResponse response, Connection dbCon, int id) {
		String query = "DELETE FROM alerts WHERE id = ?;"; 
		
		// db remove
    	try {
			PreparedStatement removeAlert = dbCon.prepareStatement(query);
			removeAlert.setInt(1, id);
			removeAlert.executeUpdate();
			
			dbCon.close();
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
    	}
	}
	
	public static ArrayList<JSONObject> getAllAlerts(HttpServletResponse response, Connection dbCon) {
		String query = "SELECT id, node_id, sensor_type, comparator, comparison_value FROM alerts ORDER BY node_id, sensor_type;"; 
		
		// db select
    	try {
			PreparedStatement selectAlerts = dbCon.prepareStatement(query);
			ResultSet rAlerts = selectAlerts.executeQuery();
			
			
			// Build JSONObjects from resultset
			ArrayList<JSONObject> alerts = new ArrayList<JSONObject>();
			while (rAlerts.next()) {					
				JSONObject currentAlert = new JSONObject();
				currentAlert.put("id", rAlerts.getInt("id"));
				currentAlert.put("node_id", rAlerts.getInt("node_id"));
				currentAlert.put("sensor_type", rAlerts.getString("sensor_type"));
				currentAlert.put("comparator", rAlerts.getString("comparator"));
				currentAlert.put("comparison_value", rAlerts.getInt("comparison_value"));
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
			EmailAlerts e = new EmailAlerts(ApplicationSettings.to, 
											ApplicationSettings.from, 
											ApplicationSettings.mailserver, 
											ApplicationSettings.user, 
											ApplicationSettings.pass);
			
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
