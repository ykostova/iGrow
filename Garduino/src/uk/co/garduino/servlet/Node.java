package uk.co.garduino.servlet;


import java.io.IOException;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class HomeController
 */
@WebServlet("/Node")
public class Node extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Node() {
        super();
        // TODO Auto-generated constructor stub
        //System.out.println("Home Controller");
		System.out.println("Node");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//directToHome(request, response);
		System.out.println("Node Hit - Get");
		
		RequestDispatcher rd = getServletContext().getRequestDispatcher("/index.html");
		rd.forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//directToHome(request, response);
		Date now = new Date();
		System.out.println("Servlet Reached via - Post Request \t@ "+now.toString());
		String data = request.getParameter("data");
		if(data!=null)
		{
			System.out.println("Data Recieved \t = "+data);
		}
		
		RequestDispatcher rd = getServletContext().getRequestDispatcher("/index.html");
		rd.forward(request, response);
	}
}
