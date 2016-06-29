import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class Register extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		try {
			response.setContentType("Text/html");
			PrintWriter out = response.getWriter();
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Register</title>");
			out.println("<link rel='stylesheet' href='css/styles.css'>");
			out.println("</head>");
			out.println("<body>");
			out.println("<form action='login'  method='POST'  id='form_cancel'>");
			out.println("</form>");

			out.println("<div class='blackground_form'>");
			out.println("<h1>Login-Bank</h1>");
			out.println("<div  class='form' >");
			out.println("<form action='login'  id='form_register' method='POST' >");
			out.println("ID card <input type='number' name='regiter_id' form='form_register'><br><br>");
			out.println("Name <input type='text' name='regiter_name' form='form_register' ><br><br>");
			out.println("LastName <input type='text' name='regiter_lname' form='form_register'><br><br>");
			out.println("username <input type='text' name='regiter_username' form='form_register'><br><br>");
			out.println("password <input type='password' name='regiter_password' form='form_register'><br><br>");
			out.println("<button type='submit' class='button' form='form_register' > Register </button>");
			out.println("<button type='submit' class='button' form='form_cancel'> Cancel </button>");
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
