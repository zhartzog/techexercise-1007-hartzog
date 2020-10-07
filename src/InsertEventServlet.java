import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class InsertEventServlet
 */
@WebServlet("/InsertEventServlet")
public class InsertEventServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public InsertEventServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String eventName = request.getParameter("eventName");
		String eventDate = request.getParameter("eventDate");
		String eventTime = request.getParameter("eventTime");
		Connection connection = null;
		String insertSql = " INSERT INTO eventTable (id, EVENTNAME, EVENTDATE, EVENTTIME) VALUES (default, ?, ?, ?)";

		try {
			DBConnection.getDBConnection(getServletContext());
			connection = DBConnection.connection;
			PreparedStatement preparedStmt = connection.prepareStatement(insertSql);
			preparedStmt.setString(1, eventName);
			preparedStmt.setString(2, eventDate);
			preparedStmt.setNString(3, eventTime);
			preparedStmt.execute();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String title = "Insert Data";
		String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";
		out.println(docType + //
				"<html>\n" + //
				"<head><title>" + title + "</title></head>\n" + //
				"<body bgcolor=\"#f0f0f0\">\n" + //
				"<h2 align=\"center\">" + title + "</h2>\n" + //
				"<ul>\n" + //

				"  <li><b>User Name</b>: " + eventName + "\n" + //
				"  <li><b>Email</b>: " + eventDate + "\n" + //
				"  <li><b>Phone</b>: " + eventTime + "\n" + //

				"</ul>\n");
		out.println("<a href=/webproject/InsertEvent.html>Search Data</a> <br>");
		out.println("</body></html>");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
