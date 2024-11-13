package at.fhooe.sail.vis.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/primitiveservlet")
public class PrimitiveServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest _req, HttpServletResponse _resp) throws ServletException, IOException {

        _resp.setStatus(HttpServletResponse.SC_OK);
        _resp.setContentType("text/html");
        PrintWriter out = _resp.getWriter();
        out.println("<html>");
        out.println("<head>Primitive Testservlet<//head>");
        out.println("<body>Hello World!</body>");
        out.println("</html");
    }
}
