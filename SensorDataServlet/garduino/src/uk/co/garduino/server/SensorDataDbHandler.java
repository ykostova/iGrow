package uk.co.garduino.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class SensorDataDbHandler {
	private Connection dbCon;
	
	public SensorDataDbHandler(Connection dbCon) {
		this.dbCon = dbCon;
	}
	
	public ArrayList<JSONObject> getReadings(HttpServletResponse response, int node, String sType, int sNumber) throws IOException {
		// build query
		String query = "SELECT * FROM node ";
		if (!sType.isEmpty()) {
			if (sType.equals("temp")) {
				query += "JOIN temp ON node.id = temp.node_id WHERE node.id = ?";
			} else if (sType.equals("humidity")) {
				query += "JOIN humidity ON node.id = humidity.node_id WHERE node.id = ?";
			} else if (sType.equals("light")) {
				query += "JOIN light ON node.id = light.node_id WHERE node.id = ?";
			} else if (sType.equals("water-level")) {
				query += "JOIN water_level ON node.id = water_level.node_id WHERE node.id = ?";
				sNumber = -10;
			}
			
			if (sNumber != -10) {
				query += " AND sensorNumber = ? ORDER BY timestamp;";
			} 
		} else {
			if (node != -10) {
				query += " WHERE node.id = ?";
			}
			query += ";";
		}
		// db select
    	try {
			PreparedStatement getSensorData = dbCon.prepareStatement(query);
			if (node != -10) {
				getSensorData.setInt(1, node);
			}
			if (!sType.isEmpty() && sNumber != -10) {
				getSensorData.setInt(2, sNumber);
			}
			
			ResultSet sensorData = getSensorData.executeQuery();
			
			// Figure out column names
			ResultSetMetaData sensorDataMetaData = sensorData.getMetaData();
			int numberOfColumns = sensorDataMetaData.getColumnCount();
			String[][] colNames = new String[numberOfColumns][2];
			
			for (int i = 1; i < numberOfColumns + 1; i++) {
				String name = sensorDataMetaData.getColumnName(i);
				String type = sensorDataMetaData.getColumnTypeName(i);
								
				colNames[i-1][0] = name;
				colNames[i-1][1] = type;
			}
			
			// Build JSONObjects from resultset
			ArrayList<JSONObject> rows = new ArrayList<JSONObject>();
			while (sensorData.next()) {					
				JSONObject row = new JSONObject();
				for (int i = 1; i < numberOfColumns + 1; i++)  {	
					String name = colNames[i-1][0];
					String type = colNames[i-1][1];
					
					if (type.equals("INT")) {
						row.put(name.toString(), sensorData.getInt(i));
					} else if (type.equals("VARCHAR")) {
						row.put(name.toString(), sensorData.getString(i));
					} else if (type.equals("TIMESTAMP")) {
						row.put(name.toString(), sensorData.getTimestamp(i).toString());
					}
				}
				rows.add(row);
			}
    		
			dbCon.close();

    		return rows;
    	} catch (SQLException ex) {
			PrintWriter out = new PrintWriter(response.getOutputStream());
			response.setContentType("text/html");
			out.print("Couldn't get data:" + ex);
			out.close();
			return null;
    	}
	} 
	
	public void sensorInsert(HttpServletResponse response, int node, String sType, int sNumber, int value) throws IOException {
			try
			{	
				String query = "";
				if (sType.equals("temp")) {
					query = "INSERT INTO temp (node_id, sensorNumber, value) VALUES (?,?,?);";
				} else if (sType.equals("humidity")) {
					query = "INSERT INTO humidity (node_id, sensorNumber, value) VALUES (?,?,?);";
				} else if (sType.equals("light")) {
					query = "INSERT INTO light (node_id, sensorNumber, value) VALUES (?,?,?);";
				} else if (sType.equals("water-level")) {
					query = "INSERT INTO water_level (node_id, sensorNumber, value) VALUES (?,?,?);";
				}
				PreparedStatement insertReading = dbCon.prepareStatement(query);
				insertReading.setInt(1, node);
				insertReading.setInt(2, sNumber);
				insertReading.setInt(3, value);
				
				insertReading.executeUpdate();
				dbCon.close();
			} catch (SQLException ex) {
				PrintWriter out = new PrintWriter(response.getOutputStream());
				response.setContentType("text/html");
				out.print("Couldn't insert:" + ex);
				out.close();
				return;
			}
		}
}
