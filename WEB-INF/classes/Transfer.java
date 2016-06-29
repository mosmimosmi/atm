import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.lang.*;

public class Transfer extends HttpServlet {

	static final String DB_URL = "jdbc:mysql://localhost/bank?useSSL=true";
	static final String USER = "root";
	static final String PASS = "123456";

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		response.setContentType("Text/html");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession(true);

		out.println("<html>");
		out.println("<head>");
		out.println("<title>Transfer</title>");
		out.println("<link rel='stylesheet' href='css/styles.css'>");
		out.println("</head>");
		out.println("<body>");

		out.println("<form action='index'  method='POST'  id='form_back'>");
		out.println("</form>");

		if (session.getAttribute("user").equals(null)) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("login");
			dispatcher.forward(request, response);
		}
		try {
			out.println("<div class='blackground_form'>");
			out.println("<h1>Transaction</h1>");
			Connect myConnect = new Connect();
			Connection conn = myConnect.connect();
			User user = (User)session.getAttribute("user");
			Account account = (Account)session.getAttribute("account");

			out.println("<div  class='front'>");

				out.println("Id Card " + user.getId() + "<br>");
				out.println("Name " + user.getName() + "<br>");
				out.println("LastName " + user.getLname() + "<br>");
				out.println("Money  " + account.getMoney() + " $<br>");

			out.println("</div>");

			out.println("<div  class='form'>");
			out.println("<form action='transfer_conform'  method='POST' id ='form_transfer'>");

			out.println("From Account " + account.getId() + "<br>");
			out.println("Cash <input type='number' name='cash' form='form_transfer' ><br>");
			String sql = "select * from account a,customer c,login l where a.acc_id !=? and(a.log_username = l.log_username "+
			"and c.cus_id = l.cus_id )";
			PreparedStatement prepstmt = conn.prepareStatement(sql);
			prepstmt.setInt(1, account.getId() );
			ResultSet	rs = prepstmt.executeQuery();
			out.println("To <select form='form_transfer' name='to_account'>");
			while (rs.next()) {
				out.println("<option value='" + rs.getString("acc_id") + "'>"+rs.getString("cus_name") + " " + rs.getString("cus_lname")+ " " +
				rs.getString("acc_id") + "</option>");
			}
			out.println("</select><br><br>");

			out.println(
					"<button type='submit' name = 'transfer' value='transfer' form='form_transfer'> Transfer </button>");
			out.println("<button type='submit' name = 'back' value='back' form='form_back'> Back </button>");
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
