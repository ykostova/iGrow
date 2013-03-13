package uk.co.garduino.server.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import uk.co.garduino.server.model.Alert;
import uk.co.garduino.server.model.ApplicationSettings;

@WebServlet("/AlertsManagement")
public class AlertsManagement extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public AlertsManagement() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ArrayList<JSONObject> alerts = null;
		
		try {
			alerts = Alert.getAllAlerts(response, DriverManager.getConnection(ApplicationSettings.dbString, ApplicationSettings.dbUser, ApplicationSettings.dbPass));
		} catch (SQLException sqex) {
			System.out.println("Couldn't get alerts: " + sqex);
		}
		
		// output
		if (alerts != null) {
			String jsonOut = JSONValue.toJSONString(alerts);
			PrintWriter out = new PrintWriter(response.getOutputStream());
			response.setContentType("application/json");
			out.print(jsonOut);
			out.close();
		} 
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// parse url
		String requestPath = request.getRequestURI();
		String[] pathComponents = requestPath.split("/");
		int node = 0;
		String sType = "";
		String comparator = "";
		int compValue = -10;
		
		try {
			node = Integer.parseInt(pathComponents[3]);
			sType = pathComponents[4];
			comparator = pathComponents[5];
			compValue = Integer.parseInt(request.getParameter("comparison_value"));
		} catch (Exception ex) {
			PrintWriter out = new PrintWriter(response.getOutputStream());
			response.setContentType("text/html");
			out.print("Could not parse url:" + ex);
			out.close();
			return;
		} 
		
		if (sType.equals("temp") || sType.equals("humidity") || sType.equals("soil_moisture") || sType.equals("light")) {
			try {
				Alert.insertAlert(response, DriverManager.getConnection(ApplicationSettings.dbString, 
																		ApplicationSettings.dbUser, 
																		ApplicationSettings.dbPass), sType, comparator, compValue, node);
			} catch (Exception ex) {
				PrintWriter out = new PrintWriter(response.getOutputStream());
				response.setContentType("text/html");
				out.print("Could not parse url:" + ex);
				out.close();
				return;
			} 
		} else {
			PrintWriter out = new PrintWriter(response.getOutputStream());
			response.setContentType("text/html");
			out.print("Invalid sensor type");
			out.close();
			return;
		}
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// parse url
		String requestPath = request.getRequestURI();
		String[] pathComponents = requestPath.split("/");
		
		int node = 0;
		String sType = "";
		String comparator = "";
		int id = -10;
		
		try {
			node = Integer.parseInt(pathComponents[3]);
			sType = pathComponents[4];
			comparator = pathComponents[5];
			id = Integer.parseInt(pathComponents[6]);
		} catch (Exception ex) {
			PrintWriter out = new PrintWriter(response.getOutputStream());
			response.setContentType("text/html");
			out.print("Could not parse url:" + ex);
			out.close();
			return;
		} 
		
		if (id > 0) {
			try {
				Alert.deleteAlert(response, DriverManager.getConnection(ApplicationSettings.dbString, 
																		ApplicationSettings.dbUser, 
																		ApplicationSettings.dbPass), id);
			} catch (SQLException e) {
				PrintWriter out = new PrintWriter(response.getOutputStream());
				response.setContentType("text/html");
				out.print("Could not remove alert, db connection error");
				out.close();
				return;
			}
		} else {
			PrintWriter out = new PrintWriter(response.getOutputStream());
			response.setContentType("text/html");
			out.print("Could not parse url, invalid id");
			out.close();
			return;
		} 

	}

}
