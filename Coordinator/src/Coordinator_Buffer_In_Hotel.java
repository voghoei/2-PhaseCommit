import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Coordinator_Buffer_In_Hotel extends Thread {
	DataInputStream hdin;
	Socket hsoc;

	public Coordinator_Buffer_In_Hotel(Socket hsoc) {
		try {
			hsoc = hsoc;
			hdin = new DataInputStream(hsoc.getInputStream());
			System.out.println("Coordinator Hotel Buffer In Connected ...");
			start();
		} catch (Exception ex) {
			System.out
					.println("exp: Coordinator Hotel Buffer in Constructor  ...");
		}
	}

	public void run() {
		try {
			// table.put(Thread.currentThread().getId(), false);
			System.out.println("Coordinator Hotel Buffer In thread:  "
					+ Thread.currentThread().getId());

		} catch (Exception ex) {
			// Logger.getLogger(Transferfile.class.getName()).log(Level.SEVERE,
			// null, ex);
		}
		while (true) {
			try {

				String Command = hdin.readUTF();
				System.out.println(Command);

			} catch (Exception ex) {
				System.out.println("\t\t exp:  Coordinator Hotel Read ");

			}
		}
	}

	protected void finalize() throws IOException {
		hdin.close();
		hsoc.close();

	}

}
