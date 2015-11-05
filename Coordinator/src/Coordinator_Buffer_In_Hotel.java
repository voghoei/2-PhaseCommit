import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javafx.util.Pair;

public class Coordinator_Buffer_In_Hotel extends Thread {
	DataInputStream hdin;
	Socket hsoc;
	ConcurrentLinkedQueue<String> hqinlocal;
	
	public Coordinator_Buffer_In_Hotel(Socket hsoc,ConcurrentLinkedQueue<String> hqin) {
		try {
			hsoc = hsoc;
			hqinlocal = hqin;
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
		try {
			while (true) {


				String Command = hdin.readUTF();
				System.out.println("while coordinator hotel in  "+ Command);
				hqinlocal.add(Command);



			}
		} catch (Exception ex) {
			System.out.println("\t\t exp:  Coordinator Hotel Read ");

		}
	}

	protected void finalize() throws IOException {
		hdin.close();
		hsoc.close();

	}

}
