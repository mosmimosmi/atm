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

public class Loan_conform extends HttpServlet {

	static final String DB_URL = "jdbc:mysql://localhost/bank?useSSL=true";
	static final String USER = "root";
	static final String PASS = "123456";

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		response.setContentType("Text/html");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession(true);
		User user = (User)session.getAttribute("user");
		Account account = (Account)session.getAttribute("account");
		Bank bank = (Bank)session.getAttribute("bank");
		out.println("<html>");
		out.println("<head>");
		out.println("<title>Loan_confrom</title>");
		out.println("<link rel='stylesheet' href='css/styles.css'>");
		out.println("</head>");
		out.println("<body>");
		try {
			out.println("<form action='loan'  method='POST'  id='form_back'>");
			out.println("</form>");

			if (session.getAttribute("user").equals(null)) {
				RequestDispatcher dispatcher = request.getRequestDispatcher("login");
				dispatcher.forward(request, response);
			}

			if (request.getParameter("money_loan_confrom") != null) {

				Connect myConnect = new Connect();
				Connection conn = myConnect.connect();
				Statement stmt = conn.createStatement();
				Date date = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
				String dateNow = dateFormat.format(date);
				String sql = "insert into loan  "
						+ "(acc_id,ban_id,loa_buget,loa_date,loa_day,loa_interest,loa_interest_total)" + "VALUES (?,?,?,?,?,?,?)";
				PreparedStatement prepstmt = conn.prepareStatement(sql);
				prepstmt.setInt(1, account.getId());
				prepstmt.setString(2,"b000000001");
				prepstmt.setDouble(3, Double.parseDouble(request.getParameter("money_loan_confrom")));
				prepstmt.setString(4, String.valueOf(dateNow));
				prepstmt.setInt(5, Integer.parseInt(request.getParameter("recovery_days")));
				prepstmt.setDouble(6,10.0);
				prepstmt.setDouble(7, Double.parseDouble(request.getParameter("total")));
				prepstmt.executeUpdate();

				sql = "insert into history  (his_date,his_money,his_type,acc_id) VALUES (?,?,?,?)";
				prepstmt = conn.prepareStatement(sql);
				prepstmt.setString(1, String.valueOf(dateNow));
				prepstmt.setDouble(2, Double.parseDouble(request.getParameter("money_loan_confrom")));
				prepstmt.setString(3,"Loan");
				prepstmt.setInt(4, account.getId() );
 				prepstmt.executeUpdate();

				Double add_money = account.getMoney() + Double.parseDouble(request.getParameter("money_loan_confrom"));
				sql = "update account set acc_money=? where acc_id=?";
					prepstmt = conn.prepareStatement(sql);
					prepstmt.setDouble(1, add_money);
					prepstmt.setInt(2, account.getId() );
		 			prepstmt.executeUpdate();

				Double bank_budget_money = bank.getBudget() - Double.parseDouble(request.getParameter("money_loan_confrom"));
				Double bank_deposit_money = bank.getDeposit() + Double.parseDouble(request.getParameter("money_loan_confrom"));
				Double total_money = bank_budget_money + bank_deposit_money;

				sql = "update bank set ban_budget=?, ban_deposit=?, ban_money=? where ban_id=?";
				prepstmt = conn.prepareStatement(sql);
				prepstmt.setDouble(1, bank_budget_money);
				prepstmt.setDouble(2, bank_deposit_money);
				prepstmt.setDouble(3, total_money);
				prepstmt.setString(4,"b000000001");
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
			out.println("Account " + account.getId() + "<br>");
			out.println("Cash " + request.getParameter("cash") + " $<br>");
			out.println("Recovery Days " + request.getParameter("days") + "<br>");
			out.println("Interest 10% <br>");
			double bank_have_money = bank.getBudget()- Double.parseDouble(request.getParameter("cash"));
			double total = 0.0;
			if (request.getParameter("cash").equals("") || request.getParameter("days").equals(""))
				total = 0.0;
			else {
				total = Double.parseDouble(request.getParameter("cash"))
						+ (Double.parseDouble(request.getParameter("cash")) * 0.1
								* Double.parseDouble(request.getParameter("days")));
				out.println("Total " + total + " $<br>");
			}

			out.println("<button type='submit' name = 'back' value='back' form='form_back'> Back </button>");
			if (total > 0 && bank_have_money >= 0 && Double.parseDouble(request.getParameter("cash")) > 0.0 ) {
				out.println("<input type='hidden' name='recovery_days' value='" + request.getParameter("days")
						+ "' form='form_conform'>");
				out.println("<input type='hidden' name='total' value='" + total + "' form='form_conform'>");
				out.println("<button type='submit' name = 'money_loan_confrom' value='" + request.getParameter("cash")
						+ "' form='form_conform'> Loan </button>");
			}

			out.println("</form>");
			out.println("</div>");
			out.println("</div>");
			out.println("</body>");
			out.println("</html>");
		} catch (Exception se) {
			out.println("<button type='submit' name = 'back' value='back' form='form_back'> Back </button>");
			se.printStackTrace();
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
