import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class Hotel_Buffer_Out extends Thread {
	Socket hSoc;
	DataOutputStream dout;

	Hotel_Buffer_Out(Socket hsoc) {
		try {
			hsoc = hsoc;
			dout = new DataOutputStream(hsoc.getOutputStream());
			System.out.println("Hotel Buffer Out Connected ...");
			start();

		} catch (IOException e) {
			System.out.println("Hotel Buffer Out Connection faild ...");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected void finalize() throws IOException {
		dout.close();
		hSoc.close();

	}

	public void run() {
		try {

			System.out.println("Hotel Buffer Out thread :  "+ Thread.currentThread().getId());
			dout.writeUTF("Hotel out test");

		} catch (Exception ex) {
			// Logger.getLogger(Transferfile.class.getName()).log(Level.SEVERE,
			// null, ex);
		}
	}

}
