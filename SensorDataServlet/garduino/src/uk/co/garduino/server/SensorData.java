package uk.co.garduino.server;

import java.io.IOException;
import org.json.simple.*;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Servlet implementation class SensorData
 */
@WebServlet( "/SensorData/*" )
public class SensorData extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    public SensorData() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    /*
	 * Parse & output URL of /node/sensor/id
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		// parse url
		String requestPath = request.getRequestURI();
		String[] pathComponents = requestPath.split("/");
		
		int node = -10;
		String sType = "";
		int sNumber = -10;
		
		if (pathComponents.length > 3) {
			try {
				node = Integer.parseInt(pathComponents[3]);
			
				if (pathComponents.length > 4) {
					sType = pathComponents[4];
				}
			
				if (pathComponents.length > 5) {
					sNumber = Integer.parseInt(pathComponents[5]);
				}
			} catch (Exception ex) {
				PrintWriter out = new PrintWriter(response.getOutputStream());
				response.setContentType("text/html");
				out.print("Could not parse url:" + ex);
				out.close();
				return;
			} 
		}
		
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
    		Connection dbCon = DriverManager.getConnection("jdbc:mysql://localhost/garduino","root", "");
			try
	    	{	
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
							row.put(name.toString(), sensorData.getTimestamp(i));
						}
					}
					rows.add(row);
				}
				
	    		dbCon.close();
	    		
	    		// output json object
	    		String jsonText = JSONValue.toJSONString(rows);
	    		PrintWriter out = new PrintWriter(response.getOutputStream());
				response.setContentType("text/html");
				out.print(jsonText);
				out.close();
	    	} catch (SQLException ex) {
				PrintWriter out = new PrintWriter(response.getOutputStream());
				response.setContentType("text/html");
				out.print("Couldn't get data:" + ex);
				out.close();
				return;
	    	}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
	/*
	 * Parse & insert URL of /node/sensor/id
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		// parse url
		String requestPath = request.getRequestURI();
		String[] pathComponents = requestPath.split("/");
		
		int node = 0;
		String sType = "";
		int sNumber = 0;
		int value = 0;
		try {
			node = Integer.parseInt(pathComponents[3]);
			sType = pathComponents[4];
			sNumber = Integer.parseInt(pathComponents[5]);
			value = Integer.parseInt(request.getParameter("value"));
		} catch (Exception ex) {
			PrintWriter out = new PrintWriter(response.getOutputStream());
			response.setContentType("text/html");
			out.print("Could not parse url:" + ex);
			out.close();
			return;
		} 
		
		// db insert
    	try {
    		Connection dbCon = DriverManager.getConnection("jdbc:mysql://localhost/garduino","root", "");
			try
	    	{	
				String query = "";
				if (sType.equals("temp")) {
					query = "INSERT INTO temp (node, sensorNumber, value, timestamp) VALUES (?,?,?, NOW());";
				} else if (sType.equals("humidity")) {
					query = "INSERT INTO humidity (node, sensorNumber, value, timestamp) VALUES (?,?,?, NOW());";
				} else if (sType.equals("light")) {
					query = "INSERT INTO light (node, sensorNumber, value, timestamp) VALUES (?,?,?, NOW());";
				} else if (sType.equals("water-level")) {
					query = "INSERT INTO water_level (node, sensorNumber, value, timestamp) VALUES (?,?,?, NOW());";
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}