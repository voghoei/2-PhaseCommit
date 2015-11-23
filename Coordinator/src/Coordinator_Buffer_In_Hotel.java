import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.util.Pair;

public class Coordinator_Buffer_In_Hotel extends Thread {
	DataInputStream hdin;
	Socket hsoc;
	ConcurrentLinkedQueue<String> hqinlocal;
	AtomicInteger statusLocal;

	public Coordinator_Buffer_In_Hotel(Socket hsoc, ConcurrentLinkedQueue<String> hqin, AtomicInteger status) {
		try {
			this.statusLocal = status;
			this.hsoc = hsoc;
			this.hqinlocal = hqin;
			hdin = new DataInputStream(hsoc.getInputStream());
			System.out.println("Coordinator Hotel Buffer In Connected ...");
			start();
		} catch (Exception ex) {
			System.out.println("exp: Coordinator Hotel Buffer in Constructor  ...");
		}
	}

	public void run() {
		try {
			String msg;
			System.out.println("Coordinator Hotel Buffer In thread:  " + Thread.currentThread().getId());

			while (true) {
				sleep(1000);
				msg = hdin.readUTF();
				if (statusLocal.get() == 1) {
					System.out.println("Hotel in , Normal mode  " + msg);
					hqinlocal.add(msg);
				} else {
					System.out.println("Hotel in, Discarde Message " + msg);
				}
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
