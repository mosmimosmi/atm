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
public class Index extends HttpServlet {

	static final String DB_URL = "jdbc:mysql://localhost/bank?useSSL=true";
	static final String USER = "root";
	static final String PASS = "123456";

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		response.setContentType("Text/html");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession(true);

		out.println("<html>");
		out.println("<head>");
		out.println("<title>Index</title>");
		out.println("<link rel='stylesheet' href='css/styles.css'>");
		out.println("</head>");
		out.println("<body>");

		out.println("<form action='transfer'  method='POST'  id='form_transfer'>");
		out.println("</form>");
		out.println("<form action='loan'  method='POST'  id='form_loan'>");
		out.println("</form>");
		out.println("<form action='history'  method='POST'  id='form_history'>");
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
			Account account = new Account();
			Bank bank = new Bank();
			String sql = "select * from account a,login l,customer c,bank where c.cus_id=? and a.log_username=l.log_username and l.cus_id=c.cus_id";
			PreparedStatement prepstmt = conn.prepareStatement(sql);

			prepstmt.setString(1, String.valueOf(user.getId()) );
			ResultSet rs = prepstmt.executeQuery();
			out.println("<div  class='front'>");
			while (rs.next()) {
				out.println("Id Card " + rs.getString("cus_id") + "<br>");
				out.println("Name " + rs.getString("cus_name") + "<br>");
				out.println("LastName " + rs.getString("cus_lname") + "<br>");
				out.println("Money  " + rs.getDouble("acc_money") + " $<br>");

				user.setName(rs.getString("cus_name"));
				user.setLname(rs.getString("cus_lname"));
				account.setId(rs.getInt("acc_id"));
				account.setUserName(rs.getString("log_username"));
				account.setMoney(rs.getDouble("acc_money"));
				bank.setId(rs.getString("ban_id"));
				bank.setName(rs.getString("ban_Name"));
				bank.setBudget(rs.getDouble("ban_budget"));
				bank.setDeposit(rs.getDouble("ban_deposit"));
				bank.setMoney(rs.getDouble("ban_money"));
				session.setAttribute("user", user);
				session.setAttribute("account", account);
				session.setAttribute("bank", bank);
			}
			out.println("</div>");

			out.println("<div  class='form'>");
			out.println("<form action='login'  method='POST' id = 'form_logout'>");
			out.println("<button type='submit' form='form_transfer'> Transfer </button>");
			out.println("<button type='submit' form='form_loan'> Loan </button><br><br>");
			out.println("<button type='submit' name = 'logout' value='logout' form='form_logout'> Logout </button>");
			out.println("<button type='submit' name = 'history' value='history' form='form_history'> history </button>");
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
