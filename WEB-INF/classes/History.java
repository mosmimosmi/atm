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

public class History extends HttpServlet {

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
			out.println("<h1>Transfer</h1>");
			Connect myConnect = new Connect();
			Connection conn = myConnect.connect();
			Account account = (Account)session.getAttribute("account");
			String sql = "select * from account a,login l,customer c where c.cus_id=? and a.log_username=l.log_username and l.cus_id=c.cus_id";
			PreparedStatement prepstmt = conn.prepareStatement(sql);
			prepstmt.setString(1,"'"+ account.getId() +"'" );
			ResultSet rs = prepstmt.executeQuery();
			out.println("<div  class='front'>");
			while (rs.next()) {
				out.println("Id Card " + rs.getString("cus_id") + "<br>");
				out.println("Name " + rs.getString("cus_name") + "<br>");
				out.println("LastName " + rs.getString("cus_lname") + "<br>");
				out.println("Money  " + rs.getDouble("acc_money") + " $<br>");
				session.setAttribute("money", rs.getDouble("acc_money"));
				session.setAttribute("account", rs.getInt("acc_id"));
				session.setAttribute("username", rs.getString("log_username"));
			}
			out.println("</div>");
			out.println("<div  class='form'>");
			sql = "SELECT * FROM history  where acc_id=? order by his_date";
			prepstmt = conn.prepareStatement(sql);
			prepstmt.setInt(1, account.getId());
			rs = prepstmt.executeQuery();
			int no = 0;
			out.println("<div  class='front'>");
			out.println("<table border='1' style='width:80%'  align='center'>");
			out.println("<tr id='style1'><td>No</td><td>Date</td><td>Time</td><td>Money</td><td>TYPE</td></tr>");
			while (rs.next()) {
				no++;
				out.println("<tr id='style2' ><td>" + no + "</td><td>" + rs.getDate("his_date") + "</td><td>" + rs.getTime("his_date") +"</td>"
				+ "<td>"	+ rs.getDouble("his_money") + "</td><td>" + rs.getString("his_type") + "</td></tr>");
			}
			out.println("<tr id='style2'><td colspan='5' > Money Balance " + account.getMoney() + "</tr></td>");
			out.println("</table>");
			out.println("<button type='submit' name = 'back' value='back' form='form_back'> Back </button>");

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
