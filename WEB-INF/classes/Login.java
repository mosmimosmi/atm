import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class Login extends HttpServlet {

	static final String DB_URL = "jdbc:mysql://localhost/bank?useSSL=true";
	static final String USER = "root";
	static final String PASS = "123456";

	public static String login(String username, String password) throws Exception {
		Connect myConnect = new Connect();

		//myConnect.GetProperties();
		Connection conn = myConnect.connect();
		String sql = "select * from login";
		PreparedStatement prepstmt = conn.prepareStatement(sql);
		ResultSet rs = prepstmt.executeQuery();
		String idIsFound = "";
		while (rs.next()) {
			String customerId = rs.getString("cus_id");
			String logUser = rs.getString("log_username");
			String logPass = rs.getString("log_password");
			if (username.equals(username) && logPass.equals(password)) {
				idIsFound = customerId;
			}
		}
		return idIsFound;
	}

	public static boolean register(String reg_id, String reg_name, String reg_lname, String reg_username,
		String reg_password) throws Exception {
		Connect myConnect = new Connect();
	//	myConnect.GetProperties();
		Connection conn = myConnect.connect();
		boolean result_register;
		String sql = "INSERT INTO customer (cus_id,cus_name,cus_lname) VALUES (?,?,?)";
		PreparedStatement prepstmt = conn.prepareStatement(sql);
		prepstmt.setString(1, reg_id);
		prepstmt.setString(2, reg_name);
		prepstmt.setString(3, reg_lname);
		prepstmt.executeUpdate();

		sql = "INSERT INTO login (cus_id,log_username,log_password) VALUES (?,?,?)";
		prepstmt = conn.prepareStatement(sql);
		prepstmt.setString(1, reg_id);
		prepstmt.setString(2, reg_username);
		prepstmt.setString(3, reg_password);
		prepstmt.executeUpdate();
		sql = "INSERT INTO account (log_username,acc_money) VALUES (?,?)";
		prepstmt = conn.prepareStatement(sql);
		prepstmt.setString(1,reg_username);
		prepstmt.setDouble(2, 0.0);
		prepstmt.executeUpdate();
		return result_register = true;

	};

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		try {
			HttpSession session = request.getSession(true);
			response.setContentType("Text/html");
			PrintWriter out = response.getWriter();
			out.println("<html>");
			out.println("<head>");
			out.println("<title>login</title>");
			out.println("<link rel='stylesheet' href='css/styles.css'>");
			out.println("</head>");
			out.println("<body>");

			out.println("<form action='register'  method='POST'  id='form_register'>");
			out.println("</form>");

			if (request.getParameter("logout") != null) {
				session.invalidate();
			}
			if (request.getParameter("username") != null && request.getParameter("password") != null) {
				String id = login(request.getParameter("username"), request.getParameter("password"));
				out.println("<script>");
				if (id.equals("")) {
					out.println("alert('Unable to login!!')");
				} else {
					User user = new User();
					user.setId(id);
					session.setAttribute("user", user);
					RequestDispatcher dispatcher = request.getRequestDispatcher("index");
					dispatcher.forward(request, response);
				}
				out.println("</script>");
			} else if (request.getParameter("regiter_id") != null && request.getParameter("regiter_name") != null &&
								 request.getParameter("regiter_lname") != null && request.getParameter("regiter_username") != null &&
								 request.getParameter("regiter_password") != null  ) {
				boolean result = register(request.getParameter("regiter_id"), request.getParameter("regiter_name"),
						request.getParameter("regiter_lname"), request.getParameter("regiter_username"),
						request.getParameter("regiter_password"));
				out.println("<script>");
				if (result == true) {
					out.println("alert('Register Sucessful!!')");
				}
				else
					out.println("alert('Register false!!')");
				out.println("</script>");
			}

			out.println("<div class='blackground_form'>");
			out.println("<h1>Login-Bank</h1>");
			out.println("<div  class='form'>");
			out.println("<form action='login'  method='POST' id='form_login'>");
			out.println("username <input type='text' name='username' form='form_login' ><br><br>");
			out.println("password <input type='password' name='password'  form='form_login'><br><br>");
			out.println("<button type='submit' class='button' form='form_login'> Login </button>");
			out.println("<button type='submit' class='button' form='form_register'> Register </button>");
			out.println("</form>");
			out.println("</div>");
			out.println("</div>");
			out.println("</body>");
			out.println("</html>");
		} catch (Exception se) {
			se.printStackTrace();
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
