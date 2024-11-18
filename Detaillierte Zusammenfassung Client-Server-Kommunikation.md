# Detaillierte Zusammenfassung: Client-Server-Kommunikation

## Einleitung

Diese Zusammenfassung beschreibt die Funktionsweise einer TCP-basierten Client-Server-Kommunikation, 
wie sie in C++ implementiert wurde. Der Server wartet auf Verbindungen, akzeptiert diese und verarbeitet 
Nachrichten. Der Client stellt eine Verbindung her, sendet Nachrichten und empfängt Antworten. Die gesamte 
Kommunikation erfolgt in einer klar definierten, sequentiellen Abfolge.

## 1. Allgemeine Architektur

- Der Server wartet auf Verbindungen von Clients, akzeptiert diese und verarbeitet Nachrichten. Er kann auch Antworten senden.
- Der Client stellt eine Verbindung zum Server her, sendet Nachrichten und empfängt Antworten.

## 2. Sequenzielle Abfolge der Interaktionen

### A) Server startet:
1. Server erstellt einen passiven Socket (`socket`).
2. Server bindet den Socket an eine Adresse und einen Port (`bind`).
3. Server schaltet den Socket in den Listen-Modus (`listen`).
4. Server wartet auf Verbindungen (`accept`).

### B) Client startet:
1. Client erstellt einen aktiven Socket (`socket`).
2. Client konfiguriert die Serveradresse (`inet_pton`, `htons`).
3. Client versucht, sich mit dem Server zu verbinden (`connect`).

### C) Kommunikation zwischen Client und Server:
1. **Client sendet eine Nachricht**:
   - `send(activeSocket, msg.c_str(), msg.length(), 0)`.
2. **Server empfängt die Nachricht**:
   - `recv(activeSocket, comBuffer, BUFFER_SIZE, 0)`.
3. **Server sendet eine Antwort**:
   - `send(activeSocket, reply.c_str(), reply.length(), 0)`.
4. **Client empfängt die Antwort**:
   - `recv(activeSocket, comBuffer, BUFFER_SIZE, 0)`.

### D) Beenden der Verbindung:
1. Der Client beendet die Verbindung (`close`).
2. Der Server erkennt dies und beendet die Verarbeitung (`close`).

## 3. Zusammenfassung der wichtigsten Methoden

### Server:
- `socket`: Erstellt einen passiven Socket.
- `bind`: Verknüpft den Socket mit Adresse und Port.
- `listen`: Versetzt den Socket in den Listen-Modus.
- `accept`: Akzeptiert eine Client-Verbindung.
- `recv`: Empfängt Daten vom Client.
- `send`: Sendet Daten an den Client.
- `close`: Schließt den aktiven Socket.

### Client:
- `socket`: Erstellt einen aktiven Socket.
- `inet_pton`: Konvertiert die Server-IP.
- `connect`: Stellt eine Verbindung zum Server her.
- `send`: Sendet Daten an den Server.
- `recv`: Empfängt Daten vom Server.
- `close`: Schließt die Verbindung.

## 4. Beispielkommunikation

Angenommen, der Server und der Client sind gestartet. Der folgende Dialog beschreibt, was geschieht:

1. **Client initiiert die Verbindung:**  
   - Der Client ruft `connect` auf, um sich mit dem Server zu verbinden.  
   - Der Server erkennt die Anfrage und akzeptiert sie (`accept`).

2. **Client sendet eine Nachricht:**  
   - Client: "Hallo Server" -> ruft `send` auf.  
   - Server empfängt die Nachricht mit `recv`.

3. **Server verarbeitet und antwortet:**  
   - Der Server verarbeitet die Nachricht ("Hallo Server") und antwortet mit "Hallo Client".  
   - Server ruft `send` auf, um die Antwort zu schicken.

4. **Client empfängt die Antwort:**  
   - Der Client empfängt "Hallo Client" mit `recv`.

5. **Client beendet die Verbindung:**  
   - Der Client sendet "quit" und ruft `close` auf.  
   - Der Server erkennt die Trennung und schließt die Verbindung.
