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
import uk.co.garduino.server.SensorDataDbHandler;

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
		
		// get the data 
		SensorDataDbHandler sensorDb;
		try {
			sensorDb = new SensorDataDbHandler(DriverManager.getConnection("jdbc:mysql://localhost/garduino","root", ""));
		} catch (SQLException sqex) {
			PrintWriter out = new PrintWriter(response.getOutputStream());
			response.setContentType("text/html");
			out.print("SQL connection exception occured: " + sqex);
			out.close();
			return;
		}
		
		ArrayList<JSONObject> results = sensorDb.getReadings(response, node, sType, sNumber);
		
		// output
		if (results != null) {
			String jsonOut = JSONValue.toJSONString(results);
			PrintWriter out = new PrintWriter(response.getOutputStream());
			response.setContentType("text/html");
			out.print(jsonOut);
			out.close();
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
		
		// handle alerts
		try {
			ArrayList<Alert> alerts = Alert.getAlertsOfTypeForNode(response, DriverManager.getConnection("jdbc:mysql://localhost/garduino","root", ""), sType, node);
			for (Alert a : alerts) {
				a.doAlert(value);
			}
		} catch (SQLException e) {
			System.out.println("Failed to email alerts due to sql error: " + e);
		}
		
		// db insert
		SensorDataDbHandler sensorDb;
		try {
			sensorDb = new SensorDataDbHandler(DriverManager.getConnection("jdbc:mysql://localhost/garduino","root", ""));
			sensorDb.sensorInsert(response, node, sType, sNumber, value);
		} catch (SQLException sqex) {
			PrintWriter out = new PrintWriter(response.getOutputStream());
			response.setContentType("text/html");
			out.print("SQL connection exception occured: " + sqex);
			out.close();
			return;
		}
	}
}