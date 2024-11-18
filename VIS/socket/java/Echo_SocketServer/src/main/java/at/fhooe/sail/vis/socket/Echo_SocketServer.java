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

		// create passiveSocket wit give portnumber
		ServerSocket passiveSocket = new ServerSocket(port);
		System.out.println("Server started on port " + port);

		boolean running = true;

		while(running){
			System.out.println("waiting for clients ...");

			// create new activeSocket and accept incoming connection query
			Socket activeStocket = passiveSocket.accept();
			while(true) {
				String msg = null;
				// store incoming stream of activeSocket in new inputstream
				InputStream in = activeStocket.getInputStream();
				// create byte-array to read incoming msg
				byte[] commBuffer = new byte[BUFFER_SIZE];
				// read and store msg into com buffer, return byte-count to rVal
				int rVal = in.read(commBuffer);

				if (rVal > 0){
					msg = new String(commBuffer, 0, rVal);
					System.out.println("recieved message: " + msg);
				} else {
					//Fehlerfall // 0 // 1
					break;
				}

				// preparing reply
				String reply = "Echo ... " + msg;
				// store msg into new outputstream object
				OutputStream out = activeStocket.getOutputStream();
				// convert out into bytearray and send it to client
				out.write(reply.getBytes());
				out.flush(); // fluuuuuuuuuuuuuuuuuuuuuush
			}
		}// while running

		System.out.println("server is shutting down ... by!");
	}
}
