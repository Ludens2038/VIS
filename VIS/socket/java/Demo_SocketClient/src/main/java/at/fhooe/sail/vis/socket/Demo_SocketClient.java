package at.fhooe.sail.vis.socket;

import at.fhooe.sail.vis.general.DataContainer;
import at.fhooe.sail.vis.general.IMyProtocolAPI;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

//import nicht möglich weil wir noch vorher was machn müssen(im build.gradle.kts dependencies)
public class Demo_SocketClient implements IMyProtocolAPI {

	static final int BUFFER_SIZE = 1024;
	private OutputStream mOut = null;
	private InputStream mIn = null;

	public static void main(String[] _argv) throws Exception {
		System.out.println("Testing demo socket client ");


		Demo_SocketClient client = new Demo_SocketClient();

		if (client.connect()){
			for (int i = 0; i < 10; i++){
				if(client.isAlive()){
					DataContainer repl = client.myMethod("Durchlauf " + i);
				}
			}
		}
	}

	@Override
	public DataContainer myMethod(String _param) throws IOException {

		String msg = "MyMethod(#)";
		msg = msg.replace("#", _param);

		mOut.write(msg.getBytes());
		mOut.flush();

		//recieve
		byte[] commBuffer = new byte[BUFFER_SIZE];
		int rVal = mIn.read(commBuffer);
		if(rVal >= 0){
			// look at answer
			msg = new String(commBuffer, 0, rVal);
			System.out.println("got reply from server: " + msg);
			return new DataContainer(msg);
		} else {
			// check error
			System.out.println("lost client");
		}
		return new DataContainer("noResult");
	}

	@Override
	public boolean connect() throws Exception {
		Socket activeSocket = new Socket("127.0.0.1", 4949);
		System.out.println("connected to server ... ");
		mOut = activeSocket.getOutputStream();
		mIn = activeSocket.getInputStream();

		return true;
	}

	@Override
	public boolean isAlive() throws Exception {
		// true wenn beide funktionieren, false wenn eins nicht funktioniert hat
		return (mIn != null && mOut != null);
	}
}
