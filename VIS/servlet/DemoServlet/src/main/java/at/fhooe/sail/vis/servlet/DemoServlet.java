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


// Die Servlet.war-Datei ist ein Archiv, das deine komplette Java-Webanwendung (inklusive Servlets, 
//    statischen Dateien und Konfigurationen) bündelt. Sie wird auf einem Java-EE-Server (wie Tomcat) bereitgestellt, 
//    der die Datei entpackt, die enthaltenen Komponenten registriert und die Anwendung ausführt. 
//    Dadurch wird dein DemoServlet unter der definierten URL (z. B. /demoservlet) zugänglich. 
//    Die WAR-Datei standardisiert und vereinfacht den Deployment-Prozess für Webanwendungen.

// Annotation, die angibt, dass dieses Servlet unter der URL "/demoservlet" augerufen werden kann.
@WebServlet("/demoservlet")
public class DemoServlet extends HttpServlet {

    // Schnittstelle für die Kommunikation mit einem Socket-Server.
    // Die Implementierung wird hier auf `null` initialisiert und später bei Bedarf instanziiert.
    IMyProtocolAPI socketClient = null;

    // Überschreibt die doGet-Methode von HttpServlet, um GET-Anfragen zu verarbeiten.
    @Override
    protected void doGet(HttpServletRequest _req, HttpServletResponse _resp) throws ServletException, IOException {

        // Setzt den HTTP-Status der Antwort auf "200 OK", beideutet das die Anfrage erfolgreich verarbeitet worden ist.
        _resp.setStatus(HttpServletResponse.SC_OK);

        // Legt den Content-Type der Antwort auf "text/html" fest.
        _resp.setContentType("text/html");

        // Erstellt einen PrintWriter, um die HTML-Antwort an den Client zu senden.
        PrintWriter out = _resp.getWriter();

        out.println("<html>");
        out.println("<head>Demo Testservlet<//head>");
        out.println("<body>");
        out.println("<h2>SocketServer</h2>");

        try {
            // Prüft, ob der Socket-Client noch nicht initialisiert ist.
            if (socketClient == null) {
                // Erstellt eine neue Instanz des Demo_SocketClient und verbindet sich mit dem Server.
                socketClient = new Demo_SocketClient();
                socketClient.connect();
            }

            // Überprüft, ob der Socket-Client mit dem Server verbunden ist und die Verbindung aktiv ist.
            if (socketClient.isAlive()) {
                // Führt eine Methode am Socket-Server aus (mit einem Beispielparameter "Foo")
                // und speichert das zurückgegebene Ergebnis in einer DataContainer-Instanz.
                DataContainer data = socketClient.myMethod("Foo");

                // Gibt die vom Server empfangene Nachricht in die HTML-Antwort aus.
                out.print("<div> msg from server: ");
                out.print(data.toString());
                out.println("</div>");
            }
        } catch (Exception _e){
            // Falls ein Fehler auftritt (z. B. der Socket-Server ist offline), wird dies im HTML ausgegeben.
            out.print("<div>");
            out.println("C++ socket server offline <br/>");

            // Gibt die Fehlermeldung aus.
            out.println(_e.getMessage() + "<br/>");

            // Schreibt den Stacktrace des Fehlers direkt in die HTML-Ausgabe.
            _e.printStackTrace(out);
            out.print("</div>");

             // Setzt den Socket-Client auf `null`, um einen erneuten Verbindungsaufbau zu ermöglichen.
            socketClient = null;
        }
        // Beendet die HTML-Ausgabe.
        out.println("</body>");
        out.println("</html");
    }
}
