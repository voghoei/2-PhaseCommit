import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Coordinator_Buffer_In_Concert extends Thread {
	DataInputStream cdin;
	Socket csoc;
	ConcurrentLinkedQueue<String> cqinlocal;
	AtomicInteger statusLocal;

	public Coordinator_Buffer_In_Concert(Socket csoc, ConcurrentLinkedQueue<String> cqin, AtomicInteger status) {
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
			System.out.println("Coordinator Concert Buffer In thread:  " + Thread.currentThread().getId());

			while (true) {
				sleep(1000);
				msg = cdin.readUTF();
				if (statusLocal.get() == 1) {
					System.out.println("Buffer Out, Normal mode: " + msg);
					cqinlocal.add(msg);
				} else {
					System.out.println("Buffer Out, Discarde Message: " + msg);
				}	
			}

		} catch (InterruptedException ex) {
			System.out.println("\t exp:  Coordinator Concert Read  ");
		} catch (Exception ex) {
			System.out.println("\t exp:  Coordinator Concert Read  ");
		}
	}

	protected void finalize() throws IOException {
		cdin.close();
		csoc.close();

	}

}
