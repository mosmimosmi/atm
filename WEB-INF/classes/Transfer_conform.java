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
import java.text.SimpleDateFormat;

public class Transfer_conform extends HttpServlet {

	static final String DB_URL = "jdbc:mysql://localhost/bank?useSSL=true";
	static final String USER = "root";
	static final String PASS = "123456";

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		response.setContentType("Text/html");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession(true);
		User user = (User)session.getAttribute("user");
		Account account = (Account)session.getAttribute("account");

		out.println("<html>");
		out.println("<head>");
		out.println("<title>Transfer_confrom</title>");
		out.println("<link rel='stylesheet' href='css/styles.css'>");
		out.println("</head>");
		out.println("<body>");
		try {
			out.println("<form action='transfer'  method='POST'  id='form_back'>");
			out.println("</form>");

			if (session.getAttribute("user").equals(null)) {
				RequestDispatcher dispatcher = request.getRequestDispatcher("login");
				dispatcher.forward(request, response);
			}
			if (request.getParameter("money_transfer_confrom") != null) {
				Connect myConnect = new Connect();
				Connection conn = myConnect.connect();
				// from account
				Double lose_money = account.getMoney()- Double.parseDouble(request.getParameter("money_transfer_confrom"));
				String sql = "update account set acc_money=? where acc_id=?";
				PreparedStatement prepstmt = conn.prepareStatement(sql);
				prepstmt.setDouble(1, lose_money);
				prepstmt.setInt(2, account.getId());
				prepstmt.executeUpdate();
				// to account

				sql = "select * from account where acc_id=?";
			 	prepstmt = conn.prepareStatement(sql);
				prepstmt.setInt(1, Integer.parseInt(request.getParameter("to_account_receive")) );
				ResultSet rs = prepstmt.executeQuery();
				Double money_of_recieve = 0.0;
				while (rs.next()) {
					money_of_recieve = rs.getDouble("acc_money");
				}
				Double add_money = money_of_recieve
						+ Double.parseDouble(request.getParameter("money_transfer_confrom"));
				sql = "update account set acc_money=? where acc_id=?";
				prepstmt = conn.prepareStatement(sql);
				prepstmt.setDouble(1, add_money);
				prepstmt.setInt(2, Integer.parseInt(request.getParameter("to_account_receive")));
				prepstmt.executeUpdate();

				Date date = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
				String dateNow = dateFormat.format(date);

				sql = "insert into transfer  (acc_id_transfer,acc_id_receive,tra_money,tra_date) VALUES (?,?,?,?)";
				prepstmt = conn.prepareStatement(sql);
				prepstmt.setInt(1, account.getId());
				prepstmt.setInt(2, Integer.parseInt(request.getParameter("to_account_receive")));
				prepstmt.setDouble(3, Double.parseDouble(request.getParameter("money_transfer_confrom")));
				prepstmt.setString(4, String.valueOf(dateNow));
				prepstmt.executeUpdate();

				sql = "insert into history  " + "(his_date,his_money,his_type,acc_id)" + "VALUES (?,?,?,?)";
				prepstmt = conn.prepareStatement(sql);
				prepstmt.setString(1, String.valueOf(dateNow));
				prepstmt.setDouble(2, Double.parseDouble(request.getParameter("money_transfer_confrom")));
				prepstmt.setString(3,"Transfer");
				prepstmt.setInt(4, account.getId());
				prepstmt.executeUpdate();

				sql = "insert into history  " + "(his_date,his_money,his_type,acc_id)" + "VALUES (?,?,?,?)";
				prepstmt = conn.prepareStatement(sql);
				prepstmt.setString(1, String.valueOf(dateNow));
				prepstmt.setDouble(2, Double.parseDouble(request.getParameter("money_transfer_confrom")));
				prepstmt.setString(3,"Receive");
				prepstmt.setInt(4, Integer.parseInt(request.getParameter("to_account_receive")));
				prepstmt.executeUpdate();

				RequestDispatcher dispatcher = request.getRequestDispatcher("index");
				dispatcher.forward(request, response);
			}

			out.println("<div class='blackground_form'>");
			out.println("<h1>Transaction</h1>");
			Connect myConnect = new Connect();
			Connection conn = myConnect.connect();


			out.println("<div  class='front'>");

				out.println("Id Card " + user.getId() + "<br>");
				out.println("Name " + user.getName() + "<br>");
				out.println("LastName " + user.getLname() + "<br>");
				out.println("Money  " + account.getMoney() + " $<br>");

			out.println("</div>");

			out.println("<div  class='form'>");
			out.println("<form action=''  method='POST' id ='form_conform'>");
			out.println("From Account " + account.getId() + "<br>");
			out.println("Send To Account " + request.getParameter("to_account") + "<br>");
			out.println("Cash " + request.getParameter("cash") + "<br>");
			out.println("<button type='submit' name = 'back' value='back' form='form_back'> Back </button>");
			Double haveMoney = account.getMoney()
					- Double.parseDouble(request.getParameter("cash"));
			if (haveMoney >= 0) {
				out.println("<input type='hidden' name='to_account_receive' value='"
						+ request.getParameter("to_account") + "' form='form_conform'>");
				out.println("<button type='submit' name = 'money_transfer_confrom' value='"
						+ request.getParameter("cash") + "' form='form_conform'> Transfer </button>");
			}
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
