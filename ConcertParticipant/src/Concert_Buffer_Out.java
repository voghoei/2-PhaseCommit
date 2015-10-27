import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class Concert_Buffer_Out extends Thread {
	Socket cSoc;
	DataOutputStream dout;

	Concert_Buffer_Out(Socket csoc) {
		try {
			csoc = csoc;
			dout = new DataOutputStream(csoc.getOutputStream());
			System.out.println("Concert Buffer Out Connected ...");
			start();

		} catch (IOException e) {
			System.out.println("Concert Buffer Out Connection faild ...");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected void finalize() throws IOException {
		dout.close();
		cSoc.close();

	}

	public void run() {
		try {

			System.out.println("Concert Buffer Out thread :  "+ Thread.currentThread().getId());
			dout.writeUTF("Concert out test");

		} catch (Exception ex) {
			// Logger.getLogger(Transferfile.class.getName()).log(Level.SEVERE,
			// null, ex);
		}
	}

}
