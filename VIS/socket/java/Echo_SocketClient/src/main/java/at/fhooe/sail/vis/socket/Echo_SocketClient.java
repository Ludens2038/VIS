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

		Socket activeSocket = new Socket("127.0.0.1", 4949);
		System.out.println("connected to server ... ");
		Scanner scan = new Scanner(System.in);

		while(true){
			//send
			System.out.println("Eingabe: ");
			String msg = scan.nextLine();
			OutputStream out = activeSocket.getOutputStream();
			out.write(msg.getBytes());
			out.flush();

			//recieve
			byte[] commBuffer = new byte[BUFFER_SIZE];
			InputStream in = activeSocket.getInputStream();
			int rVal = in.read(commBuffer);
			if(rVal >= 0){
				// look at answer
				msg = new String(commBuffer, 0, rVal);
				System.out.println("got reply from server: " + msg);
			} else {
				// check error
				break;
			}

		}//while true
	}
}
