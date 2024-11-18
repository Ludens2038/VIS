package at.fhooe.sail.vis.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Echo_SocketClient {

	static final int BUFFER_SIZE = 1024;

	public static void main(String[] _argv) throws IOException {
		System.out.println("starting up echo socket client ... ");

		// create a new Socket Object with loopback and port
		Socket activeSocket = new Socket("127.0.0.1", 4949);
		System.out.println("connected to server ... ");

		//create a new scanner object that reads console input
		Scanner scan = new Scanner(System.in);

		while(true){
			System.out.println("Eingabe: ");

			/* read and store console input
			* method nextLine waits til console input is confirmed with enter */
			String msg = scan.nextLine();

			// create a new outputstream object and store data from activeSocket
			OutputStream out = activeSocket.getOutputStream();
			out.write(msg.getBytes()); // converting string in bytearray and send to server
			out.flush(); // do not wait til buffer is full

			// create a bytearray to store incoming mesage from server
			byte[] commBuffer = new byte[BUFFER_SIZE];

			// create new inputstream object and store data that comes through activeSocket
			InputStream in = activeSocket.getInputStream();

			/* reads incoming msg into comBuffer, stores byte-count into rVal */
			int rVal = in.read(commBuffer);
			if(rVal >= 0){
				// create new string object, read comBuffer until rVal is reached
				msg = new String(commBuffer, 0, rVal);
				System.out.println("got reply from server: " + msg);
			} else {
				// check error
				break;
			}

		}//while true
	}
}
