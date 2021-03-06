import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Hotel_Buffer_In extends Thread {
	Socket hsoc;
	DataInputStream din;
	ConcurrentLinkedQueue<String> qinlocal;
	AtomicInteger statusLocal;

	Hotel_Buffer_In(Socket hsoc, ConcurrentLinkedQueue<String> qin, AtomicInteger status) {
		try {
			this.statusLocal = status;
			this.hsoc = hsoc;
			this.qinlocal = qin;
			din = new DataInputStream(hsoc.getInputStream());
			System.out.println("Hotel Buffer In Connected ...");
			start();

		} catch (IOException e) {
			System.out.println("Hotel Buffer In Connection faild ...");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			String msg;
			System.out.println("Hotel Buffer In thread:  " + Thread.currentThread().getId());
			while (true) {
				sleep(1000);
				msg = din.readUTF();
				if (statusLocal.get() == 1) {
					System.out.println("Buffer in, Normal mode: " + msg);
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
		hsoc.close();
	}

}
