import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletContext;

public class DBConnection {

	static Connection connection = null;
	static ServletContext servletContext;

	static String url = "jdbc:mysql://ec2-18-222-204-95.us-east-2.compute.amazonaws.com:3306/techDB";
	static String user = "techproject";
	static String password = "password";

	static void getDBConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your MySQL JDBC Driver?");
			e.printStackTrace();
			return;
		}
		connection = null;
		try {
			connection = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;
		}
		if (connection == null) {
			System.out.println("Failed to make connection!");
		}
	}

	static String getURL() {
		return url;
	}

	static String getUserName() {
		return user;
	}

	static String getPassword() {
		return password;
	}

	public static void getDBConnection(ServletContext context) {
		servletContext = context;
		getDBConnection();
	}
}
