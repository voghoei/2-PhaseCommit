import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Concert_Buffer_In extends Thread {
	Socket csoc;
	DataInputStream din;
	ConcurrentLinkedQueue<String> qinlocal;
	AtomicInteger statusLocal;

	Concert_Buffer_In(Socket csoc, ConcurrentLinkedQueue<String> qin, AtomicInteger status) {
		try {

			this.statusLocal = status;
			this.csoc = csoc;
			this.qinlocal = qin;
			din = new DataInputStream(csoc.getInputStream());
			System.out.println("Concert Buffer In Connected ...");
			start();

		} catch (IOException e) {
			System.out.println("exp: Concert Buffer In Connection faild ...");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			String msg;
			System.out.println("Concert Buffer In thread:  " + Thread.currentThread().getId());
			while (true) {
				sleep(1000);
				msg = din.readUTF();
				if (statusLocal.get() == 1) {
					System.out.println("Buffer in, Normal mode : " + msg);
					qinlocal.add(msg);
				} else {
					System.out.println("Buffer in, Discarde Message: " + msg);
				}

			}

		} catch (Exception ex) {
			System.out.println("\tConnection Reset ");
			System.out.println("Waiting for Connection ...");
		}
	}

	protected void finalize() throws IOException {
		din.close();
		csoc.close();
	}

}
