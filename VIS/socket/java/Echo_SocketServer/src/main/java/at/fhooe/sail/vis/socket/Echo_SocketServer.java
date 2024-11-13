package at.fhooe.sail.vis.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Echo_SocketServer {


	public static final int BUFFER_SIZE = 1024;

	public static void main(String[] _argv) throws IOException {
		System.out.println("starting up echo server ... ");

		int port = 4949;
		ServerSocket passiveSocket = new ServerSocket(port);
		System.out.println("Server started on port " + port);

		boolean running = true;

		while(running){
			System.out.println("waiting for clients ...");

			Socket activeStocket = passiveSocket.accept();
			while(true) {
				String msg = null;
				//recieve
				InputStream in = activeStocket.getInputStream();
				byte[] commBuffer = new byte[BUFFER_SIZE];
				int rVal = in.read(commBuffer);//lest die anzahl der bytes die ankommen

				if (rVal > 0){
					msg = new String(commBuffer, 0, rVal);
					System.out.println("recieved message: " + msg);
				} else {
					//Fehlerfall // 0 // 1
					break;
				}

				//send
				String reply = "Echo ... " + msg;
				OutputStream out = activeStocket.getOutputStream();
				out.write(reply.getBytes());
				out.flush();//um kurze nachrichten zu schicken, es wird nicht darauf gewartet bis der transportchunk voll sit
			}
		}// while running

		System.out.println("server is shutting down ... by!");
	}
}
