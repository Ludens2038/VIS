package at.fhooe.sail.vis.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

// Die Annotation @WebServlet registriert dieses Servlet unter der URL "/primitiveservlet".
// Wenn diese URL aufgerufen wird, wird dieses Servlet ausgeführt.
@WebServlet("/primitiveservlet")
public class PrimitiveServlet extends HttpServlet {

    // Überschreibt die Methode doGet, um HTTP-GET-Anfragen zu verarbeiten.
    @Override
    protected void doGet(HttpServletRequest _req, HttpServletResponse _resp) throws ServletException, IOException {

        // Setzt den HTTP-Status der Antwort auf "200 OK", was bedeutet, dass die Anfrage erfolgreich verarbeitet wurde.
        _resp.setStatus(HttpServletResponse.SC_OK);

        // Legt den Content-Type der Antwort auf "text/html" fest, um anzugeben, dass die Antwort HTML-Inhalt ist.
        _resp.setContentType("text/html");

        // Erstellt ein PrintWriter-Objekt, mit dem HTML-Inhalt in die HTTP-Antwort geschrieben werden kann.
        PrintWriter out = _resp.getWriter();

        // Beginnt die HTML-Ausgabe, die an den Client gesendet wird.
        out.println("<html>");
        // Definiert den Titel oder Header-Bereich der HTML-Seite.
        out.println("<head>Primitive Testservlet<//head>");
        // Fügt eine einfache Nachricht "Hello World!" in den Body der HTML-Seite ein.
        out.println("<body>Hello World!</body>");
        // Schließt das HTML-Dokument.
        out.println("</html");
    }
}
