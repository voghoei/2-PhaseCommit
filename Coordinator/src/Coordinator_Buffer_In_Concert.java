import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Coordinator_Buffer_In_Concert extends Thread {
	DataInputStream cdin;
	Socket csoc;

	public Coordinator_Buffer_In_Concert(Socket csoc) {
		try {
			csoc = csoc;
			cdin = new DataInputStream(csoc.getInputStream());
			System.out.println("Coordinator Concert Buffer In Connected ...");
			start();
		} catch (Exception ex) {
			System.out
					.println("exp: Coordinator concert Buffer in Constructor  ...");
		}
	}

	public void run() {
		try {
			// table.put(Thread.currentThread().getId(), false);
			System.out.println("Coordinator Concert Buffer In thread:  "
					+ Thread.currentThread().getId());

		} catch (Exception ex) {
			// Logger.getLogger(Transferfile.class.getName()).log(Level.SEVERE,
			// null, ex);
		}
		while (true) {
			try {

				String Command = cdin.readUTF();
				System.out.println(Command);

			} catch (Exception ex) {
				System.out.println("\t exp:  Coordinator Concert Read  ");
			}
		}
	}

	protected void finalize() throws IOException {
		cdin.close();
		csoc.close();

	}

}
