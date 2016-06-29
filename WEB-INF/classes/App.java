import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
 import javax.servlet.http.*;
 //import ejp.*;

 public class App extends HttpServlet {
 public void doGet (HttpServletRequest request, HttpServletResponse response)
 throws ServletException, IOException {
response.setContentType("text/html; charset=windows-874");
 PrintWriter out = response.getWriter();
// HTMLUtilities.createHTMLStart(out, "Show Information of Session");
 HttpSession session = request.getSession(true); //
 String session_counter = (String)session.getAttribute("counter"); //

 if (session_counter!=null) {
 session_counter = (Integer.parseInt(session_counter) + 1) + "";
 } else {
 session_counter = "1";
 }
 session.setAttribute("counter", session_counter );
 out.println("<H1><CENTER></CENTER></H1>");
 out.println("<TABLE>");
 out.println("<TR><TD></TD><TD>"
+ session_counter + "</TD></TR>");
 out.println("<TR><TD></TD><TD>"
+ session.getId() + "</TD></TR>");
 out.println("<TR><TD></TD><TD>"
+
 DateFormat.getDateTimeInstance().format(
 new Date(session.getCreationTime())) + "</TD></TR>");
 out.println("<TR><TD></TD><TD>"
+
 DateFormat.getDateTimeInstance().format(
 new Date(session.getLastAccessedTime())) + "</TD></TR>");
 out.println("<TR><TD>()</TD><TD>"
+
 session.getMaxInactiveInterval() + "</TD></TR>");
 out.println("</TABLE>");
 //HTMLUtilities.createHTMLEnd(out);
 }
 public void doPost (HttpServletRequest request, HttpServletResponse response)
 throws ServletException, IOException {
 doGet(request,response);
 }
 }
