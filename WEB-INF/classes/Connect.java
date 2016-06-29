import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class Connect {
Config config = new Config();


	public Config GetProperties() {
		try {
				Properties prop = new Properties();
				InputStream input = null;
				String filename = "config.properties";
				input = getClass().getClassLoader().getResourceAsStream(filename);
				prop.load(input);
				config.setUrl(prop.getProperty("url"));
				config.setUser(prop.getProperty("user"));
				config.setPassword(prop.getProperty("password"));
		} catch (IOException ex) {
		ex.printStackTrace();
		}
		return config;
	}

	public Connection connect() {
		Connection connecting = null;
		try {
			config = GetProperties();
			Class.forName("com.mysql.jdbc.Driver");
			connecting = DriverManager.getConnection(config.getUrl(),config.getUser(),config.getPassword());
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connecting;
	}

}
