import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Coordinator_Buffer_Out extends Thread {
	Socket hsoc;
	Socket csoc;
	DataOutputStream hdout;
	DataOutputStream cdout;

	public Coordinator_Buffer_Out(Socket hsoc, Socket csoc) {
		try {
			hsoc = hsoc;
			csoc = csoc;
			hdout = new DataOutputStream(hsoc.getOutputStream());
			cdout = new DataOutputStream(csoc.getOutputStream());
			System.out.println("Coordinator Buffer Out Connected ...");
			start();
		} catch (Exception ex) {
			System.out.println("exp: Buffer out Constructor  ...");
		}
	}

	public void run() {
		try {

			System.out.println("Coordinator Buffer Out thread:  "
					+ Thread.currentThread().getId());

			hdout.writeUTF("Hi Hotel");
			cdout.writeUTF("Hi Concert");

		} catch (Exception ex) {
			// Logger.getLogger(Transferfile.class.getName()).log(Level.SEVERE,
			// null, ex);
		}

	}

	protected void finalize() throws IOException {
		hdout.close();
		cdout.close();
		hsoc.close();
		csoc.close();

	}

}
