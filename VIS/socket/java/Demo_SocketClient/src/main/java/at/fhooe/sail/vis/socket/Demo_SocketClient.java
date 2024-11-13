package at.fhooe.sail.vis.socket;

import at.fhooe.sail.vis.general.DataContainer;
import at.fhooe.sail.vis.general.IMyProtocolAPI;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

// this class implements the IMyProtocol API
public class Demo_SocketClient implements IMyProtocolAPI {

	static final int BUFFER_SIZE = 1024;

	private OutputStream mOut = null; // object storing outgoing data
	private InputStream mIn = null;// object storing incoming data

	public static void main(String[] _argv) throws Exception {
		System.out.println("Testing demo socket client ");

		// create the client object
		Demo_SocketClient client = new Demo_SocketClient();

		// connecting client to server
		if (client.connect()){

			// if connected, the loop allows sending 10 msgs
			for (int i = 0; i < 10; i++){

				//check if client is alive
				if(client.isAlive()){

					// sending a msg to the server via myMethod
					DataContainer repl = client.myMethod("Durchlauf " + i);
				}
			}
		}
	}

	// a method that sends msgs to a server
	@Override
	public DataContainer myMethod(String _param) throws IOException {

		String msg = "MyMethod(#)";

		// replacing # with incoming parameter _param
		msg = msg.replace("#", _param);

		// converting the string into a byte-array and stores it into mOut
		mOut.write(msg.getBytes());

		// flush sends the byte-array instantly
		mOut.flush();

		// byte-array for storing incoming reply of the server
		byte[] commBuffer = new byte[BUFFER_SIZE];

		/* reads incoming msg stored in mIn, writes the data of input stream in commBuffer;
		 * returns and stores the byte-count in rVal */
		int rVal = mIn.read(commBuffer);

		if(rVal >= 0){ // if the bytecount of commBuffer is >=0

			/* create a string with content of commBuffer
			* 1. parameter: the commBuffer
			* 2. parameter: where to begin in the bytestream, we do not skip anything so the offset is 0 lol
			* 3. parameter: the length of the bytstream, so method knows hom mucht it has to read */
			msg = new String(commBuffer, 0, rVal);

			System.out.println("got reply from server: " + msg); // print given string with decoded bytestream
			return new DataContainer(msg);// create and return a new datacontainer with msg

		} else { // if mIn.read(commBuffer) returns -1
			System.out.println("lost client"); // print error msg

		}
		// guarantees that the method returns an object in any case
		return new DataContainer("noResult");
	}

	@Override
	public boolean connect() throws Exception {

		// creating an active-socket-object with hardcoded ip and port
		Socket activeSocket = new Socket("127.0.0.1", 4949);
		System.out.println("connected to server ... ");

		// initializing streams and "connecting" them to global objects
		/* returns outputstream to mOut, a global var that is used to store
		* data that will be sent to the server by MyMethod*/
		mOut = activeSocket.getOutputStream();

		/* returns inputstream to mIn, a global variable that is used to
		* store data that will get in through the input stream*/
		mIn = activeSocket.getInputStream();

		return true;
	}

	// a simple method granting that the globals are not null
	@Override
	public boolean isAlive() throws Exception {
		// true if both are not null
		return (mIn != null && mOut != null);
	}
}
