package at.fhooe.sail.vis.servlet;

import at.fhooe.sail.vis.general.DataContainer;
import at.fhooe.sail.vis.general.IMyProtocolAPI;
import at.fhooe.sail.vis.socket.Demo_SocketClient;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/demoservlet")
public class DemoServlet extends HttpServlet {

    IMyProtocolAPI socketClient = null;

    @Override
    protected void doGet(HttpServletRequest _req, HttpServletResponse _resp) throws ServletException, IOException {

        _resp.setStatus(HttpServletResponse.SC_OK);
        _resp.setContentType("text/html");
        PrintWriter out = _resp.getWriter();
        out.println("<html>");
        out.println("<head>Demo Testservlet<//head>");
        out.println("<body>");
        out.println("<h2>SocketServer</h2>");
        try {
            if (socketClient == null) {
                socketClient = new Demo_SocketClient();
                socketClient.connect();
            }
            if (socketClient.isAlive()) {
                DataContainer data = socketClient.myMethod("Foo");
                out.print("<div> msg from server: ");
                out.print(data.toString());
                out.println("</div>");
            }
        } catch (Exception _e){
            out.print("<div>");
            out.println("C++ socket server offline <br/>");
            out.println(_e.getMessage() + "<br/>");
            _e.printStackTrace(out);
            out.print("</div>");
            socketClient = null;
        }
        out.println("</body>");
        out.println("</html");
    }
}
