import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Coordinator_Buffer_In_Concert extends Thread {
	DataInputStream cdin;
	Socket csoc;
	ConcurrentLinkedQueue<String> cqinlocal;
	int statusLocal;

	public Coordinator_Buffer_In_Concert(Socket csoc, ConcurrentLinkedQueue<String> cqin, int status) {
		try {
			this.statusLocal = status;
			this.cqinlocal = cqin;
			this.csoc = csoc;
			this.cdin = new DataInputStream(csoc.getInputStream());
			System.out.println("Coordinator Concert Buffer In Connected ...");
			start();
		} catch (Exception ex) {
			System.out.println("exp: Coordinator concert Buffer in Constructor  ...");
		}
	}

	public void run() {
		String msg;
		try {
			// table.put(Thread.currentThread().getId(), false);
			System.out.println("Coordinator Concert Buffer In thread:  " + Thread.currentThread().getId());

			while (true) {
				if (statusLocal == 1) {
					msg = cdin.readUTF();
					System.out.println("while coordinator concert in, Normal mode" + msg);
					cqinlocal.add(msg);
				} else {
					msg = cdin.readUTF();
					System.out.println("while coordinator concert in, Discarde Message " + msg);
				}
				sleep(1);
			}
			
		} 
		catch (InterruptedException ex) {
			System.out.println("\t exp:  Coordinator Concert Read  ");
		} 
		catch (Exception ex) {
			System.out.println("\t exp:  Coordinator Concert Read  ");
		}
	}

	protected void finalize() throws IOException {
		cdin.close();
		csoc.close();

	}

}
