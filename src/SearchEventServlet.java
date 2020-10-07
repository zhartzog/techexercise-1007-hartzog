import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.ParseException;

@WebServlet("/SearchEventServlet")
public class SearchEventServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public SearchEventServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String keyword = request.getParameter("keyword");
		try {
			search(keyword, response);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void search(String keyword, HttpServletResponse response) throws IOException, ParseException {
		String key;
		if (keyword == null) {
			key = "";
		} else {
			key = keyword;
		}
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String title = "Database Result";
		String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + //
				"transitional//en\">\n"; //
		out.println(docType + //
				"<html>\n" + //
				"<head><title>" + title + "</title></head>\n" + //
				"<body bgcolor=\"#f0f0f0\">\n" + //
				"<h1 align=\"center\">" + title + "</h1>\n");

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			DBConnection.getDBConnection(getServletContext());
			connection = DBConnection.connection;

			if (key.isEmpty()) {
				String selectSQL = "SELECT * FROM eventTable";
				preparedStatement = connection.prepareStatement(selectSQL);
			} else {
				String selectSQL = "SELECT * FROM eventTable WHERE EVENTNAME LIKE ?";
				String theEventName = key + "%";
				preparedStatement = connection.prepareStatement(selectSQL);
				preparedStatement.setString(1, theEventName);
			}
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("id");
				String eventName = rs.getString("eventName").trim();
				String eventDate = rs.getString("eventDate").trim();
				String eventTime = rs.getString("eventTime").trim();
				SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
				Date date1 = new Date();
				Date date2 = df.parse(eventDate + " " + eventTime);
				long differenceInTime = date2.getTime() - date1.getTime();
				long differenceInMinutes = TimeUnit.MILLISECONDS.toMinutes(differenceInTime) % 60;
				long differenceInHours = TimeUnit.MILLISECONDS.toHours(differenceInTime) % 24;
				long differenceInDays = TimeUnit.MILLISECONDS.toDays(differenceInTime) % 365;
				long differenceInYears = TimeUnit.MILLISECONDS.toDays(differenceInTime) / 365;
				String timeUntil;
				if (key.isEmpty() || eventName.contains(key)) {
					out.println("ID: " + id + ", ");
					out.println("Event Name: " + eventName + ", ");
					out.println("Event Date: " + eventDate + ", ");
					out.println("Event Time: " + eventTime + ", ");
					out.println("Time until Event: " + differenceInYears + " Years, " + differenceInDays + " Days, " //
							+ differenceInHours + " Hours, " + //
							differenceInMinutes + " Minutes." + "<br>");
				}
			}
			out.println("<a href=/techproject/SearchEvent.html>Search Data</a> <br>");
			out.println("</body></html>");
			rs.close();
			preparedStatement.close();
			connection.close();
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException se2) {
			}
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
